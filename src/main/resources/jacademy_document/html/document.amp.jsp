<%@ page language="java" contentType="text/html;charset=UTF-8" %><!doctype html>
<html amp lang="en">
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
<head>
    <meta charset="utf-8">
    <script async src="https://cdn.ampproject.org/v0.js"></script>
    <c:set var="title" value="${currentNode.properties['jcr:title'].string}"/>
    <title>${fn:escapeXml(title)}</title>
    <link rel="canonical" href="http://example.ampproject.org/article-metadata.html" />
    <meta name="viewport" content="width=device-width,minimum-scale=1,initial-scale=1">
    <c:set var="imageUrl" value="${url.currentModule}/img/logo.png"/>
    <c:set var="author" value="${currentNode.properties.author.string}"/>
    <c:if test="${empty author}">
        <c:set var="author" value="The Jahia Team"/>
    </c:if>

    <script type="application/ld+json">
      {
        "@context": "http://schema.org",
        "@type": "Article",
        "headline": "Open-source framework for publishing content",
        "datePublished": ""${fn:escapeXml(currentNode.properties['j:lastPublished'].string)}"",
        "author": "${fn:escapeXml(author)}",
        "publisher": "${fn:escapeXml(currentNode.properties['j:lastPublishedBy'].string)}",
        "image": [
          "${imageUrl}"
        ]
      }
    </script>
    <style amp-boilerplate>body{-webkit-animation:-amp-start 8s steps(1,end) 0s 1 normal both;-moz-animation:-amp-start 8s steps(1,end) 0s 1 normal both;-ms-animation:-amp-start 8s steps(1,end) 0s 1 normal both;animation:-amp-start 8s steps(1,end) 0s 1 normal both}@-webkit-keyframes -amp-start{from{visibility:hidden}to{visibility:visible}}@-moz-keyframes -amp-start{from{visibility:hidden}to{visibility:visible}}@-ms-keyframes -amp-start{from{visibility:hidden}to{visibility:visible}}@-o-keyframes -amp-start{from{visibility:hidden}to{visibility:visible}}@keyframes -amp-start{from{visibility:hidden}to{visibility:visible}}</style><noscript><style amp-boilerplate>body{-webkit-animation:none;-moz-animation:none;-ms-animation:none;animation:none}</style></noscript>
    <style amp-custom>
        .btn-primary {
            color: #ffffff;
            text-shadow: 0 -1px 0 rgba(0, 0, 0, 0.25);
            background-color: #6600cc;
            background-image: -moz-linear-gradient(top, #6600cc, #6600cc);
            background-image: -webkit-gradient(linear, 0 0, 0 100%, from(#6600cc), to(#6600cc));
            background-image: -webkit-linear-gradient(top, #6600cc, #6600cc);
            background-image: -o-linear-gradient(top, #6600cc, #6600cc);
            background-image: linear-gradient(to bottom, #6600cc, #6600cc);
            background-repeat: repeat-x;
            filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#ff6600cc', endColorstr='#ff6600cc', GradientType=0);
            border-color: #6600cc #6600cc #400080;
            border-color: rgba(0, 0, 0, 0.1) rgba(0, 0, 0, 0.1) rgba(0, 0, 0, 0.25);
            filter: progid:DXImageTransform.Microsoft.gradient(enabled = false);
            color: #ffffff;
        }
        .btn {
            display: inline-block;
            padding: 4px 12px;
            margin-bottom: 10px;
            font-size: 14px;
            font-weight: bold;
            line-height: 20px;
            text-align: center;
            vertical-align: middle;
            cursor: pointer;
            background-color: #ffffff;
            border: 1px solid #cccccc;
            border-bottom-color: #b3b3b3;
            -webkit-border-radius: 4px;
            -moz-border-radius: 4px;
            border-radius: 4px;
            text-shadow: 0 1px rgba(0, 0, 0, 0.1);
        }
    </style>
    <script async custom-element="amp-fit-text" src="https://cdn.ampproject.org/v0/amp-fit-text-0.1.js"></script>

</head>
<body>
<amp-fit-text width="300" height="45" layout="responsive" max-font-size="45">
    ${title}
</amp-fit-text>

<c:set var="textContent" value="${currentNode.properties.textContent.string}"/>
${textContent}

</body>
</html>


