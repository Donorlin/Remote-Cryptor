<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http−equiv="Content−Type" content="text/html; charset=UTF−8">
    <title>
        REMOTE CRYPTOR 3000
    </title>
</head>
<body>
<div>
    <h1>REMOTE CRYPTOR 3000</h1>
    <div style="color: red">
        <%
            if (null != request.getAttribute("errorMessage")) {
                out.println(request.getAttribute("errorMessage"));
            }
        %>
    </div>
    <form action="cryptor" method="post" enctype="multipart/form-data">
        <h3>Choose file to encrypt/decrypt</h3>
        <input type="file" name="inputFile" required="required"/>
        <h3>Choose what you want to do with a file</h3>
        <input type="radio" name="type" value="encrypt" checked="checked"/>

        Encrypt (upload public key)
        <input type="radio" name="type" value="decrypt" />

        Decrypt (upload private key)

        <h3>Upload asked key</h3>
        <input type="file" name="key" required="required"/> </br>

        </br><input type="submit" value="Upload"/>
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
