<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ page import="java.util.List" %>

<c:set var="bodyContent">
    <div>
        <%
            if (request.getAttribute("userList") != null) {
                List<String> list = (List<String>) request.getAttribute("userList");
                if (!list.isEmpty()) {
        %>
        <div class="p-md-3 rounded" style="border: solid #9999;">
            <p class="h5">File sharing</p>
            <form action="${pageContext.request.contextPath}/share" method="post" enctype="multipart/form-data" id="form-share">
                <div class="form-group row">
                    <label for="input-inputFile" class="col-md-3 col-form-label">Choose a file:</label>
                    <div class="col-md-5">
                        <input type="file" class="form-control-file" id="input-inputFile" name="inputFile"
                               required="required"/>
                    </div>
                </div>
                <div class="form-group row">
                    <label for="input-shareWith" class="col-md-3 col-form-label">Share with:</label>
                    <div class="col-md-5">
                        <select name="shareWith" id="input-shareWith" class="form-control" required="required">
                            <option value="" disabled selected>Please choose a user</option>
                            <%
                                for (int i = 0; i < list.size(); i++) {
                            %>
                            <option value=<%=list.get(i)%>><%=list.get(i)%>
                            </option>
                            <%
                                }
                            %>
                        </select>
                    </div>
                </div>
                <div class="form-group row">
                    <label for="input-comment" class="col-md-3 col-form-label">Initial comment:</label>
                    <div class="col-md-5">
                        <textarea rows="4" class="form-control" id="input-comment" name="comment" form="form-share" placeholder="Write your comment here..."></textarea>
                    </div>
                </div>
                <button type="submit" class="btn btn-secondary">Share</button>
            </form>
        </div>
        <%
        } else {
        %>
        <div class="d-flex justify-content-center">
            <div class="alert alert-danger" style="display:inline-block;" role="alert">
                No other users are registered on this site. You are alone in here.
            </div>
        </div>
        <%
            }
        } else {
        %>
        <div class="d-flex justify-content-center">
            <div class="alert alert-danger" style="display:inline-block;" role="alert">
                Something went wrong. Sharing is not available at this moment. Please contact the administration.
            </div>
        </div>
        <%
            }
        %>
    </div>
</c:set>

<c:set var="infoText">
    <div>
        <p class="h4">Sharing</p>
        <p>On this page you can share your files with other registered users.</p>
    </div>
</c:set>

<t:genericPageWithMenu>
    <jsp:attribute name="activePageTitle">
        Share
    </jsp:attribute>
    <jsp:attribute name="infoText">
        ${infoText}
    </jsp:attribute>
    <jsp:body>
        ${bodyContent}
    </jsp:body>
</t:genericPageWithMenu>