<%@ page import="java.util.List" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%@include file="header.jsp"%>
<body>
<jsp:include page="menu.jsp" />
<div>
    <h1>REMOTE SHARE-CRYPTOR 3000</h1>
    <div style="color: red">
        <%
            if (null != request.getAttribute("errorMessage")) {
                out.println(request.getAttribute("errorMessage"));
            }
        %>
    </div>
    <form action="share" method="post" enctype="multipart/form-data">

        <h3>Choose file to share and encypt</h3>
        <input type="file" name="inputFile" required="required"/>

        <%
            List<String> list = (List<String>)request.getAttribute("userList");
        %>
        <h3>Choose a user to share file with</h3>
        <select name="shareWith" required="required">
            <option value="" disabled selected>Please choose a user</option>
            <%
                for (int i = 0; i < list.size(); i++) {
            %>
            <option value=<%=list.get(i)%>><%=list.get(i)%></option>
            <%
                }
            %>
        </select>
        </br></br>
        </br><input type="submit" value="Share"/>
    </form>
</div>
</body>
</html>
