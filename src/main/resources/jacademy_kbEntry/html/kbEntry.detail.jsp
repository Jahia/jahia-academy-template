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

<template:addResources type="inline">
    <script>
        $(document).ready(function () {
            $('pre code').each(function (i, block) {
                hljs.highlightBlock(block);
                var copybutton = '<div class="bd-clipboard"><span class="btn-clipboard" title="Copy to clipboard">Copy</span></div>';
                $(this).before(copybutton);
            });
            var clipboard = new ClipboardJS('.btn-clipboard', {
                target: function (trigger) {
                    return trigger.parentNode.nextElementSibling;
                }
            })
            $('code.hljs').each(function(i, block) {
                hljs.lineNumbersBlock(block);
            });
        });
    </script>
</template:addResources>

<div id="toc_${currentNode.identifier}" class="document-content">
    <div class="alert alert-info">${currentNode.properties.textContent.string}</div>
<c:if test="${jcr:isNodeType(currentNode, 'jacademix:kbUseCase')}">
    <c:set var="cause" value="${currentNode.properties.cause.string}"/>
    <c:if test="${! empty cause}">
        <h3><fmt:message key="jacademix_kbUseCase.cause"/></h3>
        ${cause}
    </c:if>
</c:if>
<c:choose>
    <c:when test="${jcr:isNodeType(currentNode, 'jacademix:kbUseCase')}">
        <h3><fmt:message key="jacademix_kbUseCase.answer"/></h3>
    </c:when>
    <c:otherwise>
        <h3><fmt:message key="jacademix_kbQa.answer"/></h3>
    </c:otherwise>
</c:choose>
${currentNode.properties.answer.string}
</div>



<jcr:node var="relatedLinksNode" path="${currentNode.path}/relatedlinks"/>

<c:if test="${! empty relatedLinksNode || renderContext.editMode}">
    <div class="alert alert-version">
        <h4>Related links</h4>
        <c:if test="${!renderContext.editMode}"><ul class="fa-ul"></c:if>
        <template:area path="relatedlinks" nodeTypes="jacademy:relatedLink" areaAsSubNode="true"/>
            <c:if test="${!renderContext.editMode}"></ul></c:if>
    </div>
</c:if>

<script>
    $('.document-content').readingTime({
        lang: '${renderContext.mainResourceLocale.language}',
        round: true,
        remoteTarget: '.readingTime'
    });
    $('.readTime').show();
</script>


