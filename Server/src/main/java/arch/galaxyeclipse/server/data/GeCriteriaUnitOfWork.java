package arch.galaxyeclipse.server.data;

import org.hibernate.Session;

import java.util.List;

/**
 *
 */
public class GeCriteriaUnitOfWork<T> extends GeHibernateUnitOfWork<List<T>> {

    private Class<T> clazz;

    public GeCriteriaUnitOfWork(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    protected void doWork(Session session) {
        setResult(session.createCriteria(clazz).list());
    }
}