package dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import utility.HibernateUtil;

public class AbstractDAO<T> {
    private SessionFactory factory = HibernateUtil.getSessionFactory();

    /**
     * Gets a current session from the hibernate factory
     * @return
     */
    protected Session getSession(){
        return factory.openSession();
    }

    /**
     * Generic method for finding by primary key.
     * @param id  - Primary key value in integer
     * @param myClass - Type of Class which will be loaded
     * @return T representing instance of the loaded hibernate class instance
     */
    public <T>  T findById(Integer id, Class myClass) {
        return (T) getSession().load(myClass, id);
    }

    /**
     * Inserts record into database
     * @param t - Instance of the Hibernate object which needs to be persisted in the db
     * @return - Key value of the newly inserted record
     */
    public Long persist(T t) {
        return (Long) getSession().save(t);
    }

    /**
     * Updates the database for provided generic type
     * @param t Generic type for the hibernate object
     */
    public void update(T t) {
        getSession().update(t);
        getSession().flush();
    }

    /**
     * Deletes the entity from the database for the provided generic type
     * @param t  Generic type for the hibernate object
     */
    public void delete(T t) {
        getSession().delete(t);
        getSession().flush();
    }
}
