import org.jahia.api.Constants
import org.jahia.services.content.*
import org.jahia.services.sites.JahiaSite
import org.jahia.taglibs.jcr.node.JCRTagUtils

import javax.jcr.NodeIterator
import javax.jcr.RepositoryException
import javax.jcr.query.Query

def logger = log;
boolean doIt = false
def JahiaSite site = org.jahia.services.sites.JahiaSitesService.getInstance().getSiteByKey("academy");

Set<String> nodesToAutoPublish = new HashSet<String>();
Set<String> nodesToManuallyPublish = new HashSet<String>();

JCRTemplate.getInstance().doExecuteWithSystemSession(null, Constants.EDIT_WORKSPACE, new JCRCallback() {
    @Override
    Object doInJCR(JCRSessionWrapper session) throws RepositoryException {
        def q = "SELECT * FROM [jacademix:isVersionPage] where NOT ['version']='current'";

        NodeIterator iterator = session.getWorkspace().getQueryManager().createQuery(q, Query.JCR_SQL2).execute().getNodes();
        while (iterator.hasNext()) {
            final JCRNodeWrapper versionNode = (JCRNodeWrapper) iterator.nextNode();
            String version = versionNode.getPropertyAsString("version");
            def q2 = "SELECT * FROM [jacademix:document] WHERE ISDESCENDANTNODE ('" + versionNode.getPath() + "')";
            NodeIterator iterator2 = session.getWorkspace().getQueryManager().createQuery(q2, Query.JCR_SQL2).execute().getNodes();
            while (iterator2.hasNext()) {
                final JCRNodeWrapper documentNode = (JCRNodeWrapper) iterator2.nextNode();
                JCRNodeWrapper parentPage = JCRTagUtils.getParentOfType(documentNode, 'jnt:page');
                boolean needToPublish = false;
                if (!parentPage.isNodeType('jmix:noindex')) {
                    parentPage.addMixin('jmix:noindex');
                    needToPublish = true;
                    logger.info("Add noindex mixin on page " + parentPage.getPath() + " (" + version + " is not the curent version)");
                }
                if (parentPage.isNodeType('jmix:sitemap')) {
                    parentPage.removeMixin('jmix:sitemap');
                    needToPublish = true;
                }
                if (needToPublish) {
                    if (hasPendingModification(parentPage)) {
                        nodesToManuallyPublish.add(parentPage.identifier);
                    } else {
                        nodesToAutoPublish.add(parentPage.identifier);
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
        if (CollectionUtils.isNotEmpty(nodesToManuallyPublish)) {
            logger.info("");
            logger.info("Nodes to publish manually:")
            for (String identifier : nodesToManuallyPublish) {
                logger.warn("   " + session.getNodeByIdentifier(identifier).getPath());
            }
        }

        return null;
    }
});



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