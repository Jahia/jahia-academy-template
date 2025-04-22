import org.apache.jackrabbit.core.JahiaRepositoryImpl
import org.jahia.services.SpringContextSingleton
import org.jahia.services.content.*
import javax.jcr.*
import javax.jcr.query.Query

// Attempts to remove a property from a node, unless it is protected
def tryRemoveProperty(javax.jcr.Node node, String propertyName) {
    if (node.hasProperty(propertyName)) {
        try {
            def propDef = node.getProperty(propertyName).definition
            if (propDef?.protected) {
                log.debug("Skipping protected property '${propertyName}' on ${node.path}")
                return
            }
        } catch (Exception defCheckEx) {
            log.debug("Could not determine if '${propertyName}' is protected on ${node.path}, skipping delete.", defCheckEx)
            return
        }

        try {
            node.getProperty(propertyName).remove()
            node.save()
            log.info("Removed property '${propertyName}' on ${node.path}")
        } catch (Exception e) {
            log.warn("Property '${propertyName}' could not be deleted on ${node.path}", e)
        }
    }
}

// Removes all known lock-related properties from a node
def cleanLocks(javax.jcr.Node node) {
    def lockProps = [
            "j:lockTypes",
            "j:processId",
            "jcr:lockOwner",
            "j:locktoken",
            "jcr:lockIsDeep"
    ]
    lockProps.each { prop ->
        tryRemoveProperty(node, prop)
    }
}

// Safely unlocks a node only if it's actually locked
def safeUnlock(JCRNodeWrapper node) {
    try {
        if (node.isLocked()) {
            node.unlock()
            node.clearAllLocks()
            log.info("Unlocked node: ${node.path}")
        } else {
            log.debug("Node not locked: ${node.path}")
        }
    } catch (javax.jcr.lock.LockException ex) {
        // Safe to ignore: the node is already unlocked or lock is orphaned
        log.debug("Unlock skipped (node not really locked): ${node.path}")
    } catch (RepositoryException ex) {
        log.warn("Unlock failed for ${node.path}", ex)
    }
}

// Connect to the repository with autoFix enabled
JahiaRepositoryImpl rep = SpringContextSingleton.getBean("jackrabbit").getRepository()
SimpleCredentials cred = (SimpleCredentials) org.apache.jackrabbit.core.security.JahiaLoginModule.getSystemCredentials(" system ")
cred.setAttribute("org.apache.jackrabbit.autoFixCorruptions", "true")
Session jcrsession = rep.login(cred, "default")

try {
    final String stmt = "SELECT * FROM [jnt:page] WHERE ISDESCENDANTNODE('/sites/academy/home/customer-center/jahia-1/downloads/how-to-upgrade')"
    javax.jcr.NodeIterator results = jcrsession.getWorkspace()
            .getQueryManager()
            .createQuery(stmt, Query.JCR_SQL2)
            .execute()
            .getNodes()

    while (results.hasNext()) {
        JCRNodeWrapper contentNode = results.nextNode() as JCRNodeWrapper
        JCRNodeWrapper node = JCRSessionFactory.getInstance()
                .getCurrentUserSession("default", null)
                .getNode(contentNode.path)

        // Process all child translation nodes
        JCRNodeIteratorWrapper children = node.getNodes() as JCRNodeIteratorWrapper
        while (children.hasNext()) {
            JCRNodeWrapper subNode = children.nextNode()
            if (subNode.isNodeType("jnt:translation")) {
                safeUnlock(subNode)
                cleanLocks(subNode)
            }
        }

        // Process the parent node
        safeUnlock(node)
        cleanLocks(node)

        log.info("Fully cleaned: ${node.path}")
    }

    jcrsession.save()
    log.info("JCR session saved.")

} catch (Exception e) {
    log.error("Unexpected error during lock cleanup", e)
} finally {
    jcrsession.logout()
    log.info("JCR session logged out.")
}
