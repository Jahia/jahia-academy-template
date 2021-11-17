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

HashMap<String, String> documentTypeToUUID = new HashMap<String, String>();
HashMap<String, JCRNodeWrapper> documentTypeToNode = new HashMap<String, String>();

//String nodeType = "jnt:page";
String nodeType = "jacademy:kbEntry";

def documentTypeForPath = new HashMap<String, Object>();
//documentTypeForPath.put("/sites/academy/home/downloads/jahia8-release-notes",["release-notes"]);
//documentTypeForPath.put("/sites/academy/home/downloads/javadoc-and-taglib-doc",["technical"]);
//documentTypeForPath.put("/sites/academy/home/jahia-8/jahia-8-interface",["functional"]);
//documentTypeForPath.put("/sites/academy/home/jahia-8/jahia-8-functional-administrators",["functional"]);
//documentTypeForPath.put("/sites/academy/home/jahia-8/jahia-8-changes-for-system-administrators",["technical"]);
//documentTypeForPath.put("/sites/academy/home/jahia-8/jahia-8-for-developers",["technical"]);
//documentTypeForPath.put("/sites/academy/home/jahia-8/known-limitations",["technical"]);
//documentTypeForPath.put("/sites/academy/home/documentation/end-user",["functional"]);
//documentTypeForPath.put("/sites/academy/home/documentation/system-administrator",["technical"]);
//documentTypeForPath.put("/sites/academy/home/documentation/system-administrator/dx/73/release-notes",["release-notes"]);
//documentTypeForPath.put("/sites/academy/home/documentation/system-administrator/jexperience/2.0/release-notes",["release-notes"]);
//documentTypeForPath.put("/sites/academy/home/documentation/system-administrator/jexperience/111/release-notes",["release-notes"]);
//documentTypeForPath.put("/sites/academy/home/documentation/system-administrator/ff/3-3/release-notes",["release-notes"]);
//documentTypeForPath.put("/sites/academy/home/documentation/system-administrator/ff/2-7/release-notes",["release-notes"]);
//documentTypeForPath.put("/sites/academy/home/documentation/developer",["technical"]);
//documentTypeForPath.put("/sites/academy/home/community/community-upgrades",["technical"]);
//documentTypeForPath.put("/sites/academy/home/community/migrate-from-jahia-73-community-edition-to-jahia-8",["technical"]);
//documentTypeForPath.put("/sites/academy/home/customer-center/jahia",["technical"]);
//documentTypeForPath.put("/sites/academy/home/customer-center/jahia/previous-versions/digital-factory-70/release-notes",["release-notes"]);
//documentTypeForPath.put("/sites/academy/home/customer-center/jexperience",["technical"]);
//documentTypeForPath.put("/sites/academy/home/customer-center/forms",["technical"]);
//documentTypeForPath.put("/sites/academy/home/customer-center/stackconnect",["technical"]);
//documentTypeForPath.put("/sites/academy/home/customer-center/jahia-cloud",["release-notes"]);
//documentTypeForPath.put("/sites/academy/home/training--kb/tutorials",["tutorials"]);
//documentTypeForPath.put("/sites/academy/home/training--kb/online-developer-training",["tutorials"]);
//documentTypeForPath.put("/sites/academy/home/training--kb/knowledge-base",["knowledge-base"]);
//documentTypeForPath.put("/sites/academy/home/legacy-1/digital-experience-manager/7-2/72",["functional"]);
//documentTypeForPath.put("/sites/academy/home/legacy-1/digital-experience-manager/7-2/72-1",["technical"]);
//documentTypeForPath.put("/sites/academy/home/legacy-1/digital-experience-manager/7-2/72-1/release-notes",["release-notes"]);
//documentTypeForPath.put("/sites/academy/home/legacy-1/digital-experience-manager/7-2/72-2",["technical"]);
//documentTypeForPath.put("/sites/academy/home/legacy-1/digital-experience-manager/71/specific/websphere-v855-installation-guid",["technical"]);
//documentTypeForPath.put("/sites/academy/home/legacy-1/digital-experience-manager/71/functional",["functional"]);
//documentTypeForPath.put("/sites/academy/home/legacy-1/digital-experience-manager/71/technical",["technical"]);
//documentTypeForPath.put("/sites/academy/home/legacy-1/digital-experience-manager/71/admin",["technical"]);
//documentTypeForPath.put("/sites/academy/home/legacy-1/digital-experience-manager/71/development",["technical"]);
//documentTypeForPath.put("/sites/academy/home/legacy-1/digital-experience-manager/71/discovery/release-notes",["release-notes"]);
//documentTypeForPath.put("/sites/academy/home/legacy-1/marketing-factory/1.10/end-user",["functional"]);
//documentTypeForPath.put("/sites/academy/home/legacy-1/marketing-factory/1.10/system-administrator/",["technical"]);
//documentTypeForPath.put("/sites/academy/home/legacy-1/marketing-factory/1.10/system-administrator/release-notes",["release-notes"]);
//documentTypeForPath.put("/sites/academy/home/legacy-1/marketing-factory/1.10/Developer",["technical"]);
//documentTypeForPath.put("/sites/academy/home/legacy-1/marketing-factory/19/end-user",["functional"]);
//documentTypeForPath.put("/sites/academy/home/legacy-1/marketing-factory/19/system-administrator",["technical"]);
//documentTypeForPath.put("/sites/academy/home/legacy-1/marketing-factory/19/system-administrator/release-notes",["release-notes"]);
//documentTypeForPath.put("/sites/academy/home/legacy-1/marketing-factory/19/developer",["technical"]);
//documentTypeForPath.put("/sites/academy/home/legacy-1/marketing-factory/18/technical",["technical"]);
//documentTypeForPath.put("/sites/academy/home/legacy-1/marketing-factory/18/user-guide",["functional"]);
//documentTypeForPath.put("/sites/academy/home/legacy-1/marketing-factory/18/release-notes",["release-notes"]);
//documentTypeForPath.put("/sites/academy/home/legacy-1/marketing-factory/17/technical",["technical"]);
//documentTypeForPath.put("/sites/academy/home/legacy-1/marketing-factory/17/functional",["functional"]);
//documentTypeForPath.put("/sites/academy/home/legacy-1/marketing-factory/17/release-notes",["release-notes"]);
//documentTypeForPath.put("/sites/academy/home/legacy-1/form-factory/30/end-user",["functional"]);
//documentTypeForPath.put("/sites/academy/home/legacy-1/form-factory/30/system-administrator",["technical"]);
//documentTypeForPath.put("/sites/academy/home/legacy-1/form-factory/30/system-administrator/release-notes",["release-notes"]);
//documentTypeForPath.put("/sites/academy/home/legacy-1/form-factory/30/developer",["technical"]);
//documentTypeForPath.put("/sites/academy/home/legacy-1/form-factory/24/end-user",["functional"]);
//documentTypeForPath.put("/sites/academy/home/legacy-1/form-factory/24/system-administrator",["technical"]);
//documentTypeForPath.put("/sites/academy/home/legacy-1/form-factory/24/system-administrator/release-notes",["release-notes"]);
//documentTypeForPath.put("/sites/academy/home/legacy-1/form-factory/24/developer",["technical"]);
//documentTypeForPath.put("/sites/academy/home/legacy-1/form-factory/23/end-user",["functional"]);
//documentTypeForPath.put("/sites/academy/home/legacy-1/form-factory/23/system-administrator",["technical"]);
//documentTypeForPath.put("/sites/academy/home/legacy-1/form-factory/23/system-administrator/release-notes",["release-notes"]);
//documentTypeForPath.put("/sites/academy/home/legacy-1/form-factory/23/developer",["technical"]);
//documentTypeForPath.put("/sites/academy/home/legacy-1/form-factory/22/end-user",["functional"]);
//documentTypeForPath.put("/sites/academy/home/legacy-1/form-factory/22/system-administrator",["technical"]);
//documentTypeForPath.put("/sites/academy/home/legacy-1/form-factory/22/system-administrator/release-notes",["release-notes"]);
//documentTypeForPath.put("/sites/academy/home/legacy-1/form-factory/22/developer",["technical"]);
//documentTypeForPath.put("/sites/academy/home/legacy-1/form-factory/21/functional-1",["functional"]);
//documentTypeForPath.put("/sites/academy/home/legacy-1/form-factory/21/technical-1",["technical"]);
//documentTypeForPath.put("/sites/academy/home/legacy-1/form-factory/21/release-notes",["release-notes"]);
//documentTypeForPath.put("/sites/academy/home/legacy-1/form-factory/20/functional",["functional"]);
//documentTypeForPath.put("/sites/academy/home/legacy-1/form-factory/20/technical",["technical"]);
//documentTypeForPath.put("/sites/academy/home/legacy-1/form-factory/20/release-notes",["release-notes"]);
//documentTypeForPath.put("/sites/academy/home/legacy-1/form-factory/10/technical",["technical"]);
//documentTypeForPath.put("/sites/academy/home/legacy-1/form-factory",["release-notes"]);
documentTypeForPath.put("/sites/academy/home/training--kb/knowledge-base",["knowledge-base"]);

