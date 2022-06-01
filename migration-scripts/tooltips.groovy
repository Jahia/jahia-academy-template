import org.jahia.api.Constants
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
propertiesToLookAt.put("jacademix:textContent",["textContent"]);
propertiesToLookAt.put("jacademix:document",["jcr:title"]);
propertiesToLookAt.put("jmix:navMenuItem",["jcr:title"]);
propertiesToLookAt.put("jacademix:alternateTitle",["alternateTitle"]);
propertiesToLookAt.put("jacademix:kbQa",["textContent","answer"]);
propertiesToLookAt.put("jacademix:kbUseCase",["textContent","answer","cause"]);

/* only search/replace in path that contains a pathRestriction */
Set<String> pathRestriction = new HashSet<String>();
pathRestriction.add("/");

/* list of search / replace text */
def searchReplace = new LinkedHashMap<String, String>();

searchReplace.put("data-toggle","data-bs-toggle");
searchReplace.put("data-content","data-bs-content");
searchReplace.put("data-container","data-bs-container");
searchReplace.put("data-placement","data-bs-placement");
searchReplace.put("data-content","data-bs-content");
searchReplace.put("data-html","data-bs-html");
searchReplace.put("data-trigger","data-bs-trigger");

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
                            }
                        }
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
            log.debug("need to update " + node.getPath());
            needToBeUpdated = true;
            node.setProperty(property, tmp);
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
