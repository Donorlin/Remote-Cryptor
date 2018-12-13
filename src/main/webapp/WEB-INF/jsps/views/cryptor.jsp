<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<c:set var="bodyContent">
    <h1 class="h3 mb-3 font-weight-normal">REMOTE CRYPTOR 3000</h1>
    <div class="p-md-3 rounded" style="border: solid #9999;">
    <div action="${pageContext.request.contextPath}/cryptor" method="post" enctype="multipart/form-data">
        <div class="form-group row">
            <label for="form-inputFile" class="col-sm-3 col-form-label">Choose file to encrypt/decrypt</label>
            <div class="col-sm-auto">
                <input type="file" class="form-control-file" id="form-inputFile" name="inputFile"
                       required="required"/>
            </div>
        </div>

        <div class="custom-control custom-radio custom-control-inline">
            <input type="radio" class="custom-control-input" id="form-radio-encrypt" name="type" value="encrypt"
                   checked="checked"/>
            <label class="custom-control-label" for="form-radio-encrypt"> Encrypt (upload public key) </label>
        </div>

        <div class="custom-control custom-radio custom-control-inline">
            <input type="radio" class="custom-control-input" id="form-radio-decrypt" name="type" value="decrypt"/>
            <label class="custom-control-label" for="form-radio-decrypt">Decrypt (upload private key)</label>
        </div>

        <div class="form-group row">
            <label for="form-input-key" class="col-sm-3 col-form-label">Upload asked key</label>
            <div class="col-sm-auto">
                <input type="file" class="form-control-file" id="form-input-key" name="key" required="required"/>
            </div>
        </div>
        <button type="submit" class="btn btn-secondary">Upload</button>
        </form>
    </div>
</c:set>

<t:genericPage>
    <jsp:attribute name="footer">
        <h3>If you do not want to upload your private key, simply download our Local Decryptor 3000 and decipher your
            file locally.</h3>
        <form action="${pageContext.request.contextPath}/decryptor" method="get">
            <input type="submit" value="Download"/>
        </form>
        <p>This page can be accessed without being logged in thus has no menu support.</p>
    </jsp:attribute>
    <jsp:body>
        ${bodyContent}
    </jsp:body>
</t:genericPage>