for (Locale locale : site.getLanguagesAsLocales()) {
    JCRTemplate.getInstance().doExecuteWithSystemSession(null, Constants.EDIT_WORKSPACE, locale, new JCRCallback() {
        @Override
        Object doInJCR(JCRSessionWrapper session) throws RepositoryException {
            JCRNodeWrapper documentTypesNode = null;
            try {
                documentTypesNode = session.getNode("/sites/systemsite/categories/documentTypes");
            } catch (javax.jcr.PathNotFoundException e) {
                JCRNodeWrapper rootCategoryNode = session.getNode("/sites/systemsite/categories");
                documentTypesNode = rootCategoryNode.addNode("documentTypes", "jnt:category");
                documentTypesNode.setProperty("jcr:title", "Document Types");
                session.save();
            }

            JCRNodeWrapper technicalNode = null;
            try {
                technicalNode = session.getNode("/sites/systemsite/categories/documentTypes/technical");
            } catch (javax.jcr.PathNotFoundException e) {
                technicalNode = documentTypesNode.addNode("technical", "jnt:category");
                technicalNode.setProperty("jcr:title", "Technical");
                session.save();
            }
            documentTypeToUUID.put("technical",technicalNode.getIdentifier());
            documentTypeToNode.put("technical",technicalNode);

            JCRNodeWrapper functionalNode = null;
            try {
                functionalNode = session.getNode("/sites/systemsite/categories/documentTypes/functional");
            } catch (javax.jcr.PathNotFoundException e) {
                functionalNode = documentTypesNode.addNode("functional", "jnt:category");
                functionalNode.setProperty("jcr:title", "Functional");
                session.save();
            }
            documentTypeToUUID.put("functional",functionalNode.getIdentifier());
            documentTypeToNode.put("functional",functionalNode);

            JCRNodeWrapper knowledgeBaseNode = null;
            try {
                knowledgeBaseNode = session.getNode("/sites/systemsite/categories/documentTypes/knowledge-base");
            } catch (javax.jcr.PathNotFoundException e) {
                knowledgeBaseNode = documentTypesNode.addNode("knowledge-base", "jnt:category");
                knowledgeBaseNode.setProperty("jcr:title", "Knowledge base");
                session.save();
            }
            documentTypeToUUID.put("knowledge-base",knowledgeBaseNode.getIdentifier());
            documentTypeToNode.put("knowledge-base",knowledgeBaseNode);

            JCRNodeWrapper tutorialsNode = null;
            try {
                tutorialsNode = session.getNode("/sites/systemsite/categories/documentTypes/tutorials");
            } catch (javax.jcr.PathNotFoundException e) {
                tutorialsNode = documentTypesNode.addNode("tutorials", "jnt:category");
                tutorialsNode.setProperty("jcr:title", "Tutorials");
                session.save();
            }
            documentTypeToUUID.put("tutorials",tutorialsNode.getIdentifier());
            documentTypeToNode.put("tutorials",tutorialsNode);

            JCRNodeWrapper releaseNotesNode = null;
            try {
                releaseNotesNode = session.getNode("/sites/systemsite/categories/documentTypes/release-notes");
            } catch (javax.jcr.PathNotFoundException e) {
                releaseNotesNode = documentTypesNode.addNode("release-notes", "jnt:category");
                releaseNotesNode.setProperty("jcr:title", "Release notes");
                session.save();
            }
            documentTypeToUUID.put("release-notes",releaseNotesNode.getIdentifier());
            documentTypeToNode.put("release-notes",releaseNotesNode);

            for (String path : documentTypeForPath.keySet()) {
                def versions = documentTypeForPath.get(path);
                for (String version : versions) {
                    logger.info(path + " -> " + version);

                    String versionUUID = documentTypeToUUID.get(version);

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
                                platformVersionsProperty = page.getProperty("documentTypes");
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
                                page.setProperty("documentTypes", new JCRValueWrapper[0]);
                                platformVersionsProperty = page.getProperty("documentTypes");
                            }

                            if (!plateformVersionAlreadySet) {
                                platformVersionsProperty = page.getProperty("documentTypes");
                                try {
                                    platformVersionsProperty.addValue(documentTypeToNode.get(version),true);
                                } catch (javax.jcr.ValueFormatException e) {
                                    logger.info("    Error when trying to set value " + versionUUID + " (" + version + ") on " + page.getPath() + " " + e.getMessage());
                                }
                                page.setProperty("documentTypes", platformVersionsProperty.getValues());
                                if (doIt) {
                                    page.saveSession();
                                }
                                logger.info("     - Add document type [" + version + "] -> done");
                            } else {
                                logger.info("     - Add document type [" + version + "] -> already set");
                            }
                        } catch (PathNotFoundException e) {

                        }

                    }


                    def q = "SELECT * FROM [" + nodeType + "] where ISDESCENDANTNODE('" + path + "')";
                    NodeIterator iterator = session.getWorkspace().getQueryManager().createQuery(q, Query.JCR_SQL2).execute().getNodes();


                    while (iterator.hasNext()) {
                        final JCRNodeWrapper page = (JCRNodeWrapper) iterator.nextNode();
                        logger.info("Add document type " + version + " on page " + page.getPath());
                        if (!page.isNodeType("jacademix:metadatas")) {
                            logger.info("    Add mixin jacademix:metadatas on " + page.getPath());
                            page.addMixin("jacademix:metadatas");
                            page.saveSession();
                        }
                        JCRPropertyWrapper platformVersionsProperty = null;
                        boolean plateformVersionAlreadySet = false;
                        try {
                            platformVersionsProperty = page.getProperty("documentTypes");
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
                            page.setProperty("documentTypes", new JCRValueWrapper[0]);
                            platformVersionsProperty = page.getProperty("documentTypes");
                        }

                        if (!plateformVersionAlreadySet) {
                            try {
                                platformVersionsProperty.addValue(documentTypeToNode.get(version),true);
                            } catch (javax.jcr.ValueFormatException e) {
                                logger.info("    Error when trying to set value " + versionUUID + " (" + version + ") on " + page.getPath());
                            }
                            page.setProperty("documentTypes", platformVersionsProperty.getValues());
                            if (doIt) {
                                page.saveSession();
                            }
                            logger.info("     - Add document type [" + version + "] -> done");
                        } else {
                            logger.info("     - Add document type [" + version + "] -> already set");
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
