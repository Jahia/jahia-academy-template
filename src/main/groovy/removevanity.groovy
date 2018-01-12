import org.jahia.api.Constants
import org.jahia.services.content.*
import org.jahia.services.sites.JahiaSite

import javax.jcr.NodeIterator
import javax.jcr.RepositoryException
import javax.jcr.query.Query

def logger = log;

boolean doIt = false;


def JahiaSite site = org.jahia.services.sites.JahiaSitesService.getInstance().getSiteByKey("academy");


for (Locale locale : site.getLanguagesAsLocales()) {
    JCRTemplate.getInstance().doExecuteWithSystemSession(null, Constants.EDIT_WORKSPACE, locale, new JCRCallback() {
        @Override
        Object doInJCR(JCRSessionWrapper session) throws RepositoryException {
            def q = "select * from [jmix:vanityUrlMapped] where isdescendantnode('/sites/academy/home/documentation/digital-experience-manager/722---do-not-suppress')";

            logger.info("Processing " + q)
            NodeIterator iterator = session.getWorkspace().getQueryManager().createQuery(q, Query.JCR_SQL2).execute().getNodes();
            while (iterator.hasNext()) {
                final JCRNodeWrapper node = (JCRNodeWrapper) iterator.nextNode();
                log.info(node.path);
                node.removeMixin("jmix:vanityUrlMapped");
            }

            q = "select * from [jnt:vanityUrls] where isdescendantnode('/sites/academy/home/documentation/digital-experience-manager/722---do-not-suppress')";

            logger.info("Processing " + q)
            iterator = session.getWorkspace().getQueryManager().createQuery(q, Query.JCR_SQL2).execute().getNodes();
            while (iterator.hasNext()) {
                final JCRNodeWrapper node = (JCRNodeWrapper) iterator.nextNode();
                log.info(node.path);
                node.remove();
            }
            if (doIt) {
                session.save();
            }

            if (doIt) {
                session.save();
            }
            return null;
        }
    }
    );
}
