<%@tag description="Generic page with menu, used for logged in users." pageEncoding="UTF-8" %>

<%@attribute name="title" fragment="true" %>

<nav class="navbar navbar-expand-lg navbar-dark bg-dark mb-md-1">
    <a class="navbar-brand" href="#">RC3000</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarNav">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/share">Share</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/myfiles">My files</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/allfiles">All files</a>
            </li>
        </ul>
        <div class="navbar-nav mx-auto text-secondary">
            <jsp:invoke fragment="title"/>
        </div>
        <ul class="navbar-nav ml-auto">
            <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/cryptor">Remote cryptor</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/decryptor">Download Local Decryptor</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/privatekey">Download private key</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/logout">Logout</a>
            </li>
        </ul>
    </div>
</nav>
