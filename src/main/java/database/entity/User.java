package database.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "USER")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "PASSWORD_HASH", columnDefinition = "binary(32)")
    private byte[] passwordHash;

    @Column(name = "SALT", columnDefinition = "binary(8)")
    private byte[] salt;

    @Column(name = "CREATE_TIME", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date create_time;

    public User() {
    }

    public User(String username, byte[] passwordHash, byte[] salt) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.salt = salt;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public byte[] getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(byte[] passwordHash) {
        this.passwordHash = passwordHash;
    }

    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }

    @PrePersist
    public void setCreate_time() {
        this.create_time = new Date();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(getId(), user.getId()) &&
                Objects.equals(getUsername(), user.getUsername()) &&
                Arrays.equals(getPasswordHash(), user.getPasswordHash()) &&
                Arrays.equals(getSalt(), user.getSalt());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getId(), getUsername());
        result = 31 * result + Arrays.hashCode(getPasswordHash());
        result = 31 * result + Arrays.hashCode(getSalt());
        return result;
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password_hash=" + Arrays.toString(passwordHash) +
                ", salt=" + Arrays.toString(salt) +
                ", create_time=" + create_time +
                '}';
    }
}
