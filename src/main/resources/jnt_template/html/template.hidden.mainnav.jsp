<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="jcr" uri="http://www.jahia.org/tags/jcr" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="template" uri="http://www.jahia.org/tags/templateLib" %>
<%@ taglib prefix="af" uri="http://academy.jahia.org/af" %>
<c:url var="homePageUrl" value="${renderContext.site.home.url}" context="/" />
<%--@elvariable id="currentNode" type="org.jahia.services.content.JCRNodeWrapper"--%>
<%--@elvariable id="renderContext" type="org.jahia.services.render.RenderContext"--%>
<nav class="jac-main-navigation navbar navbar-light border-bottom navbar-expand-lg container-fluid bg-white"
    role="navigation">
    <!-- Logo -->
    <a href="${homePageUrl}" class="navbar-brand text-decoration-nones">
        <c:set var="logoUrl" value="${url.currentModule}/images/logo-academy.svg" />
        <img src="${logoUrl}" alt="" />
    </a>

    <!-- Burger Button for responsive -->
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#mainMenu"
        aria-controls="mainMenu" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>

    <!-- Menu naviagtion -->
    <div class="collapse navbar-collapse" id="mainMenu">
        <ul class="nav navbar-nav me-auto">
            <%--
                <li>
                    <a href="${homePageUrl}" class="d-flex align-items-center mb-2 mb-lg-0 text-decoration-nones">
                        <c:set var="logoUrl" value="${url.currentModule}/images/logo-academy.svg"/>
                        <img src="${logoUrl}" alt=""/>
                    </a>
                </li>
                --%>
            <c:set var="root" value="${currentNode.properties.root.string}" />
            <c:set var="curentPageNode" value="${renderContext.mainResource.node}" />
            <c:if test="${! jcr:isNodeType(curentPageNode,'jmix:navMenuItem')}">
                <c:set var="curentPageNode" value="${jcr:getParentOfType(curentPageNode, 'jmix:navMenuItem')}" />
            </c:if>
            <c:set var="rootNode" value="${renderContext.site.home}" />
            <template:addCacheDependency node="${rootNode}"/>
            <c:set var="level1Pages" value="${jcr:getChildrenOfType(rootNode, 'jmix:navMenuItem')}" />
            <c:set var="hasLevel1Pages" value="${fn:length(level1Pages) > 0}" />
            <c:if test="${hasLevel1Pages}">
                <c:forEach items="${level1Pages}" var="level1Page" varStatus="status">
                    <c:set var="page1Active" value="false"/>
                    <template:addCacheDependency node="${level1Page}"/>
                    <c:if test="${! jcr:isNodeType(level1Page, 'jacademix:hidePage')}">
                        <c:set var="displayLevel1Page" value="true" />
                        <c:if test="${jcr:isNodeType(level1Page, 'jacademix:hidePage')}">
                            <c:set var="displayLevel1Page" value="false" />
                        </c:if>
                        <c:if test="${!empty level1Page.properties['j:displayInMenuName']}">
                            <c:set var="displayLevel1Page" value="false" />
                            <c:forEach items="${level1Page.properties['j:displayInMenuName']}" var="display">
                                <c:if test="${display.string eq currentNode.name}">
                                    <c:set var="displayLevel1Page" value="${display.string eq currentNode.name}" />
                                </c:if>
                            </c:forEach>
                        </c:if>
                        <c:if test="${displayLevel1Page}">
                            <c:choose>
                                <c:when test="${jcr:isNodeType(level1Page, 'jnt:navMenuText')}">
                                    <c:set var="page1Url" value="${af:findFirstSubPageUrl(level1Page)}" />
                                    <c:set var="page1Title" value="${level1Page.displayableName}" />
                                </c:when>
                                <c:when test="${jcr:isNodeType(level1Page, 'jnt:externalLink')}">
                                    <c:url var="page1Url" value="${level1Page.properties['j:url'].string}" />
                                    <c:set var="page1Title" value="${level1Page.displayableName}" />
                                </c:when>
                                <c:when test="${jcr:isNodeType(level1Page, 'jnt:page')}">
                                    <c:url var="page1Url" value="${level1Page.url}" />
                                    <c:set var="page1Title" value="${level1Page.displayableName}" />
                                </c:when>
                                <c:when test="${jcr:isNodeType(level1Page, 'jnt:nodeLink')}">
                                    <c:url var="page1Url" value="${level1Page.properties['j:node'].node.url}" />
                                    <c:set var="page1Title" value="${level1Page.properties['jcr:title'].string}" />
                                    <c:if test="${empty page1Title}">
                                        <c:set var="page1Title"
                                            value="${level1Page.properties['j:node'].node.displayableName}" />
                                    </c:if>
                                </c:when>
                            </c:choose>
                            <c:if test="${fn:contains(renderContext.mainResource.node.path, level1Page.path)}">
                                <c:set var="page1Active" value="true" />
                            </c:if>

                            <c:set var="level2Pages" value="${jcr:getChildrenOfType(level1Page, 'jmix:navMenuItem')}" />
                            <c:set var="hasLevel2Pages" value="${fn:length(level2Pages) > 0}" />
                            <c:set var="level2PageCounter" value="0" />
                            <c:choose>
                                <c:when test="${hasLevel2Pages}">
                                    <c:set var="level2PageMenu">
                                        <li class="nav-item  ${page1Active? ' active' :''} dropdown">
                                            <a class="nav-link dropdown-toggle ${page1Active? ' active' :''}" href="#"
                                                id="navbarDropdownMen-${currentNode.identifier}-${level1Page.identifier}"
                                                data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                                ${page1Title}
                                            </a>
                                            <div class="dropdown-menu"
                                                aria-labelledby="navbarDropdownMen-${currentNode.identifier}-${level1Page.identifier}">
                                                <a class="dropdown-item" href="${page1Url}">${page1Title}</a>
                                                <div class="dropdown-divider"></div>

                                                <c:forEach items="${level2Pages}" var="level2Page" varStatus="status">
                                                    <template:addCacheDependency node="${level2Page}"/>
                                                    <c:if test="${! jcr:isNodeType(level2Page, 'jacademix:hidePage')}">
                                                        <c:set var="displayLevel2Page" value="true" />
                                                        <c:if
                                                            test="${jcr:isNodeType(level1Page, 'jacademix:hidePage')}">
                                                            <c:set var="displayLevel1Page" value="false" />
                                                        </c:if>

                                                        <c:if
                                                            test="${!empty level2Page.properties['j:displayInMenuName']}">
                                                            <c:set var="displayLevel2Page" value="false" />
                                                            <c:forEach
                                                                items="${level2Page.properties['j:displayInMenuName']}"
                                                                var="display">
                                                                <c:if test="${display.string eq currentNode.name}">
                                                                    <c:set var="displayLevel2Page"
                                                                        value="${display.string eq currentNode.name}" />
                                                                </c:if>
                                                            </c:forEach>
                                                        </c:if>

                                                        <c:if test="${displayLevel2Page}">
                                                            <c:set var="level2PageCounter"
                                                                value="${level2PageCounter+1}" />
                                                            <c:choose>
                                                                <c:when
                                                                    test="${jcr:isNodeType(level2Page, 'jnt:navMenuText')}">
                                                                    <c:set var="page2Url" value="${af:findFirstSubPageUrl(level2Page)}" />
                                                                    <c:set var="page2Title"
                                                                        value="${level2Page.displayableName}" />
                                                                </c:when>

                                                                <c:when
                                                                    test="${jcr:isNodeType(level2Page, 'jnt:externalLink')}">
                                                                    <c:url var="page2Url"
                                                                        value="${level2Page.properties['j:url'].string}" />
                                                                    <c:set var="page2Title"
                                                                        value="${level2Page.displayableName}" />
                                                                </c:when>

                                                                <c:when
                                                                    test="${jcr:isNodeType(level2Page, 'jnt:page')}">
                                                                    <c:url var="page2Url" value="${level2Page.url}" />
                                                                    <c:set var="page2Title"
                                                                        value="${level2Page.displayableName}" />
                                                                    <c:if
                                                                        test="${fn:contains(renderContext.mainResource.path, level2Page.path)}">
                                                                        <c:set var="page2Active" value="true" />
                                                                    </c:if>
                                                                </c:when>

                                                                <c:when
                                                                    test="${jcr:isNodeType(level2Page, 'jnt:nodeLink')}">
                                                                    <c:url var="page2Url"
                                                                        value="${level2Page.properties['j:node'].node.url}" />
                                                                    <c:set var="page2Title"
                                                                        value="${level2Page.properties['jcr:title'].string}" />
                                                                    <c:if test="${empty page2Title}">
                                                                        <c:set var="page2Title"
                                                                            value="${level2Page.properties['j:node'].node.displayableName}" />
                                                                    </c:if>
                                                                </c:when>
                                                            </c:choose>

                                                            <a class="dropdown-item ${page2Active? ' active' :''}"
                                                                href="${page2Url}">${page2Title}
                                                                <c:if test="${page2Active}">
                                                                    <span class="visually-hidden">(current)</span>
                                                                </c:if>
                                                            </a>
                                                        </c:if>
                                                        <c:remove var="page2Active" />
                                                        <c:remove var="page2Url" />
                                                        <c:remove var="page2Title" />
                                                    </c:if>
                                                </c:forEach>
                                            </div>
                                        </li>
                                    </c:set>

                                    <c:choose>
                                        <c:when test="${level2PageCounter>0}">
                                            ${level2PageMenu}
                                        </c:when>
                                        <c:otherwise>
                                            <li class="nav-item ${page1Active? ' active' :''}">
                                                <a class="nav-link" href="${page1Url}">${page1Title} <c:if
                                                        test="${page1Active}">
                                                        <span class="visually-hidden">(current)</span>
                                                    </c:if>
                                                </a>
                                            </li>
                                        </c:otherwise>
                                    </c:choose>

                                    <c:remove var="level2PageCounter" />
                                    <c:remove var="level2PageMenu" />

                                </c:when>
                                <c:otherwise>
                                    <li class="nav-item ${page1Active? ' active' :''}">
                                        <a class="nav-link" href="${page1Url}">${page1Title} <c:if
                                                test="${page1Active}"><span class="visually-hidden">(current)</span>
                                            </c:if>
                                        </a>
                                    </li>
                                </c:otherwise>
                            </c:choose>
                        </c:if>
                    </c:if>
                    <c:remove var="page1Active" />
                    <c:remove var="page1Url" />
                    <c:remove var="page1Title" />
                </c:forEach>
            </c:if>
        </ul>
        <ul class="nav navbar-nav">
            <li class="nav-item">
                <a class="nav-link main_cta main_cta_contact pe-4" href="https://www.jahia.com/contact" target="_blank"><span class="btn btn-dark-fuchsia text-nowrap" style="margin-top: -0.45rem; ">Contact Us</span></a>
            </li>
        </ul>
    </div>
</nav>
