import org.jahia.api.Constants
import org.jahia.services.content.*
import org.jahia.services.sites.JahiaSite

import javax.jcr.NodeIterator
import javax.jcr.RepositoryException
import javax.jcr.query.Query

def logger = log;

def JahiaSite site = org.jahia.services.sites.JahiaSitesService.getInstance().getSiteByKey("academy");

Set<String> nodesToAutoPublish = new HashSet<String>();
Set<String> nodesToManuelPublish = new HashSet<String>();


for (Locale locale : site.getLanguagesAsLocales()) {
    JCRTemplate.getInstance().doExecuteWithSystemSession(null, Constants.EDIT_WORKSPACE, locale, new JCRCallback() {
        @Override
        Object doInJCR(JCRSessionWrapper session) throws RepositoryException {
            def q = "SELECT * FROM [jacademix:document]";

            NodeIterator iterator = session.getWorkspace().getQueryManager().createQuery(q, Query.JCR_SQL2).execute().getNodes();
            while (iterator.hasNext()) {
                final JCRNodeWrapper node = (JCRNodeWrapper) iterator.nextNode();
                String title = "";
                if (node.hasProperty("jcr:title")) {
                    JCRPropertyWrapper titleProp = node.getProperty("jcr:title");
                    if (titleProp != null) {
                        title = titleProp.getString();
                    }
                }else {
                    logger.info("Could not found property jcr:title for node " + node.getPath());
                }
                if ("".equals(title)) {
                    // get parent page title
                    String parentPageTitle = node.getParent().getParent().getDisplayableName();
                    if (! "".equals(parentPageTitle)) {
                        logger.info("Missing title for node " + node.getPath() + " -> " + parentPageTitle);
                        node.setProperty("jcr:title",parentPageTitle);
                        if (hasPendingModification(node)) {
                            nodesToManuelPublish.add(node.getIdentifier());
                        } else {
                            nodesToAutoPublish.add(node.getIdentifier())
                        }
                    }

                }
            }
            if (CollectionUtils.isNotEmpty(nodesToAutoPublish)) {
                if (doIt) {
                    JCRPublicationService.getInstance().publish(nodesToAutoPublish.asList(), Constants.EDIT_WORKSPACE, Constants.LIVE_WORKSPACE, false, null)
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