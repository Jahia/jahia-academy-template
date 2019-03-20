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
<c:set var="webappURL" value="${currentNode.properties.webappURL.string}"/>
<c:set var="webappMD5" value="${currentNode.properties.webappMD5.string}"/>


<h1>From ${currentNode.properties.from.string} to ${currentNode.properties.to.string}</h1>


<div class="alert alert-version">
    <c:if test="${! empty webappURL}">
        <a href="${webappURL}"><i class="fas fa-download fa-fw"></i> Webapp</a> <span class="smaller">(MD5: ${webappMD5})</span>
    </c:if>
    <c:if test="${jcr:isNodeType(currentNode, 'jnt:fixApplierTomcat')}">
        <c:set var="webappAndTomcatURL" value="${currentNode.properties.webappAndTomcatURL.string}"/>
        <c:set var="webappAndTomcatMD5" value="${currentNode.properties.webappAndTomcatMD5.string}"/>
        <c:if test="${! empty webappAndTomcatURL}">
            <br/><a href="${webappAndTomcatURL}"><i class="fas fa-download fa-fw"></i> Webapp + Tomcat</a> <span class="smaller">(MD5: ${webappAndTomcatMD5})</span>
        </c:if>
    </c:if>
</div>


<c:set var="howToUpgrade" value="${currentNode.properties.howToUpgrade.string}"/>
<c:set var="howToUrl" value="#"/>
<c:choose>
    <c:when test="${empty howToUpgrade}">
        <c:set var="howToNode" value="${currentNode.properties.howTo.node}"/>
        <c:if test="${! empty howToNode}">
            <c:url var="howToUrl" value="${howToNode.url}"/>
            <a href="${howToUrl}">How to upgrade</a>
        </c:if>
    </c:when>
    <c:otherwise>
        ${howToUpgrade}
    </c:otherwise>
</c:choose>
