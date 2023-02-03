<%@ taglib prefix="jcr" uri="http://www.jahia.org/tags/jcr" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="utility" uri="http://www.jahia.org/tags/utilityLib" %>
<%@ taglib prefix="template" uri="http://www.jahia.org/tags/templateLib" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="functions" uri="http://www.jahia.org/tags/functions" %>
<%--@elvariable id="currentNode" type="org.jahia.services.content.JCRNodeWrapper"--%>
<%--@elvariable id="out" type="java.io.PrintWriter"--%>
<%--@elvariable id="script" type="org.jahia.services.render.scripting.Script"--%>
<%--@elvariable id="scriptInfo" type="java.lang.String"--%>
<%--@elvariable id="workspace" type="java.lang.String"--%>
<%--@elvariable id="renderContext" type="org.jahia.services.render.RenderContext"--%>
<%--@elvariable id="currentResource" type="org.jahia.services.render.Resource"--%>
<%--@elvariable id="url" type="org.jahia.services.render.URLGenerator"--%>

<template:include view="hidden.header"/>
<c:if test="${not empty moduleMap.currentList}">
    <div class="row row-eq-height">
        <c:forEach items="${moduleMap.currentList}" var="node" begin="${moduleMap.begin}" end="${moduleMap.end}">
            <c:url var="nodeLink" value="${node.url}" context="/"/>
            <c:set var="nodeTitle" value="${node.displayableName}"/>
            <div class="col-md-4">

                <div class="simplebox position-relative">
                    <c:if test="${jcr:isNodeType(node, 'jacademix:metadatas')}">
                        <div class="jahia-badge">
                            <c:set var="personas" value="${node.properties.personas}" />
                            <c:if test="${! empty personas}">
                                <c:forEach items="${personas}" var="persona" varStatus="status">
                                    <c:set var="personaNode" value="${persona.node}" />
                                    <span class="badge rounded-pill jahia-badge persona">${personaNode.displayableName}</span>
                                </c:forEach>
                            </c:if>

                            <c:set var="products" value="${node.properties.products}" />
                            <c:if test="${! empty products}">
                                <c:forEach items="${products}" var="product" varStatus="status">
                                    <c:set var="productNode" value="${product.node}" />
                                    <c:set var="productName" value="${productNode.name}" />
                                    <span class="badge rounded-pill jahia-badge ${productName}">${productNode.displayableName}</span>
                                </c:forEach>
                            </c:if>
                        </div>
                    </c:if>
                    <h4 class="my-0">${nodeTitle}
                        <a class="stretched-link" href="${nodeLink}" title="${fn:escapeXml(nodeTitle)}"><i class="d-none fas fa-arrow-right"></i></a>
                    </h4>
                </div>
            </div>

        </c:forEach>
    </div>
    <c:if test="${functions:length(moduleMap.currentList) == 0 and not empty moduleMap.emptyListMessage}">
        ${moduleMap.emptyListMessage}
    </c:if>
</c:if>

<template:include view="hidden.footer"/>
