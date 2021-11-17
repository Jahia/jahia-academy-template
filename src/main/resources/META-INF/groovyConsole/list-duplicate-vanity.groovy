import org.jahia.api.Constants
import org.jahia.services.content.*
import org.jahia.services.sites.JahiaSite

import javax.jcr.NodeIterator
import javax.jcr.RepositoryException
import javax.jcr.query.Query

def logger = log;

def JahiaSite site = org.jahia.services.sites.JahiaSitesService.getInstance().getSiteByKey("academy");
Set<String> pageWithDefaultVanity = new HashSet<String>();
Set<String> pageWithDuplicateVanity = new HashSet<String>();

for (Locale locale : site.getLanguagesAsLocales()) {
    JCRTemplate.getInstance().doExecuteWithSystemSession(null, Constants.EDIT_WORKSPACE, locale, new JCRCallback() {
        @Override
        Object doInJCR(JCRSessionWrapper session) throws RepositoryException {
            def q = "select * from [jnt:vanityUrl] where isdescendantnode('/sites/academy/home')";
            logger.info("Processing " + q)
            NodeIterator iterator = session.getWorkspace().getQueryManager().createQuery(q, Query.JCR_SQL2).execute().getNodes();
            while (iterator.hasNext()) {
                final JCRNodeWrapper node = (JCRNodeWrapper) iterator.nextNode();
                boolean isDefault = node.getProperty('j:default').getBoolean();
                if (isDefault) {
                    JCRNodeWrapper parentPage = node.getParent().getParent();
                    String parentPagePath = parentPage.getPath();
                    boolean hasAlreadyADefaultVanity = false;
                    if (!pageWithDefaultVanity.contains(parentPagePath)) {
                        pageWithDefaultVanity.add(parentPagePath)
                    } else {
                        hasAlreadyADefaultVanity = true;
                        pageWithDuplicateVanity.add(parentPagePath);
                    }
                    String url = node.getPropertyAsString('j:url');
                    if (hasAlreadyADefaultVanity) {
                        log.info("TRUE;" + url + ";" + parentPagePath);
                    } else {
                        log.info("FALSE;" + url + ";" + parentPagePath);
                    }
                    hasAlreadyADefaultVanity = false;
                }
            }

            if (CollectionUtils.isNotEmpty(pageWithDuplicateVanity)) {

                logger.info("");
                logger.info("Nodes with duplicate default vanity:")
                for (String path : pageWithDuplicateVanity) {
                    logger.warn(path);
                }
            }

            return null;
        }
    }
    );
}
