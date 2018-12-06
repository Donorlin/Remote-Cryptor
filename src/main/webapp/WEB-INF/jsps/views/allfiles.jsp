<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ page import="database.entity.ShareLog" %>
<%@ page import="java.util.List" %>
<%@ page import="database.entity.Comment" %>

<c:set var="bodyContent">
    <div>
        <%
            List<ShareLog> list = (List<ShareLog>) request.getAttribute("sharedFiles");
            if (!list.isEmpty()) {
        %>
        <div class="d-flex justify-content-between">
            <p class="h5">List of all shared files</p>
            <form method="get" action="${pageContext.request.contextPath}/allfiles">
                <div class="form-group row">
                    <label for="input-search" class="col-sm-3 col-form-label">Search: </label>
                    <div class="col-sm-9">
                        <input type="text" class="form-control-sm" id="input-search" name="searchWord"
                               placeholder="Search">
                    </div>
                </div>
            </form>
        </div>
        <div class="accordion" id="accordion-allfiles">
            <%
                for (ShareLog log : list) {
            %>
            <div class="card">
                <div class="card-header" id="heading-<%=log.getId()%>">
                    <h5 class="mb-0">
                        <div class="row">
                            <button class="btn btn-link" type="button" data-toggle="collapse"
                                    data-target="#collapse-<%=log.getId()%>" aria-expanded="false"
                                    aria-controls="collapse-<%=log.getId()%>">
                                <%=log.getFileName()%>
                            </button>
                            <div class="col">
                                <div class="float-sm-right">
                                    <div class="row">
                                        <div>Originator: </div>
                                        <div class="font-weight-light"><%=log.getOriginator().getUsername()%>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div>Receiver: </div>
                                        <div class="font-weight-light"><%=log.getReceiver().getUsername()%>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </h5>
                </div>

                <div id="collapse-<%=log.getId()%>" class="collapse" aria-labelledby="heading-<%=log.getId()%>"
                     data-parent="#accordion-allfiles">
                    <div class="card-body">
                        <%
                            for (Comment comment : log.getComments()) {
                        %>
                        <div>
                            <div>
                                <strong><%=comment.getAuthor().getUsername()%>
                                </strong> <span
                                    class="text-muted"><%=comment.getCreateTime().toString()%></span>
                            </div>
                            <div>
                                <%=comment.toString()%>
                            </div>
                        </div>
                        <%
                            }
                        %>
                        <form action="${pageContext.request.contextPath}/comment" method="post" id="form-comment-<%=log.getId()%>">
                            <div class="form-group">
                                <input type="hidden" value="<%=log.getId()%>" name="fileId"/>
                                <label for="input-comment" class="col-md-3 col-form-label">Your comment:</label>
                                <div class="col-md-5">
                                    <textarea rows="4" class="form-control" id="input-comment" name="comment"
                                              form="form-comment-<%=log.getId()%>"
                                              placeholder="Write your comment here..." required></textarea>
                                </div>
                            </div>
                            <button type="submit" class="btn btn-secondary">Share</button>
                        </form>
                    </div>
                </div>
            </div>
            <%
                }
            %>
        </div>
        <%
        } else {
        %>
        <div class="alert alert-danger text-center" role="alert">
            There are no shared files at this moment.
        </div>
        <%
            }
        %>
    </div>
</c:set>

<c:set var="infoText">
    <div>
        <p class="h4">All shared files</p>
        <p>TODO</p>
    </div>
</c:set>

<t:genericPageWithMenu>
    <jsp:attribute name="activePageTitle">
        All shared files
    </jsp:attribute>
    <jsp:attribute name="infoText">
        ${infoText}
    </jsp:attribute>
    <jsp:body>
        ${bodyContent}
    </jsp:body>
</t:genericPageWithMenu>