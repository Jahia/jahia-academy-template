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

def printProducts(JCRNodeWrapper startNode, int level) {
    if (!startNode) return

    def children = JCRContentUtils.getChildrenOfType(startNode, "jmix:navMenuItem")
    int idx = 0

    children.eachWithIndex { menuItem, index ->
        if (isCorrectType(menuItem) && (level > 0 || menuItem.isNodeType("jacademix:isProduct"))) {
            if (menuItem.isNodeType("jacademix:isProduct")) idx++

            String menuItemUrl = null
            String menuItemTitle = menuItem.displayableName
            boolean isCurrentProductPage = renderContext.mainResource.node.path.contains(menuItem.path) || level > 0

            if (isCurrentProductPage) {
                String extraClass = level == 0 ? " product" : ""

                if (menuItem.isNodeType("jnt:page")) {
                    menuItemUrl = renderContext.getResponse().encodeURL(menuItem.url)
                } else if (menuItem.isNodeType("jnt:navMenuText")) {
                    menuItemUrl = Functions.findFirstSubPageUrl(menuItem)
                }

                if (idx > 1 && level == 0) {
                    print """
                    <li><hr class="dropdown-divider"></li>
                    """
                }

                print """
                <li><a class="dropdown-item ${extraClass}" href="${menuItemUrl}">${menuItemTitle}</a></li>
                """

                if (level == 0 && !menuItem.isNodeType("jacademix:isVersionPage")) {
                    printProducts(menuItem, level + 1)
                }
            }
        }
    }
}


def startNode = null
JCRNodeWrapper curentPageNode = renderContext.mainResource.node
JCRNodeWrapper documentationNode = currentResource.getSession().getNode("/sites/academy/home/documentation")

List<JCRNodeWrapper> parentPages = JCRTagUtils.getParentsOfType(curentPageNode, "jmix:navMenuItem")

if (!parentPages.empty) {
    List<JCRNodeWrapper> reversedList = parentPages.reverse()

    for (JCRNodeWrapper parentPage : reversedList) {
        if (JCRTagUtils.isNodeType(parentPage, "jacademix:isVersionPage")) {
            startNode = parentPage.getParent()
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
            if (documentationNode != null) {
                printProducts(documentationNode, 0)
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
