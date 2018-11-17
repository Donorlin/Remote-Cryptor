package database.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "SHARELOG")
public class ShareLog implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    private User originator;

    @ManyToOne
    private User reciever;

    @Column(name = "FILENAME", nullable = false)
    private String fileName;

    @Column(name = "PATHTOFILE", nullable = false)
    private String pathToFile;

    @Column(name = "UPLOADDATETIME", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date uploadDateTime;

    @Column(name = "DOWNLOADDATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date downloadDateTime;

    public ShareLog() {
        setUploadDateTime();
    }

    public Long getId() {
        return id;
    }

    private void setId(Long id) {
        this.id = id;
    }

    public User getOriginator() {
        return originator;
    }

    public void setOriginator(User originator) {
        this.originator = originator;
    }

    public User getReciever() {
        return reciever;
    }

    public void setReciever(User reciever) {
        this.reciever = reciever;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPathToFile() {
        return pathToFile;
    }

    public void setPathToFile(String pathToFile) {
        this.pathToFile = pathToFile;
    }

    public Date getUploadDateTime() {
        return uploadDateTime;
    }

    private void setUploadDateTime() {
        this.uploadDateTime = new Date();
    }

    public Date getDownloadDateTime() {
        return downloadDateTime;
    }

    public void setDownloadDateTime(Date downloadDateTime) {
        this.downloadDateTime = downloadDateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShareLog shareLog = (ShareLog) o;
        return Objects.equals(id, shareLog.id) &&
                Objects.equals(originator, shareLog.originator) &&
                Objects.equals(reciever, shareLog.reciever) &&
                Objects.equals(fileName, shareLog.fileName) &&
                Objects.equals(pathToFile, shareLog.pathToFile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, originator, reciever, fileName, pathToFile);
    }
}
