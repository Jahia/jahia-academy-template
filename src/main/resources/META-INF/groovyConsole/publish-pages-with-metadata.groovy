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
/*
This script publish updated content of type jacademix:metadatas
 */
Set<String> nodesToAutoPublish = new HashSet<String>();

for (Locale locale : site.getLanguagesAsLocales()) {
    JCRTemplate.getInstance().doExecuteWithSystemSession(null, Constants.EDIT_WORKSPACE, locale, new JCRCallback() {
        @Override
        Object doInJCR(JCRSessionWrapper session) throws RepositoryException {
            def q = "SELECT * FROM [jacademix:metadatas]";

            NodeIterator iterator = session.getWorkspace().getQueryManager().createQuery(q, Query.JCR_SQL2).execute().getNodes();
            while (iterator.hasNext()) {
                final JCRNodeWrapper node = (JCRNodeWrapper) iterator.nextNode();
                int status = JCRPublicationService.getInstance().getStatus(node, session, null);
                /*
                    public static final int PUBLISHED = 1;
                    public static final int MODIFIED = 3;
                    public static final int NOT_PUBLISHED = 4;
                    public static final int UNPUBLISHED = 5;
                    public static final int MANDATORY_LANGUAGE_UNPUBLISHABLE = 6;
                    public static final int LIVE_MODIFIED = 7;
                    public static final int LIVE_ONLY = 8;
                    public static final int CONFLICT = 9;
                    public static final int MANDATORY_LANGUAGE_VALID = 10;
                    public static final int DELETED = 11;
                    public static final int MARKED_FOR_DELETION = 12;

                 */
                if (status == null) {
                    status = PublicationInfo.UNPUBLISHED;
                }

                logger.info(node.getPath() + " [" + PublicationInfo.getLabel(status) + "]");
                if (status == PublicationInfo.MODIFIED) {
                    nodesToAutoPublish.add(node.getIdentifier());
                }
            }

            if (doIt) {
                session.save();
            }
            if (CollectionUtils.isNotEmpty(nodesToAutoPublish)) {
                if (doIt) {
                    JCRPublicationService.getInstance().publish(nodesToAutoPublish.asList(), Constants.EDIT_WORKSPACE, Constants.LIVE_WORKSPACE,null);
                };
                logger.info("");
                logger.info("Nodes to publish:")
                for (String identifier : nodesToAutoPublish) {
                    logger.warn("   " + session.getNodeByIdentifier(identifier).getPath());
                }
            }
            return null;
        }
    }
    );
}

