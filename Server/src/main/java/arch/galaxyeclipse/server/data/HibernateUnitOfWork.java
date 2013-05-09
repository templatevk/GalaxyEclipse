package arch.galaxyeclipse.server.data;

import arch.galaxyeclipse.shared.context.ContextHolder;
import arch.galaxyeclipse.shared.util.LogUtils;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 *
 */
@Slf4j
public abstract class HibernateUnitOfWork<T> {
    @Setter(AccessLevel.PROTECTED)
    private T result;

    public HibernateUnitOfWork() {

    }

    protected abstract void doWork(Session session);

    public T execute() {
        Session session = SessionFactoryHolder.sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        try {
            doWork(session);
            tx.commit();
        } catch (Exception e) {
            if (!tx.wasRolledBack()) {
                try {
                    tx.rollback();
                } catch (Exception ex) {
                    HibernateUnitOfWork.log.error("Error rolling back the transaction", ex);
                }
            }
            HibernateUnitOfWork.log.error("Error on " + LogUtils.getObjectInfo(this), e);
        } finally {
            session.close();
        }

        return result;
    }

    private static class SessionFactoryHolder {
        private static SessionFactory sessionFactory =
                ContextHolder.getBean(SessionFactory.class);
    }
}
