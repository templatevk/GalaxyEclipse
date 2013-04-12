package arch.galaxyeclipse.server.data;

import org.hibernate.*;

import java.util.*;

/**
 *
 */
public class CriteriaDataWorker<T> extends DataWorker<List<T>> {
    private Class<T> clazz;

    public CriteriaDataWorker(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    protected void doWork(Session session) {
        setResult(session.createCriteria(clazz).list());
    }
}
