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
<template:addResources type="css" resources="bootstrap.min.css"/>
<template:addResources type="javascript" resources="jquery.min.js,bootstrap.min.js,fontawesome-all.min.js"/>

<template:include view="hidden.header"/>

<table class="table table-striped">
    <tbody>
    <tr>
        <th scope="col">From</th>
        <th scope="col">To</th>
        <th scope="col"><i class="fas fa-download"></i> Download</th>
        <th scope="col">How to upgrade</th>
        <c:if test="${renderContext.editMode}">
            <th>Edit</th>
        </c:if>
    </tr>
    <c:forEach items="${moduleMap.currentList}" var="release" begin="${moduleMap.begin}"
               end="${moduleMap.end}" varStatus="item">
        <tr>
            <template:module node="${release}" editable="false"/>
            <c:if test="${renderContext.editMode}">
                <td><template:module node="${release}" editable="true" view="edit"/></td>
            </c:if>
        </tr>
    </c:forEach>
    </tbody>
</table>
<template:module path="*" nodeTypes="jnt:fixApplier"/>
<template:addResources type="inline">
    <script>
        $(document).ready(function () {

            $('[data-toggle="popover"]').popover({
                    'placement': 'top',
                    'trigger': 'hover',
                    'html': true
                }
            );
        });
    </script>
</template:addResources>
