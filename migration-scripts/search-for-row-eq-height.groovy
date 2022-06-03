import org.jahia.api.Constants
import org.jahia.registries.ServicesRegistry
import org.jahia.services.content.*
import org.jahia.services.sites.JahiaSite

import javax.jcr.NodeIterator
import javax.jcr.RepositoryException
import javax.jcr.query.Query

/* DANGER ZONE set to true to save the result */
boolean doIt = true;

/* siteKey */
String siteKey = "academy";

/* limit the search/replace to a certain path */
String descendentnode = "/sites/academy/home";

/* propertiesToLookAt is the list of nodeTypes/properties to search in */
def propertiesToLookAt = new HashMap<String, Object>();
//propertiesToLookAt.put("jnt:fixApplier",["howToUpgrade"]);
propertiesToLookAt.put("bootstrap5mix:createRow",["rowCssClass"]);

/* only search/replace in path that contains a pathRestriction */
Set<String> pathRestriction = new HashSet<String>();
pathRestriction.add("/");

/* list of search / replace text */
def searchReplace = new LinkedHashMap<String, String>();

searchReplace.put("row-eq-height","row-eq-height-disabled");
Set<String> nodesToAutoPublish = new HashSet<String>();
def JahiaSite site = org.jahia.services.sites.JahiaSitesService.getInstance().getSiteByKey(siteKey);
for (Locale locale : site.getLanguagesAsLocales()) {
    JCRTemplate.getInstance().doExecuteWithSystemSession(null, Constants.EDIT_WORKSPACE, locale, new JCRCallback() {
        @Override
        Object doInJCR(JCRSessionWrapper session) throws RepositoryException {

            for (String nt : propertiesToLookAt.keySet()) {
                def props = propertiesToLookAt.get(nt);
                for (String prop : props) {
                    def q = "select * from [" + nt + "] where isdescendantnode('" + descendentnode + "')";

                    NodeIterator iterator = session.getWorkspace().getQueryManager().createQuery(q, Query.JCR_SQL2).execute().getNodes();
                    while (iterator.hasNext()) {
                        final JCRNodeWrapper node = (JCRNodeWrapper) iterator.nextNode();
                        String nodePath = node.getPath();
                        if (isInPathRestriction(nodePath,pathRestriction)) {
                            if (updateNode(node, prop, searchReplace)) {
                                log.info("update [" + nt + "  " + prop + "] in path " + node.path + " " );
                                nodesToAutoPublish.add(node.identifier);
                            }
                        }
                    }
                }
            }
            if (doIt) {
                session.save();
            }
            if (CollectionUtils.isNotEmpty(nodesToAutoPublish)) {
                if (doIt) {
                    ServicesRegistry.getInstance().getJCRPublicationService().publish(nodesToAutoPublish.asList(), Constants.EDIT_WORKSPACE, Constants.LIVE_WORKSPACE, null);
                };
                logger.info("");
                logger.info("Nodes published:")
                for (String identifier : nodesToAutoPublish) {
                    logger.warn("   " + session.getNodeByIdentifier(identifier).getPath());
                }
            }
            return null;
        }
    });
}

public boolean isInPathRestriction(String nodePath, Set<String> pathToCheck) {
    Iterator<Integer> iterator = pathToCheck.iterator();
    while(iterator.hasNext()) {
        if (nodePath.contains(iterator.next())) {
            return true;
        }
    }
    return false;
}

public boolean updateNode(JCRNodeWrapper node, String property, LinkedHashMap searchReplace) {
    String prop = node.getPropertyAsString(property);
    boolean needToBeUpdated = false;
    if (prop != null) {
        String tmp = updateContent(prop,searchReplace);
        if (!tmp.equals(prop)) {
            String typeOfGrid = node.getPropertyAsString("typeOfGrid");
            String grid = "no-grid"
            if ("predefinedGrid".equals(typeOfGrid)) {
                grid = node.getPropertyAsString("grid");
            } else if ("customGrid".equals(typeOfGrid)) {
                grid = node.getPropertyAsString("gridClasses");
            }
            if ("12".equals(grid) || "no-grid".equals(grid)) {
                needToBeUpdated = true;
                node.setProperty(property, tmp);
            }
        }
    }
    return needToBeUpdated;
}

public String updateContent(String str,LinkedHashMap searchReplace) {
    if (str == null) {
        return "";
    }
    for (String searchFrom : searchReplace.keySet()) {
        def searchTo = searchReplace.get(searchFrom);
        str = str.replaceAll(searchFrom, searchTo);
    }
    return str;
}
