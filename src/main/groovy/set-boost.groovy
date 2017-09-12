import org.jahia.api.Constants
import org.jahia.services.content.*
import org.jahia.services.sites.JahiaSite
import org.jahia.taglibs.jcr.node.JCRTagUtils

import javax.jcr.NodeIterator
import javax.jcr.RepositoryException
import javax.jcr.query.Query

def logger = log;

boolean doIt = false;


def JahiaSite site = org.jahia.services.sites.JahiaSitesService.getInstance().getSiteByKey("academy");

Set<String> nodesToAutoPublish = new HashSet<String>();

for (Locale locale : site.getLanguagesAsLocales()) {
    JCRTemplate.getInstance().doExecuteWithSystemSession(null, Constants.EDIT_WORKSPACE, locale, new JCRCallback() {
        @Override
        Object doInJCR(JCRSessionWrapper session) throws RepositoryException {
            def q = "SELECT * FROM [jacademix:isVersionPage]";

            NodeIterator iterator = session.getWorkspace().getQueryManager().createQuery(q, Query.JCR_SQL2).execute().getNodes();
            while (iterator.hasNext()) {
                final JCRNodeWrapper versionNode = (JCRNodeWrapper) iterator.nextNode();
                String version = versionNode.getPropertyAsString("version");
                boolean isCurrent = "current".equals(version);
                def q2 = "SELECT * FROM [jacademix:document] WHERE ISDESCENDANTNODE ('" + versionNode.getPath() + "')";
                NodeIterator iterator2 = session.getWorkspace().getQueryManager().createQuery(q2, Query.JCR_SQL2).execute().getNodes();
                while (iterator2.hasNext()) {
                    final JCRNodeWrapper documentNode = (JCRNodeWrapper) iterator2.nextNode();
                    boolean canPublish = hasPendingModification(documentNode);
                    if (JCRTagUtils.isNodeType(documentNode, 'jacademy:boost')) {
                        if (isCurrent) {
                            boolean boost = false;
                            JCRPropertyWrapper property = documentNode.getProperty("boost");
                            if (property != null) {
                                boost = property.getBoolean();
                            }
                            if (! boost) {
                                documentNode.setProperty("boost", true);
                                if (canPublish) {
                                    nodesToAutoPublish.add(documentNode.getIdentifier());
                                }
                                logger.info("Add boost for " + documentNode.getPath());
                            }
                        } else {
                            documentNode.removeMixin('jacademy:boost');
                            if (canPublish) {
                                nodesToAutoPublish.add(documentNode.getIdentifier());
                            }
                            logger.info("Remove boost for " + documentNode.getPath());
                        }
                    } else {
                        if (isCurrent) {
                            documentNode.addMixin('jacademy:boost');
                            documentNode.setProperty("boost", true);
                            if (canPublish) {
                                nodesToAutoPublish.add(documentNode.getIdentifier());
                            }
                            logger.info("Add boost + mixin for " + documentNode.getPath());
                        }
                    }
                }
            }
            if (doIt) {
                session.save();
            }
            if (CollectionUtils.isNotEmpty(nodesToAutoPublish)) {
                if (doIt) {
                    JCRPublicationService.getInstance().publish(nodesToAutoPublish.asList(), Constants.EDIT_WORKSPACE, Constants.LIVE_WORKSPACE);
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