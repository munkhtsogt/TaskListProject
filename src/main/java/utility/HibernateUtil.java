package utility;

import model.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            return new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        // Close caches and connection pools
        getSessionFactory().close();
    }


    /*
    public static void main(String[] args) {
        SessionFactory factory = getSessionFactory();
        System.out.println("Session factory object created : " + factory);
        try {
            System.out.println("Session object created : " + factory.openSession());
            User user = new User();
            user.setUsername("zi");
            factory.openSession().save(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    */

}
