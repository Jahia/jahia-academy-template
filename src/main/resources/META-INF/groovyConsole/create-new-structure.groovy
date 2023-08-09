import org.jahia.api.Constants
import org.jahia.services.content.*
import org.jahia.services.sites.JahiaSite
import javax.jcr.RepositoryException

def logger = log
def siteKey = "academy"
def JahiaSite site = org.jahia.services.sites.JahiaSitesService.getInstance().getSiteByKey(siteKey)


site.getLanguagesAsLocales().each { locale ->
    JCRTemplate.getInstance().doExecuteWithSystemSession(null, Constants.EDIT_WORKSPACE, locale, new JCRCallback() {
        @Override
        Object doInJCR(JCRSessionWrapper session) throws RepositoryException {
            // main entry point
            createNode("/sites/academy/home/documentation/jahia","Jahia",session)
            createNode("/sites/academy/home/documentation/forms","Forms",session)
            createNode("/sites/academy/home/documentation/jexperience","jExperience",session)

            // Jahia 8
            createNode("/sites/academy/home/documentation/jahia/8","Jahia 8",session)
            createNode("/sites/academy/home/documentation/jahia/8/end-user","End User",session,"current")
            createNode("/sites/academy/home/documentation/jahia/8/sysadmin","System Administrator",session,"current")
            createNode("/sites/academy/home/documentation/jahia/8/developer","Developer",session,"current")
            createNode("/sites/academy/home/documentation/jahia/8/devops","Dev Ops",session,"current")
            moveNode("/sites/academy/home/documentation/end-user/dx/8","/sites/academy/home/documentation/jahia/8/end-user",session,true);
            moveNode("/sites/academy/home/documentation/system-administrator/dx/8","/sites/academy/home/documentation/jahia/8/sysadmin",session,true);
            moveNode("/sites/academy/home/documentation/developer/dx/8","/sites/academy/home/documentation/jahia/8/developer",session,true);
            moveNode("/sites/academy/home/documentation/system-administrator/devops","/sites/academy/home/documentation/jahia/8/devops",session,true);
            moveNode("/sites/academy/home/documentation/developer/jahia-technology-overview","/sites/academy/home/documentation/jahia/8/developer",session,true);

            // Jahia 7.3
            createNode("/sites/academy/home/documentation/jahia/7.3","Jahia 7.3",session)
            createNode("/sites/academy/home/documentation/jahia/7.3/end-user","End User",session,"7.3")
            createNode("/sites/academy/home/documentation/jahia/7.3/sysadmin","System Administrator",session,"7.3")
            createNode("/sites/academy/home/documentation/jahia/7.3/developer","Developer",session,"7.3")
            moveNode("/sites/academy/home/documentation/end-user/dx/73","/sites/academy/home/documentation/jahia/7.3/end-user",session,true);
            moveNode("/sites/academy/home/documentation/system-administrator/dx/73","/sites/academy/home/documentation/jahia/7.3/sysadmin",session,true);
            moveNode("/sites/academy/home/documentation/developer/dx/73","/sites/academy/home/documentation/jahia/7.3/developer",session,true);

            // Forms 3.3
            createNode("/sites/academy/home/documentation/forms/3.3","Forms 3.3",session)
            createNode("/sites/academy/home/documentation/forms/3.3/end-user","End User",session,"current")
            createNode("/sites/academy/home/documentation/forms/3.3/sysadmin","System Administrator",session,"current")
            createNode("/sites/academy/home/documentation/forms/3.3/developer","Developer",session,"current")
            moveNode("/sites/academy/home/documentation/end-user/ff/3-3","/sites/academy/home/documentation/forms/3.3/end-user",session,true);
            moveNode("/sites/academy/home/documentation/system-administrator/ff/3-3","/sites/academy/home/documentation/forms/3.3/sysadmin",session,true);
            moveNode("/sites/academy/home/documentation/developer/ff/3-3","/sites/academy/home/documentation/forms/3.3/developer",session,true);

            // Forms 2.7
            createNode("/sites/academy/home/documentation/forms/2.7","Forms 2.7",session)
            createNode("/sites/academy/home/documentation/forms/2.7/end-user","End User",session,"2.7")
            createNode("/sites/academy/home/documentation/forms/2.7/sysadmin","System Administrator",session,"2.7")
            createNode("/sites/academy/home/documentation/forms/2.7/developer","Developer",session,"2.7")
            moveNode("/sites/academy/home/documentation/end-user/ff/2-7","/sites/academy/home/documentation/forms/2.7/end-user",session,true);
            moveNode("/sites/academy/home/documentation/system-administrator/ff/2-7","/sites/academy/home/documentation/forms/2.7/sysadmin",session,true);
            moveNode("/sites/academy/home/documentation/developer/ff/2-7","/sites/academy/home/documentation/forms/2.7/developer",session,true);

            // jExperience 2.x and 3.x
            createNode("/sites/academy/home/documentation/jexperience/2","jExperience 2.x and 3.x",session)
            createNode("/sites/academy/home/documentation/jexperience/2/end-user","End User",session,"current")
            createNode("/sites/academy/home/documentation/jexperience/2/sysadmin","System Administrator",session,"current")
            createNode("/sites/academy/home/documentation/jexperience/2/developer","Developer",session,"current")
            moveNode("/sites/academy/home/documentation/end-user/jexperience/2","/sites/academy/home/documentation/jexperience/2/end-user",session,true);
            moveNode("/sites/academy/home/documentation/system-administrator/jexperience/2-x-and-3-x","/sites/academy/home/documentation/jexperience/2/sysadmin",session,true);
            moveNode("/sites/academy/home/documentation/developer/jexperience/2","/sites/academy/home/documentation/jexperience/2/developer",session,true);

            // jExperience 1.x
            createNode("/sites/academy/home/documentation/jexperience/1","jExperience 1.x",session)
            createNode("/sites/academy/home/documentation/jexperience/1/end-user","End User",session,"1")
            createNode("/sites/academy/home/documentation/jexperience/1/sysadmin","System Administrator",session,"1")
            createNode("/sites/academy/home/documentation/jexperience/1/developer","Developer",session,"1")
            moveNode("/sites/academy/home/documentation/end-user/jexperience/1","/sites/academy/home/documentation/jexperience/1/end-user",session,true);
            moveNode("/sites/academy/home/documentation/system-administrator/jexperience/1","/sites/academy/home/documentation/jexperience/1/sysadmin",session,true);
            moveNode("/sites/academy/home/documentation/developer/jexperience/1.x","/sites/academy/home/documentation/jexperience/1/developer",session,true);

            // jahia-cloud
            createNode("/sites/academy/home/documentation/jahia-cloud","Jahia Cloud",session,"current")
            moveNode("/sites/academy/home/documentation/system-administrator/jahia-cloud/managing-your-cloud-environment","/sites/academy/home/documentation/jahia-cloud",session,true)

            createNode("/sites/academy/home/documentation/augmented-search","Augmented search",session,"current");
            moveNode("/sites/academy/home/documentation/developer/augmented-search","/sites/academy/home/documentation/augmented-search",session,true)

            // cleanup
            removeNode("/sites/academy/home/documentation/end-user",session);
            removeNode("/sites/academy/home/documentation/developer",session);
            removeNode("/sites/academy/home/documentation/system-administrator",session);
            removeNode("/sites/academy/home/documentation/end-user-previous",session);
            removeNode("/sites/academy/home/documentation/system-administrator-previous",session);
            removeNode("/sites/academy/home/documentation/developer-previous",session);

            return null
        }
    })
}
void removeNode(String path, JCRSessionWrapper session) throws RepositoryException {
    try {
        JCRNodeWrapper node = session.getNode(path)
        node.remove()
        session.save();
        logger.info("Remove node $path");
    } catch (javax.jcr.PathNotFoundException e) {
        logger.error("Could not find node to remove $path");
    }
}

