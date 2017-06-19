import org.jahia.api.Constants
import org.jahia.services.SpringContextSingleton
import org.jahia.services.content.JCRCallback
import org.jahia.services.content.JCRNodeWrapper
import org.jahia.services.content.JCRPublicationService
import org.jahia.services.content.JCRSessionWrapper
import org.jahia.services.content.JCRTemplate
import org.jahia.services.content.JCRValueWrapper
import org.jahia.services.content.PublicationInfo
import org.jahia.services.seo.VanityUrl
import org.jahia.services.seo.jcr.VanityUrlManager
import org.jahia.services.sites.JahiaSite
import org.jahia.taglibs.jcr.node.JCRTagUtils
import java.text.Normalizer

import javax.jcr.NodeIterator
import javax.jcr.RepositoryException
import javax.jcr.query.Query

// set doIt to true to execute (if false, nothing is changed)
boolean doIt = false;

// enf of configutation
// ----------------------------------------------------------------------------
Set<String> nodesToAutoPublish = new HashSet<String>();
Set<String> nodesToManuelPublish = new HashSet<String>();

Set<String> currentVersionPath = new HashSet<String>();


Collection contentTypes = new HashSet<String>();

def logger = log;

def JahiaSite site = org.jahia.services.sites.JahiaSitesService.getInstance().getSiteByKey("academy");
if (site != null) {
    for (Locale locale : site.getLanguagesAsLocales()) {

        if ("en".equals(locale.toString())) {

            JCRTemplate.getInstance().doExecuteWithSystemSession(null, Constants.EDIT_WORKSPACE, locale, new JCRCallback() {
                @Override
                Object doInJCR(JCRSessionWrapper session) throws RepositoryException {

                    // get all current versions;
                    String stmt = "select * from [jacademix:isVersionPage]";
                    NodeIterator iterator = session.getWorkspace().getQueryManager().createQuery(stmt, Query.JCR_SQL2).execute().getNodes();
                    while (iterator.hasNext()) {
                        JCRNodeWrapper versionPage = (JCRNodeWrapper) iterator.nextNode();
                        String version = versionPage.getPropertyAsString("version");
                        if ("current".equals(version)) {
                            currentVersionPath.add(versionPage.getPath());
                        }


                    }

                    // get all version page
                    stmt = "select * from [jacademix:document]";
                    iterator = session.getWorkspace().getQueryManager().createQuery(stmt, Query.JCR_SQL2).execute().getNodes();
                    while (iterator.hasNext()) {
                        final JCRNodeWrapper documentNode = (JCRNodeWrapper) iterator.nextNode();
                        boolean isCurrent = false;
                        Iterator currentVersionPathIterator = currentVersionPath.iterator();
                        while (currentVersionPathIterator.hasNext()) {
                            String currentPath = currentVersionPathIterator.next();
                            if (documentNode.getPath().contains(currentPath)) {
                                isCurrent = true;
                            }
                        }
                        if (isCurrent) {
                            if (documentNode.isNodeType("jacademy:boost")) {
                                boolean boost = documentNode.getProperty("boost").getBoolean();
                                if (!boost) {
                                    if (hasPendingModification(documentNode)) {
                                        nodesToManuelPublish.add(documentNode.identifier);
                                    } else {
                                        nodesToAutoPublish.add(documentNode.identifier);
                                    }
                                    logger.info("+ " + documentNode.getPath());
                                    documentNode.setProperty("boost", true);
                                }
                            } else {
                                if (hasPendingModification(documentNode)) {
                                    nodesToManuelPublish.add(documentNode.identifier);
                                } else {
                                    nodesToAutoPublish.add(documentNode.identifier);
                                }
                                logger.info("+ " + documentNode.getPath());
                                documentNode.addMixin("jacademy:boost");
                                documentNode.setProperty("boost", true);

                            }
                        } else {
                            if (documentNode.isNodeType("jacademy:boost")) {
                                if (hasPendingModification(documentNode)) {
                                    nodesToManuelPublish.add(documentNode.identifier);
                                } else {
                                    nodesToAutoPublish.add(documentNode.identifier);
                                }
                                logger.info("- " + documentNode.getPath());
                                documentNode.removeMixin("jacademy:boost");
                            }

                        }

                    }
                    if (doIt) {
                        session.save();
                    }


                    if (CollectionUtils.isNotEmpty(nodesToAutoPublish)) {
                        if (doIt) {
                            JCRPublicationService.getInstance().publish(nodesToAutoPublish.asList(), Constants.EDIT_WORKSPACE, Constants.LIVE_WORKSPACE, false, "Change boost status")
                        };
                        logger.info("");
                        logger.info("Nodes which where republished:")
                        for (String identifier : nodesToAutoPublish) {
                            logger.warn("   " + session.getNodeByIdentifier(identifier).getPath());
                        }
                    }
                    if (CollectionUtils.isNotEmpty(nodesToManuelPublish)) {

                        logger.info("");
                        logger.info("Nodes publish manually:")
                        for (String identifier : nodesToManuelPublish) {
                            logger.warn("   " + identifier + " " + session.getNodeByIdentifier(identifier).getPath()) + "/vanityUrlMapping/*";
                        }
                    }
                    if (doIt) {
                        session.save();
                    }
                    return null;
                }
            });
        }
    }
}
logger.info("End of set-boost script");

public boolean hasPendingModification(final JCRNodeWrapper node) throws RepositoryException {
    try {
        if (!Constants.EDIT_WORKSPACE.equals(node.getSession().getWorkspace().getName())) {
            throw new IllegalArgumentException("The node has to be accessed through a session opened on the default workspace");
        }
        return JCRTemplate.getInstance().doExecuteWithSystemSession(null, Constants.LIVE_WORKSPACE, null, new JCRCallback<Boolean>() {
            public Boolean doInJCR(JCRSessionWrapper session) throws RepositoryException {
                int status = JCRPublicationService.getInstance().getStatus(node, session, null);
                if (status == null) {
                    status = PublicationInfo.UNPUBLISHED;
                }
                return PublicationInfo.PUBLISHED != status;
            }
        });
    } catch (Exception e) {
        return false;
    }
}
