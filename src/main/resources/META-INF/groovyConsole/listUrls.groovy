import javax.jcr.NodeIterator
import javax.jcr.query.Query
import javax.jcr.RepositoryException
import org.jahia.api.Constants
import org.jahia.services.content.JCRCallback
import org.jahia.services.content.JCRNodeWrapper
import org.jahia.services.content.JCRSessionWrapper
import org.jahia.services.content.JCRTemplate
import org.jahia.services.content.JCRValueWrapper
import org.jahia.services.sites.JahiaSite

// enf of configutation
// ----------------------------------------------------------------------------
Set<String> allURLs = new HashSet<String>();

Collection contentTypes = new HashSet<String>();

Set<String> workspaces = new HashSet<String>();
workspaces.add(Constants.EDIT_WORKSPACE);
workspaces.add(Constants.LIVE_WORKSPACE);

def logger = log;

def JahiaSite site = org.jahia.services.sites.JahiaSitesService.getInstance().getSiteByKey("casa");

for (String workspace : workspaces) {
    Set<String> inactiveLanguages = site.getInactiveLanguages();
    Set<String> inactiveLiveLanguages = site.getInactiveLiveLanguages();
    for (Locale locale : site.getLanguagesAsLocales()) {
        if (Constants.EDIT_WORKSPACE.endsWith(workspace)) {
            if (inactiveLanguages.contains(locale.toString())) {
                continue;
            }
        }
        if (Constants.LIVE_WORKSPACE.endsWith(workspace)) {
            if (inactiveLiveLanguages.contains(locale.toString())) {
                continue;
            }
        }
        JCRTemplate.getInstance().doExecuteWithSystemSession(null, workspace, locale, new JCRCallback() {
            @Override
            Object doInJCR(JCRSessionWrapper session) throws RepositoryException {
                String stmt = "select * from [jnt:contentTemplate]";
                NodeIterator iterator = session.getWorkspace().getQueryManager().createQuery(stmt, Query.JCR_SQL2).execute().getNodes();
                while (iterator.hasNext()) {
                    final JCRNodeWrapper contentTemplateNode = (JCRNodeWrapper) iterator.nextNode();
                    if (contentTemplateNode.hasProperty('j:applyOn')) {
                        for (JCRValueWrapper valueWrapper : contentTemplateNode.getProperty('j:applyOn').getValues()) {
                            String contentType = valueWrapper.getString();
                            contentTypes.add(contentType);
                        }
                    }
                }
                // remove unwanted types
                contentTypes.remove("cnt:employee");
                contentTypes.remove("jnt:content");
                contentTypes.remove("jnt:contentFolder");
                contentTypes.remove("jnt:file");
                contentTypes.remove("jnt:folder");
                contentTypes.remove("jnt:globalSettings");
                contentTypes.remove("jnt:module");
                contentTypes.remove("jnt:nodeType");
                contentTypes.remove("jnt:topic");
                contentTypes.remove("jnt:user");
                contentTypes.remove("jnt:vfsMountPointFactoryPage");
                contentTypes.remove("jnt:virtualsite");
                contentTypes.remove("wemnt:optimizationTest");
                contentTypes.remove("wemnt:personalizedContent");
                contentTypes.remove("fcnt:formDisplay");

                // add wanted types
                contentTypes.add("jnt:page");
                contentTypes.add("jnt:file");

                String hostName = "https://" + site.getServerName();


                Iterator contentTypeIterator = contentTypes.iterator();
                while (contentTypeIterator.hasNext()) {
                    String contentType = contentTypeIterator.next();
                    //logger.info(contentType);
                    stmt = "select * from [" + contentType + "] where isdescendantnode('" + site.getJCRLocalPath() + "')";
                    iterator = session.getWorkspace().getQueryManager().createQuery(stmt, Query.JCR_SQL2).execute().getNodes();
                    while (iterator.hasNext()) {
                        final JCRNodeWrapper page = (JCRNodeWrapper) iterator.nextNode();
                        String url = page.getUrl();
                        if ("jnt:file".equals(contentType)) {
                            if (url.toLowerCase().endsWith("pdf")) {
                                if (!allURLs.contains(url)) {
                                    allURLs.add(hostName + url);
                                }
                            }
                        } else {
                            if (!allURLs.contains(url)) {
                                allURLs.add(hostName + url);
                            }
                        }
                    }
                }

                return null;
            }
        });
    }
}


