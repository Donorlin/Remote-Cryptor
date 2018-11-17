<%--
  Created by IntelliJ IDEA.
  User: mrave
  Date: 17.11.2018
  Time: 3:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>
        <%@ page import="java.util.Map" %>
        <%@ page import="java.util.HashMap" %>
        <%@page contentType="text/html" pageEncoding="UTF-8" %>
        <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
        <html>
        <head>
            <meta http−equiv="Content−Type" content="text/html; charset=UTF−8">
            <title>
                REMOTE SHARE-CRYPTOR 3000
            </title>
        </head>
<body>
<div>
    <h1>REMOTE SHARE-CRYPTOR 3000</h1>
    <div style="color: red">
        <%
            if (null != request.getAttribute("errorMessage")) {
                out.println(request.getAttribute("errorMessage"));
            }
        %>
    </div>
    <form action="recieved" method="post" enctype="multipart/form-data">

        <%
            Map<Long, String> list = (HashMap<Long, String>) request.getAttribute("sharedFiles");
        %>
        <h3>Choose a shared file to decrypt and download</h3>
        <table>
            <tr>
                <th></th>
                <th>File name</th>
            </tr>
            <%
                for (Long key : list.keySet()) {
            %>
            <tr>
                <td><input type="radio" name="fileId" value=<%=key%>/></td>
                <td><%=list.get(key)%></td>
            </tr>
            <%
                }
            %>
        </table>
        </br>
        </br><input type="submit" value="Decrypt and download"/>
    </form>
    </br>
    </br>
    <form action="logout" method="get">
        <input type="submit" value="Log out"/>
    </form>
    <p>Made by DJ, AB, FK.</p>
</div>
</body>
</html>
</title>
</head>
<body>

</body>
</html>
