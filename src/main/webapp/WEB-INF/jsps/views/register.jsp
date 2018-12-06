<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>


<c:set var="headerContent">
    <h2 class="h3 mb-3 font-weight-normal">Please sign up</h2>
</c:set>

<c:set var="bodyContent">
    <div>
        <form class="form-signin" method="post" action="">
            <label for="inputUsername" class="sr-only">Username</label>
            <input type="text" name="username" id="inputUsername" class="form-control" placeholder="Username" required
                   autofocus>
            <label for="inputPassword" class="sr-only">Password</label>
            <input type="password" name="password" id="inputPassword" class="form-control" placeholder="Password"
                   required>
            <button class="btn btn-lg btn-primary btn-block" type="submit">Sign up</button>
        </form>
        <p>Already register? Click <a href="login">here</a> to log in.</p>
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