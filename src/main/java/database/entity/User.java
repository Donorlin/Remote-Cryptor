package database.entity;

import crypto.CryptoUtils;
import crypto.HashSaltUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.security.KeyPair;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
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
    private Date createTime;

    @OneToOne(orphanRemoval = true, cascade = CascadeType.PERSIST, optional = false)
    @JoinColumn(name = "PRVT_KY")
    private PrivateKey privateKey;

    @Column(name = "PUBLIC_KEY", nullable = false, columnDefinition = "BLOB")
    private byte[] publicKey;

    @OneToMany(mappedBy = "originator")
    private List<ShareLog> sharedFiles;

    @OneToMany(mappedBy = "receiver")
    private List<ShareLog> receivedFiles;


    public User() {
    }

    public User(String username, String password) throws Exception {
        this.username = username;
        this.salt = HashSaltUtils.getNextSalt();
        this.passwordHash = HashSaltUtils.getPasswordSaltHash(password, this.salt);
        KeyPair keyPair = CryptoUtils.generateKeyPair();
        this.publicKey = keyPair.getPublic().getEncoded();
        this.privateKey = new PrivateKey(keyPair.getPrivate().getEncoded());
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    public byte[] getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(byte[] publicKey) {
        this.publicKey = publicKey;
    }

    public Date getCreateTime() {
        return createTime;
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

    public List<ShareLog> getSharedFiles() {
        return sharedFiles;
    }

    public void setSharedFiles(List<ShareLog> sharedFiles) {
        this.sharedFiles = sharedFiles;
    }

    public List<ShareLog> getReceivedFiles() {
        return receivedFiles;
    }

    public void setReceivedFiles(List<ShareLog> recievedFiles) {
        this.receivedFiles = recievedFiles;
    }

    @PrePersist
    public void setCreateTime() {
        this.createTime = new Date();
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
                ", create_time=" + createTime +
                '}';
    }
}
