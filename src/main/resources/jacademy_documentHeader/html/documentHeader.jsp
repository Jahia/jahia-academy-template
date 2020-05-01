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
        <c:when test="${jcr:isNodeType(boundComponent, 'jacademix:document')}">
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
        <div class="row">
            <div class="col-xs-8 ">
                <c:set var="author" value="${documentNode.properties.author.string}"/>
                <div class="role-wrapper">
                    <i class="fas fa-edit fa-fw" aria-hidden="true"></i>&nbsp;
                    <fmt:message key="academy.document.writtenBy">
                        <fmt:param value="${empty author ? 'The Jahia Team' : author}"/>
                    </fmt:message>
                </div>
                <c:set var="audiences" value="${documentNode.properties.audiences}"/>
                <c:if test="${! empty audiences}">

                    <c:forEach items="${audiences}" var="audience" varStatus="status">
                        <c:if test="${status.first}"><div class="role-wrapper hidden-print"><i class="fas fa-users fa-fw text-muted" aria-hidden="true"></i>&nbsp;</c:if>
                        <c:set var="audienceNode" value="${audience.node}"/>
                        <div class="label label-success">${audienceNode.displayableName}</div>
                        <c:if test="${status.last}"></div></c:if>
                    </c:forEach>
                </c:if>
                <c:set var="tagList" value="${documentNode.properties['j:tagList']}"/>
                <c:if test="${! empty tagList}">
                    <%-- FIXME: This is hardcoded path... --%>
                    <jcr:node var="searchPage" path="/sites/academy/home/search"/>

                    <c:forEach items="${tagList}" var="tag" varStatus="status">
                        <c:if test="${status.first}"><div class="role-wrapper hidden-print"><i class="fas fa-tags fa-fw text-muted" aria-hidden="true"></i>&nbsp;</c:if>
                        <c:if test="${! empty searchPage}">
                            <c:url var="searchTagUrl" value="${searchPage.url}" context="/">
                                <c:param name="src_terms[0].term" value="${tag.string}"/>
                                <c:param name="src_terms[0].fields.tags" value="true"/>
                                <c:param name="src_sites.values" value="${renderContext.site.siteKey}"/>
                                <c:param name="autoSuggest" value="false"/>
                            </c:url>
                        </c:if>
                        <a class="label label-info" href="${searchTagUrl}">${tag.string}</a>
                        <c:if test="${status.last}"></div></c:if>
                        <c:remove var="searchTagUrl"/>
                    </c:forEach>
                </c:if>
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
                <c:if test="${jcr:hasPermission(documentNode, 'jContentAccess') && jcr:isNodeType(documentNode, 'jacademy:kbEntry')}">
                    <div class="role-wrapper hidden-print smaller">
                        <c:set var="key" value="${documentNode.properties.jiraKey.string}"/>
                        <i class="fas fa-ticket-alt"></i>&nbsp;&nbsp;&nbsp;<a
                            href="https://support.jahia.com/browse/${key}">${key}</a>
                    </div>
                </c:if>

                <c:if test="${jcr:isNodeType(documentNode, 'jacademix:textContent,jacademy:kbEntry')}">
                    <div class="role-wrapper hidden-print smaller">
                        <span class="readTime">
                            <i class="far fa-clock fa-fw " aria-hidden="true"></i>&nbsp;&nbsp;&nbsp;Estimated reading time: <span
                                class="eta"></span>
                        </span>
                    </div>
                </c:if>

            </div>

            <c:set var="pdfNode" value="${documentNode.properties.pdf.node}"/>
            <c:if test="${! empty pdfNode}">
                <c:url var="pdfUrl" value="${pdfNode.url}" context="/"/>
            </c:if>
            <c:set var="isTechwiki" value="${fn:contains(fn:toLowerCase(documentNode.path),'/techwiki/')}"/>
            <c:set var="ishowTo" value="${fn:contains(fn:toLowerCase(documentNode.path),'/how-to/')}"/>
            <c:set var="pageNodes"
                   value="${jcr:getMeAndParentsOfType(renderContext.mainResource.node, 'jacademix:isVersionPage')}"/>
            <c:set var="pageNodesSize"
                   value="${fn:length(pageNodes)}"/>

            <c:if test="${! empty pdfUrl or pageNodesSize > 0 or jcr:isNodeType(documentNode, 'jacademix:specificVersions')}">
                <div class="col-xs-4 hidden-print text-right">
                    <c:if test="${pageNodesSize > 0 && ! isTechwiki && ! ishowTo}">

                        <c:forEach var="pageNode" items="${pageNodes}" varStatus="status">
                            <c:if test="${status.first}">
                                <c:set var="localPathToDoc"
                                       value="${fn:replace(renderContext.mainResource.node.path, pageNode.path, '')}"/>
                                <c:set var="versionNodes"
                                       value="${jcr:getChildrenOfType(pageNode.parent, 'jacademix:isVersionPage')}"/>


                                <div class="btn-group">
                                    <c:set var="hasOtherVersions" value="${fn:length(versionNodes)>1}"/>
                                    <c:if test="${hasOtherVersions}">
                                        <button class="btn btn-default btn-lg dropdown-toggle version" type="button"
                                                data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                            <i class="fas fa-code-branch text-muted" aria-hidden="true"></i>
                                                ${pageNode.displayableName}
                                            <c:if test="${hasOtherVersions}">&nbsp;<i class="fas fa-angle-down" aria-hidden="true"></i></c:if>
                                        </button>
                                        <c:forEach var="versionNode" items="${versionNodes}" varStatus="vStatus">
                                            <c:if test="${vStatus.first}">
                                                <ul class="dropdown-menu">
                                            </c:if>
                                            <jcr:node var="versionDocNode"
                                                      path="${versionNode.path}${localPathToDoc}"/>
                                            <c:set var="isCurrent"
                                                   value="${fn:contains(renderContext.mainResource.node.path, versionNode.path)}"/>
                                            <c:choose>
                                                <c:when test="${! empty versionDocNode }">
                                                    <c:url var="versionUrl"
                                                           value="${isCurrent?'#' : versionDocNode.url}"/>
                                                    <li${isCurrent?' class="active"':''}><a
                                                            href="${versionUrl}">${versionNode.displayableName}</a></li>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:url var="versionPageUrl" value="${versionNode.url}"/>
                                                    <li${isCurrent?' class="active"':''}><a
                                                            href="${versionPageUrl}">${versionNode.displayableName}</a>
                                                    </li>
                                                </c:otherwise>
                                            </c:choose>
                                            <c:remove var="versionDocNode"/>
                                            <c:if test="${vStatus.last}">
                                                </ul>
                                            </c:if>
                                        </c:forEach>
                                    </c:if>
                                </div>
                            </c:if>
                            <c:remove var="versionNodes"/>
                            <c:remove var="hasOtherVersions"/>
                        </c:forEach>
                    </c:if>
                    <c:if test="${jcr:isNodeType(documentNode, 'jacademix:specificVersions')}">
                        <c:set var="specificVersions" value="${documentNode.properties.specificVersions}"/>
                        <c:forEach items="${specificVersions}" var="specificVersion" varStatus="status">
                            <c:if test="${status.first}"><div class="role-wrapper hidden-print"></c:if>
                            <span class="label label-default">${specificVersion.string}</span>
                            <c:if test="${status.last}"></div></c:if>
                        </c:forEach>
                    </c:if>
                    <c:if test="${! empty pdfUrl}">
                        <c:set var="pdfTitle" value="${documentNode.properties.pdfTitle.string}"/>
                        <c:if test="${empty pdfTitle}">
                            <fmt:message key="academy.document.download" var="pdfTitle"/>
                        </c:if>

                        <div><a href="${pdfUrl}" class="text-muted"><i class="far fa-file-pdf text-muted fa-fw"
                                                                       aria-hidden="true"></i>&nbsp;${pdfTitle}</a>
                        </div>
                    </c:if>
                </div>
            </c:if>
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