if (CollectionUtils.isNotEmpty(allURLs)) {
    for (String url : allURLs.sort()) {
        logger.info(url);
    }
}
import javax.jcr.NodeIterator
import javax.jcr.query.Query
import javax.jcr.RepositoryException
import org.jahia.api.Constants
import org.jahia.services.content.JCRCallback
import org.jahia.services.content.JCRNodeWrapper
import org.jahia.services.content.JCRSessionWrapper
import org.jahia.services.content.JCRTemplate
import org.jahia.services.content.JCRValueWrapper
import org.jahia.services.sites.JahiaSite

// enf of configutation
// ----------------------------------------------------------------------------
Set<String> allURLs = new HashSet<String>();

Collection contentTypes = new HashSet<String>();

Set<String> workspaces = new HashSet<String>();
workspaces.add(Constants.EDIT_WORKSPACE);
workspaces.add(Constants.LIVE_WORKSPACE);

def logger = log;

def JahiaSite site = org.jahia.services.sites.JahiaSitesService.getInstance().getSiteByKey("casa");

for (String workspace : workspaces) {
    Set<String> inactiveLanguages = site.getInactiveLanguages();
    Set<String> inactiveLiveLanguages = site.getInactiveLiveLanguages();
    for (Locale locale : site.getLanguagesAsLocales()) {
        if (Constants.EDIT_WORKSPACE.endsWith(workspace)) {
            if (inactiveLanguages.contains(locale.toString())) {
                continue;
            }
        }
        if (Constants.LIVE_WORKSPACE.endsWith(workspace)) {
            if (inactiveLiveLanguages.contains(locale.toString())) {
                continue;
            }
        }
        JCRTemplate.getInstance().doExecuteWithSystemSession(null, workspace, locale, new JCRCallback() {
            @Override
            Object doInJCR(JCRSessionWrapper session) throws RepositoryException {
                String stmt = "select * from [jnt:contentTemplate]";
                NodeIterator iterator = session.getWorkspace().getQueryManager().createQuery(stmt, Query.JCR_SQL2).execute().getNodes();
                while (iterator.hasNext()) {
                    final JCRNodeWrapper contentTemplateNode = (JCRNodeWrapper) iterator.nextNode();
                    if (contentTemplateNode.hasProperty('j:applyOn')) {
                        for (JCRValueWrapper valueWrapper : contentTemplateNode.getProperty('j:applyOn').getValues()) {
                            String contentType = valueWrapper.getString();
                            contentTypes.add(contentType);
                        }
                    }
                }
                // remove unwanted types
                contentTypes.remove("cnt:employee");
                contentTypes.remove("jnt:content");
                contentTypes.remove("jnt:contentFolder");
                contentTypes.remove("jnt:file");
                contentTypes.remove("jnt:folder");
                contentTypes.remove("jnt:globalSettings");
                contentTypes.remove("jnt:module");
                contentTypes.remove("jnt:nodeType");
                contentTypes.remove("jnt:topic");
                contentTypes.remove("jnt:user");
                contentTypes.remove("jnt:vfsMountPointFactoryPage");
                contentTypes.remove("jnt:virtualsite");
                contentTypes.remove("wemnt:optimizationTest");
                contentTypes.remove("wemnt:personalizedContent");
                contentTypes.remove("fcnt:formDisplay");

                // add wanted types
                contentTypes.add("jnt:page");
                contentTypes.add("jnt:file");

                String hostName = "https://" + site.getServerName();


                Iterator contentTypeIterator = contentTypes.iterator();
                while (contentTypeIterator.hasNext()) {
                    String contentType = contentTypeIterator.next();
                    //logger.info(contentType);
                    stmt = "select * from [" + contentType + "] where isdescendantnode('" + site.getJCRLocalPath() + "')";
                    iterator = session.getWorkspace().getQueryManager().createQuery(stmt, Query.JCR_SQL2).execute().getNodes();
                    while (iterator.hasNext()) {
                        final JCRNodeWrapper page = (JCRNodeWrapper) iterator.nextNode();
                        String url = page.getUrl();
                        if ("jnt:file".equals(contentType)) {
                            if (url.toLowerCase().endsWith("pdf")) {
                                if (!allURLs.contains(url)) {
                                    allURLs.add(hostName + url);
                                }
                            }
                        } else {
                            if (!allURLs.contains(url)) {
                                allURLs.add(hostName + url);
                            }
                        }
                    }
                }

                return null;
            }
        });
    }
}


if (CollectionUtils.isNotEmpty(allURLs)) {
    for (String url : allURLs.sort()) {
        logger.info(url);
    }
}
