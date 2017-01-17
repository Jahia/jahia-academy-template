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
<%--
<template:addResources type="css" resources="ck/googlecode.css"/>
<template:addResources type="javascript" resources="academy/ck/lib/highlight.pack.js"/>

--%>
<%--<template:addResources type="css" resources="highlightjs-line-numbers.css"/>--%>
<template:addResources type="inline">
    <script>
        $(document).ready(function () {
            $('pre code').each(function (i, block) {
                hljs.highlightBlock(block);
                var copybutton = '<div class="bd-clipboard"><span class="btn-clipboard" title="Copy to clipboard">Copy</span></div>';
                $(this).before(copybutton);
            });
            var clipboard = new Clipboard('.btn-clipboard', {
                target: function (trigger) {
                    return trigger.parentNode.nextElementSibling;
                }
            })            //$('code.hljs').each(function(i, block) {
            //    hljs.lineNumbersBlock(block);
            //});
        });
    </script>
</template:addResources>
<c:set var="textContent" value="${currentNode.properties.textContent.string}"/>
<div id="toc_${currentNode.identifier}" class="document-content">
    ${textContent}
    <c:if test="${empty textContent}">
        <c:set var="pdfNode" value="${currentNode.properties.pdf.node}"/>
        <c:if test="${! empty pdfNode}">
            <c:url var="pdfUrl" value="${pdfNode.url}" context="/"/>
            <div class="alert alert-warning">Sorry, there is no HTML version for this document. Get the latest PDF version here <a
                    class="btn btn-danger" href="${pdfUrl}" type="button"><i aria-hidden="true"
                                                                             class="fa fa-download fa-fw"></i> ${currentNode.displayableName}
            </a></div>
        </c:if>

    </c:if>
</div>
<c:if test="${! empty textContent}">
    <script>
    $('.document-content').readingTime({
        lang: '${renderContext.mainResourceLocale.language}',
        round: true,
        remoteTarget: '.readingTime'
    });
    $('.readTime').show();
    </script>
</c:if>
