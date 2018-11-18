<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%@include file="header.jsp"%>
<body>
<jsp:include page="menu.jsp"/>
<div>
    <h1>REMOTE SHARE-CRYPTOR 3000</h1>
    <%
        Map<Long, String> list = (HashMap<Long, String>) request.getAttribute("sharedFiles");
        if (!list.isEmpty()) {
    %>
    <div style="color: red">
        Please refresh this page after your download started.
    </div>
    <form action="recieved" method="post">
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
                <td><%=list.get(key)%>
                </td>
            </tr>
            <%
                }
            %>
        </table>
        </br>
        </br><input type="submit" value="Decrypt and download"/>
    </form>
    <%
    } else {
    %>
    <div style="color: red">
        You have no recieved files.
    </div>
    <%
        }
    %>
</div>
</body>
</html>
</title>
</head>
<body>

</body>
</html>
