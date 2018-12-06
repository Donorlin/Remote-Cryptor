<%@tag description="Generic page with menu, used for logged in users." pageEncoding="UTF-8" %>
<%@taglib prefix="tc" tagdir="/WEB-INF/tags/components" %>
<%@attribute name="activePageTitle" fragment="true" %>
<%@attribute name="infoText" fragment="true" %>
<!DOCTYPE html>
<html>
<head>
    <meta http−equiv="Content−Type" content="text/html; charset=UTF−8">
    <script src="${pageContext.request.contextPath}/static/js/jquery-3.3.1.min.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/static/js/popper.min.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/static/js/bootstrap.min.js" type="text/javascript"></script>
    <script type="text/javascript" defer>
        $(document).ready(function(){
            var xtoken = '${pageContext.request.getAttribute("X-TOKEN")}';
            $('<input>').attr({
                type: 'hidden',
                name: 'X-TOKEN',
                value: xtoken
            }).appendTo('form');
            console.log(xtoken);
        });
    </script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/bootstrap.min.css" type="text/css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/index.css" type="text/css"/>

    <title>
        <jsp:invoke fragment="activePageTitle"/>
        - RC3000
    </title>
</head>

<body>
<tc:menuWithTitle>
    <jsp:attribute name="title">
        <div style="color: rgba(255,255,255,.5);">
            <jsp:invoke fragment="activePageTitle"/>
        </div>
    </jsp:attribute>
</tc:menuWithTitle>


<div class="container mt-md-5 mb-md-5">
    <jsp:include page="/WEB-INF/jsps/components/error.jsp"/>
    <jsp:invoke fragment="infoText"/>
    <jsp:doBody/>
</div>

<jsp:include page="/WEB-INF/jsps/components/footer.jsp"/>


</body>


</html>