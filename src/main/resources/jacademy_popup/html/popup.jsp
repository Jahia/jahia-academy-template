<%--TODO: jacademy:popup --%>



<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="template" uri="http://www.jahia.org/tags/templateLib" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="jcr" uri="http://www.jahia.org/tags/jcr" %>
<%@ taglib prefix="ui" uri="http://www.jahia.org/tags/uiComponentsLib" %>
<%--@elvariable id="currentNode" type="org.jahia.services.content.JCRNodeWrapper"--%>
<%--@elvariable id="out" type="java.io.PrintWriter"--%>
<%--@elvariable id="script" type="org.jahia.services.render.scripting.Script"--%>
<%--@elvariable id="scriptInfo" type="java.lang.String"--%>
<%--@elvariable id="workspace" type="java.lang.String"--%>
<%--@elvariable id="renderContext" type="org.jahia.services.render.RenderContext"--%>
<%--@elvariable id="currentResource" type="org.jahia.services.render.Resource"--%>
<%--@elvariable id="url" type="org.jahia.services.render.URLGenerator"--%>
<template:addResources type="css" resources="bootstrap.min.css"/>
<template:addResources type="javascript" resources="jquery.min.js,jquery.cookie.js"/>

<c:set var="size" value="modal-${currentNode.properties.size.string}"/>
<c:set var="title" value="${currentNode.properties['jcr:title'].string}"/>
<c:set var="close" value="${currentNode.properties.closeText.string}"/>
<c:set var="displayOnlyOnce" value="${currentNode.properties.displayOnlyOnce.boolean}"/>
<c:if test="${empty displayOnlyOnce}">
    <c:set var="displayOnlyOnce" value="true"/>
</c:if>


<div class="<c:if test='${!renderContext.editMode}'>modal fade</c:if>" id="myModal${currentNode.name}" tabindex="-1"
     role="dialog" aria-labelledby="myModalLabel${currentNode.name}"
     aria-hidden="${renderContext.editMode ? 'false' : 'true'}">
    <div class="modal-dialog ${size}"<c:if test='${renderContext.editMode}'> style="margin:5px;"</c:if>>
        <div class="modal-content">
            <c:if test="${empty close}">
                <a href="#" class="pull-right" data-dismiss="modal" type="button" style="position:absolute;top:10px;right:10px;width:20px;height:20px;z-index:1051">x</a>
            </c:if>
            <c:if test="${not empty title}">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><span
                            aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                    <h4 class="modal-title" id="myModalLabel${currentNode.name}">${title}</h4>
                </div>
            </c:if>
            <div class="modal-body">
                <c:forEach items="${jcr:getChildrenOfType(currentNode, 'jmix:droppableContent')}"
                           var="droppableContent">
                    <template:module node="${droppableContent}" editable="true"/>
                </c:forEach>
                <c:if test="${renderContext.editMode}">
                    <template:module path="*" nodeTypes="jmix:droppableContent"/>
                </c:if>
            </div>
            <c:if test="${! empty close}">
                <div class="modal-footer">
                    <button type="button" class="btn btn-primary"
                            data-dismiss="modal">${currentNode.properties.closeText.string}</button>
                </div>
            </c:if>
        </div>
    </div>
</div>
<c:if test="${!renderContext.editMode}">
    <template:addResources type="inline" targetTag="${renderContext.editMode?'head':'body'}">
    <script>
        <c:choose>
        <c:when test="${displayOnlyOnce}">
        <c:set var="expires" value="${currentNode.properties.expires.long}"/>
        <c:if test="${empty expires}">
        <c:set var="expires" value="365"/>
        </c:if>
        if ( $( "#myModal${currentNode.name}" ).length ) {
            if ($.cookie('popup-${currentNode.identifier}') == null) {
                setTimeout(function () {
                    console.log("Open modal #myModal${currentNode.name} and set cookie popup-${currentNode.identifier}");
                    $('#myModal${currentNode.name}').modal('show');
                    <c:url var="u" value="${renderContext.mainResource.node.url}" context="/"/>
                    $.cookie('popup-${currentNode.identifier}', '${fn:replace(u,'/','_')}', { expires: ${expires}, raw: true });
                }, ${currentNode.properties.delay.long});
            }
        }
        </c:when>
        <c:otherwise>
        if ( $( "#myModal${currentNode.name}" ).length ) {
            setTimeout(function () {
                console.log("Open modal #myModal${currentNode.name}");
                $('#myModal${currentNode.name}').modal('show');
            }, ${currentNode.properties.delay.long});
        }
        </c:otherwise>
        </c:choose>
    </script>
    </template:addResources>
</c:if>
