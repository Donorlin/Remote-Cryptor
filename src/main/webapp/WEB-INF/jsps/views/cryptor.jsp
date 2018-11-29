<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<c:set var="bodyContent">

    <div>
        <form action="cryptor" method="post" enctype="multipart/form-data">
            <h3>Choose file to encrypt/decrypt</h3>
            <input type="file" name="inputFile" required="required"/>
            <h3>Choose what you want to do with a file</h3>
            <input type="radio" name="type" value="encrypt" checked="checked"/>

            Encrypt (upload public key)
            <input type="radio" name="type" value="decrypt"/>

            Decrypt (upload private key)

            <h3>Upload asked key</h3>
            <input type="file" name="key" required="required"/> </br>

            </br><input type="submit" value="Upload"/>
        </form>
    </div>
</c:set>

<t:genericPage>
    <jsp:attribute name="header">
            <h1 class="h3 mb-3 font-weight-normal">REMOTE CRYPTOR 3000</h1>
    </jsp:attribute>
    <jsp:attribute name="footer">
        <h3>If you do not want to upload your private key, simply download our Local Decryptor 3000 and decipher your
            file locally.</h3>
        <form action="decryptor" method="get">
            <input type="submit" value="Download"/>
        </form>
        <p>This page can be accessed without being logged in thus has no menu support.</p>
    </jsp:attribute>
    <jsp:body>
        ${bodyContent}
    </jsp:body>
</t:genericPage>