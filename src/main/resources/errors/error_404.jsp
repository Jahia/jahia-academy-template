<%@page language="java" contentType="text/html; charset=UTF-8" session="false" %><!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:catch var="catchException">
    <c:import url='<%=request.getScheme() + "://" + request.getServerName().toString() + ":" + request.getServerPort() + "/404"%>' />
</c:catch>
<c:if test = "${catchException != null}">
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="robots" content="noindex, nofollow"/>

    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>404</title>
    <link href="/modules/bootstrap3-core/css/bootstrap.min.css" rel="stylesheet">
    <link href="/modules/font-awesome/css/font-awesome.min.css" rel="stylesheet">

</head>
<body>

<div class="container">
    <div class="row">
        <p class="col-md-12">
            <h2>
                404 Not Found</h2>
            <p>Sorry, an error has occured, Requested page not found!</p>
            <p><a href="/" class="btn btn-primary btn-lg"><i class="fa fa-home" aria-hidden="true"></i> Take Me Home</a></p>
            <!--
            ${catchException.message}
            -->
        </div>
    </div>
</div>
</body>
</html>
</c:if>