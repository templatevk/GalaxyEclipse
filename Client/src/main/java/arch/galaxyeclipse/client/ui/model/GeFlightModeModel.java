package arch.galaxyeclipse.client.ui.model;

import arch.galaxyeclipse.client.ui.actor.GeActor;
import arch.galaxyeclipse.client.ui.actor.GeLocationObjectActor;
import arch.galaxyeclipse.client.ui.actor.IGeActorFactory;
import arch.galaxyeclipse.shared.context.GeContextHolder;
import arch.galaxyeclipse.shared.protocol.GeProtocol.GeLocationInfoPacket.GeLocationObjectPacket;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

/**
 *
 */
public class GeFlightModeModel {

    private
    @Getter
    @Setter
    NavigableSet<GeLocationObjectActor> sortedActors;
    private
    @Getter
    @Setter
    GeActor background;

    private IGeActorFactory actorFactory;
    private Map<Integer, GeLocationObjectActor> actorsCache;

    public GeFlightModeModel() {
        this(null);
    }

    public GeFlightModeModel(GeActor background) {
        this.background = background;
        actorsCache = new HashMap<>();
        sortedActors = new TreeSet<>();
        actorFactory = GeContextHolder.getBean(IGeActorFactory.class);
    }

    public void refreshActors(List<GeLocationObjectPacket> lopList) {
        List<Integer> newActorIds = new ArrayList<>(lopList.size());
        for (GeLocationObjectPacket lop : lopList) {
            int objectId = lop.getObjectId();
            GeLocationObjectActor actor = actorsCache.get(objectId);

            if (actor == null) {
                actor = actorFactory.createLocationObjectActor(lop);
                actorsCache.put(objectId, actor);
            } else {
                actor.setLop(lop);
            }
            newActorIds.add(objectId);
        }
        actorsCache.keySet().retainAll(newActorIds);

        sortedActors = new TreeSet<>(actorsCache.values());
    }
}