<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>


<c:set var="headerContent">
    <h1 class="h3 mb-3 font-weight-normal">Please sign in</h1>
</c:set>

<c:set var="bodyContent">
    <div>
        <form class="form-signin" method="post" action="login">
            <label for="inputUsername" class="sr-only">Username</label>
            <input type="text" name="username" id="inputUsername" class="form-control" placeholder="Username" required
                   autofocus>
            <label for="inputPassword"  class="sr-only">Password</label>
            <input type="password" name="password" id="inputPassword" class="form-control" placeholder="Password" required>

            <button class="btn btn-lg btn-primary btn-block" type="submit">Sign in</button>
        </form>
        <p>Not registered, yet? Click <a href="register">here</a> to register.</p>
    </div>
</c:set>

<t:genericSignPage>
    <jsp:attribute name="header">
        ${headerContent}
    </jsp:attribute>
    <jsp:body>
        ${bodyContent}
    </jsp:body>
</t:genericSignPage>