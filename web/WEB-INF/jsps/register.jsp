<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http−equiv="Content−Type" content="text/html; charset=UTF−8">
    <title>
        REMOTE CRYPTOR 3000 - registration page
    </title>
</head>
<body>
<div>
    <h3>Register page</h3>
    <div style="color: red">
        <%
            if (null != request.getAttribute("errorMessage")) {
                out.println(request.getAttribute("errorMessage"));
            }
        %>
    </div>
    <form action="" method="post">
        <p>Username: </p>
        <input type="text" name="username" required="required"/> </br>
        <p>Password: </p>
        <input type="password" name="password" required="required"/> </br>
        </br><input type="submit" value="Register"/>
        <p>Already register? Click <a href="login">here</a> to log in.</p>
    </form>
</div>
</body>
</html>
