<%@ taglib prefix="functions" uri="http://www.jahia.org/tags/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="jcr" uri="http://www.jahia.org/tags/jcr" %>
<%@ taglib prefix="template" uri="http://www.jahia.org/tags/templateLib" %>
<%--@elvariable id="currentNode" type="org.jahia.services.content.JCRNodeWrapper"--%>
<%--@elvariable id="renderContext" type="org.jahia.services.render.RenderContext"--%>
<c:set value="${renderContext.site.properties['j:title'].string}" var="title"/>
<jcr:nodeProperty node="${currentNode}" name="j:styleName" var="styleName"/>
<jcr:nodeProperty node="${currentNode}" name="option" var="option"/>
<jcr:nodeProperty node="${currentNode}" name="inverse" var="inverse"/>
<c:choose>
    <c:when test="${renderContext.editModeConfigName eq 'studiomode'}">
        <ul>
            <c:forEach items="${jcr:getChildrenOfType(currentNode, 'bootstrap3mix:navBarItem')}" var="child"
                       varStatus="status">
                <template:module node="${child}"/>
            </c:forEach>
            <template:module path="*"/>
        </ul>
    </c:when>
    <c:otherwise>
        <c:set var="navbarClasses" value=" "/>
        <c:if test="${not empty option and not empty option.string}">
            <c:set var="navbarClasses" value="${navbarClasses} ${option.string}"/>
        </c:if>
        <c:if test="${not empty inverse and inverse.boolean}">
            <c:set var="navbarClasses" value="${navbarClasses} navbar-inverse"/>
        </c:if>
        <c:if test="${not empty styleName}">
            <c:set var="navbarClasses" value="${navbarClasses} ${styleName.string}"/>
        </c:if>
        <nav class="${navbarClasses}">
            <div class="container hidden-print">
                <!-- Brand and toggle get grouped for better mobile display -->
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#academy-navbar-collapse-1" aria-expanded="false">
                        <span class="sr-only">Toggle navigation</span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>
                    <c:url var="logoUrl" value="${url.currentModule}/img/jahia-academy.svg"/>
                    <a class="navbar-brand" href="${renderContext.site.home.url}"><img class="logo" src="${logoUrl}" alt=""></a>
                </div>

                <!-- Collect the nav links, forms, and other content for toggling -->
                <div class="collapse navbar-collapse" id="academy-navbar-collapse-1">

                        <c:forEach items="${jcr:getChildrenOfType(currentNode, 'bootstrap3mix:navBarItem')}" var="child"
                                   varStatus="status">
                            <c:if test="${child.properties['position'].string eq 'right'}">
                                <template:module node="${child}"/>
                            </c:if>
                        </c:forEach>

                </div>
                <!-- /.navbar-collapse -->
            </div>
            <template:area path="navbarNext"/>
        </nav>
    </c:otherwise>
</c:choose>