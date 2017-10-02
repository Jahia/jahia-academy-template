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

<c:if test="${jcr:isNodeType(currentNode, 'jacademix:advancedCard')}">
    <c:set var="backgroundImageNode" value="${currentNode.properties.backgroundImage.node}"/>
    <c:set var="backgroundColor" value="background-color:${currentNode.properties.backgroundColor.string};"/>
    <c:set var="textColor" value="color:${currentNode.properties.textColor.string}"/>
    <c:if test="${textColor != 'color:'}">
        <script>$("<style>").prop("type", "text/css").html("#${currentNode.identifier} .content, #${currentNode.identifier} .content a {${textColor};}").appendTo("head");</script>
    </c:if>
</c:if>



<div class="product" style="${backgroundColor}${textColor}" id="${currentNode.identifier}">
    <c:if test="${! empty backgroundImageNode}">
        <div class="bg"
             style="background: url('${backgroundImageNode.url}')  no-repeat center center;-webkit-background-size: cover;-moz-background-size: cover;-o-background-size: cover;background-size: cover;height:250px;"></div>
    </c:if>
    <div class="content${! empty backgroundColor? ' text-white' : ''}">
        ${currentNode.properties.textContent.string}
    </div>
</div>