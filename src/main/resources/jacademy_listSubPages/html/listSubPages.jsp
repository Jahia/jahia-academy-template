<%@ taglib prefix="template" uri="http://www.jahia.org/tags/templateLib" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="jcr" uri="http://www.jahia.org/tags/jcr" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--@elvariable id="currentNode" type="org.jahia.services.content.JCRNodeWrapper"--%>
<%--@elvariable id="out" type="java.io.PrintWriter"--%>
<%--@elvariable id="script" type="org.jahia.services.render.scripting.Script"--%>
<%--@elvariable id="scriptInfo" type="java.lang.String"--%>
<%--@elvariable id="workspace" type="java.lang.String"--%>
<%--@elvariable id="renderContext" type="org.jahia.services.render.RenderContext"--%>
<%--@elvariable id="currentResource" type="org.jahia.services.render.Resource"--%>
<%--@elvariable id="url" type="org.jahia.services.render.URLGenerator"--%>
<c:set var="parentPage" value="${currentNode.properties['parentPage'].node}"/>
<c:choose>
    <c:when test="${! empty parentPage}">
        <c:set var="depth" value="${currentNode.properties.depth.string}"/>
        <c:if test="${empty depth}">
            <c:set var="depth" value="1-level"/>
        </c:if>
        <ul class="book fa-ul">
            <c:set var="pages" value="${jcr:getChildrenOfType(parentPage, 'jmix:navMenuItem')}"/>
            <c:set var="displayParentPage" value="${currentNode.properties.displayParentPage.string}"/>
            <c:if test="${empty displayParentPage}">
                <c:set var="displayParentPage" value="false"/>
            </c:if>
            <c:if test="${displayParentPage eq 'true'}">
                <c:if test="${jcr:isNodeType(parentPage, 'jnt:page')}">
                    <li><span class="fa-li" ><i class="fas fa-chevron-right"></i></span>
                            <c:set var="pageTitle" value="${parentPage.displayableName}"/>
                            <c:url var="pageUrl" value="${parentPage.url}" context="/"/>
                        <a href="${pageUrl}">${pageTitle}</a>
                    </li>
                </c:if>
            </c:if>

            <c:forEach items="${pages}" var="page" varStatus="status">
                <template:addCacheDependency node="${page}"/>
                <li><span class="fa-li" ><i class="fas fa-chevron-right"></i></span>
                    <c:set var="pageTitle" value="${page.displayableName}"/>
                    <c:choose>
                        <c:when test="${jcr:isNodeType(page, 'jnt:navMenuText')}">
                            <c:set var="pageUrl" value="#"/>
                            <%-- try to get the first subpage --%>
                            <c:if test="${depth ne '2-level-accordion'}">
                                <c:set var="subpages" value="${jcr:getChildrenOfType(page, 'jmix:navMenuItem')}"/>
                                <c:forEach items="${subpages}" var="subpage" varStatus="status">
                                    <c:if test="${status.first}">
                                        <c:choose>
                                            <c:when test="${jcr:isNodeType(subpage, 'jnt:navMenuText')}">
                                                <c:set var="pageUrl" value="#"/>
                                                <c:set var="subsubpages" value="${jcr:getChildrenOfType(subpage, 'jnt:page')}"/>
                                                <c:forEach items="${subsubpages}" var="subsubpage" varStatus="substatus">
                                                    <c:if test="${substatus.first}">
                                                        <c:url var="pageUrl" value="${subsubpage.url}" context="/"/>
                                                    </c:if>
                                                </c:forEach>
                                                <c:remove var="subsubpages"/>
                                            </c:when>
                                            <c:when test="${jcr:isNodeType(subpage, 'jnt:externalLink')}">
                                                <c:url var="pageUrl" value="${subpage.properties['j:url'].string}"/>
                                            </c:when>
                                            <c:when test="${jcr:isNodeType(subpage, 'jnt:page')}">
                                                <c:url var="pageUrl" value="${subpage.url}" context="/"/>
                                            </c:when>
                                            <c:when test="${jcr:isNodeType(subpage, 'jnt:nodeLink')}">
                                                <c:url var="pageUrl" value="${subpage.properties['j:node'].node.url}" context="/"/>
                                            </c:when>
                                        </c:choose>
                                    </c:if>
                                </c:forEach>
                            </c:if>
                        </c:when>
                        <c:when test="${jcr:isNodeType(page, 'jnt:externalLink')}">
                            <c:url var="pageUrl" value="${page.properties['j:url'].string}"/>
                        </c:when>
                        <c:when test="${jcr:isNodeType(page, 'jnt:page')}">
                            <c:url var="pageUrl" value="${page.url}" context="/"/>
                        </c:when>
                        <c:when test="${jcr:isNodeType(page, 'jnt:nodeLink')}">
                            <c:url var="pageUrl" value="${page.properties['j:node'].node.url}" context="/"/>
                            <c:if test="${empty pageTitle}">
                                <c:set var="pageTitle"
                                       value="${page.properties['j:node'].node.displayableName}"/>
                            </c:if>
                        </c:when>
                    </c:choose>

                    <c:set var="subpages" value="${jcr:getChildrenOfType(page, 'jmix:navMenuItem')}"/>
                    <c:set var="hasSubpages" value="${fn:length(subpages) > 0}"/>

                    <c:choose>
                        <c:when test="${depth eq '2-level-accordion' && hasSubpages}">
                            <a data-toggle="collapse" aria-expanded="false" aria-controls="ul${page.identifier}" href="#ul${page.identifier}">${pageTitle} <i class="fas fa-fw fa-angle-down"></i><i class="fas fa-fw fa-angle-up"></i></a>
                        </c:when>
                        <c:otherwise>
                            <a href="${pageUrl}">${pageTitle}</a>
                    </c:otherwise>
                    </c:choose>


                    <c:if test="${depth ne '1-level'}">
                        <c:if test="${hasSubpages}">
                            <div id="ul${page.identifier}" class="${depth eq '2-level-accordion' ? 'collapse' : ''}">
                                <ul class="fa-ul">
                                    <c:if test="${depth eq '2-level-accordion' && ! jcr:isNodeType(page, 'jnt:navMenuText')}">
                                        <li><span class="fa-li" ><i class="fas fa-chevron-right"></i></span><a href="${pageUrl}">${pageTitle}</a></li>
                                    </c:if>
                                    <c:forEach items="${subpages}" var="subpage" varStatus="status">
                                        <c:set var="subpageTitle" value="${subpage.displayableName}"/>
                                        <c:choose>
                                            <c:when test="${jcr:isNodeType(subpage, 'jnt:navMenuText')}">
                                                <c:set var="subpageUrl" value="#"/>
                                                <c:set var="subsubpages" value="${jcr:getChildrenOfType(subpage, 'jnt:page')}"/>
                                                <c:forEach items="${subsubpages}" var="subsubpage" varStatus="substatus">
                                                    <c:if test="${substatus.first}">
                                                        <c:url var="subpageUrl" value="${subsubpage.url}" context="/"/>
                                                    </c:if>
                                                </c:forEach>
                                            </c:when>
                                            <c:when test="${jcr:isNodeType(subpage, 'jnt:externalLink')}">
                                                <c:url var="subpageUrl" value="${subpage.properties['j:url'].string}"/>
                                            </c:when>
                                            <c:when test="${jcr:isNodeType(subpage, 'jnt:page')}">
                                                <c:url var="subpageUrl" value="${subpage.url}" context="/"/>
                                            </c:when>
                                            <c:when test="${jcr:isNodeType(subpage, 'jnt:nodeLink')}">
                                                <c:url var="subpageUrl" value="${subpage.properties['j:node'].node.url}" context="/"/>
                                                <c:if test="${empty subpageTitle}">
                                                    <c:set var="subpageTitle"
                                                           value="${subpage.properties['j:node'].node.displayableName}"/>
                                                </c:if>
                                            </c:when>
                                        </c:choose>
                                        <li><span class="fa-li" ><i class="fas fa-chevron-right"></i></span><a href="${subpageUrl}">${subpageTitle}</a></li>
                                    </c:forEach>
                                </ul>
                            </div>
                        </c:if>
                    </c:if>

                </li>
            </c:forEach>
        </ul>
    </c:when>
    <c:otherwise>
        <c:if test="${renderContext.editMode}">
            Please select a parent page / label
        </c:if>
    </c:otherwise>
</c:choose>
