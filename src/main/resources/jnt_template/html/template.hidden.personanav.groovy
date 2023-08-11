import org.jahia.utils.JcrUtils

import javax.jcr.ItemNotFoundException
import org.jahia.services.content.JCRContentUtils
import org.jahia.services.content.JCRNodeWrapper
import org.jahia.taglibs.jcr.node.JCRTagUtils
import org.slf4j.LoggerFactory

logger = LoggerFactory.getLogger(this.class)

def printMenu(JCRNodeWrapper startNode) {
    if (startNode) {
        def children = JCRContentUtils.getChildrenOfType(startNode, "jmix:navMenuItem")
        children.eachWithIndex { menuItem, index ->
            if (menuItem) {
                def correctType = true
                if (menuItem.isNodeType("jmix:navMenu")) {
                    correctType = false
                }
                if (menuItem.properties["j:displayInMenuName"]) {
                    correctType = menuItem.properties["j:displayInMenuName"]
                            .any { it.string == currentNode.name }
                }
                if (correctType) {
                    String menuItemUrl = null
                    String menuItemTitle = menuItem.displayableName
                    String statusClass = renderContext.mainResource.node.path.contains(menuItem.path) ? ' active' : ''
                    if (menuItem.isNodeType("jnt:page")) {
                        menuItemUrl = renderContext.getResponse().encodeURL(menuItem.url)
                    } else if (menuItem.isNodeType("jnt:navMenuText")) {
                        menuItemUrl = findFirstSubPageUrl(menuItem)
                    }
                    print """
                        <li class="nav-item">
                            <a class="nav-link${statusClass}" href="${menuItemUrl ?: '#'}">${menuItemTitle}</a>
                        </li>
                    """
                }
            }
        }
    }
}

def printProducts(JCRNodeWrapper startNode, int level) {
    if (startNode) {
        def children = JCRContentUtils.getChildrenOfType(startNode, "jmix:navMenuItem")
        int idx = 0;
        children.eachWithIndex { menuItem, index ->
            if (menuItem) {
                def correctType = true
                if (menuItem.isNodeType("jmix:navMenu")) {
                    correctType = false
                }
                if (menuItem.properties["j:displayInMenuName"]) {
                    correctType = menuItem.properties["j:displayInMenuName"]
                            .any { it.string == currentNode.name }
                }
                if (correctType && (level > 0 || menuItem.isNodeType("jacademix:isProduct"))) {
                    if (menuItem.isNodeType("jacademix:isProduct")) {
                        idx++;
                    }
                    String menuItemUrl = null
                    String menuItemTitle = menuItem.displayableName
                    String statusClass = renderContext.mainResource.node.path.contains(menuItem.path) ? ' active' : ''
                    String extraClass = level == 0 ? " product":""
                    if (menuItem.isNodeType("jnt:page")) {
                        menuItemUrl = renderContext.getResponse().encodeURL(menuItem.url)
                    } else if (menuItem.isNodeType("jnt:navMenuText")) {
                        menuItemUrl = findFirstSubPageUrl(menuItem)
                    }
                    if (idx>1 && level == 0) {
                        print """
                            <li><hr class="dropdown-divider"></li>
                        """
                    }
                    print """
                        <li><a class="dropdown-item ${extraClass}" href="${menuItemUrl}">${menuItemTitle}</a></li>
                    """
                    if (level == 0 && ! menuItem.isNodeType("jacademix:isVersionPage")) {
                        printProducts(menuItem,level+1);
                    }
                }
            }
        }
    }
}


def findFirstSubPageUrl(JCRNodeWrapper node) {
    if (!node) return null

    def children = JCRContentUtils.getChildrenOfType(node, "jmix:navMenuItem")

    for (menuItem in children) {
        if (menuItem.isNodeType("jnt:page")) {
            return menuItem.getUrl()
        } else {
            def subPageUrl = findFirstSubPageUrl(menuItem)
            if (subPageUrl) {
                return subPageUrl
            }
        }
    }

    return null
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
    try {
        currentResource.dependencies.add(renderContext.mainResource.node.getParent().getCanonicalPath())
    } catch (ItemNotFoundException e) {
        // Handle exception if needed
    }

    print """
        <div class="row darkblue">
            <div class="col-md-8">
                <ul class="nav nav-underline ">
    """
    printMenu(startNode)
    print """
                </ul>
            </div>            
            <div class="col-md-4 text-end pe-4">                
                <div class="btn-group">
                  <button type="button" class="btn btn-light dropdown-toggle" data-bs-toggle="dropdown" aria-expanded="false">
                    ${startNode.getDisplayableName()}
                  </button>
                  <ul class="dropdown-menu">
    """
    if (documentationNode != null) {
        printProducts(documentationNode,0)
    }
    print """
                  </ul>
                </div>
                
                
            </div>
        </div>
    """
}
