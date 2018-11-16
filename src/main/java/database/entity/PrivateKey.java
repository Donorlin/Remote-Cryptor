package database.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

@Entity
@Table(name = "PRVT_KY")
public class PrivateKey implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @Column(name = "PRIVATE_KEY", nullable = false, columnDefinition = "BLOB")
    private byte[] privateKey;

    public PrivateKey() {
    }

    public PrivateKey(byte[] privateKey) {
        this.privateKey = privateKey;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(byte[] privateKey) {
        this.privateKey = privateKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PrivateKey that = (PrivateKey) o;
        return Objects.equals(getId(), that.getId()) &&
                Arrays.equals(getPrivateKey(), that.getPrivateKey());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getId());
        result = 31 * result + Arrays.hashCode(getPrivateKey());
        return result;
    }
}
