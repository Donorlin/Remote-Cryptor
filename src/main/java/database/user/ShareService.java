package database.user;

import crypto.CryptoUtils;
import database.EntityManagerFactorySingleton;
import database.entity.ShareLog;
import database.entity.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShareService {

    private EntityManagerFactory emf;

    private String shareDirectory;

    public ShareService(String shareDirectory) {
        emf = EntityManagerFactorySingleton.getEMF();
        this.shareDirectory = shareDirectory;
    }

    public boolean shareFile(String originatorUsername, String receiverUsername, File fileToShare) {
        EntityManager entityManager = emf.createEntityManager();
        UserService userService = new UserService();
        User originator = userService.getUserByUsername(originatorUsername);
        User receiver = userService.getUserByUsername(receiverUsername);
        String fileName = fileToShare.getName();

        File shareDir = new File(shareDirectory);
        if (!shareDir.exists()) {
            shareDir.mkdir();
        }

        try {
            File encryptedFile = File.createTempFile(originatorUsername, receiverUsername, shareDir);

            CryptoUtils.encrypt(fileToShare, encryptedFile, receiverUsername);

            ShareLog shareLog = new ShareLog();
            shareLog.setOriginator(originator);
            shareLog.setReceiver(receiver);
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

    public File decryptSharedFile(String receiverUsername, Long fileId) throws Exception {
        EntityManager entityManager = emf.createEntityManager();
        ShareLog shareLog = entityManager.find(ShareLog.class, fileId);

        File sharedFile = new File(shareLog.getPathToFile());
        File decryptedFile = new File(shareDirectory + File.separator + shareLog.getFileName());
        CryptoUtils.decrypt(sharedFile, decryptedFile, receiverUsername);

        shareLog.setDownloadDateTime(new Date());

        entityManager.getTransaction().begin();
        entityManager.merge(shareLog);
        entityManager.getTransaction().commit();
        entityManager.close();
        sharedFile.delete();
        return decryptedFile;
    }

    public File getSharedFile(Long fileId) {
        EntityManager entityManager = emf.createEntityManager();
        ShareLog shareLog = entityManager.find(ShareLog.class, fileId);

        File sharedFile = new File(shareLog.getPathToFile());
        shareLog.setDownloadDateTime(new Date());

        entityManager.getTransaction().begin();
        entityManager.merge(shareLog);
        entityManager.getTransaction().commit();
        entityManager.close();
        return sharedFile;
    }

    // Long - id of Sharelog
    // String - fileName of Sharelog
    public Map<Long, String> getSharedFilesListByUsername(String username) {
        EntityManager entityManager = emf.createEntityManager();
        Map<Long, String> result = new HashMap<>();
        TypedQuery<ShareLog> q = entityManager.createQuery("select s from ShareLog s where s.receiver.username = :username and s.downloadDateTime = null ", ShareLog.class);
        q.setParameter("username", username);
        List<ShareLog> qResultList = q.getResultList();

        qResultList.forEach(item -> {
            result.put(item.getId(), item.getFileName());
        });

        entityManager.close();
        return result;
    }


}
