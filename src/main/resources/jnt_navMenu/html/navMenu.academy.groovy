import org.apache.taglibs.standard.functions.Functions
import org.jahia.services.content.JCRContentUtils
import org.jahia.services.render.RenderService
import org.jahia.services.render.Resource
import org.jahia.taglibs.jcr.node.JCRTagUtils
import org.slf4j.LoggerFactory

import javax.jcr.ItemNotFoundException

title = currentNode.properties['jcr:title']
baseline = currentNode.properties['j:baselineNode']
maxDepth = currentNode.properties['j:maxDepth']
startLevel = currentNode.properties['j:startLevel']
styleName = currentNode.properties['j:styleName']
layoutID = currentNode.properties['j:layoutID']

def base;
if (!baseline || baseline.string == 'home') {
    base = renderContext.site.home
} else if (baseline.string == 'currentPage') {
    base = JCRTagUtils.getMeAndParentsOfType(renderContext.mainResource.node, "jnt:page")[0]
}
if (!base) {
    base = renderContext.mainResource.node
}
startLevelValue = startLevel ? startLevel.long : 0

def empty = true
def printMenu;
printMenu = { node, navMenuLevel, omitFormatting ->
    if (navMenuLevel == 1) {
        print("<div class='row row-eq-height'>")
    }
    // jnt:navMenuText
    firstEntry = true;
    if (node) {
        children = JCRContentUtils.getChildrenOfType(node, "jmix:navMenuItem")
        def nbOfChilds = children.size();
        def closeUl = false;
        children.eachWithIndex() { menuItem, index ->
            try {
                if (!menuItem.isNodeType("jacademix:hidePage")) {

                    itemPath = menuItem.path;
                    inpath = renderContext.mainResource.node.path == itemPath || renderContext.mainResource.node.path.startsWith(itemPath + "/");
                    def referenceIsBroken = false;
                    if (menuItem.isNodeType("jmix:nodeReference")) {
                        try {
                            currentResource.dependencies.add(menuItem.properties['j:node'].string);
                        } catch (ItemNotFoundException e) {
                        }
                        try {
                            if (menuItem.properties['j:node'].node != null) {
                                selected = renderContext.mainResource.node.path == menuItem.properties['j:node'].node.path;
                            } else {
                                selected = false;
                                referenceIsBroken = true;
                            }
                        } catch (ItemNotFoundException e) {
                            selected = false;
                            referenceIsBroken = true;
                        }
                    } else {
                        selected = renderContext.mainResource.node.path == itemPath
                    }
                    correctType = true
                    if (menuItem.isNodeType("jmix:navMenu")) {
                        correctType = false
                    }
                    if (menuItem.properties['j:displayInMenuName']) {
                        correctType = false
                        menuItem.properties['j:displayInMenuName'].each() {
                            correctType |= (it.string == currentNode.name)
                        }
                    }
                    if (!referenceIsBroken && correctType && (startLevelValue < navMenuLevel || inpath)) {
                        hasChildren = navMenuLevel < maxDepth.long && JCRTagUtils.hasChildrenOfType(menuItem, "jnt:page,jmix:sitemap,jnt:nodeLink,jnt:externalLink,jnt:navMenuText")
                        if (startLevelValue < navMenuLevel) {
                            //print ("<h1>"+itemPath+closeUl+"</h1>")
                            listItemCssClass = (hasChildren ? "hasChildren" : "noChildren") + (inpath ? " inPath" : "") + (selected ? " selected" : "") + (index == 0 ? " firstInLevel" : "") + (index == nbOfChilds - 1 ? " lastInLevel" : "");
                            Resource resource = new Resource(menuItem, "html", "menuElement", currentResource.getContextConfiguration());
                            currentResource.getDependencies().add(menuItem.getCanonicalPath())
                            def render = RenderService.getInstance().render(resource, renderContext)
                            def currentMenuLevel = navMenuLevel - startLevelValue
                            def description = menuItem.properties["jcr:description"]
                            def tooltip = ""
                            def collapseClass = currentMenuLevel > 2 ? "collapse" : ""
                            def shortdoc = false
                            if (currentMenuLevel <= 2 && menuItem.primaryNodeTypeName == "jnt:page") {
                                shortdoc = true
                            }
                            if (description) {
                                tooltip = "data-bs-toggle=\"tooltip\" data-placement=\"right\" title=\"" + description.string + "\""
                            }
                            def linkURL = ""
                            def collapseAttributes = ""
                            if (!hasChildren) {
                                linkURL = "primaryNodeType=\"" + menuItem.primaryNodeType + "\""
                                if (menuItem.primaryNodeTypeName == "jnt:page") {
                                    linkURL = "href=\"" + menuItem.url + "\""
                                }
                            } else {
                                if (menuItem.primaryNodeTypeName == "jnt:page") {
                                    linkURL = "href=\"" + menuItem.url + "\""
                                } else {
                                    linkURL = "href=\"#menu" + menuItem.identifier + "\" data-bs-toggle=\"collapse\" aria-expanded=\"false\" aria-controls=\"menu" + menuItem.identifier + "\"";
                                }
                                collapseAttributes = "role=\"button\""
                            }
                            if (render != "") {
                                if (currentMenuLevel == 1) {
                                    print("<div class='col-md-4'><div class='simplebox'>")
                                    print("<h3 " + tooltip + ">")
                                    if (shortdoc) {
                                        print("<a " + linkURL + collapseAttributes + ">")
                                    }
                                    print(menuItem.displayableName)
                                    if (shortdoc) {
                                        print("</a>")
                                    }
                                    print("</h3>")
                                } else {
                                    if (firstEntry) {
                                        empty = false;
                                        print("<ul id=\"menu" + node.identifier + "\" class=\"" + collapseClass + " book fa-ul level_${currentMenuLevel}\">")
                                        closeUl = true;
                                    }
                                    if (menuItem.primaryNodeTypeName == "jnt:page" || hasChildren) {
                                        print("<li class=\"${listItemCssClass}\" " + tooltip + ">")
                                        print ("<span class=\"fa-li\" ><i class=\"fas fa-chevron-right\"></i></span>");
                                        print("<a " + linkURL + collapseAttributes + ">")
                                        print(menuItem.displayableName)
                                        if (hasChildren && menuItem.primaryNodeTypeName != "jnt:page") {
                                            print("<i class=\"fas fa-fw fa-angle-down\"></i><i class=\"fas fa-fw fa-angle-up\"></i>");

                                        }
                                        print("</a>")
                                    }
                                }
                            }
                            if (hasChildren) {
                                printMenu(menuItem, navMenuLevel + 1, true)
                            }
                            if (render != "") {
                                if (currentMenuLevel == 1) {
                                    print("</div></div>")
                                } else {
                                    if (menuItem.primaryNodeTypeName == "jnt:page" || hasChildren) {
                                        print "</li>"
                                    }
                                }
                                firstEntry = false;
                            }
                        } else if (hasChildren) {
                            //                    print "<li>"
                            printMenu(menuItem, navMenuLevel + 1, true)
                            //                    print "</li>"
                        }
                    }
                }
            } catch (Exception e) {
                logger = LoggerFactory.getLogger(this.class)
                logger.warn("Error processing nav-menu link with id " + menuItem.identifier, e);
            }
            if (closeUl && index == (nbOfChilds - 1)) {
                print("</ul>");
                closeUl = false;
            }
        }
        if (empty && renderContext.editMode) {
            print "<div class=\"navbar\">Please create some topics ...</div>"
            empty = false;
        }
    }
    if (navMenuLevel == 1) {
        print("</div>")
    }
}
// Add dependencies to parent of main resource so that we are aware of new pages at sibling level
try {
    currentResource.dependencies.add(renderContext.mainResource.node.getParent().getCanonicalPath());
} catch (ItemNotFoundException e) {
}
printMenu(base, 1, false)
