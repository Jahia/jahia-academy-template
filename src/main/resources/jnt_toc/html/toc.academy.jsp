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
<template:addResources type="javascript" resources="jquery.min.js,jquery.toc.js"/>
<c:set var="heading" value="${currentNode.properties.heading.string}"/>
<c:if test="${empty heading}">
    <c:set var="heading" value="h2"/>
</c:if>
<c:set var="depth" value="${currentNode.properties.depth.string}"/>
<c:if test="${empty depth}">
    <c:set var="depth" value="1"/>
</c:if>
<c:choose>
    <c:when test="${heading eq 'h1'}">
        <c:choose>
            <c:when test="${depth eq '1'}"><c:set var="tocHeadings" value="h1"/></c:when>
            <c:when test="${depth eq '2'}"><c:set var="tocHeadings" value="h1,h2"/></c:when>
            <c:when test="${depth eq '3'}"><c:set var="tocHeadings" value="h1,h2,h3"/></c:when>
            <c:when test="${depth eq '4'}"><c:set var="tocHeadings" value="h1,h2,h3,h4"/></c:when>
        </c:choose>
    </c:when>
    <c:when test="${heading eq 'h2'}">
        <c:choose>
            <c:when test="${depth eq '1'}"><c:set var="tocHeadings" value="h2"/></c:when>
            <c:when test="${depth eq '2'}"><c:set var="tocHeadings" value="h2,h3"/></c:when>
            <c:when test="${depth eq '3'}"><c:set var="tocHeadings" value="h2,h3,h4"/></c:when>
            <c:when test="${depth eq '4'}"><c:set var="tocHeadings" value="h2,h3,h4,h5"/></c:when>
        </c:choose>
    </c:when>
    <c:when test="${heading eq 'h3'}">
        <c:choose>
            <c:when test="${depth eq '1'}"><c:set var="tocHeadings" value="h3"/></c:when>
            <c:when test="${depth eq '2'}"><c:set var="tocHeadings" value="h3,h4"/></c:when>
            <c:when test="${depth eq '3'}"><c:set var="tocHeadings" value="h3,h4,h5"/></c:when>
            <c:when test="${depth eq '4'}"><c:set var="tocHeadings" value="h3,h4,h5,h6"/></c:when>
        </c:choose>
    </c:when>
    <c:when test="${heading eq 'h4'}">
        <c:choose>
            <c:when test="${depth eq '1'}"><c:set var="tocHeadings" value="h4"/></c:when>
            <c:when test="${depth eq '2'}"><c:set var="tocHeadings" value="h4,h5"/></c:when>
            <c:when test="${depth eq '3'}"><c:set var="tocHeadings" value="h4,h5,h6"/></c:when>
            <c:when test="${depth eq '4'}"><c:set var="tocHeadings" value="h4,h5,h6,h7"/></c:when>
        </c:choose>
    </c:when>
</c:choose>
<c:set var="bindedComponent" value="${ui:getBindedComponent(currentNode, renderContext, 'j:bindedComponent')}"/>
<c:if test="${bindedComponent != null}">
    <c:if test="${! jcr:isNodeType(bindedComponent, 'jacademix:document')}">
        <%-- If bound component  is not a document search for a document in its children --%>
        <c:set var="documentChildren" value="${jcr:getChildrenOfType(bindedComponent,'jacademix:document')}"/>
        <c:if test="${fn:length(documentChildren)>0}">
            <c:set var="bindedComponent" value="${documentChildren[0]}"/>
        </c:if>
    </c:if>
</c:if>

<c:choose>
    <c:when test="${bindedComponent.path eq renderContext.mainResource.node.path}">
        <c:set var="toc" value="body"/>
    </c:when>
    <c:otherwise>
        <c:set var="toc" value="div#toc_${bindedComponent.identifier}"/>
    </c:otherwise>
</c:choose>
<c:set var="currentPageNode" value="${renderContext.mainResource.node}"/>
<c:set var="isMultiplePageDoc" value="${jcr:isNodeType(currentPageNode, 'jacademix:isMultiplePageDoc') || jcr:isNodeType(currentPageNode, 'jnt:fixApplier')}"/>


