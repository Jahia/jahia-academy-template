import org.jahia.api.Constants
import org.jahia.services.content.*

import javax.jcr.NodeIterator
import javax.jcr.PathNotFoundException
import javax.jcr.RepositoryException
import javax.jcr.query.Query

def logger = log;

//String categoryFrom = "/sites/systemsite/categories/products/marketing-factory";
//String categoryTo = "/sites/systemsite/categories/products/jexperience";

//String categoryFrom = "/sites/systemsite/categories/products/form-factory";
//String categoryTo = "/sites/systemsite/categories/products/forms";

String categoryFrom = "/sites/systemsite/categories/products/digital-experience-manager";
String categoryTo = "/sites/systemsite/categories/products/jahia";

//String categoryFrom = "/sites/systemsite/categories/products/workspace-factory";
//String categoryFrom = "/sites/systemsite/categories/products/private-appstore";
//String categoryFrom = "/sites/systemsite/categories/products/commerce-factory";
//String categoryFrom = "/sites/systemsite/categories/products/connectors";
//String categoryTo = "/sites/systemsite/categories/products/other";


String nodeType = "jacademix:metadatas";
String propertyName = "products";
boolean doIt = true;

Set<String> nodesToAutoPublish = new HashSet<String>();
Set<String> nodesToManuallyPublish = new HashSet<String>();

JCRTemplate.getInstance().doExecuteWithSystemSession(null, Constants.EDIT_WORKSPACE, null, new JCRCallback() {
    @Override
    Object doInJCR(JCRSessionWrapper session) throws RepositoryException {
        JCRNodeWrapper fromCategoryNode = null;
        try {
            fromCategoryNode = session.getNode(categoryFrom);
        } catch (javax.jcr.PathNotFoundException e) {
            logger.warn(e.getMessage());
        }
        JCRNodeWrapper toCategoryNode = null;
        try {
            toCategoryNode = session.getNode(categoryTo);
        } catch (javax.jcr.PathNotFoundException e) {
            logger.warn(e.getMessage());
        }


        if (fromCategoryNode != null && toCategoryNode != null) {
            String categoryFromUUID = fromCategoryNode.getIdentifier();
            String categoryToUUID = toCategoryNode.getIdentifier();

            def q = "SELECT * FROM [" + nodeType + "] where [" + propertyName + "]='" + categoryFromUUID + "'";
            logger.info(q);
            NodeIterator iterator = session.getWorkspace().getQueryManager().createQuery(q, Query.JCR_SQL2).execute().getNodes();
            while (iterator.hasNext()) {
                final JCRNodeWrapper node = (JCRNodeWrapper) iterator.nextNode();
                logger.info("Found category (" + fromCategoryNode.getDisplayableName() + ") on node " + node.getPath());
                boolean toCategoryAlreadySet = false;

                try {
                    if (! hasPendingModification(node)) {
                        nodesToAutoPublish.add(node.getIdentifier());
                    } else {
                        nodesToManuallyPublish.add(node.getIdentifier());
                    }

                    JCRPropertyWrapper categoryProp = node.getProperty(propertyName);

                    JCRValueWrapper valueToRemove = null;
                    JCRValueWrapper[] categoryValues = categoryProp.getValues()
                    for (JCRValueWrapper categoryValue : categoryValues) {
                        if (categoryValue.getString().equals(categoryToUUID)) {
                            toCategoryAlreadySet = true;
                        }
                        if (categoryValue.getString().equals(categoryFromUUID)) {
                            valueToRemove = categoryValue;
                        }
                    }
                    try {
                        categoryProp.removeValue(valueToRemove);
                        logger.info("    Remove category " + valueToRemove.getString() + " (" + fromCategoryNode.getDisplayableName() + ") on " + node.getPath());
                    } catch (javax.jcr.ValueFormatException e) {
                        logger.info("    Error when trying to rempve category " + categoryFromUUID + " (" + fromCategoryNode.getDisplayableName() + ") on " + node.getPath() + " " + e.getMessage());
                    }
                    if (! toCategoryAlreadySet ) {
                        try {
                            categoryProp.addValue(toCategoryNode);
                            logger.info("    Set category " + categoryToUUID + " (" + toCategoryNode.getDisplayableName() + ") on " + node.getPath());
                        } catch (javax.jcr.ValueFormatException e) {
                            logger.info("    Error when trying to set category " + categoryToUUID + " (" + toCategoryNode.getDisplayableName() + ") on " + node.getPath() + " " + e.getMessage());
                        }
                    }
                    node.setProperty(propertyName, categoryProp.getValues());
                    if (doIt) {
                        node.saveSession();
                    }
                } catch (PathNotFoundException e) {
                }


            }

        }
        if (CollectionUtils.isNotEmpty(nodesToAutoPublish)) {
            if (doIt) {
                JCRPublicationService.getInstance().publish(nodesToAutoPublish.asList(), Constants.EDIT_WORKSPACE, Constants.LIVE_WORKSPACE, null);
            };
            logger.info("");
            logger.info("Nodes which where republished:")
            for (String identifier : nodesToAutoPublish) {
                logger.warn("   " + session.getNodeByIdentifier(identifier).getPath());
            }
        }
        if (CollectionUtils.isNotEmpty(nodesToManuallyPublish)) {
            logger.info("");
            logger.info("Nodes to publish manually:")
            for (String identifier : nodesToManuallyPublish) {
                logger.warn("   " + session.getNodeByIdentifier(identifier).getPath());
            }
        }
        if (doIt) {
            session.save();
        }

        return null;
    }
});

private boolean hasPendingModifications(JCRNodeWrapper node) {
    if (!node.hasProperty("j:lastPublished")) return true
    if (!node.hasProperty("j:published") || !node.getProperty("j:published").getBoolean()) return true
    java.util.Calendar lastModified = node.getProperty("jcr:lastModified").getDate()
    java.util.Calendar lastPublished = node.getProperty("j:lastPublished").getDate()
    return lastModified > lastPublished
}

