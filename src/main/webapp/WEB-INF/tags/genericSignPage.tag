<!DOCTYPE html>
<%@tag description="Generic page with menu, used for logged in users." pageEncoding="UTF-8" %>
<%@attribute name="header" fragment="true" %>

<html>
<head>
    <meta http−equiv="Content−Type" content="text/html; charset=UTF−8">
    <script src="${pageContext.request.contextPath}/static/js/jquery-3.3.1.min.js"></script>
    <script src="${pageContext.request.contextPath}/static/js/popper.min.js"></script>
    <script src="${pageContext.request.contextPath}/static/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/bootstrap.min.css" type="text/css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/signInUp.css" type="text/css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/index.css" type="text/css">

    <title>
        RC3000
    </title>
</head>

<body>
<div class="container  h-100">
    <div class="col h-100 text-center align-items-center">
        <div class="row">
            <div class="col">
                <jsp:invoke fragment="header"/>
            </div>
        </div>
        <div class="row">
            <div class="col">
                <jsp:doBody/>
            </div>
        </div>
        <div class="row">
            <jsp:include page="/WEB-INF/jsps/components/error.jsp"/>
        </div>
        <jsp:include page="/WEB-INF/jsps/components/footer.jsp"/>
    </div>
</div>
</body>


</html>