package utility;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

public enum DbSession {
    INSTANCE;
    SessionFactory factory = HibernateUtil.getSessionFactory();
    private Session session;
    DbSession(){
        openSession();
    }

    public Session getInstance() {
        if(this.session == null){
            openSession();
        }
        return this.session;
    }

    private void openSession(){
        try {
            this.session = factory.openSession();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void close(){
        if(this.session.isOpen()){
            this.session.close();
        }
    }
}
