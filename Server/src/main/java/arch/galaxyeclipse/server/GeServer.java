package arch.galaxyeclipse.server;

import arch.galaxyeclipse.server.data.GeDictionaryTypesMapperHelper;
import arch.galaxyeclipse.server.data.GeHibernateUnitOfWork;
import arch.galaxyeclipse.server.network.IGeServerNetworkManager;
import arch.galaxyeclipse.shared.GeEnvType;
import arch.galaxyeclipse.shared.context.GeContextHolder;
import arch.galaxyeclipse.shared.thread.GeExecutor;
import arch.galaxyeclipse.shared.types.GeDictionaryTypesMapper;
import arch.galaxyeclipse.shared.types.GeLocationObjectBehaviorTypesMapperType;
import arch.galaxyeclipse.shared.types.GeLocationObjectTypesMapperType;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.slf4j.LoggerFactory;

@Slf4j
public class GeServer {
    private IGeServerNetworkManager serverNetworkManager;
    private GeDictionaryTypesMapper dictionaryTypesMapper;

	public GeServer() {

	}

    public static void main(String[] args) throws Exception {
        GeServer server = new GeServer();
        try {
            server.start();
        } catch (Exception e) {
            GeServer.log.error("Error during server startup", e);
        }
    }

    public void start() {
        preconfigure();
        hibernateAllPlayers();
        serverNetworkManager.startServer();
    }

    public void stop() {
        serverNetworkManager.stopServer();
        GeContextHolder.getBean(GeExecutor.class).shutdownNow();
        hibernateAllPlayers();
        GeContextHolder.INSTANCE.close();
    }

    private void preconfigure() {
        StatusPrinter.print((LoggerContext) LoggerFactory.getILoggerFactory());
        GeContextHolder.INSTANCE.getClass();

        serverNetworkManager = GeContextHolder.getBean(IGeServerNetworkManager.class);
        dictionaryTypesMapper = GeContextHolder.getBean(GeDictionaryTypesMapper.class);

        GeDictionaryTypesMapperHelper.fillAll(dictionaryTypesMapper);

        if (GeServer.log.isInfoEnabled()) {
            GeServer.log.info("Server initialized for " + GeEnvType.CURRENT.toString() + " environment");
        }
    }

    private void hibernateAllPlayers() {
        new GeHibernateUnitOfWork() {
            @Override
            protected void doWork(Session session) {
                // Stop the ships
                session.createQuery("update GeShipState ss set " +
                        "ss.shipStateMoveSpeed = 0, ss.shipStateRotationSpeed = 0")
                        .executeUpdate();

                // Indicate players are offline
                int idIgnored = dictionaryTypesMapper.getIdByLocationObjectBehaviorType(
                        GeLocationObjectBehaviorTypesMapperType.IGNORED);
                int idPlayer = dictionaryTypesMapper.getIdByLocationObjectType(
                        GeLocationObjectTypesMapperType.PLAYER);
                session.createQuery("update GeLocationObject lo set " +
                        "lo.locationObjectBehaviorTypeId = :idIgnored " +
                        "where lo.locationObjectTypeId = :idPlayer")
                        .setParameter("idIgnored", idIgnored)
                        .setParameter("idPlayer", idPlayer)
                        .executeUpdate();
            }
        }.execute();

        if (GeServer.log.isInfoEnabled()) {
            GeServer.log.info("All players have been hibernated");
        }
    }
}