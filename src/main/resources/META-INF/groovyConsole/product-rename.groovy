import org.jahia.api.Constants
import org.jahia.services.content.*
import org.jahia.services.sites.JahiaSite

import javax.jcr.NodeIterator
import javax.jcr.RepositoryException
import javax.jcr.query.Query

def logger = log;
boolean doIt = false;

String descendentnode = "/sites/academy";

def JahiaSite site = org.jahia.services.sites.JahiaSitesService.getInstance().getSiteByKey("academy");

def propertiesToLookAt = new HashMap<String, Object>();
propertiesToLookAt.put("jacademix:textContent",["textContent"]);
propertiesToLookAt.put("jacademix:document",["jcr:title"]);
propertiesToLookAt.put("jmix:navMenuItem",["jcr:title"]);
propertiesToLookAt.put("jacademix:alternateTitle",["alternateTitle"]);

Set<String> pathToCheck = new HashSet<String>();
pathToCheck.add("/dx/73/");
pathToCheck.add("/mf/110/");
pathToCheck.add("/ff/23-1/");
pathToCheck.add("/dx/techwiki/");

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
                        if (isInPath(nodePath,pathToCheck)) {
                            if (updateNode(node, prop)) {
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

public boolean isInPath(String nodePath,Set<String> pathToCheck) {
    Iterator<Integer> iterator = pathToCheck.iterator();
    while(iterator.hasNext()) {
        if (nodePath.contains(iterator.next())) {
            return true;
        }
    }
    return false;
}

public boolean updateNode(JCRNodeWrapper node, String property) {
    String prop = node.getPropertyAsString(property);
    boolean needToBeUpdated = false;
    if (prop != null) {
        String tmp = updateContent(prop);
        if (!tmp.equals(prop)) {
            needToBeUpdated = true;
            node.setProperty(property, tmp);
        }
    }
    return needToBeUpdated;
}

public String updateContent(final String str) {
    if (str == null) {
        return "";
    }
    return str.replaceAll("Digital Experience Manager (DX)", "Jahia").
            replaceAll("Jahia Digital Experience Manager", "Jahia").
            replaceAll("Digital Experience Manager", "Jahia").
            replaceAll("Jahia DX Manager", "Jahia").
            replaceAll("Jahia DX", "Jahia").
            replaceAll(" DX", " Jahia").
            replaceAll("DX ", "Jahia ").
            replaceAll("Marketing Factory (MF)", "JExperience").
            replaceAll("Marketing Factory", "JExperience").
            replaceAll(" MF", " JExperience").
            replaceAll("MF ", "JExperience ").
            replaceAll("Form Factory (FF)", "Forms").
            replaceAll("Form Factory", "Forms").
            replaceAll(" FF", " Forms").
            replaceAll("FF ", "Forms ");
}
