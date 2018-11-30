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
            <p class="h5">List of my files</p>
            <form method="get" action="received">
                <div class="form-group row">
                    <label for="input-search" class="col-sm-3 col-form-label">Search: </label>
                    <div class="col-sm-9">
                        <input type="text" class="form-control-sm" id="input-search" name="searchWord"
                               placeholder="Search">
                    </div>
                </div>
            </form>
        </div>
        <div class="accordion" id="accordion-myfiles">
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
                            <div class="ml-auto">
                                <form method="post" action="received">
                                    <input type="hidden" value="<%=log.getId()%>" name="fileId"/>
                                    <input type="hidden" value="dont" name="decrypt"/>
                                    <button class="btn btn-link" type="submit">Download encrypted</button>
                                </form>
                                <form method="post" action="received">
                                    <input type="hidden" value="<%=log.getId()%>" name="fileId"/>
                                    <button class="btn btn-link" type="submit">Download decrypted</button>
                                </form>
                            </div>
                        </div>
                    </h5>
                </div>

                <div id="collapse-<%=log.getId()%>" class="collapse" aria-labelledby="heading-<%=log.getId()%>"
                     data-parent="#accordion-myfiles">
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
                        <form action="comment" method="post" id="form-comment-<%=log.getId()%>">
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
            You have no received files.
        </div>
        <%
            }
        %>
    </div>
</c:set>

<c:set var="infoText">
    <div>
        <p class="h4">My files</p>
        <p>On this page you can see files that somebody else shared with you. You can download encrypted or decrypted
            version of these files.
            If you choose to download encrypted version of the file, you need to have a Local Decryptor 3000 downloaded
            on your machine and your
            private key to be able to decrypt your file. On the right side of the menu bar you have options to download
            both.</p>
    </div>
</c:set>

<t:genericPageWithMenu>
    <jsp:attribute name="activePageTitle">
        My files
    </jsp:attribute>
    <jsp:attribute name="infoText">
        ${infoText}
    </jsp:attribute>
    <jsp:body>
        ${bodyContent}
    </jsp:body>
</t:genericPageWithMenu>