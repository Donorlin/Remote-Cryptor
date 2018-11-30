package database.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "COMMENT")
public class Comment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @Column(name = "COMMENT", nullable = false, columnDefinition = "TEXT")
    private String comment;

    @Column(name = "CREATE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    @ManyToOne
    private User author;

    @ManyToOne
    private ShareLog shareLog;

    public Comment(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getCreateTime() {
        return createTime;
    }

    @PrePersist
    public void setCreateTime() {
        this.createTime = new Date();
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public ShareLog getShareLog() {
        return shareLog;
    }

    public void setShareLog(ShareLog shareLog) {
        this.shareLog = shareLog;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment1 = (Comment) o;
        return Objects.equals(id, comment1.id) &&
                Objects.equals(comment, comment1.comment) &&
                Objects.equals(createTime, comment1.createTime) &&
                Objects.equals(author, comment1.author) &&
                Objects.equals(shareLog, comment1.shareLog);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, comment, createTime, author, shareLog);
    }

    @Override
    public String toString() {
        return "Comment{" +
                "comment='" + comment + '\'' +
                ", createTime=" + createTime +
                ", author=" + author.getUsername() +
                '}';
    }
}
