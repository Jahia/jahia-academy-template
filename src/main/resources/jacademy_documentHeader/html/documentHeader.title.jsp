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
<c:set var="boundComponent" value="${ui:getBindedComponent(currentNode, renderContext, 'j:bindedComponent')}"/>
<c:set var="documentNode" value="${null}"/>
<%-- Check for a document Node to bind --%>
<c:if test="${boundComponent != null}">
    <c:choose>
        <c:when test="${jcr:isNodeType(boundComponent, 'jacademix:document')}">
            <%-- If bound component is not a document search for a document in its children --%>
            <c:set var="documentNode" value="${boundComponent}"/>
        </c:when>
        <c:otherwise>
            <%-- If bound component  is not a document search for a document in its children --%>
            <c:set var="documentChildren" value="${jcr:getChildrenOfType(boundComponent,'jacademix:document')}"/>
            <c:if test="${fn:length(documentChildren)>0}">
                <c:set var="documentNode" value="${documentChildren[0]}"/>
            </c:if>
        </c:otherwise>
    </c:choose>
</c:if>

<%-- Then display the document Header --%>
<c:if test="${documentNode != null}">
    <h1>
        <c:set var="currentPageNode" value="${renderContext.mainResource.node}"/>
        <c:if test="${jcr:isNodeType(currentPageNode, 'jacademix:isMultiplePageDoc')}">
            <c:set var="parentPage" value="${jcr:getParentOfType(currentPageNode, 'jmix:navMenuItem')}"/>
            <span class="hidden-xs">${parentPage.displayableName}</span>
        </c:if>
        <c:set var="title" value="${documentNode.properties['jcr:title'].string}"/>
        <c:if test="${empty title}">
            <c:set var="title" value="${renderContext.mainResource.node.displayableName}"/>
        </c:if>
            ${title}
    </h1>
</c:if>
