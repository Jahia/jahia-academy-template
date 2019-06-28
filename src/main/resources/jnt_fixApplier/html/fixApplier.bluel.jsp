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
<c:set var="releaseNotesNode" value="${currentNode.properties.releaseNotes.node}"/>
<c:set var="toVerstion" value="${currentNode.properties.to.string}"/>


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
<c:if test="${! empty releaseNotesNode}">
    <c:url var="releaseNotesUrl" value="${releaseNotesNode.url}"/>
    <br/><a href="${releaseNotesUrl}"><i class="far fa-sticky-note fa-fw"></i> What's new on ${toVerstion}?</a>
</c:if>
