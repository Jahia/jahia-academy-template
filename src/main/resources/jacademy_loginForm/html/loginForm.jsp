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
    <ui:loginArea onsubmit="loginButton.disabled = true; return true;">
        <div class="modal-body">
            <ui:isLoginError var="loginResult">
                <div class="alert alert-danger" role="alert">
                    <fmt:message key="${loginResult == 'account_locked' ? 'message.accountLocked' : 'message.invalidUsernamePassword'}"/>
                </div>
                <c:set var="error" value="true"/>
            </ui:isLoginError>
            <c:if test="${! empty param.loginError}">
                <div class="alert alert-warning" role="alert">
                    <fmt:message key="${param.loginError == 'account_locked' ? 'message.accountLocked' : 'message.invalidUsernamePassword'}"/>
                </div>
                <c:set var="error" value="true"/>
            </c:if>

            <div class="input-group mb-3">
                <fmt:message key="label.username" var="usernameLabel"/>
                <span class="input-group-text" id="username">
                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-person-fill" viewBox="0 0 16 16">
                          <path d="M3 14s-1 0-1-1 1-4 6-4 6 3 6 4-1 1-1 1H3zm5-6a3 3 0 1 0 0-6 3 3 0 0 0 0 6z"/>
                        </svg>
                    </span>
                <input type="text" class="form-control" id="username" name="username" placeholder="${fn:escapeXml(usernameLabel)}" <c:if test="${! empty username}"><c:out value=" "/>value="${fn:escapeXml(username)}"</c:if> required autofocus aria-describedby="username">
            </div>

            <div class="input-group mb-3">
                <fmt:message key="label.password" var="passwordLabel"/>
                <span class="input-group-text" id="password">
                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-key-fill" viewBox="0 0 16 16">
                          <path d="M3.5 11.5a3.5 3.5 0 1 1 3.163-5H14L15.5 8 14 9.5l-1-1-1 1-1-1-1 1-1-1-1 1H6.663a3.5 3.5 0 0 1-3.163 2zM2.5 9a1 1 0 1 0 0-2 1 1 0 0 0 0 2z"/>
                        </svg>
                    </span>
                <input type="password" class="form-control" id="password" name="password" placeholder="${fn:escapeXml(passwordLabel)}" required aria-describedby="password">
            </div>
        </div>
        <div class="modal-footer">
            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
            <button type="submit" name="loginButton" class="btn btn-primary"><fmt:message key='bootstrap5nt_navbar.label.login'/></button>
        </div>

    </ui:loginArea>
</c:if>

