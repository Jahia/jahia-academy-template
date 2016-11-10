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
        <c:when test="${boundComponent.primaryNodeType eq 'jacademy:document'}">
            <%-- If bound component is not a document search for a document in its children --%>
            <c:set var="documentNode" value="${boundComponent}"/>
        </c:when>
        <c:otherwise>
            <%-- If bound component  is not a document search for a document in its children --%>
            <c:set var="documentChildren" value="${jcr:getChildrenOfType(boundComponent,'jacademy:document')}"/>
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
                <h1 class="doc-child main-title">${documentNode.properties['jcr:title'].string}</h1>
                <h2 class="doc-child author">
                <fmt:message key="academy.document.writtenBy">
                    <fmt:param value="${documentNode.properties['jcr:createdBy'].string}"/>
                </fmt:message>
                </h2>
                <c:set var="audiences" value="${documentNode.properties.audiences}"/>
                <c:if test="${! empty audiences}">
                    <div class="role-wrapper">
                        <c:forEach items="${audiences}" var="audience" varStatus="status">
                            <c:set var="audienceNode" value="${audience.node}"/>
                            <div class="role role-${audienceNode.name}">${audienceNode.displayableName}</div>
                        </c:forEach>
                    </div>
                </c:if>

            </div>
            <div class="col-md-5 col-sm-12 action-wrapper">
                <div class="version-switcher">
                    <label for="version"><fmt:message key="academy.document.version"/></label>
                    <select name="version" id="version">
                        <option>7.1.2</option>
                        <option>7.2</option>
                        <option>7.2.2</option>
                        <option>7.3.1</option>
                        <option>7.4.2</option>
                        <option>7.5.2</option>
                        <option>8.2</option>
                    </select>
                    <span>Read in French</span>
                </div>
                <div class="action-buttons">
                    <a href="#">
                        <div class="action-btn"><img src="<c:url value="${url.currentModule}/img/pdf_ic.png"/>" alt=""><span><fmt:message key="academy.document.download"/></span><span><fmt:message key="academy.document.format.pdf"/></span></div>
                    </a>
                    <a href="#">
                        <div class="action-btn"><img src="<c:url value="${url.currentModule}/img/forum_ic.png"/>" alt=""><span><fmt:message key="academy.document.goto"/></span><span><fmt:message key="academy.document.forum"/></span></div>
                    </a>
                </div>
            </div>
        </div>
    </c:when>
    <c:otherwise>
        <%-- Or a wrong binding message --%>
        <fmt:message key="jacademy_documentHeader" var="documentHeaderName"/>
        <fmt:message key="academy.document.empty">
            <fmt:param value="${documentHeaderName}"/>
        </fmt:message>
    </c:otherwise>
</c:choose>
