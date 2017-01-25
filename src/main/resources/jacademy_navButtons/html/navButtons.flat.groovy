import org.jahia.services.content.JCRContentUtils
import org.jahia.services.content.JCRNodeWrapper
import org.jahia.taglibs.jcr.node.JCRTagUtils
import org.slf4j.LoggerFactory
import javax.jcr.ItemNotFoundException

/*
getFlatTree will generate a flat tree of pages. For instance such tree

    - A (page)
    - B (page)
    - C (navMenuText)
        - C1 (page)
        - C2 (page)
        - C3 (page)
    - D (page)
    - E (page)
    - F (page)

will generate an array with [A, B, C1, C2, C3, D, E, F]

 */
def getFlatTree;
getFlatTree = { node, list ->
    if (node) {
        menuItems = JCRContentUtils.getChildrenOfType(node, "jmix:navMenuItem")
        menuItems.eachWithIndex() { menuItem, index ->
            try {
                if (!menuItem.isNodeType("jacademix:hidePage")) {
                    hasChildren = JCRTagUtils.hasChildrenOfType(menuItem, "jnt:page,jnt:navMenuText")
                    if (menuItem.isNodeType("jnt:page")) {
                        list.add(menuItem);
                        //print("${menuItem.path} <br/>")
                    }
                    try {
                        currentResource.dependencies.add(menuItem.getCanonicalPath());
                    } catch (ItemNotFoundException infe) {
                    }
                    if (hasChildren) {
                        list = getFlatTree(menuItem, list)
                    }
                }
            } catch (Exception e) {
                logger = LoggerFactory.getLogger(this.class)
                logger.warn("Error processing nav-menu link with id " + menuItem.identifier, e);
            }
        }
    }
    return list;
}
try {
    currentResource.dependencies.add(renderContext.mainResource.node.getParent().getCanonicalPath());
} catch (ItemNotFoundException infe) {
}

def mainResourceNode = renderContext.mainResource.node;

// generate a previous/next buttons to link to the previous/next page.
def flatTree = getFlatTree(mainResourceNode.getParent().getParent().getParent(), []);
for (int i = 0; i < flatTree.size(); i++) {
    JCRNodeWrapper currentNode = flatTree.get(i);
    if (mainResourceNode.path == currentNode.path) {
        if (i - 1 >= 0) {
            JCRNodeWrapper previousNode = flatTree.get(i - 1);
            if (previousNode.isNodeType("jacademix:isMultiplePageDoc") &&
                    previousNode.getParent().getPath() != mainResourceNode.getParent().getPath()) {
                print("<a class=\"btn btn-link pull-left\" href=\"${previousNode.url}\" ><i\n" +
                        " class=\"fa fa-arrow-left fa-fw\"></i>Previous: ${previousNode.getParent().displayableName} / ${previousNode.displayableName}</a>")

            } else {
                print("<a class=\"btn btn-link pull-left\" href=\"${previousNode.url}\" ><i\n" +
                        " class=\"fa fa-arrow-left fa-fw\"></i>Previous: ${previousNode.displayableName}</a>")
            }
        }
        //print("--- C " + currentNode.path + " " + currentNode.getDisplayableName() + " * <br/>")
        if (i + 1 < flatTree.size()) {
            JCRNodeWrapper nextNode = flatTree.get(i + 1);
            if (nextNode.isNodeType("jacademix:isMultiplePageDoc") &&
                    nextNode.getParent().getPath() != mainResourceNode.getParent().getPath()) {
                print("<a class=\"btn btn-link pull-right\" href=\"${nextNode.url}\" >Next: ${nextNode.getParent().displayableName} / ${nextNode.displayableName}&nbsp;<i\n" +
                        " class=\"fa fa-arrow-right\"></i></a>")
            } else {
                print("<a class=\"btn btn-link pull-right\" href=\"${nextNode.url}\" >Next: ${nextNode.displayableName}&nbsp;<i\n" +
                        " class=\"fa fa-arrow-right\"></i></a>")
            }
        }
        break;
    }
}
