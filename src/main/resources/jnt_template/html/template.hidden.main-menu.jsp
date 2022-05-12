<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="jcr" uri="http://www.jahia.org/tags/jcr" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="functions" uri="http://www.jahia.org/tags/functions" %>
<%@ taglib prefix="template" uri="http://www.jahia.org/tags/templateLib" %>
<%--@elvariable id="currentNode" type="org.jahia.services.content.JCRNodeWrapper"--%>
<%--@elvariable id="renderContext" type="org.jahia.services.render.RenderContext"--%>
<ul class="jac-topbar nav justify-content-end bg-light align-items-center px-4">
    <li class="nav-item">
        <a class="nav-link" href="https://www.jahia.com">jahia.com</a>
    </li>

    <li class="nav-item">
        <template:include view="hidden.login"/>
    </li>
</ul>
<header class="border-bottom border-gray sticky-top" id="top">
    <div class="bg-white">
        <nav class="navbar navbar-light border-bottom" role="navigation">
            <template:include view="hidden.mainnav"/>
        </nav>
    </div>
</header>
