import org.jahia.api.Constants
import org.jahia.services.content.*
import org.jahia.registries.ServicesRegistry

import javax.jcr.NodeIterator
import javax.jcr.RepositoryException
import javax.jcr.query.Query

def logger = log;
Boolean doIt = false;
Boolean removeGhosts = false;

Set<String> nodesToAutoPublish = new HashSet<String>();
Set<String> nodesToManuelPublish = new HashSet<String>();
Set<String> ghostsToRemove = new HashSet<String>();
JCRTemplate.getInstance().doExecuteWithSystemSession(null, Constants.EDIT_WORKSPACE, new JCRCallback<Object>() {
    @Override
    public Object doInJCR(JCRSessionWrapper session) throws RepositoryException {
        def gridQuery = "SELECT * FROM [bootstrap5nt:grid]";

        NodeIterator gridIterator = session.getWorkspace().getQueryManager().createQuery(gridQuery, Query.JCR_SQL2).execute().getNodes();
        while (gridIterator.hasNext()) {
            final JCRNodeWrapper gridNode = (JCRNodeWrapper) gridIterator.nextNode();
            String gridName = gridNode.getName();
            String gridPath = gridNode.getPath();
            if (! gridPath.startsWith("/modules")) {
                // get all grid area (sub nodes of type contentLists, not areas created in the studio)

                JCRNodeIteratorWrapper childIterator = gridNode.getNodes();
                while (childIterator.hasNext()) {
                    JCRNodeWrapper listNode = (JCRNodeWrapper) childIterator.nextNode();
                    String listPath = listNode.getPath();
                    if (listNode.isNodeType("jnt:contentList")) {
                        String currentListName = listNode.getName();
                        // Ex:  if list is mygrid-main -> rename to main
                        // Ex:  if list is mygrid -> rename to main  (this was used for nogrid type)
                        String newListName = currentListName.replace(gridName + '-', '').replace(gridName, '');
                        if ("".equals(newListName)) {
                            newListName = "main";
                        }
                        // only rename if needed
                        if (! ''.equals(newListName) && !newListName.equals(currentListName)) {
                            if (! newListName.startsWith("main") && ! newListName.startsWith("side") && ! newListName.startsWith("extra") && ! newListName.startsWith("col")) {
                                logger.info("  - Found a ghost node for path " + listNode.getPath() + " Check if you can remove it.");
                                if (removeGhosts) {
                                    ghostsToRemove.add(listNode.getPath());
                                }
                            } else {
                                logger.info("  - Rename [" + currentListName + "] to [" + newListName + "] " + listPath);
                                if (hasPendingModifications(listNode)) {
                                    nodesToManuelPublish.add(listNode.getIdentifier());
                                } else {
                                    nodesToAutoPublish.add(listNode.getIdentifier());
                                }
                                // First check if new list exists
                                String newListPath = listNode.getParent().getPath() + "/" + newListName;
                                logger.info("     - Check if list [" + newListName + "] exists and if it's empty for path " + newListPath);
                                try {
                                    JCRNodeWrapper newListNode = session.getNode(newListPath);
                                    // Check if new list is NOT empty.
                                    JCRNodeIteratorWrapper subNewNodesIterator = newListNode.getNodes();
                                    if (subNewNodesIterator.hasNext()) {
                                        // New list is not empty -> move content from old to new
                                        logger.info("     - List " + newListPath + " is NOT empty. We need to move [" + currentListName + "] subnode to the new list [" + newListName + "]");
                                        try {
                                            JCRNodeIteratorWrapper subNodesToMoveIterator = listNode.getNodes();
                                            while (subNodesToMoveIterator.hasNext()) {
                                                JCRNodeWrapper subNodeToMove = (JCRNodeWrapper) subNodesToMoveIterator.nextNode();
                                                String subNodeName = subNodeToMove.getName();
                                                String oldPath = listPath + "/" + subNodeName;
                                                String newPath = newListPath + "/" + subNodeName;
                                                logger.info("     - Move " + oldPath + " to " + newPath);
                                                if (doIt) {
                                                    try {
                                                        session.move(oldPath, newPath)
                                                        session.save();
                                                    } catch (Exception xx) {
                                                        logger.info("Error: " + xx.getMessage());
                                                    }
                                                }
                                            }
                                            logger.info("     - Remove old list " + listPath);
                                            try {
                                                if (doIt) {
                                                    session.removeItem(listNode.getPath());
                                                    session.save();
                                                }
                                            } catch (javax.jcr.ItemNotFoundException ex) {
                                                logger.info(ex.getMessage());
                                            }
                                        } catch (RepositoryException re) {
                                            logger.info("Error. Could not get subnodes of " + listPath);
                                        }

                                    } else {
                                        // New list is empty -> remove the new one then rename the old.
                                        logger.info("     - List " + newListPath + " is empty. Remove it then rename the old one.")
                                        try {
                                            logger.info("     - Remove " + newListPath);
                                            if (doIt) {
                                                session.removeItem(newListPath);
                                                session.save();
                                            }
                                            try {
                                                logger.info("     - Rename " + listPath + " to " + newListPath);
                                                if (doIt) {
                                                    session.move(listPath, newListPath)
                                                    session.save();
                                                }
                                            } catch (javax.jcr.PathNotFoundException pnfe) {
                                                logger.info("     - Could not find  " + listPath + " " + pnfe.printStackTrace());
                                            } catch (RepositoryException re) {
                                                logger.info("     - Unable to rename " + listPath + " " + re.printStackTrace());
                                            }
                                        } catch (javax.jcr.ItemNotFoundException ex) {
                                            logger.info("     - Unable to remove " + listPath + " " + ex.printStackTrace());
                                        }
                                    }



                                } catch (javax.jcr.PathNotFoundException pnfe) {
                                    // New list do not exist. Let's do a simple rename
                                    //logger.info("     - List " + newListPath + " does not exist.");
                                    logger.info("     - Rename " + listPath + " to " + newListPath);
                                    if (doIt) {
                                        try {
                                            session.move(listPath,newListPath);
                                            session.save();
                                        } catch (Exception e) {
                                            logger.info("Error: " + e.getMessage());
                                        }
                                    }

                                } catch (RepositoryException re) {
                                    logger.info("Error, could not found " + newListPath);
                                }
                            }

                        }
                    } else {
                        logger.info("Error grid " + listPath + " isnot of type jcr:contentList");
                    }
                }
            }
        }


        if (CollectionUtils.isNotEmpty(nodesToAutoPublish)) {
            if (doIt) {
                ServicesRegistry.getInstance().getJCRPublicationService().publish(nodesToAutoPublish.asList(), Constants.EDIT_WORKSPACE, Constants.LIVE_WORKSPACE, null);
            };
            logger.info("");
            logger.info("Nodes published:")
            for (String identifier : nodesToAutoPublish) {
                logger.warn("   " + session.getNodeByIdentifier(identifier).getPath());
            }
        }
        if (CollectionUtils.isNotEmpty(nodesToManuelPublish)) {

            logger.info("");
            logger.info("Nodes publish manually (has pending modifications). Live content is not visible anymore until publication:")
            for (String identifier : nodesToManuelPublish) {
                logger.warn("   " + identifier + " " + session.getNodeByIdentifier(identifier).getPath());
            }
        }


            return null;
    }

});


