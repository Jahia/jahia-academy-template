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

<div class="container-lg ">
    <div class="row">
        <div class="col-12">
            <article class="pb-5 pt-3 bg-white" id="article">
                <div class="mb-4">
                    <c:set var="tagList" value="${currentNode.properties['j:tagList']}"/>
                    <c:if test="${! empty tagList}">
                        <c:forEach items="${tagList}" var="tag" varStatus="status">
                            <span class="badge bg-success">${tag.string}</span>
                        </c:forEach>
                    </c:if>

                    <c:if test="${jcr:isNodeType(currentNode, 'jacademix:metadatas')}">
                        <c:set var="personas" value="${currentNode.properties.personas}"/>
                        <c:if test="${! empty personas}">
                            <c:forEach items="${personas}" var="persona" varStatus="status">
                                <c:set var="personaNode" value="${persona.node}"/>
                                <span class="badge bg-success">${personaNode.displayableName}</span>
                            </c:forEach>
                        </c:if>
                    </c:if>
                    <h1 class="pt-1">${currentNode.displayableName}</h1>
                    <c:set var="lastPublishedDate" value="${currentNode.properties['j:lastPublished'].time}"/>
                    <c:if test="${! empty lastPublishedDate}">
                        <c:choose>
                            <c:when test="${language eq 'fr'}">
                                <fmt:formatDate value="${lastPublishedDate}" pattern="d MMMM yyyy" var="formatedReleaseDate"/>
                            </c:when>
                            <c:otherwise>
                                <fmt:formatDate value="${lastPublishedDate}" pattern="MMMM d, yyyy" var="formatedReleaseDate"/>
                            </c:otherwise>
                        </c:choose>
                        <div class="text-secondary small">${formatedReleaseDate} - <span class="eta"></span> read</div>
                    </c:if>



                </div>
                <h2 class="text-primary"><fmt:message key="jacademix_kbQa.jcr_title"/></h2>
                ${currentNode.properties.textContent.string}
                <c:if test="${jcr:isNodeType(currentNode, 'jacademix:kbUseCase')}">
                    <c:set var="cause" value="${currentNode.properties.cause.string}"/>
                    <c:if test="${! empty cause}">
                        <h2 class="text-primary"><fmt:message key="jacademix_kbUseCase.cause"/></h2>
                        ${cause}
                    </c:if>
                </c:if>
                <c:choose>
                    <c:when test="${jcr:isNodeType(currentNode, 'jacademix:kbUseCase')}">
                        <h2 class="text-primary"><fmt:message key="jacademix_kbUseCase.answer"/></h2>
                    </c:when>
                    <c:otherwise>
                        <h2 class="text-primary"><fmt:message key="jacademix_kbQa.answer"/></h2>
                    </c:otherwise>
                </c:choose>
                ${currentNode.properties.answer.string}

                <jcr:node var="relatedLinksNode" path="${currentNode.path}/relatedlinks"/>

                <c:if test="${! empty relatedLinksNode || renderContext.editMode}">
                    <template:area path="relatedlinks" nodeTypes="jacademy:relatedLink" areaAsSubNode="true" var="relatedlinks"/>
                    <c:if test="${! empty relatedlinks}">
                        <div class="alert alert-version">
                            <h4>Related links</h4>
                            <c:if test="${!renderContext.editMode}">
                            <ul class="fa-ul list-unstyled"></c:if>
                                ${relatedlinks}
                                <c:if test="${!renderContext.editMode}"></ul>
                            </c:if>
                        </div>
                    </c:if>
                </c:if>


            </article>
        </div>
    </div>
</div>



<template:addResources type="css" resources="github.min.css"/>

<template:addResources type="javascript" resources="jquery.min.js" targetTag="${renderContext.editMode?'head':'body'}"/>
<template:addResources type="javascript" resources="clipboard.min.js" targetTag="${renderContext.editMode?'head':'body'}"/>
<template:addResources type="javascript" resources="highlight.min.js" targetTag="${renderContext.editMode?'head':'body'}"/>
<template:addResources type="javascript" resources="highlightjs-line-numbers.min.js" targetTag="${renderContext.editMode?'head':'body'}"/>
<template:addResources type="inline" targetTag="${renderContext.editMode?'head':'body'}">
    <script>
        $(document).ready(function () {
            $('pre code').each(function (i, block) {
                hljs.highlightElement(block);
            });
            $('code.hljs').each(function (i, block) {
                hljs.lineNumbersBlock(block);
            });
        });

        var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'))
        var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
            return new bootstrap.Tooltip(tooltipTriggerEl)
        })

        // Insert copy to clipboard button before .highlight
        var btnHtml = '<div class="bd-clipboard"><button type="button" class="btn-clipboard" title="Copy to clipboard">Copy</button></div>'
        document.querySelectorAll('pre')
            .forEach(function (element) {
                element.insertAdjacentHTML('beforebegin', btnHtml)
            })

        document.querySelectorAll('.btn-clipboard')
            .forEach(function (btn) {
                var tooltipBtn = new bootstrap.Tooltip(btn)

                btn.addEventListener('mouseleave', function () {
                    // Explicitly hide tooltip, since after clicking it remains
                    // focused (as it's a button), so tooltip would otherwise
                    // remain visible until focus is moved away
                    tooltipBtn.hide()
                })
            })

        var clipboard = new ClipboardJS('.btn-clipboard', {
            target: function (trigger) {
                return trigger.parentNode.nextElementSibling
            }
        })

        clipboard.on('success', function (event) {
            var tooltipBtn = bootstrap.Tooltip.getInstance(event.trigger)

            event.trigger.setAttribute('data-bs-original-title', 'Copied!')
            tooltipBtn.show()

            event.trigger.setAttribute('data-bs-original-title', 'Copy to clipboard')
            event.clearSelection()
        })

        clipboard.on('error', function (event) {
            var modifierKey = /mac/i.test(navigator.userAgent) ? '\u2318' : 'Ctrl-'
            var fallbackMsg = 'Press ' + modifierKey + 'C to copy'
            var tooltipBtn = bootstrap.Tooltip.getInstance(event.trigger)

            event.trigger.setAttribute('data-bs-original-title', fallbackMsg)
            tooltipBtn.show()

            event.trigger.setAttribute('data-bs-original-title', 'Copy to clipboard')
        })
    </script>
</template:addResources>
