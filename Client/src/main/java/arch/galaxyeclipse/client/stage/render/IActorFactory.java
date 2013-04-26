package arch.galaxyeclipse.client.stage.render;

import arch.galaxyeclipse.shared.protocol.GeProtocol.LocationInfo.*;

/**
 * Responsible for retreiving pictures from the file system and creating Actors.
 */
public interface IActorFactory {
    GeActor createLocationObjectActor(LocationObject locationObject);

    GeActor createBackgroundActor(int locationId);
}
