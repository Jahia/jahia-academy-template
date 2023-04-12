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
<template:addResources type="javascript" resources="jquery.min.js"/>
<template:addResources type="javascript" resources="version_compare.js" targetTag="${renderContext.editMode?'head':'body'}"/>
<c:set var="currentVersion" value="${param.dxversion}"/>
<c:set var="expanded" value="${currentResource.moduleParams.expanded}"/>

<c:set var="language" value="${currentResource.locale.language}"/>
<c:set var="releaseDate" value="${currentNode.properties.releaseDate.time}"/>
<c:choose>
    <c:when test="${language eq 'fr'}">
        <fmt:formatDate value="${releaseDate}" pattern="d MMMM yyyy" var="formatedReleaseDate"/>
    </c:when>
    <c:otherwise>
        <fmt:formatDate value="${releaseDate}" pattern="MMMM d, yyyy" var="formatedReleaseDate"/>
    </c:otherwise>
</c:choose>
<c:set var="releaseNotesNode" value="${currentNode.properties.releaseNotes.node}"/>
<style>
    .whatsnew.new h4 {
        color:pink;
    }
</style>

<template:include view="hidden.header"/>

<div class="simplebox">
    <a class="accordion accordion-button${expanded?' ':' collapsed'}" data-bs-toggle="collapse" href="#collapse${currentNode.identifier}" role="button" aria-expanded="true" aria-controls="collapse${currentNode.identifier}"><h4>${currentNode.properties['jcr:title'].string}</h4></a>
<div class="collapse${expanded ? ' show':''}" id="collapse${currentNode.identifier}">

    <div class="description">${currentNode.properties.textContent.string}</div>
    <div class="releaseDate"><small>${formatedReleaseDate}</small></div>
    <c:if test="${! empty releaseNotesNode}">
        <c:url var="releaseNotesNodeUrl" value="${releaseNotesNode.url}" context="/"/>
        <div class="releaseNotes">
            <a href="${releaseNotesNodeUrl}" title="${fn:escapeXml(releaseNotesNode.displayableName)}"><small><fmt:message key="jacademy_whatsNewDX.releaseNotes"/></small></a>
        </div>
    </c:if>
    <%--<c:forEach items="${jcr:getChildrenOfType(currentNode, 'jmix:droppableContent')}" var="droppableContent">--%>
    <c:forEach items="${moduleMap.currentList}" var="droppableContent" begin="${moduleMap.begin}" end="${moduleMap.end}" varStatus="item">
        <template:module node="${droppableContent}" editable="true"/>
    </c:forEach>
    <c:if test="${renderContext.editMode}">
        <template:module path="*" nodeTypes="jmix:droppableContent"/>
    </c:if>

</div>
</div>

<c:if test="${! empty currentVersion}">
    <template:addResources type="inline" targetTag="${renderContext.editMode?'head':'body'}">
        <script>
            $(document).ready(function () {
                $( ".whatsnew.dx" ).each(function() {
                    var version = $(this).data('version');
                    if (versionCompare(version,'${currentVersion}')>0) {
                        $(this).addClass('new');
                    }
                    if (versionCompare(version,'${currentVersion}')==0) {
                        $(this).addClass('current');
                    }
                });
            });
        </script>
    </template:addResources>
</c:if>
