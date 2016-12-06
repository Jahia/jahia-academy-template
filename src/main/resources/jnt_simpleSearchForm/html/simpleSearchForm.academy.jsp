<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="jcr" uri="http://www.jahia.org/tags/jcr" %>
<%@ taglib prefix="s" uri="http://www.jahia.org/tags/search" %>
<%@ taglib prefix="template" uri="http://www.jahia.org/tags/templateLib" %>


<template:addCacheDependency uuid="${currentNode.properties.result.string}"/>
<c:if test="${not empty currentNode.properties.result.node}">
    <c:url var="searchUrl" value='${currentNode.properties.result.node.url}' context="/"/>
    <s:form method="post" class="form-inline" action="${searchUrl}">
        <s:site value="${renderContext.site.name}" includeReferencesFrom="systemsite" display="false"/>
        <s:language value="${renderContext.mainResource.locale}" display="false"/>
        <div class="form-group">
            <label for="searchTerm">Search for</label>
            <%--<s:term match="all_words" id="searchTerm" placeholder="Type any terms" searchIn="siteContent,tags,files" class="form-control"/>--%>
            <s:term match="all_words" id="searchTerm" placeholder="Type any terms" searchIn="siteContent,tags" class="form-control"/>
        </div>
        <button type="submit" class="btn btn-default"><fmt:message key='search.submit'/></button>
    </s:form>
</c:if>
