import javax.jcr.ItemNotFoundException
import org.jahia.services.content.JCRContentUtils
import org.jahia.services.content.JCRNodeWrapper
import org.jahia.services.render.RenderService
import org.jahia.services.render.Resource
import org.jahia.taglibs.jcr.node.JCRTagUtils
import org.slf4j.LoggerFactory

logger = LoggerFactory.getLogger(this.class)

def printMenu;
printMenu = { startNode, level, maxlevel ->
    if (startNode != null) {
        children = JCRContentUtils.getChildrenOfType(startNode, "jmix:navMenuItem")
        children.eachWithIndex() { menuItem, index ->
            if (menuItem != null) {
                def correctType = true;
                if (menuItem.isNodeType("jmix:navMenu")) {
                    correctType = false;
                }
                if (menuItem.properties['j:displayInMenuName']) {
                    correctType = false;
                    menuItem.properties['j:displayInMenuName'].each() {
                        correctType |= (it.string.equals(currentNode.name))
                    }
                }
                if (correctType) {

                    boolean hasChildren = level < maxlevel && JCRTagUtils.hasChildrenOfType(menuItem, "jmix:navMenuItem")
                    String menuItemUrl = null;
                    String menuItemId = "page-" + menuItem.getIdentifier();
                    String menuItemTitle = menuItem.displayableName;
                    boolean isActive = renderContext.mainResource.node.path.indexOf(menuItem.path) > -1;
                    boolean isCurrent = renderContext.mainResource.node.path.equals(menuItem.path);
                    String statusClass = isCurrent ? ' active' : isActive ? ' inpath' : '';

                    if (menuItem.isNodeType('jnt:page')) {
                        menuItemUrl = renderContext.getResponse().encodeURL(menuItem.url);
                    } else if (menuItem.isNodeType('jnt:nodeLink')) {
                        JCRNodeWrapper refNode = menuItem.properties['j:node'].node;
                        if (refNode != null) {
                            isActive = renderContext.mainResource.node.path.indexOf(refNode.path) > -1;
                            isCurrent = renderContext.mainResource.node.path.equals(refNode.path);
                            statusClass = isCurrent ? ' active' : isActive ? ' inpath' : '';
                            currentResource.dependencies.add(refNode.getCanonicalPath());
                            menuItemTitle = menuItem.getPropertyAsString("jcr:title");
                            if (menuItemTitle == null || "".equals(menuItemTitle)) {
                                menuItemTitle = refNode.displayableName;
                            }
                            menuItemUrl = renderContext.getResponse().encodeURL(refNode.url);
                        }
                    } else if (menuItem.isNodeType('jnt:externalLink')) {
                        menuItemUrl = menuItem.properties['j:url'].string;
                    }
                    if (menuItemUrl == null || "".equals(menuItemUrl)) {
                        menuItemUrl = "#";
                    }

                    if (hasChildren && level < maxlevel) {
                        if (level == 1) {
                            print "<li>";
                            print "<a class='d-inline-flex align-items-center rounded collapsed' href='${menuItemUrl}' id='${menuItemId}'>${menuItemTitle}"
                            if (isCurrent) {
                                print " <span class='visually-hidden'>(current)</span>";
                            }
                            print "</a>";
                            print "<ul>";
                            printMenu(menuItem, level + 1, maxlevel);
                            print "</ul>";

                            print "</li>";
                        } else if (level == 2) {
                            print "<li>";
                            print "<a class='btn d-inline-flex align-items-center rounded collapse' data-bs-target='#sideMenu-${currentNode.identifier}-${menuItem.identifier}' data-bs-toggle='collapse' aria-expanded='false' href='#'>${menuItemTitle}"
                            if (isCurrent) {
                                print " <span class='visually-hidden'>(current)</span>";
                            }
                            print "</a>";
                            print "<div class=\"collapse${isActive?'d':''}\" id=\"sideMenu-${currentNode.identifier}-${menuItem.identifier}\">"
                            print "<ul class=\"fw-normal pb-1 small\">";
                            printMenu(menuItem, level + 1, maxlevel);
                            print "</ul>";
                            print "</div>"
                            print "</li>";
                        } else {
                            print "<li>";
                            print "<a href='${menuItemUrl}' id='${menuItemId}'>${menuItemTitle}"
                            if (isCurrent) {
                                print " <span class='visually-hidden'>(current)</span>";
                            }
                            print "</a>";
                            print "<ul class=\"fw-normal pb-1 small\">";
                            print "<li><a href='${menuItemUrl}' id='${menuItemId}'>${menuItemTitle}</a></li>";
                            //print "<li class='dropdown-divider'></li>";
                            printMenu(menuItem, level + 1, maxlevel);
                            print "</ul>";
                            print "</li>";
                        }
                    } else {
                        if (level == 1) {
                            print "<li>";
                            print "<a class=\"d-inline-flex align-items-center rounded ${isCurrent ? ' active' : ''}\" href=\"${menuItemUrl}\" id='${menuItemId}'>${menuItemTitle}";
                            if (isCurrent) {
                                print " <span class=\"visually-hidden\">(current)</span>";
                            }
                            print "</a>"
                            print "</li>";
                        } else if (level == 2) {
                                print "<li>";
                                print "<a class=\"d-inline-flex align-items-center rounded ${isCurrent?' active':''}\" href=\"${menuItemUrl}\" id='${menuItemId}'>${menuItemTitle}";
                                if (isCurrent) {
                                    print " <span class=\"visually-hidden\">(current)</span>";
                                }
                                print "</a>"
                                print "</li>";
                        } else {
                            print "<li>";
                            print "<a class=\"d-inline-flex align-items-center rounded ${isCurrent ? ' active' : ''}\" href=\"${menuItemUrl}\" id='${menuItemId}'>${menuItemTitle}";
                            if (isCurrent) {
                                print " <span class=\"visually-hidden\">(current)</span>";
                            }
                            print "</a>"
                            print "</li>";

                        }
                    }
                }
            }
        }
    }
}

long maxlevel = 5;

JCRNodeWrapper startNode = null;

JCRNodeWrapper curentPageNode = renderContext.mainResource.node;
List<JCRNodeWrapper> parentPages = JCRTagUtils.getParentsOfType(curentPageNode,"jmix:navMenuItem");
if (!parentPages.empty) {
    List<JCRNodeWrapper> reversedList = new ArrayList();
    reversedList.addAll(parentPages);
    Collections.reverse(reversedList);
    for (JCRNodeWrapper parentPage : reversedList) {
        if (JCRTagUtils.isNodeType(parentPage,'jacademix:isVersionPage')) {
            startNode = parentPage;
            break;
        }
    }
}
if (startNode != null) {
    // Add dependencies to parent of main resource so that we are aware of new pages at sibling level
    try {
        currentResource.dependencies.add(renderContext.mainResource.node.getParent().getCanonicalPath());
    } catch (ItemNotFoundException e) {
    }
    print "<aside class=\"sticky-top bg-light d-none d-sm-block vh-100\">\n" +
            "    <nav class=\"collapse bd-links pt-4 sticky-top \" id=\"bd-docs-nav\" aria-label=\"Docs navigation\">";
    print "<ul class=\"mb-0 py-2 pt-md-1\">";
    printMenu(startNode, 1,  maxlevel)
    print "</ul>"
    print "</nav>";
    print "</aside>";
}