if (removeGhosts) {
    if (CollectionUtils.isNotEmpty(ghostsToRemove)) {
        logger.info("");
        logger.info("Remove ghosts from default");
        JCRTemplate.getInstance().doExecuteWithSystemSession(null, Constants.EDIT_WORKSPACE, new JCRCallback<Object>() {
            @Override
            public Object doInJCR(JCRSessionWrapper session) throws RepositoryException {
                for (String path : ghostsToRemove) {
                    logger.warn("   Remove " + path);
                    try {
                        if (doIt) {
                            session.removeItem(path);
                            session.save();
                        }
                    } catch (javax.jcr.ItemNotFoundException ex) {
                        logger.info(ex.getMessage());
                    }
                }
            }
        });
        logger.info("Remove ghosts from live");
        JCRTemplate.getInstance().doExecuteWithSystemSession(null, Constants.LIVE_WORKSPACE, new JCRCallback<Object>() {
            @Override
            public Object doInJCR(JCRSessionWrapper session) throws RepositoryException {
                for (String path : ghostsToRemove) {
                    logger.warn("   Remove " + path);
                    try {
                        if (doIt) {
                            session.removeItem(path);
                            session.save();
                        }
                    } catch (javax.jcr.ItemNotFoundException ex) {
                        logger.info(ex.getMessage());
                    }
                }
            }
        });


    }
}

private void renameGrid(String name, JCRNodeWrapper node, JCRSessionWrapper session, boolean doIt) {

}

private boolean hasPendingModifications(JCRNodeWrapper node) {
    try {
        if (!node.isNodeType(Constants.JAHIAMIX_LASTPUBLISHED)) return false;
        if (!node.hasProperty(Constants.LASTPUBLISHED)) return true;
        final GregorianCalendar lastPublished = (GregorianCalendar) node.getProperty(Constants.LASTPUBLISHED).getDate();
        if (lastPublished == null) return true;
        if (!node.hasProperty(Constants.JCR_LASTMODIFIED)) {
            // If this occurs, then it should be detected by an integrityCheck. But here there's no way to deal with such node.
            logger.error("The node has no last modification date set " + node.getPath());
            return false;
        }
        final GregorianCalendar lastModified = (GregorianCalendar) node.getProperty(Constants.JCR_LASTMODIFIED).getDate();

        return lastModified.after(lastPublished);
    } catch (RepositoryException e) {
        logger.error("", e);
        // If we can't validate that there's some pending modifications here, then we assume that there are no one.
        return false;
    }
}