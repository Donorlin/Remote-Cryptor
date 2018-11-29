<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>

<c:set var="bodyContent">
    <div>
        <h1>REMOTE SHARE-CRYPTOR 3000</h1>
        <%
            Map<Long, String> list = (HashMap<Long, String>) request.getAttribute("sharedFiles");
            if (!list.isEmpty()) {
        %>
        <div class="alert alert-warning alert-dismissible fade show" role="alert">
            Please refresh this page after your download started.
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>





        <form action="received" method="post">
            <h3>Choose a shared file to decrypt and download</h3>
            <table>
                <tr>
                    <th></th>
                    <th>File name</th>
                </tr>
                <%
                    for (Long key : list.keySet()) {
                %>
                <tr>
                    <td><input type="radio" name="fileId" value=<%=key%>
                    /></td>
                    <td><%=list.get(key)%>
                    </td>
                </tr>
                <%
                    }
                %>
            </table>
            </br>
            Do not decrypt <input type="checkbox" name="decrypt" value="dont"></br>
            </br><input type="submit" value="Download"/>
        </form>



        <%
        } else {
        %>
        <div class="alert alert-danger text-center" role="alert">
            You have no received files.
        </div>
        <%
            }
        %>


    </div>
</c:set>


<t:genericPageWithMenu>
    <jsp:attribute name="activePageTitle">
        My files
    </jsp:attribute>
    <jsp:body>
        ${bodyContent}
    </jsp:body>
</t:genericPageWithMenu>