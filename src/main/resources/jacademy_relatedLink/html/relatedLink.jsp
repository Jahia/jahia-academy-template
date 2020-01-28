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

<c:set var="linkType" value="${currentNode.properties.linkType.string}"/>
<c:set var="linkTitle" value="${currentNode.properties.linkTitle.string}"/>
<c:if test="${empty linkTitle}">
    <c:set var="linkTitle" value="${currentNode.properties['jcr:title'].string}"/>
</c:if>
<c:url var="linkUrl" value="${currentNode.url}" context="/"/>
<c:choose>
    <c:when test="${linkType == 'internal'}">
        <c:set var="linkNode" value="${currentNode.properties.internalLink.node}"/>
        <c:if test="${! empty linkNode}">
            <c:if test="${empty linkTitle}">
                <c:set var="linkTitle" value="${linkNode.displayableName}"/>
            </c:if>
            <c:url var="linkUrl" value="${linkNode.url}" context="/"/>
        </c:if>
    </c:when>
    <c:when test="${linkType == 'file'}">
        <c:set var="linkNode" value="${currentNode.properties.fileLink.node}"/>
        <c:if test="${! empty linkNode}">
            <c:if test="${empty linkTitle}">
                <c:set var="linkTitle" value="${linkNode.displayableName}"/>
            </c:if>
            <c:url var="linkUrl" value="${linkNode.url}" context="/"/>
        </c:if>
    </c:when>
    <c:when test="${linkType == 'external'}">
        <c:set var="linkUrl" value="${currentNode.properties.externalLink.string}"/>
    </c:when>
</c:choose>
<c:choose>
    <c:when test="${renderContext.editMode}">
        <a href="${linkUrl}">${linkTitle}</a>
    </c:when>
    <c:otherwise>
        <c:if test="${! empty linkUrl}">
            <li><span class="fa-li"><i class="fas fa-long-arrow-alt-right"></i></span><a href="${linkUrl}">${linkTitle}</a></li>
        </c:if>
    </c:otherwise>
</c:choose>
