<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ui" uri="http://www.jahia.org/tags/uiComponentsLib" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="template" uri="http://www.jahia.org/tags/templateLib" %>
<%@ taglib prefix="jcr" uri="http://www.jahia.org/tags/jcr" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%--@elvariable id="currentNode" type="org.jahia.services.content.JCRNodeWrapper"--%>
<%--@elvariable id="out" type="java.io.PrintWriter"--%>
<%--@elvariable id="script" type="org.jahia.services.render.scripting.Script"--%>
<%--@elvariable id="scriptInfo" type="java.lang.String"--%>
<%--@elvariable id="workspace" type="java.lang.String"--%>
<%--@elvariable id="renderContext" type="org.jahia.services.render.RenderContext"--%>
<%--@elvariable id="currentResource" type="org.jahia.services.render.Resource"--%>
<%--@elvariable id="url" type="org.jahia.services.render.URLGenerator"--%>
<%--@elvariable id="currentAliasUser" type="org.jahia.services.usermanager.JahiaUser"--%>
<c:if test="${(!renderContext.loggedIn || currentAliasUser.username eq 'guest') || renderContext.editMode}">
    ${currentNode.properties.textContent.string}
    <ui:loginArea class="form-inline">
        <c:if test="${not empty param['loginError']}">
            <div class="alert alert-danger" role="alert">
                <strong><i class="fa fa-exclamation-triangle fa-fw" aria-hidden="true"></i>&nbsp;Error:</strong>
                <fmt:message
                        key="${loginResult == 'account_locked' ? 'message.accountLocked' : 'message.invalidUsernamePassword'}"/>
            </div>
        </c:if>
        <div class="form-group">
            <label class="sr-only" for="username"><fmt:message key='jacademy_loginForm.username'/></label>
            <input type="text" class="form-control" id="username" name="username" placeholder="<fmt:message key='jacademy_loginForm.username'/>">
        </div>
        <div class="form-group">
            <label class="sr-only" for="password"><fmt:message key='jacademy_loginForm.password'/></label>
            <input type="password" class="form-control" id="password" name="password" placeholder="<fmt:message key='jacademy_loginForm.password'/>">
        </div>
        <button type="submit" class="btn btn-default"><fmt:message key='jacademy_loginForm.signIn'/></button>
    </ui:loginArea>
</c:if>

