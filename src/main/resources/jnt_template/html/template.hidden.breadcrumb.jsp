<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="jcr" uri="http://www.jahia.org/tags/jcr" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="functions" uri="http://www.jahia.org/tags/functions" %>
<%@ taglib prefix="template" uri="http://www.jahia.org/tags/templateLib" %>

<%--@elvariable id="currentNode" type="org.jahia.services.content.JCRNodeWrapper"--%>
<%--@elvariable id="renderContext" type="org.jahia.services.render.RenderContext"--%>


<c:set var="pageNodes" value="${jcr:getParentsOfType(currentNode, 'jmix:navMenuItem')}"/>
<c:if test="${empty pageNodes}">
    <c:set var="mainResourceNode" value="${renderContext.mainResource.node}"/>
    <c:choose>
        <c:when test="${jcr:isNodeType(mainResourceNode, 'jnt:page')}">
            <c:set var="pageNodes"
                   value="${jcr:getMeAndParentsOfType(mainResourceNode,'jmix:navMenuItem')}"/>
        </c:when>
        <c:otherwise>
            <c:set var="pageNodes"
                   value="${jcr:getParentsOfType(mainResourceNode, 'jmix:navMenuItem')}"/>
        </c:otherwise>
    </c:choose>
</c:if>
<c:if test="${fn:length(pageNodes) > 1}">
    <nav aria-label="breadcrumb">
        <ol class="breadcrumb d-flex align-items-center py-3 m-0 small">
            <c:forEach items="${functions:reverse(pageNodes)}" var="pageNode" varStatus="status">
                <c:if test="${status.index > 1}">
                    <c:if test="${! jcr:isNodeType(pageNode, 'jacademix:hidePage')}">
                        <%-- now we check if there is sister pages --%>
                        <c:set var="parentPage" value="${jcr:getParentOfType(pageNode, 'jmix:navMenuItem')}"/>

                        <c:set var="sisterPages"
                               value="${jcr:getChildrenOfType(parentPage, 'jmix:navMenuItem')}"/>
                        <c:set var="hasSisterPages" value="${fn:length(sisterPages) > 1}"/>
                        <c:choose>
                            <c:when test="${hasSisterPages}">
                                <%--
                                <li class="breadcrumb-item dropdown">
                                    <a href="#" class="dropdown-toggle" type="button" id="breadcrumb2" data-bs-toggle="dropdown"
                                       aria-expanded="false">Library</a>
                                    <ul class="dropdown-menu" aria-labelledby="breadcrumb2">
                                        <li><a class="dropdown-item" href="#">Action</a></li>
                                        <li><a class="dropdown-item" href="#">Another action</a></li>
                                        <li>
                                            <a class="dropdown-item" href="#">Something else here</a>
                                        </li>
                                    </ul>
                                </li>
                                --%>
                                <li class="breadcrumb-item dropdown">
                                    <template:addCacheDependency node="${pageNode}"/>
                                    <a href="#" class="dropdown-toggle" data-bs-toggle="dropdown" role="button"
                                       aria-expanded="false" id="breadcrumb_${pageNode.identifier}">
                                            ${pageNode.displayableName}
                                    </a>
                                    <ul class="dropdown-menu" aria-labelledby="breadcrumb_${pageNode.identifier}">
                                            <%-- FIXME: This is what we call manual recursivity.... should be a call to groovy script... --%>
                                        <c:forEach items="${sisterPages}" var="sisterPage" varStatus="status">
                                            <c:if test="${! jcr:isNodeType(sisterPage, 'jacademix:hidePage')}">
                                                <c:choose>
                                                    <c:when test="${jcr:isNodeType(sisterPage, 'jnt:navMenuText')}">
                                                        <template:addCacheDependency node="${sisterPage}"/>
                                                        <c:set var="active"><c:if
                                                                test="${fn:contains(renderContext.mainResource.path,sisterPage.path)}">active</c:if></c:set>
                                                        <c:set var="subsisterPages"
                                                               value="${jcr:getChildrenOfType(sisterPage, 'jmix:navMenuItem')}"/>
                                                        <c:set var="hassubSisterPages"
                                                               value="${fn:length(subsisterPages) > 0}"/>
                                                        <c:choose>
                                                            <c:when test="${hassubSisterPages}">
                                                                <li class="dropdown-item dropdown ${active} dropend">
                                                                    <a href="#" class="dropdown-toggle"
                                                                       data-bs-toggle="dropdown" role="button"
                                                                       aria-expanded="false"
                                                                       role="button"
                                                                       id="breadcrumb_${sisterPage.identifier}">${sisterPage.displayableName}</a>
                                                                    <ul class="submenu dropdown-menu"
                                                                        aria-labelledby="breadcrumb_${sisterPage.identifier}">
                                                                        <c:forEach items="${subsisterPages}"
                                                                                   var="subsisterPage"
                                                                                   varStatus="status">
                                                                            <c:if test="${! jcr:isNodeType(subsisterPage, 'jacademix:hidePage')}">
                                                                                <c:choose>
                                                                                    <c:when test="${jcr:isNodeType(subsisterPage, 'jnt:navMenuText')}">
                                                                                        <%-- This is a label -> link to the first page if exist --%>
                                                                                        <template:addCacheDependency
                                                                                                node="${subsisterPage}"/>
                                                                                        <c:set var="subsubsisterPages"
                                                                                               value="${jcr:getChildrenOfType(subsisterPage, 'jnt:page')}"/>
                                                                                        <c:forEach
                                                                                                items="${subsubsisterPages}"
                                                                                                var="subsubsisterPage"
                                                                                                varStatus="substatus">
                                                                                            <c:if test="${substatus.first}">
                                                                                                <c:if test="${! jcr:isNodeType(subsubsisterPage, 'jacademix:hidePage')}">
                                                                                                    <c:url var="subsubsisterPageUrl"
                                                                                                           value="${subsubsisterPage.url}"/>
                                                                                                    <li class="dropdown-item"${fn:contains(renderContext.mainResource.path,subsisterPage.path) ? ' active' : ''}>
                                                                                                        <a
                                                                                                                href="${subsubsisterPageUrl}">${subsisterPage.displayableName}</a>
                                                                                                    </li>
                                                                                                </c:if>
                                                                                            </c:if>
                                                                                        </c:forEach>
                                                                                    </c:when>
                                                                                    <c:otherwise>
                                                                                        <template:addCacheDependency
                                                                                                node="${subsisterPage}"/>
                                                                                        <c:url var="subsisterPageUrl"
                                                                                               value="${subsisterPage.url}"/>
                                                                                        <li class="dropdown-item${fn:contains(renderContext.mainResource.path,subsisterPage.path)?' avtive' : ''}">
                                                                                            <a
                                                                                                    href="${subsisterPageUrl}">${subsisterPage.displayableName}</a>
                                                                                        </li>

                                                                                    </c:otherwise>
                                                                                </c:choose>
                                                                            </c:if>
                                                                        </c:forEach>
                                                                    </ul>
                                                                </li>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <li class="dropdown-item">
                                                                    <a href="#">${sisterPage.displayableName}</a>
                                                                </li>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </c:when>
                                                    <c:when test="${jcr:isNodeType(sisterPage, 'jnt:page')}">
                                                        <c:url var="sisterPageUrl" value="${sisterPage.url}"
                                                               context="/"/>
                                                        <li class="dropdown-item${fn:contains(renderContext.mainResource.path,sisterPage.path) ? ' active':''}">
                                                            <a
                                                                    href="${sisterPageUrl}">${sisterPage.displayableName}</a>
                                                        </li>
                                                    </c:when>
                                                </c:choose>
                                            </c:if>
                                            <c:remove var="subsisterPages"/>
                                            <c:remove var="hassubSisterPages"/>
                                        </c:forEach>
                                    </ul>
                                </li>
                            </c:when>
                            <c:otherwise>
                                <li class="breadcrumb-item">
                                    <c:choose>
                                        <c:when test="${jcr:findDisplayableNode(pageNode, renderContext) ne pageNode}">
                                            <a href="#"><c:out value="${pageNode.displayableName}"/></a>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="<c:url value='${pageNode.url}' context='/'/>"><c:out
                                                    value="${pageNode.displayableName}"/></a>
                                        </c:otherwise>
                                    </c:choose>
                                </li>
                            </c:otherwise>
                        </c:choose>
                        <c:remove var="sisterPages"/>
                        <c:remove var="hasSisterPages"/>
                    </c:if>
                </c:if>
                <c:remove var="pageUrl"/>
            </c:forEach>
        </ol>
    </nav>