<nav class="bs-docs-sidebar hidden-print hidden-sm hidden-xs hidden-print <c:if test="${!renderContext.editMode}">xxx</c:if>" id="sidebar" data-spy="affix" data-offset-top="195">

    <c:set var="parentPage" value="${jcr:getParentOfType(currentPageNode, 'jmix:navMenuItem')}"/>
    <c:set var="sisterPages" value="${jcr:getChildrenOfType(currentPageNode.parent, 'jnt:page,jnt:fixApplier')}"/>
    <c:set var="currentPageIndex" value="0"/>

    <c:choose>
        <c:when test="${isMultiplePageDoc}">
            <c:choose>
                <c:when test="${jcr:isNodeType(currentPageNode, 'jnt:fixApplier')}">
                    <c:url var="parentPageUrl" value="${parentPage.url}" context="/"/>
                    <a href="${parentPageUrl}" data-scrollto="#top"><strong>${parentPage.displayableName}</strong></a>
                </c:when>
                <c:otherwise>
                    <a href="#top" data-scrollto="#top"><strong>${parentPage.displayableName}</strong></a>
                </c:otherwise>
            </c:choose>

            <c:forEach items="${sisterPages}" var="sisterPage" varStatus="status">
                <c:choose>
                    <c:when test="${currentPageNode.path eq sisterPage.path}">
                        <c:choose>
                            <c:when test="${jcr:isNodeType(sisterPage, 'jnt:fixApplier')}">
                                <c:set var="fromVersion" value="${sisterPage.properties.from.string}"/>
                                <c:set var="toVersion" value="${sisterPage.properties.to.string}"/>
                                <c:set var="pageTitle" value="From ${fromVersion} to ${toVersion}"/>
                            </c:when>
                            <c:otherwise>
                                <c:set var="pageTitle" value="${currentPageNode.displayableName}"/>
                            </c:otherwise>
                        </c:choose>

                        <a href="#top" data-scrollto="#top"><strong>${pageTitle}</strong></a>
                        <c:remove var="pageTitle"/>
                        <ul data-toc="${toc}" data-toc-headings="${tocHeadings}" class="nav bs-docs-sidenav sister" ></ul>
                    </c:when>
                    <c:otherwise>
                        <c:url var="pageUrl" value="${sisterPage.url}" context="/"/>
                        <c:choose>
                            <c:when test="${jcr:isNodeType(sisterPage, 'jnt:fixApplier')}">
                                <c:set var="fromVersion" value="${sisterPage.properties.from.string}"/>
                                <c:set var="toVersion" value="${sisterPage.properties.to.string}"/>
                                <c:set var="sisterPageTitle" value="From ${fromVersion} to ${toVersion}"/>
                            </c:when>
                            <c:otherwise>
                                <c:set var="sisterPageTitle" value="${sisterPage.displayableName}"/>
                            </c:otherwise>
                        </c:choose>

                        <a href="${pageUrl}" class="sister light back-to-top notopmargin"> ${sisterPageTitle}</a>
                        <c:remove var="sisterPageTitle"/>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <c:forEach items="${sisterPages}" var="sisterPage" varStatus="status">
                <c:if test="${currentPageNode.path eq sisterPage.path}">
                    <c:set var="currentPageIndex" value="${status.index}"/>
                </c:if>
            </c:forEach>
            <ul data-toc="${toc}" data-toc-headings="${tocHeadings}" class="nav bs-docs-sidenav" ></ul>
        </c:otherwise>
    </c:choose>

    <a href="#top" data-scrollto="#top" class="back-to-top light mt20"> Back to top <i class="fas fa-level-up-alt" aria-hidden="true"></i></a>

    <c:if test="${jcr:isNodeType(currentPageNode,'jacademy:kbEntry' )}">
        <c:set var="parentUrl">javascript:history.back()</c:set>
        <a href="${parentUrl}" class="back-to-top"><i class="fas fa-long-arrow-alt-left fa-fw"></i> <fmt:message
                key="jacademy_kbEntry.back"/></a>
    </c:if>

    <a href="#" id="incfont">A+</a>
    <a href="#" id="decfont">A-</a>
</nav>
