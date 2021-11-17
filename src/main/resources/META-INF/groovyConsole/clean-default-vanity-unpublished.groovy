import org.jahia.api.Constants
import org.jahia.services.content.*
import org.jahia.services.sites.JahiaSite

import javax.jcr.NodeIterator
import javax.jcr.RepositoryException
import javax.jcr.query.Query

def logger = log;

def JahiaSite site = org.jahia.services.sites.JahiaSitesService.getInstance().getSiteByKey("academy");
Set<String> vanityNodesToAutoPublish = new HashSet<String>();

for (Locale locale : site.getLanguagesAsLocales()) {
    JCRTemplate.getInstance().doExecuteWithSystemSession(null, Constants.EDIT_WORKSPACE, locale, new JCRCallback() {
        @Override
        Object doInJCR(JCRSessionWrapper session) throws RepositoryException {
            def q = "select * from [jnt:vanityUrl] where isdescendantnode('/sites/academy/home') and [j:default]='true'";
            logger.info("Processing " + q)
            NodeIterator iterator = session.getWorkspace().getQueryManager().createQuery(q, Query.JCR_SQL2).execute().getNodes();
            while (iterator.hasNext()) {
                final JCRNodeWrapper node = (JCRNodeWrapper) iterator.nextNode();
                if (node.hasProperty('j:published')) {
                    String created = node.getPropertyAsString('jcr:created');
                    if (created.startsWith("2021-09-09T")) {
                        node.setProperty('j:default','false');
                        vanityNodesToAutoPublish.add(node.getIdentifier());
                        node.saveSession();
                        logger.warn(created + " " + node.getPath());
                    }

                }

            }
            //session.save();
            if (CollectionUtils.isNotEmpty(vanityNodesToAutoPublish)) {
                JCRPublicationService.getInstance().publish(vanityNodesToAutoPublish.asList(), Constants.EDIT_WORKSPACE, Constants.LIVE_WORKSPACE, null);
                logger.info("");
                logger.info("Nodes which where republished:")
                for (String identifier : vanityNodesToAutoPublish) {
                    logger.warn("   " + session.getNodeByIdentifier(identifier).getPath());
                }
            }
            return null;
        }
    }
    );
}
