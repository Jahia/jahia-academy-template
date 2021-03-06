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
<div class="row masonry">
    <c:forEach items="${currentNode.nodes}" var="subchild" varStatus="statusCount">
        <%--<c:if test="${col_number eq 0}">
            <c:set var="currentStyle" value="${styleC0}"/>
        </c:if>
        <c:if test="${col_number eq 1}">
            <c:set var="currentStyle" value="${styleC1}"/>
        </c:if>
        <c:if test="${col_number eq 2}">
            <c:set var="currentStyle" value="${styleC2}"/>
        </c:if>
        <article class="white-panel r${row_number} c${col_number}" style="${currentStyle}" attr-row="${row_number}"
                 attr-col="${col_number}">--%>
        <div class="col-md-4 col-sm-6 col-xs-12">
            <div class="white-panel">
                <template:module node="${subchild}" editable="${renderContext.editMode}"/>
                    <%--<c:choose>
                        <c:when test="${col_number == 2}">
                            <c:set var="col_number" value="0"/>
                            <c:set var="row_number" value="${row_number+1}"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="col_number" value="${col_number+1}"/>
                        </c:otherwise>
                    </c:choose>
                    --%>
            </div>
        </div>
    </c:forEach>
</div>
<c:if test="${renderContext.editMode}">
    <template:module path="*" nodeTypes="jacademy:productCard"/>
</c:if>