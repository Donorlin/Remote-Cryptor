package database.dao;

import crypto.CryptoUtils;
import database.EntityManagerFactorySingleton;
import database.entity.Comment;
import database.entity.ShareLog;
import database.entity.User;

import javax.persistence.Entity;
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


    public ShareService() {
        emf = EntityManagerFactorySingleton.getEMF();
    }

    public ShareService(String shareDirectory) {
        emf = EntityManagerFactorySingleton.getEMF();
        this.shareDirectory = shareDirectory;
    }

    public boolean shareFile(String originatorUsername, String receiverUsername, File fileToShare, String comment) {
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

            if (comment != null && !comment.isEmpty()) {
                Comment initialComment = new Comment();
                initialComment.setComment(comment);
                initialComment.setAuthor(originator);
                initialComment.setShareLog(shareLog);
                entityManager.persist(initialComment);
            }

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

    public List<ShareLog> getSharedFilesListByUsername(String username) {
        EntityManager entityManager = emf.createEntityManager();
        TypedQuery<ShareLog> q = entityManager.createQuery("select s from ShareLog s where s.receiver.username = :username", ShareLog.class);
        q.setParameter("username", username);
        List<ShareLog> qResultList = q.getResultList();
        entityManager.close();
        return qResultList;
    }

    public List<ShareLog> getSharedFilesBySearch(String username, String searchString) {
        EntityManager entityManager = emf.createEntityManager();
        TypedQuery<ShareLog> q = entityManager.createQuery("select s from ShareLog s join Comment c on c.shareLog.id = s.id where s.receiver.username = :username and (s.fileName like :searchString or c.comment like :searchString or c.author.username like :searchString or s.originator.username like :searchString)",ShareLog.class);
        q.setParameter("searchString", "%" + searchString + "%");
        q.setParameter("username", username);
        List<ShareLog> qResultList = q.getResultList();
        entityManager.close();
        return qResultList;
    }

    public ShareLog getShareLogByFileId(String fileId) {
        EntityManager entityManager = emf.createEntityManager();
        return entityManager.find(ShareLog.class, Long.parseLong(fileId));
    }

}
