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
<c:set var="boundComponent" value="${ui:getBindedComponent(currentNode, renderContext, 'j:bindedComponent')}"/>
<c:set var="documentNode" value="${null}"/>
<%-- Check for a document Node to bind --%>
<c:if test="${boundComponent != null}">
    <c:choose>
        <c:when test="${boundComponent.primaryNodeType eq 'jacademix:document'}">
            <%-- If bound component is not a document search for a document in its children --%>
            <c:set var="documentNode" value="${boundComponent}"/>
        </c:when>
        <c:otherwise>
            <%-- If bound component  is not a document search for a document in its children --%>
            <c:set var="documentChildren" value="${jcr:getChildrenOfType(boundComponent,'jacademix:document')}"/>
            <c:if test="${fn:length(documentChildren)>0}">
                <c:set var="documentNode" value="${documentChildren[0]}"/>
            </c:if>
        </c:otherwise>
    </c:choose>
</c:if>
<%-- Then display the document Header --%>
<c:choose>
    <c:when test="${documentNode != null}">
        <div class="row documentation-header">
            <div class="col-md-7 col-sm-12">



                <h1 class="doc-child main-title">
                    <c:set var="currentPageNode" value="${renderContext.mainResource.node}"/>
                    <c:if test="${jcr:isNodeType(currentPageNode, 'jacademix:isMultiplePageDoc')}">
                        <c:set var="parentPage" value="${jcr:getParentOfType(currentPageNode, 'jmix:navMenuItem')}"/>
                        <span class="hidden-xs">${parentPage.displayableName}</span>
                    </c:if>
                    <c:set var="title" value="${documentNode.properties['jcr:title'].string}"/>
                    <c:if test="${empty title}">
                        <c:set var="title" value="${renderContext.mainResource.node.displayableName}"/>
                    </c:if>
                    ${title}
                </h1>
                <c:set var="author" value="${documentNode.properties.author.string}"/>
                <h2 class="doc-child author">
                    <fmt:message key="academy.document.writtenBy">
                        <fmt:param value="${empty author ? 'The Jahia Team' : author}"/>
                    </fmt:message>
                </h2>
                <%--
                <h2 class="doc-child author">
                    <c:set var="creator" value="${documentNode.properties['jcr:createdBy'].string}"/>
                    <c:if test="${creator eq 'root'}">
                        <c:set var="creator" value="The Jahia Team"/>
                    </c:if>
                    <fmt:message key="academy.document.writtenBy">
                        <fmt:param value="${creator}"/>
                    </fmt:message>
                </h2>
                --%>
                <c:set var="audiences" value="${documentNode.properties.audiences}"/>
                <c:if test="${! empty audiences}">
                    <div class="role-wrapper">
                        <c:forEach items="${audiences}" var="audience" varStatus="status">
                            <c:set var="audienceNode" value="${audience.node}"/>
                            <div class="role role-${audienceNode.name}">${audienceNode.displayableName}</div>
                        </c:forEach>
                    </div>
                </c:if>
                <c:if test="${jcr:isNodeType(documentNode, 'jacademix:textContent')}">
                    <span class="readTime">
                        Estimated reading time: <span class="eta"></span>
                    </span>
                </c:if>
            </div>
            <div class="col-md-5 col-sm-12 action-wrapper hidden-print">
                <div class="version-switcher">
                    <label for="version"><fmt:message key="academy.document.version"/></label>
                    <select name="version" id="version">
                        <c:set var="pageNodes"
                               value="${jcr:getMeAndParentsOfType(renderContext.mainResource.node, 'jacademix:isVersionPage')}"/>
                            <%--
                            we try to get the parent page with a jacademix:isVersionPage mixin. This will be the current version
                            of the page.
                            --%>
                        <c:forEach var="pageNode" items="${pageNodes}" end="0">
                            <%--
                            localPathToDoc is the "right part of the path" from the version page
                            Typically if full path is /sites/academy/home/documentation/dx/7.1/converting-osgi-module
                            and the version (the page with jacademix:isVersionPage) is 7.1, then
                            the localPathToDoc will be /converting-osgi-module
                            --%>
                            <c:set var="localPathToDoc"
                                   value="${fn:replace(renderContext.mainResource.node.path, pageNode.path, '')}"/>
                            <c:set var="versionNodes"
                                   value="${jcr:getChildrenOfType(pageNode.parent, 'jacademix:isVersionPage')}"/>
                            <%--
                            Now we iterate all different page version and check if a node with localPathToDoc
                            eists. If yes, the keep this URL, else give URL to version page
                            --%>
                            <c:forEach var="versionNode" items="${versionNodes}">
                                <jcr:node var="versionDocNode" path="${versionNode.path}${localPathToDoc}"/>
                                <c:url var="versionTitle" value="${versionNode.displayableName}"/>
                                <c:set var="isCurrent"
                                       value="${fn:contains(renderContext.mainResource.node.path, versionNode.path)}"/>
                                <c:choose>
                                    <c:when test="${! empty versionDocNode}">
                                        <c:url var="versionUrl" value="${versionDocNode.url}"/>
                                        <option value="${versionUrl}"
                                                <c:if test="${isCurrent}">selected="selected"</c:if>>${versionTitle}</option>
                                    </c:when>
                                    <%--
                                    Question: Should we generate a link to home doc for this version if the doc do not exist?
                                    let say no for now... else uncomment the otherwise part.
                                    <c:otherwise>
                                        <c:url var="versionUrl" value="${versionNode.url}"/>
                                        <option value="${versionUrl}" <c:if test="${isCurrent}">selected="selected"</c:if>>${versionTitle}</option>
                                    </c:otherwise>
                                    --%>
                                </c:choose>
                                <c:remove var="versionDocNode"/>
                                <c:remove var="versionUrl"/>
                            </c:forEach>
                        </c:forEach>
                    </select>
                    <template:addResources type="inline">
                        <script>
                            $(function () {
                                // bind change event to select
                                $('#version').on('change', function () {
                                    var url = $(this).val(); // get selected value
                                    if (url) { // require a URL
                                        window.location = url; // redirect
                                    }
                                    return false;
                                });
                            });
                        </script>
                    </template:addResources>
                        <%--<span>Read in French</span>--%>
                </div>

                <c:set var="pdfNode" value="${documentNode.properties.pdf.node}"/>
                <c:if test="${! empty pdfNode}">
                    <c:url var="pdfUrl" value="${pdfNode.url}" context="/"/>
                </c:if>
                <%-- TODO: also create link to deicated forum --%>
                <c:if test="${! empty pdfUrl}">
                    <div class="action-buttons">
                        <a href="${pdfUrl}">
                            <div class="action-btn"><i class="fa fa-file-pdf-o fa-2x text-danger" aria-hidden="true"></i><span><fmt:message
                                    key="academy.document.download"/></span><%--<span><fmt:message
                                    key="academy.document.format.pdf"/></span>--%></div>
                        </a>
                            <%--
                            <a href="#">
                                <div class="action-btn"><img src="<c:url value="${url.currentModule}/img/forum_ic.png"/>"
                                                             alt=""><span><fmt:message
                                        key="academy.document.goto"/></span><span><fmt:message
                                        key="academy.document.forum"/></span></div>
                            </a>
                            --%>
                    </div>
                </c:if>
            </div>
        </div>
    </c:when>
    <c:otherwise>
        <c:if test="${renderContext.editMode}">
            <%-- Or a wrong binding message --%>
            <fmt:message key="jacademy_documentHeader" var="documentHeaderName"/>
            <fmt:message key="academy.document.empty">
                <fmt:param value="${documentHeaderName}"/>
            </fmt:message>
        </c:if>
    </c:otherwise>
</c:choose>
