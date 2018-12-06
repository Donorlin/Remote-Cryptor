<%@tag description="Generic page with menu, used for logged in users." pageEncoding="UTF-8" %>
<%@attribute name="header" fragment="true" %>
<%@attribute name="footer" fragment="true" %>
<!DOCTYPE html>
<html>
<head>
    <meta http−equiv="Content−Type" content="text/html; charset=UTF−8" />
    <script src="${pageContext.request.contextPath}/static/js/jquery-3.3.1.min.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/static/js/popper.min.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/static/js/bootstrap.min.js" type="text/javascript"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/bootstrap.min.css" type="text/css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/index.css" type="text/css"/>

    <title>
        REMOTE CRYPTOR 3000
    </title>
</head>

<body>
<jsp:invoke fragment="header"/>

<div class="container">
    <jsp:doBody/>
</div>

<jsp:include page="/WEB-INF/jsps/components/error.jsp"/>

<div class="row">
    <div class="col text-center align-items-center">
        <jsp:invoke fragment="footer"/>
    </div>
</div>

<jsp:include page="/WEB-INF/jsps/components/footer.jsp"/>


</body>


</html>