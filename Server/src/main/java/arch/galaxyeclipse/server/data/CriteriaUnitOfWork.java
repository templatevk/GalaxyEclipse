package arch.galaxyeclipse.server.data;

import org.hibernate.*;

import java.util.*;

/**
 *
 */
public class CriteriaUnitOfWork<T> extends HibernateUnitOfWork<List<T>> {
    private Class<T> clazz;

    public CriteriaUnitOfWork(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    protected void doWork(Session session) {
        setResult(session.createCriteria(clazz).list());
    }
}