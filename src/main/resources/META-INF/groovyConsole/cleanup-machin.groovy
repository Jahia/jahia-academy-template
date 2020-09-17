import org.jahia.api.Constants
import org.jahia.services.content.*
import org.jahia.services.sites.JahiaSite
import org.jahia.taglibs.jcr.node.JCRTagUtils

import javax.jcr.NodeIterator
import javax.jcr.PathNotFoundException
import javax.jcr.RepositoryException
import javax.jcr.query.Query

def logger = log;

def JahiaSite site = org.jahia.services.sites.JahiaSitesService.getInstance().getSiteByKey("academy");
boolean doIt = false;
Set<String> nodesToAutoPublish = new HashSet<String>();

// list of path to clean for any reason...
Set<String> nodePathToClean = new HashSet<String>();
nodePathToClean.add("/sites/academy/home/documentation/developer/dx/73/first-steps-with-dx");
nodePathToClean.add("/sites/academy/home/documentation/end-user/dx/73/functional/local-site-manager");
nodePathToClean.add("/sites/academy/home/documentation/end-user/dx/8/optional-features/local-site-manager");
nodePathToClean.add("/sites/academy/home/legacy-1/digital-experience-manager/7-2/72");
nodePathToClean.add("/sites/academy/home/legacy-1/digital-experience-manager/7-2/72-1");
nodePathToClean.add("/sites/academy/home/legacy-1/digital-experience-manager/7-2/72-1/technical/elasticsearch-search-provider/facets---elasticsearch");
nodePathToClean.add("/sites/academy/home/legacy-1/digital-experience-manager/7-2/72-1/technical/elasticsearch-search-provider/how-to-develop-your-own-facet-type");
nodePathToClean.add("/sites/academy/home/legacy-1/digital-experience-manager/7-2/72-2");
nodePathToClean.add("/sites/academy/home/legacy-1/digital-experience-manager/legacy-versions");
nodePathToClean.add("/sites/academy/home/legacy-1/form-factory/2-3/developer");
nodePathToClean.add("/sites/academy/home/legacy-1/form-factory/2-3/end-user");
nodePathToClean.add("/sites/academy/home/legacy-1/form-factory/2-3/system-administrator");
nodePathToClean.add("/sites/academy/home/legacy-1/jahia-internal");
nodePathToClean.add("/sites/academy/home/legacy-1/marketing-factory/1-10/110-1");
nodePathToClean.add("/sites/academy/home/legacy-1/marketing-factory/1-10/Developer");
nodePathToClean.add("/sites/academy/home/legacy-1/marketing-factory/1-10/end-user");
nodePathToClean.add("/sites/academy/home/legacy-1/marketing-factory/19/welcome-to-marketing-factory-19");

for (Locale locale : site.getLanguagesAsLocales()) {
    JCRTemplate.getInstance().doExecuteWithSystemSession(null, Constants.EDIT_WORKSPACE, locale, new JCRCallback() {
        @Override
        Object doInJCR(JCRSessionWrapper session) throws RepositoryException {
            Iterator<String> it = nodePathToClean.iterator();
            boolean ignoreThisNode = false;
            while(it.hasNext()){
                String path = it.next();
                try {
                    JCRNodeWrapper node = session.getNode(path);
                    if (node.isNodeType("jacademix:isVersionPage")) {
                        try {
                            if (doIt) {
                                node.removeMixin("jacademix:isVersionPage");
                            }
                            nodesToAutoPublish.add(node.getIdentifier());
                            logger.info("Remove mixin jacademix:isVersionPage for node " + node.getPath());
                        } catch (RepositoryException e) {

                        }
                    } else {
                        logger.info("No mixin jacademix:isVersionPage found for node " + node.getPath());
                    }
                } catch (PathNotFoundException pathNotFoundException) {
                    logger.info("Error: could not find node for path " + path);
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
            return null;
        }
    }
    );
}
