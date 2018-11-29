<%
    if (null != request.getAttribute("errorMessage")) {

%>
<div class="alert alert-danger text-center" role="alert">
    <%
        out.println(request.getAttribute("errorMessage"));
    %>
</div>
<%
    }
%>