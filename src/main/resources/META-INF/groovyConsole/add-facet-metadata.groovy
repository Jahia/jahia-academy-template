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

HashMap<String, String> versionToUUID = new HashMap<String, String>();
HashMap<String, JCRNodeWrapper> versionToNode = new HashMap<String, String>();

def versionForPath = new HashMap<String, Object>();
versionForPath.put("/sites/academy/home/community/community-upgrades",["jahia-73"]);
versionForPath.put("/sites/academy/home/community/jahia-8-for-the-community",["jahia-8"]);
versionForPath.put("/sites/academy/home/community/migrate-from-jahia-73-community-edition-to-jahia-8",["jahia-73"]);
versionForPath.put("/sites/academy/home/customer-center/forms",["jahia-8","jahia-73","legacy"]);
versionForPath.put("/sites/academy/home/customer-center/jahia-cloud",["jahia-8"]);
versionForPath.put("/sites/academy/home/customer-center/jahia/dx-source-code-previous-releases",["jahia-73"]);
versionForPath.put("/sites/academy/home/customer-center/jahia/previous-versions",["jahia-73","legacy"]);
versionForPath.put("/sites/academy/home/customer-center/jexperience",["jahia-8","jahia-73","legacy"]);
versionForPath.put("/sites/academy/home/customer-center/limited-maintenance-extension-for-jahia-7-3",["jahia-73"]);
versionForPath.put("/sites/academy/home/customer-center/stackconnect",["jahia-8","jahia-73"]);
versionForPath.put("/sites/academy/home/documentation/developer-previous",["legacy"]);
versionForPath.put("/sites/academy/home/documentation/developer/augmented-search/21",["jahia-8"]);
versionForPath.put("/sites/academy/home/documentation/developer/dx/73",["jahia-73"]);
versionForPath.put("/sites/academy/home/documentation/developer/dx/8",["jahia-8"]);
versionForPath.put("/sites/academy/home/documentation/developer/ff/2-7",["jahia-73"]);
versionForPath.put("/sites/academy/home/documentation/developer/ff/3-3",["jahia-8"]);
versionForPath.put("/sites/academy/home/documentation/developer/jexperience/111",["jahia-73"]);
versionForPath.put("/sites/academy/home/documentation/developer/jexperience/2.0",["jahia-8"]);
versionForPath.put("/sites/academy/home/documentation/end-user-previous",["legacy"]);
versionForPath.put("/sites/academy/home/documentation/end-user/dx/73",["jahia-73"]);
versionForPath.put("/sites/academy/home/documentation/end-user/dx/8",["jahia-8"]);
versionForPath.put("/sites/academy/home/documentation/end-user/ff/2-7",["jahia-73"]);
versionForPath.put("/sites/academy/home/documentation/end-user/ff/3-3",["jahia-8"]);
versionForPath.put("/sites/academy/home/documentation/end-user/jexperience/111",["jahia-73"]);
versionForPath.put("/sites/academy/home/documentation/end-user/jexperience/2.0",["jahia-8"]);
versionForPath.put("/sites/academy/home/documentation/end-user/stackconnect/about-stackconnect",["jahia-8"]);
versionForPath.put("/sites/academy/home/documentation/system-administrator-previous",["legacy"]);
versionForPath.put("/sites/academy/home/documentation/system-administrator/devops/docker",["jahia-8"]);
versionForPath.put("/sites/academy/home/documentation/system-administrator/devops/monitoring-your-jahia-platform/healthcheck",["jahia-8","jahia-73"]);
versionForPath.put("/sites/academy/home/documentation/system-administrator/devops/monitoring-your-jahia-platform/monitoring-your-servers",["jahia-8","jahia-73"]);
versionForPath.put("/sites/academy/home/documentation/system-administrator/devops/monitoring-your-jahia-platform/using-rest-api-to-execute-karaf-commands",["jahia-8","jahia-73"]);
versionForPath.put("/sites/academy/home/documentation/system-administrator/devops/provisioning",["jahia-8"]);
versionForPath.put("/sites/academy/home/documentation/system-administrator/dx/73",["jahia-73"]);
versionForPath.put("/sites/academy/home/documentation/system-administrator/dx/8",["jahia-8"]);
versionForPath.put("/sites/academy/home/documentation/system-administrator/ff/2-7",["jahia-73"]);
versionForPath.put("/sites/academy/home/documentation/system-administrator/ff/3-3",["jahia-8"]);
versionForPath.put("/sites/academy/home/documentation/system-administrator/jahia-cloud",["jahia-8"]);
versionForPath.put("/sites/academy/home/documentation/system-administrator/jexperience/111",["jahia-73"]);
versionForPath.put("/sites/academy/home/documentation/system-administrator/jexperience/2.0",["jahia-8"]);
versionForPath.put("/sites/academy/home/downloads",["jahia-8"]);
versionForPath.put("/sites/academy/home/jahia-8",["jahia-8"]);
versionForPath.put("/sites/academy/home/legacy-1",["legacy"]);
versionForPath.put("/sites/academy/home/rebranding",["jahia-8"]);
versionForPath.put("/sites/academy/home/training--kb",["jahia-8","jahia-73","legacy"]);
versionForPath.put("/sites/academy/home/whats-new",["jahia-8","jahia-73"]);

