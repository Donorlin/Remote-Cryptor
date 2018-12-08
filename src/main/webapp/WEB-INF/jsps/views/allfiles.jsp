<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
                            <span class="align-self-center font-weight-light text-muted"><%=log.getUploadDateTime().toString()%></span>
                            <div class="col">
                                <div class="float-sm-right">
                                    <div class="row">
                                        <div class="col">
                                            <div>From:</div>
                                        </div>
                                        <div class="col">
                                            <div class="font-weight-light"><%=log.getOriginator().getUsername()%>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col">
                                            <div>To:</div>
                                        </div>
                                        <div class="col">
                                            <div class="font-weight-light"><%=log.getReceiver().getUsername()%>
                                            </div>
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
                        <c:set var="comment" value="<%=comment.getComment()%>"/>
                        <%
                            if (request.getSession().getAttribute("username").equals(comment.getAuthor().getUsername())) {
                        %>
                        <div class="row ml-2 mb-2">
                            <%
                            } else {
                            %>
                            <div class="row mb-2 mr-2 justify-content-end">
                                <%
                                    }
                                %>
                                <div class="col-auto d-inline-block p-1 border border-secondary rounded">
                                    <div class="border-bottom border-secondary">
                                        <strong><%=comment.getAuthor().getUsername()%>
                                        </strong> <span
                                            class="text-muted"><%=comment.getCreateTime().toString()%></span>
                                    </div>
                                    <div>
                                            ${fn:escapeXml(comment)}
                                    </div>
                                </div>
                            </div>
                            <%
                                }
                            %>
                            <form action="${pageContext.request.contextPath}/comment" method="post"
                                  id="form-comment-<%=log.getId()%>">
                                <div class="form-group">
                                    <input type="hidden" value="<%=log.getId()%>" name="fileId"/>
                                    <label for="input-comment" class="col-md-3 col-form-label">Your comment:</label>
                                    <div class="col-md-4">
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
    </div>
</c:set>

<c:set var="infoText">
    <div>
        <p class="h4">All shared files</p>
        <p>On this page you can see all shared files. You can not download any, even if some files are meant for you.
            You can download your files <a href="${pageContext.request.contextPath}/myfiles">here</a>. Feel free to
            leave a comment or two if you want a file owner to share a given file with you. </p>
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