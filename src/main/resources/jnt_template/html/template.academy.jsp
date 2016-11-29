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
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
    <meta name="robots" content="noindex">
    <meta name="googlebot" content="noindex">
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <meta charset="utf-8">
    <title><fmt:message key="academy.welcome"/></title>
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
    <template:addResources type="css" resources="foundation.css" media="screen,print"/>
    <template:addResources type="css" resources="clipboard.css" media="screen,print"/>
    <template:addResources type="css" resources="font-awesome.min.css" media="screen,print"/>
    <template:addResources type="css" resources="academy.css" media="screen,print"/>
    <template:addResources type="css" resources="libraries/lity.min.css" media="screen"/>
    <c:if test="${renderContext.editMode}">
        <template:addResources type="css" resources="academy.edit.css"/>
    </c:if>

</head>
<body data-spy="scroll" data-target="#sidebar">
<template:area path="pagecontent"/>
<template:area path="footer"/>
</body>
</html>
