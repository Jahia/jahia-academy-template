import org.jahia.api.Constants
import org.jahia.services.content.*
import org.jahia.registries.ServicesRegistry

import javax.jcr.NodeIterator
import javax.jcr.RepositoryException
import javax.jcr.query.Query

boolean doIt = false;
def logger = log;

Set<String> nodesToAutoPublish = new HashSet<String>();
Set<String> nodesToManuelPublish = new HashSet<String>();
JCRTemplate.getInstance().doExecuteWithSystemSession(null, Constants.EDIT_WORKSPACE, new JCRCallback<Object>() {
    @Override
    public Object doInJCR(JCRSessionWrapper session) throws RepositoryException {
        def q = "SELECT * FROM [bootstrap5mix:predefinedGrid]";

        NodeIterator iterator = session.getWorkspace().getQueryManager().createQuery(q, Query.JCR_SQL2).execute().getNodes();
        while (iterator.hasNext()) {
            final JCRNodeWrapper node = (JCRNodeWrapper) iterator.nextNode();
            if (!node.isNodeType('bootstrap5mix:createRow')) {
                node.addMixin('bootstrap5mix:createRow');
                logger.info("Add mixin bootstrap5mix:createRow on " + node.getPath());
            }
            String typeOfGrid = "";
            if (node.hasProperty("typeOfGrid")) {
                typeOfGrid = node.getProperty('typeOfGrid').getString();
            }
            if (! "predefinedGrid".equals(typeOfGrid)) {
                if (hasPendingModifications(node)) {
                    nodesToManuelPublish.add(node.getIdentifier());
                } else {
                    nodesToAutoPublish.add(node.getIdentifier())
                }
                node.setProperty("typeOfGrid", "predefinedGrid");
            }
        }
        q = "SELECT * FROM [bootstrap5mix:customGrid]";
        iterator = session.getWorkspace().getQueryManager().createQuery(q, Query.JCR_SQL2).execute().getNodes();
        while (iterator.hasNext()) {
            final JCRNodeWrapper node = (JCRNodeWrapper) iterator.nextNode();
            if (!node.isNodeType('bootstrap5mix:createRow')) {
                node.addMixin('bootstrap5mix:createRow');
                logger.info("Add mixin bootstrap5mix:createRow on " + node.getPath());
            }
            String typeOfGrid = "";
            if (node.hasProperty("typeOfGrid")) {
                typeOfGrid = node.getProperty('typeOfGrid').getString();
            }
            if (! "customGrid".equals(typeOfGrid)) {
                if (hasPendingModifications(node)) {
                    nodesToManuelPublish.add(node.getIdentifier());
                } else {
                    nodesToAutoPublish.add(node.getIdentifier())
                }
                node.setProperty("typeOfGrid", "customGrid");
            }
        }
        q = "SELECT * FROM [bootstrap5mix:createRow]";
        iterator = session.getWorkspace().getQueryManager().createQuery(q, Query.JCR_SQL2).execute().getNodes();
        while (iterator.hasNext()) {
            final JCRNodeWrapper node = (JCRNodeWrapper) iterator.nextNode();
            String typeOfGrid = "";
            if (node.hasProperty("typeOfGrid")) {
                JCRPropertyWrapper typeOfGridProp = node.getProperty("typeOfGrid");
                if (typeOfGridProp != null) {
                    typeOfGrid = typeOfGridProp.getString();
                }
            }
            if ("".equals(typeOfGrid)) {
                logger.info("Set [typeOfGrid] to 'nogrid' on " + node.getPath() + " (legacy value on list value was " + node.properties.gridType.string + ")")
                if (hasPendingModifications(node)) {
                    nodesToManuelPublish.add(node.getIdentifier());
                } else {
                    nodesToAutoPublish.add(node.getIdentifier())
                }
                node.setProperty("typeOfGrid", "nogrid");
            }
        }

        if (doIt) {
            session.save();
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
            logger.info("Nodes publish manually (has pending modifications):")
            for (String identifier : nodesToManuelPublish) {
                logger.warn("   " + identifier + " " + session.getNodeByIdentifier(identifier).getPath());
            }
        }
        return null;
    }
});

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
        final GregorianCalendar lastModified = (GregorianCalendar)  node.getProperty(Constants.JCR_LASTMODIFIED).getDate();

        return lastModified.after(lastPublished);
    } catch (RepositoryException e) {
        logger.error("", e);
        // If we can't validate that there's some pending modifications here, then we assume that there are no one.
        return false;
    }
}