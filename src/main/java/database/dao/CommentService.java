package database.dao;

import database.EntityManagerFactorySingleton;
import database.entity.Comment;
import database.entity.ShareLog;
import database.entity.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class CommentService {

    private EntityManagerFactory emf;

    public CommentService() {
        emf = EntityManagerFactorySingleton.getEMF();
    }

    public void addComment(String fileId, String author, String comment) {
        EntityManager entityManager = emf.createEntityManager();
        ShareService shareService = new ShareService();
        UserService userService = new UserService();

        ShareLog shareLogDao = shareService.getShareLogByFileId(fileId);
        User authorDao = userService.getUserByUsername(author);

        System.out.println("SL dao " + shareLogDao);
        System.out.println("US dao " + authorDao);

        Comment commentDao = new Comment();
        commentDao.setShareLog(shareLogDao);
        commentDao.setAuthor(authorDao);
        commentDao.setComment(comment);

        entityManager.getTransaction().begin();
        entityManager.persist(commentDao);
        entityManager.getTransaction().commit();
        entityManager.close();
    }


}
