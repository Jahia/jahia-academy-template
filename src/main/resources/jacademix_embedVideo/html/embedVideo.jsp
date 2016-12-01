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
<c:set var="provider" value="${currentNode.properties.provider.string}"/>
<c:set var="id" value="${currentNode.properties.id.string}"/>
<c:choose>
    <c:when test="${provider eq 'youtube'}">
        <c:set var="videoUrl" value="https://www.youtube.com/embed/${id}"/>
    </c:when>
    <c:when test="${provider eq 'vimeo'}">
        <c:set var="videoUrl" value="https://player.vimeo.com/video/${id}"/>
    </c:when>
</c:choose>

<c:if test="${renderContext.editMode}">
    <div class="alert alert-info" role="alert">
        Provider: ${provider}<br/>
        ID: ${id}
    </div>
</c:if>
<div class="embed-responsive embed-responsive-16by9">
    <iframe allowfullscreen="" class="embed-responsive-item" frameborder="0" src="${videoUrl}"
            webkitallowfullscreen mozallowfullscreen allowfullscreen></iframe>
</div>
