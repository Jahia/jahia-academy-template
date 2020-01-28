<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="template" uri="http://www.jahia.org/tags/templateLib" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="jcr" uri="http://www.jahia.org/tags/jcr" %>
<%@ taglib prefix="ui" uri="http://www.jahia.org/tags/uiComponentsLib" %>
<%@ taglib prefix="functions" uri="http://www.jahia.org/tags/functions" %>
<%@ taglib prefix="query" uri="http://www.jahia.org/tags/queryLib" %>
<%@ taglib prefix="utility" uri="http://www.jahia.org/tags/utilityLib" %>
<%@ taglib prefix="s" uri="http://www.jahia.org/tags/search" %>
<%--@elvariable id="currentNode" type="org.jahia.services.content.JCRNodeWrapper"--%>
<%--@elvariable id="out" type="java.io.PrintWriter"--%>
<%--@elvariable id="script" type="org.jahia.services.render.scripting.Script"--%>
<%--@elvariable id="scriptInfo" type="java.lang.String"--%>
<%--@elvariable id="workspace" type="java.lang.String"--%>
<%--@elvariable id="renderContext" type="org.jahia.services.render.RenderContext"--%>
<%--@elvariable id="currentResource" type="org.jahia.services.render.Resource"--%>
<%--@elvariable id="url" type="org.jahia.services.render.URLGenerator"--%>

<c:set var="pageNodes" value="${jcr:getMeAndParentsOfType(renderContext.mainResource.node, 'jacademix:isVersionPage')}"/>
<c:set var="isAcurrentVersion" value="false"/>
<c:set var="hasFindCurrentVersion" value="false"/>
<c:choose>
    <c:when test="${fn:length(pageNodes) > 0}">
        <c:forEach var="pageNode" items="${pageNodes}" end="0">
            <template:addCacheDependency path="${pageNode.path}"/>
            <c:url var="currentVersionNodeUrl" value="${pageNode.url}" context="/"/>
            <c:set var="currentVersion" value="${pageNode.properties.version.string}"/>
            <c:choose>
                <c:when test="${currentVersion eq 'current'}">
                    <c:set var="isAcurrentVersion" value="true"/>
                    <c:set var="hasFindCurrentVersion" value="true"/>
                </c:when>
                <c:when test="${fn:startsWith(currentVersion, 'current')}">
                    <c:set var="isAcurrentVersion" value="true"/>
                </c:when>
                <c:otherwise>
                    <%-- try to finf the sbinding page with current version --%>
                    <c:set var="localPathToDoc" value="${fn:replace(renderContext.mainResource.node.path, pageNode.path, '')}"/>
                    <c:set var="versionNodes" value="${jcr:getChildrenOfType(pageNode.parent, 'jacademix:isVersionPage')}"/>
                    <c:forEach var="versionNode" items="${versionNodes}">
                        <c:set var="versionNodeVersion" value="${versionNode.properties.version.string}"/>
                        <c:if test="${versionNodeVersion eq 'current'}">
                            <jcr:node var="versionDocNode" path="${versionNode.path}${localPathToDoc}"/>
                            <template:addCacheDependency path="${versionDocNode.path}"/>
                            <c:choose>
                                <c:when test="${! empty versionDocNode}">
                                    <c:url var="currentVersionNodeUrl" value="${versionDocNode.url}" context="/"/>
                                </c:when>
                                <c:otherwise>
                                    <c:url var="currentVersionNodeUrl" value="${versionNode.url}" context="/"/>
                                </c:otherwise>
                            </c:choose>
                            <c:set var="hasFindCurrentVersion" value="true"/>
                        </c:if>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </c:forEach>
    </c:when>
    <c:otherwise>
        <c:url var="currentVersionNodeUrl" value="${renderContext.site.home.url}" context="/"/>
    </c:otherwise>
</c:choose>


<c:if test="${! isAcurrentVersion && hasFindCurrentVersion}">
    <div class="alert alert-warning" role="alert">
        <fmt:message key="jacademy_checkCurrentVersion.alert">
            <fmt:param value="${currentVersionNodeUrl}"/>
        </fmt:message>
    </div>
</c:if>
<c:if test="${! hasFindCurrentVersion && renderContext.editMode}">
    <div class="alert alert-danger" role="alert">
        <fmt:message key="jacademy_checkCurrentVersion.noCurrent"/>
    </div>
</c:if>


