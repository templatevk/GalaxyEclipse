package arch.galaxyeclipse.client.stage.render;

import arch.galaxyeclipse.shared.protocol.GeProtocol.LocationInfo.*;

import java.util.*;

/**
 * Responsible for retreiving pictures from the file system and creating Actors.
 */
public interface IActorFactory {
    StageActor createActor(LocationObject locationObject);
}
