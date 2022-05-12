<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="template" uri="http://www.jahia.org/tags/templateLib" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="jcr" uri="http://www.jahia.org/tags/jcr" %>
<%@ taglib prefix="jnt" uri="http://www.jahia.org/tags/templateLib" %>
<%--@elvariable id="currentNode" type="org.jahia.services.content.JCRNodeWrapper"--%>
<%--@elvariable id="out" type="java.io.PrintWriter"--%>
<%--@elvariable id="script" type="org.jahia.services.render.scripting.Script"--%>
<%--@elvariable id="scriptInfo" type="java.lang.String"--%>
<%--@elvariable id="workspace" type="java.lang.String"--%>
<%--@elvariable id="renderContext" type="org.jahia.services.render.RenderContext"--%>
<%--@elvariable id="currentResource" type="org.jahia.services.render.Resource"--%>
<%--@elvariable id="url" type="org.jahia.services.render.URLGenerator"--%>
<c:set var="heading" value="${currentNode.properties.heading.string}"/>
<c:set var="expanded" value="${currentNode.properties.expanded.string eq 'true'}"/>

<a class="accordion-button${expanded?' ':' collapsed'}" data-bs-toggle="collapse" href="#collapse${currentNode.identifier}" role="button" aria-expanded="${expanded}" aria-controls="collapse${currentNode.identifier}"><${heading}>${currentNode.displayableName}</${heading}></a>
<div class="collapse${expanded ? ' show':''}" id="collapse${currentNode.identifier}">

    ${currentNode.properties.textContent.string}
    <c:forEach items="${jcr:getChildrenOfType(currentNode, 'jmix:droppableContent')}" var="droppableContent">
        <template:module node="${droppableContent}" editable="true"/>
    </c:forEach>
    <c:if test="${renderContext.editMode}">
        <template:module path="*" nodeTypes="jmix:droppableContent"/>
    </c:if>

</div>

