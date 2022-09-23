<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="template" uri="http://www.jahia.org/tags/templateLib" %>
<template:addResources type="css" resources="animate.min.css"/>
<template:addResources type="javascript" resources="jquery.min.js"/>
<template:addResources type="javascript" resources="jquery.appear.min.js" targetTag="${renderContext.editMode?'head':'body'}"/>
<c:set var="animation" value=" ${currentNode.properties['j:animation'].string}"/>
<c:set var="animationDelay" value="${currentNode.properties['j:animationDelay'].double}"/>
<c:set var="animationDuration" value="${currentNode.properties['j:animationDuration'].double}"/>
<c:set var="animationIterationCount" value="${currentNode.properties['j:animationIterationCount'].string}"/>
<c:set var="identifier" value="${currentNode.identifier}"/>
<c:set var="animationDelayUsage" value="${currentNode.properties['j:animationDelayUsage'].string}"/>


<div id="animate-${identifier}" style="${renderContext.editMode?'':'opacity: 0'}">
    ${wrappedContent}
</div>
<c:if test="${! renderContext.editMode}">
<template:addResources type="inline" targetTag="${renderContext.editMode?'head':'body'}">
    <script>
        $(document).ready(function () {
            $('#animate-${identifier}').appear(function () {
                <c:if test="${! empty animationDuration}">
                $("#animate-${identifier}").css("animation-duration", "${animationDuration}s");
                </c:if>
                <c:if test="${! empty animationIterationCount && animationIterationCount ne '1'}">
                $("#animate-${identifier}").css("animation-iteration-count", "${animationIterationCount}");
                </c:if>
                <c:choose>
                    <c:when test="${animationDelayUsage eq 'delayBeforeAnimation'}">
                        $("#animate-${identifier}").css("opacity", "1");
                        <c:if test="${! empty animationDelay && animationDelay > 0}">
                            $("#animate-${identifier}").css("animation-delay", "${animationDelay}s");
                        </c:if>
                        $("#animate-${identifier}").addClass("animated ${animation}");
                    </c:when>
                    <c:otherwise>
                        <c:choose>
                            <c:when test="${! empty animationDelay && animationDelay > 0}">
                                setTimeout(function() {
                                    $("#animate-${identifier}").css("opacity", "1");
                                    $("#animate-${identifier}").addClass("animated ${animation}");
                                }, ${animationDelay*1000});
                            </c:when>
                            <c:otherwise>
                                $("#animate-${identifier}").css("opacity", "1");
                                $("#animate-${identifier}").addClass("animated ${animation}");
                            </c:otherwise>
                        </c:choose>
                    </c:otherwise>
                </c:choose>
            });
        })
    </script>
</template:addResources>
</c:if>
