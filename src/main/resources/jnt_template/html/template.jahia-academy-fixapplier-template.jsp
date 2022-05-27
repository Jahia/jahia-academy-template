<!DOCTYPE html>
<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="template" uri="http://www.jahia.org/tags/templateLib" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="jcr" uri="http://www.jahia.org/tags/jcr" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--@elvariable id="currentNode" type="org.jahia.services.content.JCRNodeWrapper"--%>
<%--@elvariable id="out" type="java.io.PrintWriter"--%>
<%--@elvariable id="script" type="org.jahia.services.render.scripting.Script"--%>
<%--@elvariable id="scriptInfo" type="java.lang.String"--%>
<%--@elvariable id="workspace" type="java.lang.String"--%>
<%--@elvariable id="renderContext" type="org.jahia.services.render.RenderContext"--%>
<%--@elvariable id="currentResource" type="org.jahia.services.render.Resource"--%>
<aside%--@elvariable id="url" type="org.jahia.services.render.URLGenerator" --%>
    <c:set var="language" value="${renderContext.mainResourceLocale.language}" />
    <c:set var="mainResourceNode" value="${renderContext.mainResource.node}" />
    <html lang="${language}">

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <!-- Required meta tags -->
        <meta charset="utf-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />

        <!-- Bootstrap CSS -->
        <template:addResources type="css" resources="bootstrap.min.css" />
        <template:addResources type="css" resources="all.min.css" />
        <template:addResources type="css" resources="multilevel.css" />
        <!-- Google Font -->
        <link href="https://fonts.googleapis.com/css2?family=Lato:wght@300;400;700&display=swap" rel="stylesheet">

        <%--
    TODO: jacademix:alternateTitle
    --%>
        <c:if test="${jcr:isNodeType(mainResourceNode, 'jacademix:alternateTitle')}">
            <c:set var="pageTitle" value="${mainResourceNode.properties.alternateTitle}" />
        </c:if>
        <c:if test="${empty pageTitle}">
            <c:set var="pageTitle" value="${mainResourceNode.displayableName}" />
        </c:if>
        <title>${fn:escapeXml(pageTitle)}</title>
    </head>

    <body class="jac-template-fixapplier d-flex flex-column h-100 " data-bs-spy="scroll" data-bs-target="#toc"
        data-bs-offset="180" tabindex="0">
        <template:include view="hidden.main-menu" />

        <main class="jac-main container-fluid">
            <div class="row">
                <!-- Secondary-navigation -->
                <template:include view="hidden.sidenav" var="sidenav" />
                <c:if test="${! empty sidenav}">
                    <aside class="d-none d-md-block col-3 col-xxl-3 p-0">
                        ${sidenav}
                    </aside>
                </c:if>
                <div class="col-sm-12 col-md-${empty sidenav ? '12' : '9'} col-xxl-${empty sidenav ? '10' : '8'}">
                    <div class="container-lg">
                        <div class="row gx-5">
                            <div class="col-12 col-lg-9 px-5">
                                <!-- Page content -->
                                <article class="jac-content my-5 bg-white" id="article">
                                    <c:if test="${jcr:isNodeType(mainResourceNode, 'jacademix:metadatas')}">
                                        <c:set var="personas" value="${mainResourceNode.properties.personas}" />
                                        <c:if test="${! empty personas}">
                                            <c:forEach items="${personas}" var="persona" varStatus="status">
                                                <c:set var="personaNode" value="${persona.node}" />
                                                <span class="badge bg-success">${personaNode.displayableName}</span>
                                            </c:forEach>
                                        </c:if>
                                    </c:if>
                                    <h1 class="jac-content-title">${pageTitle}</h1>
                                    <c:set var="lastPublishedDate"
                                        value="${mainResourceNode.properties['j:lastPublished'].time}" />
                                    <c:if test="${! empty lastPublishedDate}">
                                        <c:choose>
                                            <c:when test="${language eq 'fr'}">
                                                <fmt:formatDate value="${lastPublishedDate}" pattern="d MMMM yyyy"
                                                    var="formatedReleaseDate" />
                                            </c:when>
                                            <c:otherwise>
                                                <fmt:formatDate value="${lastPublishedDate}" pattern="MMMM d, yyyy"
                                                    var="formatedReleaseDate" />
                                            </c:otherwise>
                                        </c:choose>
                                        <div class="text-secondary small">${formatedReleaseDate} - <span
                                                class="eta"></span>
                                            read</div>
                                    </c:if>

                                    <div class="mt-4">
                                        <template:area path="pagecontent" />
                                    </div>
                                </article>
                            </div>

                            <!-- In this page -->
                            <div class="col-3">
                                <nav class="sticky-top toc d-none d-lg-block py-4" id="toc">
                                    <strong class="text-primary mb-2 d-block">In this page</strong>
                                    <!-- <nav id="toc2" data-toggle="#article" data-scope="h2"></nav> -->
                                    <ul class="toc-list" data-toc-headings="h2, h3" data-toc="#article"></ul>
                                    <hr>
                                    <a href="#" data-scrollto="#top" class="nav-link text-muted">Back to top</a>
                                </nav>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </main>

        <!-- Footer -->
        <footer class="footer py-3 px-5 bg-dark">
            <template:area path="footer" areaAsSubNode="true" moduleType="absoluteArea" level="0" />
        </footer>

        <template:addResources type="javascript" resources="bootstrap.bundle.min.js"
            targetTag="${renderContext.editMode?'head':'body'}" />
        <template:addResources type="javascript" resources="jquery.min.js"
            targetTag="${renderContext.editMode?'head':'body'}" />
        <template:addResources type="javascript" resources="toc.min.js"
            targetTag="${renderContext.editMode?'head':'body'}" />
        <template:addResources type="javascript" resources="index.bundle.min.js"
            targetTag="${renderContext.editMode?'head':'body'}" />


        <c:if test="${renderContext.previewMode}">

            <div style="
    position: fixed;
    top: 90%;
    right: 100px;
    z-index: 10000;
    transform: translate(-50%, -50%);
    background: rgba(247, 201, 241, 0.4);
    padding: .5rem 1rem;
    border-radius: 30px;
">
                <div class="d-block d-sm-none">Extra Small (xs)</div>
                <div class="d-none d-sm-block d-md-none">Small (sm)</div>
                <div class="d-none d-md-block d-lg-none">Medium (md)</div>
                <div class="d-none d-lg-block d-xl-none">Large (lg)</div>
                <div class="d-none d-xl-block d-xxl-none">X-Large (xl)</div>
                <div class="d-none d-xxl-block">XX-Large (xxl)</div>
            </div>
        </c:if>
    </body>

    </html>
