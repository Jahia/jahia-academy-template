import org.jahia.api.Constants
import org.jahia.services.SpringContextSingleton
import org.jahia.services.cache.Cache
import org.jahia.services.cache.CacheService
import org.jahia.services.content.*
import org.jahia.services.sites.JahiaSite
import org.jahia.taglibs.jcr.node.JCRTagUtils

import javax.jcr.NodeIterator
import javax.jcr.RepositoryException
import javax.jcr.query.Query

def logger = log;

boolean doIt = false;


def JahiaSite site = org.jahia.services.sites.JahiaSitesService.getInstance().getSiteByKey("jahiacom");

boolean needToFlush = false;

JCRTemplate.getInstance().doExecuteWithSystemSession(null, Constants.LIVE_WORKSPACE, new JCRCallback() {
    @Override
    Object doInJCR(JCRSessionWrapper session) throws RepositoryException {
        def q = "select * from [jmix:spamFilteringSpamDetected]";

        logger.info("Remove spam");
        NodeIterator iterator = session.getWorkspace().getQueryManager().createQuery(q, Query.JCR_SQL2).execute().getNodes();
        while (iterator.hasNext()) {
            final JCRNodeWrapper node = (JCRNodeWrapper) iterator.nextNode();
            logger.info("Remove spam " + node.getPath() + " [" + node.getDisplayableName().trim() + "]");
            needToFlush = true;
            node.remove();
        }
        if (doIt) {
            session.save();
        }


        q = "select * from [jnt:topic]";
        logger.info("Remove empty topics");
        iterator = session.getWorkspace().getQueryManager().createQuery(q, Query.JCR_SQL2).execute().getNodes();
        while (iterator.hasNext()) {
            final JCRNodeWrapper node = (JCRNodeWrapper) iterator.nextNode();
            List<JCRNodeWrapper> posts = JCRTagUtils.getChildrenOfType(node,'jnt:post');
            if (posts.size() == 0) {
                logger.info("Remove empty posts " + node.getPath() + " [" + node.getDisplayableName().trim() + "]");
                needToFlush = true;
                node.remove();
            }
            //log.info(node.path);
        }
        if (doIt) {
            session.save();
        }
        q = "select * from [jnt:topic]";
        logger.info("Fix Topic Last Contribution Date")
        iterator = session.getWorkspace().getQueryManager().createQuery(q, Query.JCR_SQL2).execute().getNodes();
        while (iterator.hasNext()) {
            final JCRNodeWrapper topic = (JCRNodeWrapper) iterator.nextNode();
            try {
                long currentTopicTime = topic.getProperty("topicLastContributionDate").getDate().getTimeInMillis();
                java.util.GregorianCalendar latestCal = null;
                long latestTime = -1;
                List<JCRNodeWrapper> posts = JCRContentUtils.getChildrenOfType(topic, "jnt:post")
                if (posts != null) {
                    for (int i = 0; i < posts.size(); i++) {
                        JCRNodeWrapper post = posts.get(i);
                        if (!post.isNodeType("jmix:spamFilteringSpamDetected")) {
                            java.util.GregorianCalendar cal = post.getProperty("jcr:created").getDate();
                            long time = cal.getTimeInMillis();
                            if (time > latestTime) {
                                latestTime = time;
                                latestCal = cal;
                            }
                        }
                    }
                }
                if (latestTime != -1 && Math.abs(latestTime - currentTopicTime) > 1000) {
                    topic.setProperty("topicLastContributionDate", latestCal);
                    long diff = (latestTime - currentTopicTime) / 1000;
                    logger.info("Reset Last Contribution Date for " + topic.getPath() + " -> diff " + diff.toString() + "s");
                    needToFlush = true;
                }
            } catch (javax.jcr.PathNotFoundException e) {
                e.getMessage();
            }
        }
        if (doIt) {
            session.save();
        }
        logger.info("Flush cache");
        if (doIt && needToFlush) {
            CacheService cacheService = (CacheService) SpringContextSingleton.getBean("JahiaCacheService");
            Cache autoCompleteSearchCache = cacheService.getCache("HTMLCache", true);
            logger.info("Flushing " + autoCompleteSearchCache.size() + " HTMLCache entries...");
            autoCompleteSearchCache.flush();
        }


        return null;

    }
});

