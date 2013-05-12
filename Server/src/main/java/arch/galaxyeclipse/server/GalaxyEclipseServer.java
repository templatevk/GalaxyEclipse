package arch.galaxyeclipse.server;

import arch.galaxyeclipse.server.data.DictionaryTypesMapperHelper;
import arch.galaxyeclipse.server.data.HibernateUnitOfWork;
import arch.galaxyeclipse.server.network.IServerNetworkManager;
import arch.galaxyeclipse.shared.EnvType;
import arch.galaxyeclipse.shared.GeConstants;
import arch.galaxyeclipse.shared.context.ContextHolder;
import arch.galaxyeclipse.shared.thread.GeExecutor;
import arch.galaxyeclipse.shared.types.DictionaryTypesMapper;
import arch.galaxyeclipse.shared.types.LocationObjectBehaviorTypesMapperType;
import arch.galaxyeclipse.shared.types.LocationObjectTypesMapperType;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.slf4j.LoggerFactory;

@Slf4j
public class GalaxyEclipseServer {
    private IServerNetworkManager serverNetworkManager;
    private DictionaryTypesMapper dictionaryTypesMapper;

	public GalaxyEclipseServer() {

	}

    public static void main(String[] args) throws Exception {
        GalaxyEclipseServer server = new GalaxyEclipseServer();
        try {
            server.start();
        } catch (Exception e) {
            log.error("Error during server startup", e);
        }
    }

    public void start() {
        preconfigure();
        hibernateAllPlayers();
        serverNetworkManager.startServer(GeConstants.HOST, GeConstants.PORT);
    }

    public void stop() {
        serverNetworkManager.stopServer();
        ContextHolder.getBean(GeExecutor.class).shutdownNow();
        hibernateAllPlayers();
        ContextHolder.INSTANCE.close();
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
                int idPlayer = dictionaryTypesMapper.getIdByLocationObjectType(
                        LocationObjectTypesMapperType.PLAYER);
                session.createQuery("update LocationObject lo set " +
                        "lo.locationObjectBehaviorTypeId = :idIgnored " +
                        "where lo.locationObjectTypeId = :idPlayer")
                        .setParameter("idIgnored", idIgnored)
                        .setParameter("idPlayer", idPlayer)
                        .executeUpdate();
            }
        }.execute();

        if (log.isInfoEnabled()) {
            log.info("All players have been hibernated");
        }
    }
}