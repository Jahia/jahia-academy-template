import org.jahia.api.Constants
import org.jahia.services.content.*
import org.jahia.services.sites.JahiaSite
import javax.jcr.NodeIterator
import javax.jcr.PathNotFoundException
import javax.jcr.RepositoryException
import javax.jcr.query.Query

def logger = log;

def JahiaSite site = org.jahia.services.sites.JahiaSitesService.getInstance().getSiteByKey("academy");

Set<String> nodesToAutoPublish = new HashSet<String>();

boolean doIt = true;

HashMap<String, String> productToUUID = new HashMap<String, String>();
HashMap<String, JCRNodeWrapper> productToNode = new HashMap<String, String>();

String nodeType = "jnt:page";
//String nodeType = "jacademy:kbEntry";

def productForPath = new HashMap<String, Object>();
productForPath.put("/sites/academy/home/documentation/system-administrator/jahia-cloud/managing-your-cloud-environment",["cloud"]);
productForPath.put("/sites/academy/home/documentation/end-user/stackconnect/about-stackconnect",["stackConnect"]);
productForPath.put("/sites/academy/home/documentation/developer/augmented-search",["augmented-search"]);

for (Locale locale : site.getLanguagesAsLocales()) {
    JCRTemplate.getInstance().doExecuteWithSystemSession(null, Constants.EDIT_WORKSPACE, locale, new JCRCallback() {
        @Override
        Object doInJCR(JCRSessionWrapper session) throws RepositoryException {
            JCRNodeWrapper productsNode = null;
            try {
                productsNode = session.getNode("/sites/systemsite/categories/products");
            } catch (javax.jcr.PathNotFoundException e) {
                JCRNodeWrapper rootCategoryNode = session.getNode("/sites/systemsite/categories");
                productsNode = rootCategoryNode.addNode("products", "jnt:category");
                productsNode.setProperty("jcr:title", "Products");
                session.save();
            }

            JCRNodeWrapper cloudNode = null;
            try {
                cloudNode = session.getNode("/sites/systemsite/categories/products/cloud");
            } catch (javax.jcr.PathNotFoundException e) {
                cloudNode = productsNode.addNode("cloud", "jnt:category");
                cloudNode.setProperty("jcr:title", "Jahia Cloud");
                session.save();
            }
            productToUUID.put("cloud",cloudNode.getIdentifier());
            productToNode.put("cloud",cloudNode);

            JCRNodeWrapper jahiaNode = null;
            try {
                jahiaNode = session.getNode("/sites/systemsite/categories/products/jahia");
            } catch (javax.jcr.PathNotFoundException e) {
                jahiaNode = productsNode.addNode("jahia", "jnt:category");
                jahiaNode.setProperty("jcr:title", "Jahia");
                session.save();
            }
            productToUUID.put("jahia",jahiaNode.getIdentifier());
            productToNode.put("jahia",jahiaNode);

            JCRNodeWrapper jexperienceNode = null;
            try {
                jexperienceNode = session.getNode("/sites/systemsite/categories/products/jexperience");
            } catch (javax.jcr.PathNotFoundException e) {
                jexperienceNode = productsNode.addNode("jexperience", "jnt:category");
                jexperienceNode.setProperty("jcr:title", "jExperience");
                session.save();
            }
            productToUUID.put("jexperience",jexperienceNode.getIdentifier());
            productToNode.put("jexperience",jexperienceNode);

            JCRNodeWrapper formsNode = null;
            try {
                formsNode = session.getNode("/sites/systemsite/categories/products/forms");
            } catch (javax.jcr.PathNotFoundException e) {
                formsNode = productsNode.addNode("forms", "jnt:category");
                formsNode.setProperty("jcr:title", "Forms");
                session.save();
            }
            productToUUID.put("forms",formsNode.getIdentifier());
            productToNode.put("forms",formsNode);

            JCRNodeWrapper augmentedSearchNode = null;
            try {
                augmentedSearchNode = session.getNode("/sites/systemsite/categories/products/augmented-search");
            } catch (javax.jcr.PathNotFoundException e) {
                augmentedSearchNode = productsNode.addNode("augmented-search", "jnt:category");
                augmentedSearchNode.setProperty("jcr:title", "Augmented Search");
                session.save();
            }
            productToUUID.put("augmented-search",augmentedSearchNode.getIdentifier());
            productToNode.put("augmented-search",augmentedSearchNode);

            JCRNodeWrapper stackConnectNode = null;
            try {
                stackConnectNode = session.getNode("/sites/systemsite/categories/products/stackConnect");
            } catch (javax.jcr.PathNotFoundException e) {
                stackConnectNode = productsNode.addNode("stackConnect", "jnt:category");
                stackConnectNode.setProperty("jcr:title", "StackConnect");
                session.save();
            }
            productToUUID.put("stackConnect",stackConnectNode.getIdentifier());
            productToNode.put("stackConnect",stackConnectNode);



            for (String path : productForPath.keySet()) {
                def versions = productForPath.get(path);
                for (String version : versions) {
                    logger.info(path + " -> " + version);

                    String versionUUID = productToUUID.get(version);

                    if (true) {
                        // current node
                        try {
                            JCRNodeWrapper page = (JCRNodeWrapper) session.getNode(path);
                            logger.info("Add document type " + version + " on page " + page.getPath());
                            if (!page.isNodeType("jacademix:metadatas")) {
                                logger.info("    Add mixin jacademix:metadatas on " + page.getPath());
                                page.addMixin("jacademix:metadatas");
                                page.saveSession();
                            }
                            JCRPropertyWrapper platformVersionsProperty = null;
                            boolean plateformVersionAlreadySet = false;
                            try {
                                platformVersionsProperty = page.getProperty("products");
                                // bypass loop

                                JCRValueWrapper[] platformVersionsValues = platformVersionsProperty.getValues()
                                for (JCRValueWrapper platformVersionsValue : platformVersionsValues) {
                                    //logger.info("Found productValue " + productValue);
                                    if (platformVersionsValue.getString().equals(versionUUID)) {
                                        plateformVersionAlreadySet = true;
                                    }
                                }

                            } catch (PathNotFoundException e) {
                                // prop do not exists... create it
                                page.setProperty("products", new JCRValueWrapper[0]);
                                platformVersionsProperty = page.getProperty("products");
                            }

                            if (!plateformVersionAlreadySet) {
                                platformVersionsProperty = page.getProperty("products");
                                try {
                                    platformVersionsProperty.addValue(productToNode.get(version),true);
                                } catch (javax.jcr.ValueFormatException e) {
                                    logger.info("    Error when trying to set value " + versionUUID + " (" + version + ") on " + page.getPath() + " " + e.getMessage());
                                }
                                page.setProperty("products", platformVersionsProperty.getValues());
                                if (doIt) {
                                    page.saveSession();
                                }
                                logger.info("     - Add product [" + version + "] -> done");
                            } else {
                                logger.info("     - Add product [" + version + "] -> already set");
                            }
                        } catch (PathNotFoundException e) {

                        }

                    }


                    def q = "SELECT * FROM [" + nodeType + "] where ISDESCENDANTNODE('" + path + "')";
                    NodeIterator iterator = session.getWorkspace().getQueryManager().createQuery(q, Query.JCR_SQL2).execute().getNodes();


                    while (iterator.hasNext()) {
                        final JCRNodeWrapper page = (JCRNodeWrapper) iterator.nextNode();
                        logger.info("Add product " + version + " on page " + page.getPath());
                        if (!page.isNodeType("jacademix:metadatas")) {
                            logger.info("    Add mixin jacademix:metadatas on " + page.getPath());
                            page.addMixin("jacademix:metadatas");
                            page.saveSession();
                        }
                        JCRPropertyWrapper platformVersionsProperty = null;
                        boolean plateformVersionAlreadySet = false;
                        try {
                            platformVersionsProperty = page.getProperty("products");
                            // bypass loop

                            JCRValueWrapper[] platformVersionsValues = platformVersionsProperty.getValues()
                            for (JCRValueWrapper platformVersionsValue : platformVersionsValues) {
                                //logger.info("Found productValue " + productValue);
                                if (platformVersionsValue.getString().equals(versionUUID)) {
                                    plateformVersionAlreadySet = true;
                                }
                            }

                        } catch (PathNotFoundException e) {
                            // prop do not exists... create it
                            page.setProperty("products", new JCRValueWrapper[0]);
                            platformVersionsProperty = page.getProperty("products");
                        }

                        if (!plateformVersionAlreadySet) {
                            try {
                                platformVersionsProperty.addValue(productToNode.get(version),true);
                            } catch (javax.jcr.ValueFormatException e) {
                                logger.info("    Error when trying to set value " + versionUUID + " (" + version + ") on " + page.getPath());
                            }
                            page.setProperty("products", platformVersionsProperty.getValues());
                            if (doIt) {
                                page.saveSession();
                            }
                            logger.info("     - Add product [" + version + "] -> done");
                        } else {
                            logger.info("     - Add product [" + version + "] -> already set");
                        }
                    }
                }
            }
            if (doIt) {
                session.save();
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

            return null;
        }
    }
    );
}
