<%@ taglib prefix="jcr" uri="http://www.jahia.org/tags/jcr" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="utility" uri="http://www.jahia.org/tags/utilityLib" %>
<%@ taglib prefix="template" uri="http://www.jahia.org/tags/templateLib" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="functions" uri="http://www.jahia.org/tags/functions" %>
<%@ taglib prefix="user" uri="http://www.jahia.org/tags/user" %>
<%@ taglib prefix="query" uri="http://www.jahia.org/tags/queryLib" %>
<%--@elvariable id="currentNode" type="org.jahia.services.content.JCRNodeWrapper"--%>
<%--@elvariable id="out" type="java.io.PrintWriter"--%>
<%--@elvariable id="script" type="org.jahia.services.render.scripting.Script"--%>
<%--@elvariable id="scriptInfo" type="java.lang.String"--%>
<%--@elvariable id="workspace" type="java.lang.String"--%>
<%--@elvariable id="renderContext" type="org.jahia.services.render.RenderContext"--%>
<%--@elvariable id="currentResource" type="org.jahia.services.render.Resource"--%>
<%--@elvariable id="currentUser" type="org.jahia.services.usermanager.JahiaUser"--%>
<%--@elvariable id="url" type="org.jahia.services.render.URLGenerator"--%>

<c:set var="nbPosts" value="${currentNode.properties.nbPosts.long}"/>
<c:if test="${empty nbPosts}">
    <c:set var="nbPosts" value="5"/>
</c:if>

<c:set var="query" value="select * from [jacademy:boost] as doc where ISDESCENDANTNODE(doc, '${renderContext.site.path}/home/documentation') order by 'j:lastPublished' desc"/>
<jcr:sql var="docs" sql="${query}" limit="${nbPosts}"/>

<c:set var="title" value="${currentNode.properties['jcr:title'].string}"/>
<c:if test="${! empty title}">
    <h3>${title}</h3>
</c:if>

<ul class="lastdoc">
    <c:forEach items="${docs.nodes}" var="doc" varStatus="status">
        <c:set var="pageNodes" value="${jcr:getMeAndParentsOfType(doc,'jmix:navMenuItem')}"/>
        <c:set var="hasFoundDocumentationNode" value="false"/>
        <c:set var="titlePath" value=""/>

        <c:forEach items="${pageNodes}" var="pageNode" varStatus="status">
            <c:if test="${pageNode.name eq 'documentation'}">
                <c:set var="hasFoundDocumentationNode" value="true"/>
            </c:if>
            <c:if test="${! hasFoundDocumentationNode}">
                <c:set var="titlePath" value="${' / '}${pageNode.displayableName}${titlePath}"/>
            </c:if>
        </c:forEach>

        <c:if test="${! jcr:isNodeType(doc, 'jnt:page')}">
            <c:set var="doc" value="${jcr:getParentOfType(doc, 'jnt:page')}"/>
        </c:if>
        <c:url var="docUrl" value="${doc.url}" context="/"/>
        <li><a href="${docUrl}">${titlePath}</a></li>
    </c:forEach>
</ul>