void moveNode(String sourcePath, String targetPath, JCRSessionWrapper session) throws RepositoryException {
    moveNode(sourcePath,targetPath,session,false);
}

void moveNode(String sourcePath, String targetPath, JCRSessionWrapper session, boolean removeSource) throws RepositoryException {
    // Attempt to retrieve the source and target nodes
    JCRNodeWrapper sourceNode;
    JCRNodeWrapper targetNode;
    try {
        sourceNode = session.getNode(sourcePath);
        try {
            targetNode = session.getNode(targetPath);
        } catch (javax.jcr.PathNotFoundException e2) {
            logger.error("Could not find source node at ${targetNode}");
            return;
        }
    } catch (javax.jcr.PathNotFoundException e) {
        logger.error("Could not find source node at ${sourcePath}");
        return;
    }

    // Retrieve the children of the source node that are of type "jmix:navMenuItem"
    List<JCRNodeWrapper> children = JCRContentUtils.getChildrenOfType(sourceNode, "jmix:navMenuItem");

    // Move each child node to the target node
    children.each { child ->
        String oldPath = child.getPath();
        String nodeName = getNodeName(oldPath);
        String newPath = "${targetPath}/${nodeName}";

        try {
            logger.info("Moving node from ${oldPath} to ${newPath}");
            session.move(oldPath, newPath);
            session.save();
        } catch (javax.jcr.RepositoryException e) {
            logger.error("Could not move node from ${oldPath} to ${newPath}", e);
        }
    }
    // DANGER but why not
    if (removeSource) {
        removeNode(sourcePath,session);
    }
}

JCRNodeWrapper createNode(String path, String title, JCRSessionWrapper session) throws RepositoryException {
    return createNode(path, title, session, null)

}
JCRNodeWrapper createNode(String path, String title, JCRSessionWrapper session, String version) throws RepositoryException {
    String parentPath = getParentPath(path)
    String nodeName = getNodeName(path)
    title = title ?: nodeName
    JCRNodeWrapper parentNode = null

    try {
        parentNode = session.getNode(parentPath)
    } catch (javax.jcr.PathNotFoundException e) {
        logger.info("Parent node not found. Creating parent node $parentPath")

        parentNode = createNode(parentPath, null, session)
    }

    if (parentNode != null) {
        try {
            logger.info("Add node $path")
            JCRNodeWrapper newNode = parentNode.addNode(nodeName, "jnt:navMenuText")
            newNode.setProperty("jcr:title", title)
            if (version != null) {
                newNode.addMixin("jacademix:isVersionPage");
                newNode.setProperty("version",version);
            }
            session.save()
            return newNode
        } catch (javax.jcr.ItemExistsException e) {
            logger.debug("Node $path already exisis, ignoring");
        }
    }
    return null
}


String getParentPath(String path) {
    if (!path) return null
    path = path.replaceAll(/\/+$/, "")
    def lastSlashIndex = path.lastIndexOf('/')
    lastSlashIndex != -1 ? path.substring(0, lastSlashIndex) : null
}

String getNodeName(String path) {
    if (!path) return null
    path = path.replaceAll(/\/+$/, "")
    def lastSlashIndex = path.lastIndexOf('/')
    lastSlashIndex != -1 ? path.substring(lastSlashIndex + 1) : path
}
