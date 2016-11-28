<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="template" uri="http://www.jahia.org/tags/templateLib" %>
<%@ taglib prefix="functions" uri="http://www.jahia.org/tags/functions" %>
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
<template:addResources type="css" resources="bootstrap.min.css"/>

<c:set var="pageNodes" value="${jcr:getParentsOfType(currentNode, 'jnt:page')}"/>
<c:if test="${empty pageNodes}">
    <c:choose>
        <c:when test="${jcr:isNodeType(renderContext.mainResource.node, 'jnt:page')}">
            <c:set var="pageNodes"
                   value="${jcr:getMeAndParentsOfType(renderContext.mainResource.node,'jmix:navMenuItem')}"/>
        </c:when>
        <c:otherwise>
            <c:set var="pageNodes"
                   value="${jcr:getParentsOfType(renderContext.mainResource.node, 'jmix:navMenuItem')}"/>
        </c:otherwise>
    </c:choose>
</c:if>
<c:if test="${fn:length(pageNodes) > 1}">
    <c:set var="cssClass"/>
    <c:if test="${jcr:isNodeType(currentNode,'bootstrap3mix:advancedBreadcrumb' )}">
        <c:set var="cssClass" value="${currentNode.properties.cssClass.string}"/>
    </c:if>
    <!-- academy view -->
    <ol class='breadcrumb<c:if test="${not empty cssClass}"><c:out value=" "/>${cssClass}</c:if>'>
        <c:forEach items="${functions:reverse(pageNodes)}" var="pageNode" varStatus="status">

            <%-- now we check if there is sister pages --%>
            <c:set var="parentPage" value="${jcr:getParentOfType(pageNode, 'jmix:navMenuItem')}"/>
            <c:set var="sisterPages" value="${jcr:getChildrenOfType(parentPage, 'jnt:page')}"/>
            <c:set var="hasSisterPages" value="${fn:length(sisterPages) > 1}"/>
            <c:choose>
                <c:when test="${hasSisterPages}">
                    <li class="dropdown">
                        <c:choose>
                            <c:when test="${jcr:findDisplayableNode(pageNode, renderContext) ne pageNode}">
                                <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
                                   aria-expanded="false"><c:out value="${pageNode.displayableName}"/></a>
                            </c:when>
                            <c:otherwise>
                                <a href="<c:url value='${pageNode.url}'/>" class="dropdown-toggle" data-toggle="dropdown"
                                   role="button" aria-haspopup="true" aria-expanded="false">
                                    <c:out value="${pageNode.displayableName}"/>
                                </a>
                            </c:otherwise>
                        </c:choose>
                        <ul class="dropdown-menu">
                            <c:forEach items="${sisterPages}" var="sisterPage">
                                <c:url var="sisterPageUrl" value="${sisterPage.url}"/>
                                <c:set var="active"><c:if test="${fn:contains(renderContext.mainResource.path,sisterPage.path)}"> class="active"</c:if></c:set>
                                <li ${active}><a href="${sisterPageUrl}">${sisterPage.displayableName}</a></li>
                            </c:forEach>
                        </ul>
                    </li>
                </c:when>
                <c:otherwise>
                    <li>
                        <c:choose>
                            <c:when test="${jcr:findDisplayableNode(pageNode, renderContext) ne pageNode}">
                                <a href="#"><c:out value="${pageNode.displayableName}"/></a>
                            </c:when>
                            <c:otherwise>
                                <a href="<c:url value='${pageNode.url}'/>"><c:out value="${pageNode.displayableName}"/></a>
                            </c:otherwise>
                        </c:choose>
                    </li>
                </c:otherwise>

            </c:choose>

        </c:forEach>
    </ol>
</c:if>