<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="jcr" uri="http://www.jahia.org/tags/jcr" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="template" uri="http://www.jahia.org/tags/templateLib" %>

<%--@elvariable id="currentNode" type="org.jahia.services.content.JCRNodeWrapper"--%>
<%--@elvariable id="renderContext" type="org.jahia.services.render.RenderContext"--%>

${currentNode.properties.textContent.string}


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
