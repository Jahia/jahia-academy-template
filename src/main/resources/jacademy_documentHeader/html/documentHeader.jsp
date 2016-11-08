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
<c:set var="bindedComponent" value="${ui:getBindedComponent(currentNode, renderContext, 'j:bindedComponent')}"/>
<c:choose>
    <c:when test="${bindedComponent != null}">
        <div class="row documentation-header">
            <div class="col-md-7 col-sm-12">
                <h1 class="doc-child main-title">${bindedComponent.properties['jcr:title'].string}</h1>
                <h2 class="doc-child author">
                <fmt:message key="academy.document.writtenBy">
                    <fmt:param value="${bindedComponent.properties['jcr:createdBy'].string}"/>
                </fmt:message>
                </h2>
                <div class="role-wrapper">
                    <c:if test="${bindedComponent.properties['marketers'].boolean}"><div class="role-marketers"><fmt:message key="academy.document.marketers"/></div></c:if>
                    <c:if test="${bindedComponent.properties['developers'].boolean}"><div class="role-developers"><fmt:message key="academy.document.developers"/></div></c:if>
                    <c:if test="${bindedComponent.properties['sys_admin'].boolean}"><div class="role-sysadmin"><fmt:message key="academy.document.sys_admin"/></div></c:if>
                </div>
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
        <fmt:message key="academy.binded.empty"/>
    </c:otherwise>
</c:choose>
