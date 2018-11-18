<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%@include file="header.jsp"%>
<body>
<div>
    <h3>Login page</h3>
    <div style="color: red">
        <%
            if (null != request.getAttribute("errorMessage")) {
                out.println(request.getAttribute("errorMessage"));
            }
        %>
    </div>
    <form action="login" method="post">
        <p>Username: </p>
        <input type="text" name="username" required="required"/> </br>
        <p>Password: </p>
        <input type="password" name="password" required="required"/> </br>
        </br><input type="submit" value="Log in"/>
        <p>Not registered, yet? Click <a href="/">here</a> to register.</p>
    </form>
</div>
</body>
</html>
