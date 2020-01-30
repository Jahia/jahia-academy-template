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

<c:set var="title" value="${currentNode.properties['jcr:title'].string}"/>
<c:if test="${! empty title}">
    <h3>${title}</h3>
</c:if>

<c:set var="maxPosts" value="${currentNode.properties['nbPosts'].long}"/>

<c:set var="startNodePath" value="/sites/jahiacom/home"/>

<c:set var="statement"
       value="select * from [jcnt:blogEntry] as blog where ISDESCENDANTNODE(blog, '${startNodePath}') "/>

<c:set var="postTypes" value="${currentNode.properties['postType']}"/>
<c:set var="postsCount" value="${fn:length(postTypes)}"/>
<c:if test="${postsCount > 0}">
    <c:set var="statement" value="${statement} and ("/>

    <c:forEach items="${postTypes}" var="postType" varStatus="status">
        <c:set var="type" value="${fn:replace(postType.string, 'cnt:', '')}"/>

        <c:if test="${! status.first}">
            <c:set var="statement" value="${statement} or"/>
        </c:if>
        <c:set var="statement" value="${statement} blog.['jcr:primaryType'] like '%${type}%'"/>
        <c:if test="${status.last}">
            <c:set var="statement" value="${statement})"/>
        </c:if>
    </c:forEach>
</c:if>
<c:set var="statement" value="${statement} order by blog.['date'] desc"/>

<jcr:sql var="blogs" sql="${statement}" limit="${maxPosts}"/>

<ul>
    <c:forEach items="${blogs.nodes}" var="blog" varStatus="status">
        <c:url var="blogUrl" value="${blog.url}" context="/"/>
        <li><a href="${blogUrl}">${blog.displayableName}</a></li>
    </c:forEach>
</ul>
