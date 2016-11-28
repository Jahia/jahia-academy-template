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
<div class="hidden-xs hidden-sm navbar-right"><a href="#"><img class="navbar-search" src="<c:url value='${url.currentModule}/img/search.png'/>" alt=""></a></div>

<c:if test="${renderContext.loggedIn}">
    <c:set var="firstname" value="${currentUser.properties['j:firstName']}"/>
    <c:set var="lastname" value="${currentUser.properties['j:lastName']}"/>

    <c:set var="userDisplayName">
        <c:if test="${! empty firstname}">${firstname}${' '}</c:if>${lastname}
    </c:set>
    <c:if test="${empty fn:trim(userDisplayName)}"><c:set var="userDisplayName" value="${currentUser.name}"/></c:if>

    <%-- TODO: find a better place to put this switch mode menu --%>
    <ul class="nav navbar-nav navbar-right">
        <li class="dropdown">
            <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false"><i class="glyphicon glyphicon-user"></i> ${userDisplayName} <span class="caret"></span></a>
            <ul class="dropdown-menu">
                <c:catch var="errorSwitchToLive">
                    <c:if test="${! renderContext.liveMode}">
                        <li><a href="<c:url value='${url.live}' context='/'/>"><i class="glyphicon glyphicon-ok"></i> Live</a></li>
                    </c:if>
                </c:catch>
                <c:if test="${errorSwitchToLive != null}">
                    <!--
                    <p>The exception is : ${errorSwitchToLive} <br />
                    There is an exception (errorSwitchToLive): ${errorSwitchToLive.message}</p>
                    -->
                </c:if>
                <c:catch var="errorSwitchToPreview">
                    <c:if test="${! renderContext.previewMode && jcr:hasPermission(renderContext.mainResource.node, 'editModeAccess')}">
                        <c:url value="${url.preview}" var="previewUrl" context='/'/>
                        <li><a href="${previewUrl}"><i class="glyphicon glyphicon-check"></i> Preview</a></li>
                    </c:if>
                </c:catch>
                <c:if test="${errorSwitchToPreview != null}">
                    <!--
                    <p>The exception is : ${errorSwitchToPreview} <br />
                    There is an exception (errorSwitchToPreview): ${errorSwitchToPreview.message}</p>
                    -->
                </c:if>
                <c:catch var="errorSwitchToEdit">
                    <c:if test="${! renderContext.editMode && jcr:hasPermission(renderContext.mainResource.node, 'editModeAccess')}">
                        <li><a href="<c:url value='${url.edit}' context='/'/>"><i class="glyphicon glyphicon-edit"></i> Edit</a></li>
                    </c:if>
                </c:catch>
                <c:if test="${errorSwitchToEdit != null}">
                    <!--
                    <p>The exception is : ${errorSwitchToEdit} <br />
                    There is an exception (errorSwitchToEdit): ${errorSwitchToEdit.message}</p>
                    -->
                </c:if>
                <li role="separator" class="divider"></li>
                <li>
                    <c:url value='${url.logout}' var="fullLogoutUrl">
                        <c:param name="redirect" value="${logoutUrl}"/>
                    </c:url>
                    <a href="${fullLogoutUrl}"><i class="glyphicon glyphicon-minus-sign"></i> Logout</a>
                </li>
            </ul>
        </li>
    </ul>
</c:if>