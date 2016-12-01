<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html;charset=UTF-8" %>
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
<c:set var="currentLang" value="${renderContext.mainResourceLocale.language}"/>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="${currentLang}">
<head>
    <meta name="robots" content="noindex">
    <meta name="googlebot" content="noindex">
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <meta charset="utf-8">
    <c:set var="pageTitle"
           value="${renderContext.mainResource.node.displayableName}"/>
    <c:if test="${jcr:isNodeType(renderContext.mainResource.node, 'jacademix:alternateTitle')}">
        <c:set var="alternateTitle" value="${renderContext.mainResource.node.properties.alternateTitle.string}"/>
        <c:if test="${not empty alternateTitle}">
            <c:set var="pageTitle"
                   value="${alternateTitle}"/>
        </c:if>
    </c:if>
    <title>${fn:escapeXml(pageTitle)}</title>
    <c:if test="${not empty renderContext.mainResource.node.properties['jcr:description'].string}">
        <c:set var="pageDescription"
               value="${fn:substring(renderContext.mainResource.node.properties['jcr:description'].string,0,160)}"/>
        <meta name="description" content="${fn:escapeXml(pageDescription)}"/>
        <meta property="og:description" content="${fn:escapeXml(pageDescription)}"/>
    </c:if>
    <meta property="og:type" content="article"/>
    <c:choose>
        <c:when test="${currentLang eq 'en'}">
            <meta property="og:locale" content="en_US"/>
        </c:when>
        <c:when test="${currentLang eq 'fr'}">
            <meta property="og:locale" content="fr_FR"/>
        </c:when>
        <c:when test="${currentLang eq 'de'}">
            <meta property="og:locale" content="de_DE"/>
        </c:when>
        <c:otherwise>
            <meta property="og:locale" content="${currentLang}"/>
        </c:otherwise>
    </c:choose>
    <meta property="og:title" content="${fn:escapeXml(pageTitle)}"/>
    <c:choose>
        <c:when test="${pageContext.request.serverPort == 80 || pageContext.request.serverPort == 443}">
            <c:set var="serverUrl" value="${pageContext.request.scheme}://${pageContext.request.serverName}"/>
        </c:when>
        <c:otherwise>
            <c:set var="serverUrl"
                   value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}"/>
        </c:otherwise>
    </c:choose>
    <c:url var="currentPageUrl" value="${renderContext.mainResource.node.url}"/>
    <meta property="og:url" content="${serverUrl}${currentPageUrl}"/>
    <c:set var="imageUrl" value="${url.currentModule}/img/logo.png"/>
    <c:set var="imageWidth" value="250"/>
    <c:set var="imageHeight" value="120"/>
    <meta property="og:image" content="${serverUrl}${imageUrl}"/>
    <meta property="og:image:width" content="${imageWidth}"/>
    <meta property="og:image:height" content="${imageHeight}"/>

    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <link href='//fonts.googleapis.com/css?family=Lato&subset=latin,latin-ext' rel='stylesheet' type='text/css'>
    <template:addResources type="javascript" resources="jquery.min.js"/>
    <template:addResources type="javascript" resources="jquery-ui.min.js"/>
    <template:addResources type="javascript" resources="bootstrap.min.js"/>
    <template:addResources type="javascript" resources="highlight.pack.js"/>
    <template:addResources type="javascript" resources="clipboard.min.js"/>
    <template:addResources type="javascript" resources="academy/academy.js"/>
    <template:addResources type="javascript" resources="academy/libraries/dynamicgrid.js"/>
    <template:addResources type="javascript" resources="academy/libraries/lity.min.js"/>
    <template:addResources type="css" resources="bootstrap.min.css" media="screen,print"/>
    <template:addResources type="css" resources="bootstrapXl.css" media="screen,print"/>
    <template:addResources type="css" resources="foundation.css" media="screen,print"/>
    <template:addResources type="css" resources="clipboard.css" media="screen,print"/>
    <template:addResources type="css" resources="font-awesome.min.css" media="screen,print"/>
    <template:addResources type="css" resources="academy.css" media="screen,print"/>
    <template:addResources type="css" resources="libraries/lity.min.css" media="screen"/>
    <c:if test="${renderContext.editMode}">
        <template:addResources type="css" resources="academy.edit.css"/>
    </c:if>

</head>
<body data-spy="scroll" data-target="#sidebar"><a id="top"></a>
<template:area path="pagecontent"/>
<template:area path="footer"/>
</body>
</html>
