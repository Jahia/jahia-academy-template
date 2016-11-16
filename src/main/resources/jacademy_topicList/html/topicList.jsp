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
<section class="documentation-list-wrapper" id="pinBoot" style="height: 524px;">
<c:forEach items="${renderContext.mainResource.node.nodes}" var="topicPage">
        <c:if test="${jcr:isNodeType(topicPage, 'jmix:sitemap')}">
            <article class="doc-list r1 c0" style="width: 380px; left: 0px; top: 0px;">
                <h3>${topicPage.displayableName}</h3>
                <template:module node="${topicPage}" view="academytopic"/>
            </article>
        </c:if>
</c:forEach>
</section>

<%--
<section class="documentation-list-wrapper" id="pinBoot" style="height: 524px;">
    <article class="doc-list r1 c0" style="width: 380px; left: 0px; top: 0px;">
        <h3>Deployment and Maintenance</h3>
        <ul class="documentation-list">
            <li><a href="documentation_child.html">Architecture</a></li>
            <li><a href="documentation_child.html">Configuration</a></li>
            <li><a href="documentation_child.html">Download DX</a></li>
            <li><a href="documentation_child.html">Caching</a></li>
            <li><a href="documentation_child.html">Security</a></li>
            <li><a href="documentation_child.html">Tools</a></li>
        </ul>
    </article>
    <article class="doc-list r1 c1" style="width: 380px; left: 390px; top: 0px;">
        <h3>Development</h3>
        <ul class="documentation-list">
            <li><a href="documentation_child.html">Modules</a></li>
            <li><a href="documentation_child.html">Components</a></li>
            <li><a href="documentation_child.html">Jahia APIs</a></li>
            <li><a href="documentation_child.html">OSGi Bundles &amp; Services</a></li>
            <li><a href="documentation_child.html">Mobile APP Development</a></li>
            <li><a href="documentation_child.html">Content Modeling</a></li>
            <li><a href="documentation_child.html">Templates</a></li>
            <li><a href="documentation_child.html">Content Rendering</a></li>
            <li><a href="documentation_child.html">Layout</a></li>
            <li><a href="documentation_child.html">Search</a></li>
            <li><a href="documentation_child.html">Services</a></li>
        </ul>
    </article>
    <article class="doc-list r1 c2" style="width: 380px; left: 780px; top: 0px;">
        <h3>Deployment and Maintenance</h3>
        <ul class="documentation-list">
            <li><a href="documentation_child.html">Architecture</a></li>
            <li><a href="documentation_child.html">Configuration</a></li>
            <li><a href="documentation_child.html">Download DX</a></li>
            <li><a href="documentation_child.html">Caching</a></li>
            <li><a href="documentation_child.html">Security</a></li>
            <li><a href="documentation_child.html">Tools</a></li>
        </ul>
    </article>
    <article class="doc-list r2 c0" style="width: 380px; left: 0px; top: 287px;">
        <h3>Authoring &amp; Content Creation</h3>
        <ul class="documentation-list">
            <li><a href="documentation_child.html">Content</a></li>
            <li><a href="documentation_child.html">Edit Mode</a></li>
            <li><a href="documentation_child.html">Administration</a></li>
        </ul>
    </article>
</section>
--%>