package arch.galaxyeclipse.client.ui.actor;

import arch.galaxyeclipse.shared.protocol.GeProtocol.LocationInfoPacket.LocationObjectPacket;

/**
 * Responsible for retreiving pictures from the file system and creating Actors.
 */
public interface IActorFactory {

    LocationObjectActor createLocationObjectActor(LocationObjectPacket locationObjectPacket);

    BackgroundActor createBackgroundActor(int locationId);
}
