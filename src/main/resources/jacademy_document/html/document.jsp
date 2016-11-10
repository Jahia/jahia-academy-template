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
<template:addResources type="css" resources="doc.css"/>
<template:addResources type="css" resources="github-gist.css"/>
<%--<template:addResources type="css" resources="highlightjs-line-numbers.css"/>--%>
<template:addResources type="javascript" resources="highlight.pack.js"/>
<%--<template:addResources type="javascript" resources="highlightjs-line-numbers.min.js"/>--%>
<template:addResources type="javascript" resources="clipboard.min.js"/>
<template:addResources type="css" resources="clipboard.css"/>
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

<div id="toc_${currentNode.identifier}" class="document-content">${currentNode.properties['textContent'].string}</div>