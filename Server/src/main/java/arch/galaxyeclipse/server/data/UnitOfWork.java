package arch.galaxyeclipse.server.data;

import arch.galaxyeclipse.shared.context.*;
import arch.galaxyeclipse.shared.util.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.hibernate.*;

/**
 *
 */
@Slf4j
public abstract class UnitOfWork<T> {
    @Setter(AccessLevel.PROTECTED)
    private T result;

    public UnitOfWork() {

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
                tx.rollback();
            }
            UnitOfWork.log.error("Error on " + LogUtils.getObjectInfo(this), e);
        } finally {
            session.close();
        }

        return result;
    }

    private static class SessionFactoryHolder {
        private static SessionFactory sessionFactory =
                ContextHolder.INSTANCE.getBean(SessionFactory.class);
    }
}