</c:if>

<%--


<nav aria-label="breadcrumb">
    <ol class="breadcrumb d-flex align-items-center px-5 py-3 m-0">
        <li class="breadcrumb-item"><a href="#">Home</a></li>
        <li class="breadcrumb-item dropdown">
            <a href="#" class="dropdown-toggle" type="button" id="breadcrumb2" data-bs-toggle="dropdown"
               aria-expanded="false">Library</a>
            <ul class="dropdown-menu" aria-labelledby="breadcrumb2">
                <li><a class="dropdown-item" href="#">Action</a></li>
                <li><a class="dropdown-item" href="#">Another action</a></li>
                <li>
                    <a class="dropdown-item" href="#">Something else here</a>
                </li>
            </ul>
        </li>
        <li class="breadcrumb-item dropdown active">
            <a href="#" class="dropdown-toggle" type="button" id="breadcrumb3" data-bs-toggle="dropdown"
               aria-expanded="false">Data</a>
            <ul class="dropdown-menu" aria-labelledby="breadcrumb3">
                <li><a class="dropdown-item" href="#">Action</a></li>
                <li><a class="dropdown-item" href="#">Another action</a></li>
                <li>
                    <a class="dropdown-item" href="#">Something else here</a>
                </li>
            </ul>
        </li>
    </ol>
</nav>
--%>
