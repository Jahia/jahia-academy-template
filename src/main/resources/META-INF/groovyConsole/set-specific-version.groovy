import org.jahia.api.Constants
import org.jahia.services.content.*
import org.jahia.services.sites.JahiaSite
import org.jahia.services.content.Value
import javax.jcr.Value
import javax.jcr.NodeIterator
import javax.jcr.RepositoryException
import javax.jcr.query.Query

def logger = log;

boolean doIt = false;


def JahiaSite site = org.jahia.services.sites.JahiaSitesService.getInstance().getSiteByKey("academy");
if (site != null) {
    for (Locale locale : site.getLanguagesAsLocales()) {
        JCRTemplate.getInstance().doExecuteWithSystemSession(null, Constants.EDIT_WORKSPACE, locale, new JCRCallback() {
            Object doInJCR(JCRSessionWrapper session) throws RepositoryException {
                JCRNodeWrapper refererenceNode = session.getNode("/sites/academy/tmp/document-area/tmp");
                JCRPropertyWrapper specificVersions = refererenceNode.getProperty("specificVersions");
                Value[] v = null;
                if (refererenceNode != null) {
                    v = specificVersions.getValues();
                }
                if (v == null) {
                    logger.info("Sorry, dude, but v is NULL");
                }
                if ("en".equals(locale.toString())) {
                    //def q = "select * from [cnt:blog] where isdescendantnode('/sites/jahiacom/home/hot-topics/blog')";
                    def q = "select * from [jacademy:document] where isdescendantnode('/sites/academy/home/documentation/Techwiki')";
                    logger.info("Processing " + q)
                    NodeIterator iterator = session.getWorkspace().getQueryManager().createQuery(q, Query.JCR_SQL2).execute().getNodes();
                    while (iterator.hasNext()) {
                        final JCRNodeWrapper documentNode = (JCRNodeWrapper) iterator.nextNode();
                        if (v != null) {
                            if (!documentNode.isNodeType("jacademix:specificVersions")) {
                                documentNode.addMixin("jacademix:specificVersions");
                            }
                            documentNode.setProperty("specificVersions", v);
                            logger.info(documentNode.getPath());
                        }
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