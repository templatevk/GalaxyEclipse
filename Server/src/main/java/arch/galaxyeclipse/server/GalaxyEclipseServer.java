package arch.galaxyeclipse.server;

import arch.galaxyeclipse.server.data.*;
import arch.galaxyeclipse.server.network.*;
import arch.galaxyeclipse.shared.*;
import arch.galaxyeclipse.shared.context.*;
import arch.galaxyeclipse.shared.types.*;
import ch.qos.logback.classic.*;
import ch.qos.logback.core.util.*;
import lombok.extern.slf4j.*;
import org.hibernate.Session;
import org.slf4j.*;
import org.springframework.data.redis.connection.jedis.JedisConnection;

@Slf4j
public class GalaxyEclipseServer {
    private IServerNetworkManager serverNetworkManager;
    private DictionaryTypesMapper dictionaryTypesMapper;

	public GalaxyEclipseServer() {

	}

    public static void main(String[] args) throws Exception {
        GalaxyEclipseServer server = new GalaxyEclipseServer();
        try {
            server.preconfigure();
            server.start();
        } catch (Exception e) {
            log.error("Error during server startup", e);
        }
    }

    public void start() {
        preconfigure();
        hibernateAllPlayers();
        clearRedisDb();
        serverNetworkManager.startServer(SharedInfo.HOST, SharedInfo.PORT);
    }

    public void stop() {
        serverNetworkManager.stopServer();
        persistRedisDb();
        hibernateAllPlayers();
        ContextHolder.INSTANCE.close();
    }

    private void clearRedisDb() {
        new JedisUnitOfWork() {
            @Override
            protected void doWork(JedisConnection connection) {
                connection.flushDb();
            }
        }.execute();
    }

    private void persistRedisDb() {

    }

    private void preconfigure() {
        StatusPrinter.print((LoggerContext) LoggerFactory.getILoggerFactory());
        ContextHolder.INSTANCE.getClass();

        serverNetworkManager = ContextHolder.getBean(IServerNetworkManager.class);
        dictionaryTypesMapper = ContextHolder.getBean(DictionaryTypesMapper.class);

        DictionaryTypesMapperHelper.fillAll(dictionaryTypesMapper);

        if (log.isInfoEnabled()) {
            log.info("Server initialized for " + EnvType.CURRENT.toString() + " environment");
        }
    }

    private void hibernateAllPlayers() {
        new HibernateUnitOfWork() {
            @Override
            protected void doWork(Session session) {
                // Stop the ships
                session.createQuery("update ShipState ss set " +
                        "ss.shipStateMoveSpeed = 0, ss.shipStateRotationSpeed = 0")
                        .executeUpdate();

                // Indicate players are offline
                int idIgnored = dictionaryTypesMapper.getIdByLocationObjectBehaviorType(
                        LocationObjectBehaviorTypesMapperType.IGNORED);
                session.createQuery("update LocationObject lo set " +
                        "lo.locationObjectBehaviorTypeId = :idIgnored")
                        .setParameter("idIgnored", idIgnored)
                        .executeUpdate();
            }
        }.execute();

        if (log.isInfoEnabled()) {
            log.info("All players have been hibernated");
        }
    }
}