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
<c:url var="detailView" value="${currentNode.url}" context="/"/>
<c:set var="tagList" value="${currentNode.properties['j:tagList']}"/>
<jcr:node var="searchPage" path="/sites/academy/home/search"/>
<c:set var="title" value="${currentNode.displayableName}"/>
<c:set var="summary" value="${currentNode.properties.textContent.string}"/>

<c:set var="catNode" value="${currentNode.properties.cat.node}"/>
<c:set var="icon" value="far fa-lightbulb "/>
<c:if test="${! empty catNode}">
    <c:set var="catName" value="${catNode.name}"/>
    <c:choose>
        <c:when test="${catName eq 'install'}">
            <c:set var="icon" value="fas fa-download "/>
        </c:when>
        <c:when test="${catName eq 'dev'}">
            <c:set var="icon" value="fas fa-code "/>
        </c:when>
        <c:when test="${catName eq 'perf'}">
            <c:set var="icon" value="fas fa-rocket "/>
        </c:when>
        <c:when test="${catName eq 'exploit'}">
            <c:set var="icon" value="fas fa-industry"/>
        </c:when>
    </c:choose>
</c:if>


<div class="media kb" data-href="${detailView}">
    <div class="media-left">
        <a href="${detailView}" class="text-muted">
            <i class="${icon} fa-fw fa-2x"></i>
        </a>
    </div>
    <div class="media-body">
        <h3 class="media-heading"><a href="${detailView}">${title}</a></h3>
        <c:set var="textSummry" value="${functions:removeHtmlTags(summary)}"/>
        ${functions:abbreviate(textSummry, 150, 250, '...')}
        <a href="${detailView}" class="text-muted"><i class="fas fa-arrow-right"></i></a>
        <div class="tags">
            <c:if test="${! empty tagList}">
                <c:forEach items="${tagList}" var="tag" varStatus="status">
                    <c:if test="${! empty searchPage}">
                        <c:url var="searchTagUrl" value="${searchPage.url}" context="/">
                            <c:param name="src_terms[0].term" value="${tag.string}"/>
                            <c:param name="src_terms[0].fields.tags" value="true"/>
                            <c:param name="src_sites.values" value="${renderContext.site.siteKey}"/>
                            <c:param name="autoSuggest" value="false"/>
                        </c:url>
                        <c:if test="${status.first}">
                            <i class="fas fa-tags fa-fw text-muted" aria-hidden="true"></i>
                        </c:if>
                        <a class="label label-info" href="${searchTagUrl}">${tag.string}</a>
                    </c:if>
                </c:forEach>
            </c:if>
        </div>
    </div>
</div>
