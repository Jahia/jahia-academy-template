import org.apache.jackrabbit.core.JahiaRepositoryImpl
import org.jahia.services.SpringContextSingleton
import org.jahia.services.content.*
import javax.jcr.*
import javax.jcr.query.Query

// Supprime une propriété JCR si elle existe, sauf si elle est protégée
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

// Supprime toutes les propriétés liées au lock sur un noeud
def cleanLocks(javax.jcr.Node node) {
    ["j:lockTypes", "j:processId", "jcr:lockOwner", "j:locktoken", "jcr:lockIsDeep"].each {
        tryRemoveProperty(node, it)
    }
}

// Déverrouille un JCRNodeWrapper s'il est effectivement verrouillé
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
        // Bénin : le nœud n'était pas vraiment locké
        log.debug("Unlock skipped (node not really locked): ${node.path}")
    } catch (RepositoryException ex) {
        log.warn("Unlock failed for ${node.path}", ex)
    }
}

// Connexion au repository avec auto-fix activé
JahiaRepositoryImpl rep = SpringContextSingleton.getBean("jackrabbit").getRepository()
SimpleCredentials cred = (SimpleCredentials) org.apache.jackrabbit.core.security.JahiaLoginModule.getSystemCredentials(" system ")
cred.setAttribute("org.apache.jackrabbit.autoFixCorruptions", "true")
Session jcrsession = rep.login(cred, "default")

try {
    final String stmt = "SELECT * FROM [jnt:content] WHERE ISDESCENDANTNODE('/sites/academy/home/customer-center/jahia-1/downloads')"
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

        // Nettoyage des sous-noeuds de traduction
        JCRNodeIteratorWrapper children = node.getNodes() as JCRNodeIteratorWrapper
        while (children.hasNext()) {
            JCRNodeWrapper subNode = children.nextNode()
            if (subNode.isNodeType("jnt:translation")) {
                safeUnlock(subNode)
                cleanLocks(subNode.getRealNode())
            }
        }

        // Nettoyage du nœud principal
        safeUnlock(node)
        cleanLocks(node.getRealNode())

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
