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
<%--@elvariable id="url" type="org.jahia.services.render.URLGenerator" --%>
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
    <c:set var="displayInThisPage" value="${! jcr:isNodeType(mainResourceNode, 'jacademix:hideInThisPageMenu')}" />
    <%--
TODO: jacademix:alternateTitle
--%>
    <c:if test="${jcr:isNodeType(mainResourceNode, 'jacademix:alternateTitle')}">
        <c:set var="pageTitle" value="${mainResourceNode.properties.alternateTitle}" />
    </c:if>
    <c:if test="${empty pageTitle}">
        <c:set var="pageTitle" value="${mainResourceNode.displayableName}" />
    </c:if>
    <c:if test="${jcr:isNodeType(mainResourceNode, 'jnt:fixApplier')}">
        <c:set var="pageTitle" value="From ${mainResourceNode.properties.from.string} to ${mainResourceNode.properties.to.string}"/>
    </c:if>
    <title>${fn:escapeXml(pageTitle)}</title>
</head>

<body class="jac-template-fixapplier d-flex flex-column h-100 " data-bs-spy="scroll" data-bs-target="#toc"
    data-bs-offset="180" tabindex="0">
    <template:include view="hidden.main-menu" />
    <template:include view="hidden.sidenav" var="sidenav" />
    <script>
        console.log("sidenav is [${sidenav}]");
    </script>
    <main class="jac-main ${empty sidenav?'container':'container-fluid'}">
        <div class="row">
            <!-- Secondary-navigation -->
            <c:if test="${! empty sidenav}">
                <aside class="bg-light d-none d-md-block col-md-3 p-0">
                    ${sidenav}
                </aside>
            </c:if>

            <div class="col-sm-12 col-md-${empty sidenav ? '12' : '9'} mb-4">
                <div class="row gx-5 mx-5">
                    <div class="col-12 ${displayInThisPage? 'col-lg-9':' '} px-5">
                        <!-- Page content -->
                        <article class="jac-content bg-white" id="article">
                            <h1 class="jac-content-title">${pageTitle}</h1>
                            <c:set var="lastPublishedDate" value="${mainResourceNode.properties['j:lastPublished'].time}" />
                            <c:if test="${! empty lastPublishedDate}">
                                <c:choose>
                                    <c:when test="${language eq 'fr'}">
                                        <fmt:formatDate value="${lastPublishedDate}" pattern="d MMMM yyyy" var="formatedReleaseDate" />
                                    </c:when>
                                    <c:otherwise>
                                        <fmt:formatDate value="${lastPublishedDate}" pattern="MMMM d, yyyy" var="formatedReleaseDate" />
                                    </c:otherwise>
                                </c:choose>
                                <div class="text-secondary small">Published ${formatedReleaseDate}</div>
                            </c:if>
                            <div class="my-5">
                                <template:area path="pagecontent" />
                            </div>
                        </article>
                    </div>

                    <c:if test="${displayInThisPage}">
                        <div class="col-3">
                            <nav class="sticky-top toc d-none d-lg-block py-4" id="toc">
                                <strong class="text-primary mb-2 d-block">In this page</strong>
                                <ul class="toc-list" data-toc-headings="h2, h3" data-toc="#article"></ul>
                                <hr>
                                <a href="#" data-scrollto="#top" class="nav-link text-muted">Back to top</a>
                            </nav>
                        </div>
                    </c:if>
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

    <template:addResources type="inline" targetTag="${renderContext.editMode?'head':'body'}">
        <script>
            var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'))
            var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
                return new bootstrap.Tooltip(tooltipTriggerEl)
            })

            var popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'))
            var popoverList = popoverTriggerList.map(function (popoverTriggerEl) {
                return new bootstrap.Popover(popoverTriggerEl)
            })
        </script>
    </template:addResources>
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
