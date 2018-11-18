package database.user;

import crypto.CryptoUtils;
import database.entity.ShareLog;
import database.entity.User;
import org.apache.commons.io.FileUtils;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShareService {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("Cryptor-pu");

    private String shareDirectory;

    public ShareService(String shareDirectory) {
        this.shareDirectory = shareDirectory;
    }

    public boolean shareFile(String originatorUsername, String recieverUsername, File fileToShare) {
        EntityManager entityManager = emf.createEntityManager();
        UserService userService = new UserService();
        User originator = userService.getUserByUsername(originatorUsername);
        User reciever = userService.getUserByUsername(recieverUsername);
        String fileName = fileToShare.getName();

        File shareDir = new File(shareDirectory);
        if (!shareDir.exists()) {
            shareDir.mkdir();
        }

        try {
            File encryptedFile = File.createTempFile(originatorUsername, recieverUsername, shareDir);

            CryptoUtils.encrypt(fileToShare, encryptedFile, recieverUsername);

            ShareLog shareLog = new ShareLog();
            shareLog.setOriginator(originator);
            shareLog.setReciever(reciever);
            shareLog.setFileName(fileName);
            shareLog.setPathToFile(encryptedFile.getPath());


            entityManager.getTransaction().begin();
            entityManager.persist(shareLog);
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            System.out.println(ex);
            entityManager.getTransaction().rollback();
            entityManager.close();
            return false;
        }
        entityManager.close();
        return true;
    }

    public File decryptSharedFile(String recieverUsername, Long fileId) throws Exception {
        EntityManager entityManager = emf.createEntityManager();
        ShareLog shareLog = entityManager.find(ShareLog.class, fileId);

        File sharedFile = new File(shareLog.getPathToFile());
        File decryptedFile = new File(shareDirectory + File.separator + shareLog.getFileName());
        CryptoUtils.decrypt(sharedFile, decryptedFile, recieverUsername);

        shareLog.setDownloadDateTime(new Date());

        entityManager.getTransaction().begin();
        entityManager.merge(shareLog);
        entityManager.getTransaction().commit();
        entityManager.close();
        sharedFile.delete();
        return decryptedFile;
    }

    // Long - id of Sharelog
    // String - fileName of Sharelog
    public Map<Long, String> getSharedFilesListByUsername(String username) {
        EntityManager entityManager = emf.createEntityManager();
        Map<Long, String> result = new HashMap<>();
        TypedQuery<ShareLog> q = entityManager.createQuery("select s from ShareLog s where s.reciever.username = :username and s.downloadDateTime = null ", ShareLog.class);
        q.setParameter("username", username);
        List<ShareLog> qResultList = q.getResultList();

        qResultList.forEach(item -> {
            result.put(item.getId(), item.getFileName());
        });

        entityManager.close();
        return result;
    }



}
