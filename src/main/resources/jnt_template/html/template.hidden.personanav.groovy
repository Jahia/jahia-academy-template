import org.jahia.services.content.JCRSessionWrapper
import org.jahia.utils.JcrUtils
import javax.jcr.ItemNotFoundException
import org.jahia.services.content.JCRContentUtils
import org.jahia.services.content.JCRNodeWrapper
import org.jahia.taglibs.jcr.node.JCRTagUtils
import org.slf4j.LoggerFactory
import org.jahia.modules.academy.Functions

logger = LoggerFactory.getLogger(this.class)

// Helper method to check if menuItem should be processed
def isCorrectType(menuItem) {
    if (!menuItem) return false

    if (menuItem.isNodeType("jmix:navMenu")) return false

    if (menuItem.properties["j:displayInMenuName"]) {
        return menuItem.properties["j:displayInMenuName"].any { it.string == currentNode.name }
    }

    return true
}

def printMenu(JCRNodeWrapper startNode, String style) {
    if (!startNode) return

    def children = JCRContentUtils.getChildrenOfType(startNode, "jmix:navMenuItem")
    children.eachWithIndex { menuItem, index ->
        if (isCorrectType(menuItem) && ! menuItem.isNodeType("jacademix:hideInPersonaMenu")) {
            String menuItemUrl = null
            String menuItemTitle = menuItem.displayableName
            String statusClass = renderContext.mainResource.node.path.contains(menuItem.path) ? ' active' : ''

            if (menuItem.isNodeType("jnt:page")) {
                menuItemUrl = renderContext.getResponse().encodeURL(menuItem.url)
            } else if (menuItem.isNodeType("jnt:navMenuText")) {
                menuItemUrl = Functions.findFirstSubPageUrl(menuItem)
            }

            if ("tab".equals(style)) {
                print """
                <li class="nav-item">
                    <a class="nav-link${statusClass}" href="${menuItemUrl ?: '#'}">${menuItemTitle}</a>
                </li>
                """
            } else {
                print """
                <li><a class="dropdown-item" href="${menuItemUrl ?: '#'}">${menuItemTitle}</a></li>
                """
            }
        }
    }
}

def printPersona(JCRNodeWrapper startNode) {
    if (!startNode) return

    def children = JCRContentUtils.getChildrenOfType(startNode, "jmix:navMenuItem")
    children.find { menuItem ->
        if (isCorrectType(menuItem) && renderContext.mainResource.node.path.contains(menuItem.path)) {
            print menuItem.displayableName
            return true
        }
        return false
    }
}

def printProducts(JCRNodeWrapper startNode, int level, JCRSessionWrapper session, String relativePath, String product, String version, String persona) {
    if (!startNode) return

    def children = JCRContentUtils.getChildrenOfType(startNode, "jmix:navMenuItem")
    int idx = 0

    children.eachWithIndex { menuItem, index ->
        String candidatePath = null
        JCRNodeWrapper candidateNode = null;
        String menuItemTitle = menuItem.displayableName
        // we iterate the versions. So we try to get the new path based on the current version
        try {
            candidatePath = "/sites/academy/home/documentation/" + product + "/" + menuItem.getName() + "/" + persona + relativePath;
            candidateNode = currentResource.getSession().getNode(candidatePath)
            menuItemUrl = candidateNode.getUrl();
        } catch  (javax.jcr.PathNotFoundException e) {
            // previous candidatePath id not work... we try to generate path to the persona now
            try {
                candidatePath = "/sites/academy/home/documentation/" + product + "/" + menuItem.getName() + "/" + persona;
                candidateNode = currentResource.getSession().getNode(candidatePath)
                menuItemUrl = Functions.findFirstSubPageUrl(candidateNode)
            } catch (javax.jcr.PathNotFoundException e2) {
                menuItemUrl = Functions.findFirstSubPageUrl(menuItem)
            }
        }

        print """
        <li><a class="dropdown-item" href="${menuItemUrl}">${menuItemTitle}</a></li>
        """
    }
}


def startNode = null
String documentationPath = "/sites/academy/home/documentation";
JCRNodeWrapper curentPageNode = renderContext.mainResource.node
JCRNodeWrapper documentationNode = currentResource.getSession().getNode(documentationPath)
List<JCRNodeWrapper> parentPages = JCRTagUtils.getParentsOfType(curentPageNode, "jmix:navMenuItem")

// documentationPath / product / version / persona (has mixin jacademix:isVersionPage) / relativePath

String persona = null
String version = null
String product = null

JCRNodeWrapper personaNode = null
JCRNodeWrapper versionNode = null
JCRNodeWrapper productNode = null

String relativePath = null // right part after persona

if (!parentPages.empty) {
    List<JCRNodeWrapper> reversedList = parentPages.reverse()

    for (JCRNodeWrapper parentPage : reversedList) {
        if (JCRTagUtils.isNodeType(parentPage, "jacademix:isVersionPage")) {
            personaNode = parentPage;
            persona = personaNode.getName();

            versionNode = personaNode.getParent()
            version = versionNode.getName()

            startNode = parentPage.getParent()

            relativePath = curentPageNode.getPath().replace(personaNode.getPath(), "");

            break
        }
    }

    for (JCRNodeWrapper parentPage : reversedList) {
        if (JCRTagUtils.isNodeType(parentPage, "jacademix:isProduct")) {
            productNode = parentPage;
            product = productNode.getName()
            break
        }
    }
}


if (startNode) {
    if (JCRContentUtils.getChildrenOfType(startNode, "jacademix:isVersionPage").size() > 1) {
        try {
            currentResource.dependencies.add(renderContext.mainResource.node.getParent().getCanonicalPath())
        } catch (ItemNotFoundException e) {
            // Handle exception if needed
        }

        print """
        <div class="row darkblue">
            <div class="col-8">
                <div class="d-inline d-md-none">
                    <button type="button" class="btn btn-light dropdown-toggle" data-bs-toggle="dropdown" aria-expanded="false">
                        """
        printPersona(startNode)
        print """
                    </button>
                    <ul class="dropdown-menu">
                    """
        printMenu(startNode, "dropdown")
        print """
                    </ul>
                </div>
                <div class="d-none d-md-block">
                    <ul class="nav nav-underline ">
        """
        printMenu(startNode, "tab")
        print """
                    </ul>
                </div>           
            </div> 
        """

        // Affichage conditionnel de la colonne droite
        def currentPath = renderContext.mainResource.node.getPath()
        if (currentPath.startsWith("/sites/academy/home/documentation/")) {
            print """
            <div class="col-4 text-end pe-4">                
                <div class="btn-group">
                    <button type="button" class="btn btn-light dropdown-toggle" data-bs-toggle="dropdown" aria-expanded="false">
                        ${startNode.getDisplayableName()}
                    </button>
                    <ul class="dropdown-menu">
            """
            if (productNode != null) {
                printProducts(productNode, 0, currentResource.getSession(), relativePath, product, version, persona);
            }
            print """
                    </ul>
                </div>
            </div>
            """
        }

        print """
        </div>
        """
    }
}