for (Locale locale : site.getLanguagesAsLocales()) {
    JCRTemplate.getInstance().doExecuteWithSystemSession(null, Constants.EDIT_WORKSPACE, locale, new JCRCallback() {
        @Override
        Object doInJCR(JCRSessionWrapper session) throws RepositoryException {
            JCRNodeWrapper platformVersionsNode = null;
            try {
                platformVersionsNode = session.getNode("/sites/systemsite/categories/platformVersions");
            } catch (javax.jcr.PathNotFoundException e) {
                JCRNodeWrapper rootCategoryNode = session.getNode("/sites/systemsite/categories");
                platformVersionsNode = rootCategoryNode.addNode("platformVersions", "jnt:category");
                platformVersionsNode.setProperty("jcr:title", "Platform versions");
                session.save();
            }

            JCRNodeWrapper jahia8Node = null;
            try {
                jahia8Node = session.getNode("/sites/systemsite/categories/platformVersions/jahia-8");
            } catch (javax.jcr.PathNotFoundException e) {
                jahia8Node = platformVersionsNode.addNode("jahia-8", "jnt:category");
                jahia8Node.setProperty("jcr:title", "Jahia 8");
                session.save();
            }
            versionToUUID.put("jahia-8",jahia8Node.getIdentifier());
            versionToNode.put("jahia-8",jahia8Node);

            JCRNodeWrapper jahia73Node = null;
            try {
                jahia73Node = session.getNode("/sites/systemsite/categories/platformVersions/jahia-73");
            } catch (javax.jcr.PathNotFoundException e) {
                jahia73Node = platformVersionsNode.addNode("jahia-73", "jnt:category");
                jahia73Node.setProperty("jcr:title", "Jahia 7.3");
                session.save();
            }
            versionToUUID.put("jahia-73",jahia73Node.getIdentifier());
            versionToNode.put("jahia-73",jahia73Node);

            JCRNodeWrapper legacyNode = null;
            try {
                legacyNode = session.getNode("/sites/systemsite/categories/platformVersions/legacy");
            } catch (javax.jcr.PathNotFoundException e) {
                legacyNode = platformVersionsNode.addNode("legacy", "jnt:category");
                legacyNode.setProperty("jcr:title", "Legacy");
                session.save();
            }
            versionToUUID.put("legacy",legacyNode.getIdentifier());
            versionToNode.put("legacy",legacyNode);


            for (String path : versionForPath.keySet()) {
                def versions = versionForPath.get(path);
                for (String version : versions) {
                    logger.info(path + " -> " + version);

                    String versionUUID = versionToUUID.get(version);

                    if (true) {
                        // current node
                        try {
                            JCRNodeWrapper page = (JCRNodeWrapper) session.getNode(path);
                            logger.info("Add version " + version + " on page " + page.getPath());
                            if (!page.isNodeType("jacademix:metadatas")) {
                                logger.info("    Add mixin jacademix:metadatas on " + page.getPath());
                                page.addMixin("jacademix:metadatas");
                                page.saveSession();
                            }
                            JCRPropertyWrapper platformVersionsProperty = null;
                            boolean plateformVersionAlreadySet = false;
                            try {
                                platformVersionsProperty = page.getProperty("platformVersions");
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
                                page.setProperty("platformVersions", new JCRValueWrapper[0]);
                                platformVersionsProperty = page.getProperty("platformVersions");
                            }

                            if (!plateformVersionAlreadySet) {
                                platformVersionsProperty = page.getProperty("platformVersions");
                                try {
                                    platformVersionsProperty.addValue(versionToNode.get(version),true);
                                } catch (javax.jcr.ValueFormatException e) {
                                    logger.info("    Error when trying to set value " + versionUUID + " (" + version + ") on " + page.getPath() + " " + e.getMessage());
                                }
                                page.setProperty("platformVersions", platformVersionsProperty.getValues());
                                if (doIt) {
                                    page.saveSession();
                                }
                                logger.info("     - Add version [" + version + "] -> done");
                            } else {
                                logger.info("     - Add version [" + version + "] -> already set");
                            }
                        } catch (PathNotFoundException e) {

                        }

                    }


                    def q = "SELECT * FROM [jnt:page] where ISDESCENDANTNODE('" + path + "')";
                    NodeIterator iterator = session.getWorkspace().getQueryManager().createQuery(q, Query.JCR_SQL2).execute().getNodes();


                    while (iterator.hasNext()) {
                        final JCRNodeWrapper page = (JCRNodeWrapper) iterator.nextNode();
                        logger.info("Add version " + version + " on page " + page.getPath());
                        if (!page.isNodeType("jacademix:metadatas")) {
                            logger.info("    Add mixin jacademix:metadatas on " + page.getPath());
                            page.addMixin("jacademix:metadatas");
                            page.saveSession();
                        }
                        JCRPropertyWrapper platformVersionsProperty = null;
                        boolean plateformVersionAlreadySet = false;
                        try {
                            platformVersionsProperty = page.getProperty("platformVersions");
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
                            page.setProperty("platformVersions", new JCRValueWrapper[0]);
                            platformVersionsProperty = page.getProperty("platformVersions");
                        }

                        if (!plateformVersionAlreadySet) {
                            try {
                                platformVersionsProperty.addValue(versionToNode.get(version),true);
                            } catch (javax.jcr.ValueFormatException e) {
                                logger.info("    Error when trying to set value " + versionUUID + " (" + version + ") on " + page.getPath());
                            }
                            page.setProperty("platformVersions", platformVersionsProperty.getValues());
                            if (doIt) {
                                page.saveSession();
                            }
                            logger.info("     - Add version [" + version + "] -> done");
                        } else {
                            logger.info("     - Add version [" + version + "] -> already set");
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
