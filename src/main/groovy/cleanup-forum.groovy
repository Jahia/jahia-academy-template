import org.jahia.api.Constants
import org.jahia.services.content.*
import org.jahia.services.sites.JahiaSite
import org.jahia.taglibs.jcr.node.JCRTagUtils

import javax.jcr.NodeIterator
import javax.jcr.RepositoryException
import javax.jcr.query.Query

def logger = log;

boolean doIt = false;


def JahiaSite site = org.jahia.services.sites.JahiaSitesService.getInstance().getSiteByKey("jahiacom");


for (Locale locale : site.getLanguagesAsLocales()) {
    if ('en'.equals(locale.toString())) {
        JCRTemplate.getInstance().doExecuteWithSystemSession(null, Constants.LIVE_WORKSPACE, locale, new JCRCallback() {
            @Override
            Object doInJCR(JCRSessionWrapper session) throws RepositoryException {
                def q = "select * from [jmix:spamFilteringSpamDetected]";

                logger.info("Processing " + q)
                NodeIterator iterator = session.getWorkspace().getQueryManager().createQuery(q, Query.JCR_SQL2).execute().getNodes();
                while (iterator.hasNext()) {
                    final JCRNodeWrapper node = (JCRNodeWrapper) iterator.nextNode();
                    logger.info("Remove spam " + node.getPath() + " [" + node.getDisplayableName() + "]");
                    node.remove();
                }
                if (doIt) {
                    session.save();
                }


                q = "select * from [jnt:topic]";
                logger.info("Processing " + q)
                iterator = session.getWorkspace().getQueryManager().createQuery(q, Query.JCR_SQL2).execute().getNodes();
                while (iterator.hasNext()) {
                    final JCRNodeWrapper node = (JCRNodeWrapper) iterator.nextNode();
                    List<JCRNodeWrapper> posts = JCRTagUtils.getChildrenOfType(node,'jnt:post');
                    if (posts.size() == 0) {
                        logger.info("Could not find posts for " + node.getPath() + " [" + node.getDisplayableName() + "]");
                        node.remove();
                    }
                    //log.info(node.path);
                }
                if (doIt) {
                    session.save();
                }
                return null;
            }
        });
    }

}
