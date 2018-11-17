<%@ page import="java.util.List" %>
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
    </br>
    </br>
    </br>
    </br>
    <h3>If you do not want to upload your private key, simply download our Local Decryptor 3000 and decipher your file locally.</h3>
    <form action="decryptor" method="get">
        <input type="submit" value="Download"/>
    </form>
    </br>
    </br>
    </br>
    <form action="logout" method="get">
        <input type="submit" value="Log out"/>
    </form>
    <p>Made by DJ, AB, FK.</p>
</div>
</body>
</html>
