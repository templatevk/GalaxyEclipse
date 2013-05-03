package arch.galaxyeclipse.server.network.handler;

import arch.galaxyeclipse.server.data.*;
import arch.galaxyeclipse.server.data.model.*;
import arch.galaxyeclipse.shared.protocol.*;
import arch.galaxyeclipse.shared.thread.*;
import org.hibernate.*;

import lombok.*;
import lombok.extern.slf4j.*;

import static arch.galaxyeclipse.shared.SharedInfo.*;

/**
 *
 */
@Slf4j
class ClientActionHandler extends PacketHandlerDecorator {
    private PlayerInfoHolder playerInfoHolder;
    private ShipConfig shipConfig;
    private ShipState shipState;
    private LocationObject locationObject;

    private RotatingTask rotatingTask;
    private PostRotatingTask postRotatingTask;

    private boolean rotating;

    public ClientActionHandler(IChannelAwarePacketHandler decoratedPacketHandler) {
        super(decoratedPacketHandler);

        playerInfoHolder = getServerChannelHandler().getPlayerInfoHolder();
        shipConfig = playerInfoHolder.getShipConfig();
        shipState = playerInfoHolder.getShipState();
        locationObject = playerInfoHolder.getLocationObject();

        rotatingTask = new RotatingTask(playerInfoHolder);
        postRotatingTask = new PostRotatingTask(playerInfoHolder);

        rotating = false;
    }

    @Override
    protected boolean handleImp(GeProtocol.Packet packet) {
        switch (packet.getType()) {
            case CLIENT_ACTION:
                GeProtocol.ClientAction clientAction = packet.getClientAction();
                switch (clientAction.getType()) {
                    case ROTATE_LEFT:
                    case ROTATE_RIGHT:
                        processRotation(clientAction);
                        break;
                    case MOVE:
                        break;
                    case STOP:
                        break;
                    case OBJECT_CLICK:
                        break;
                    case LOOT_PICK:
                        break;
                    case ATTACK:
                        break;
                    case ROCKET_SHOOT:
                        break;
                }
                return true;
        }
        return false;
    }

    private void processRotation(GeProtocol.ClientAction clientAction) {
        rotatingTask.setRotationType(clientAction.getType());
        postRotatingTask.setRotationType(clientAction.getType());

        if (!rotating) {
            rotatingTask.start();
            postRotatingTask.stop();
        } else {
            rotatingTask.stop();
            postRotatingTask.start();
        }

        rotating = !rotating;
    }

    @Data
    private static class RotatingTask extends TaskRunnablePair<RotatingTask>
            implements Runnable {
        public static final int MAX_ANGLE = 360;

        private GeProtocol.ClientAction.ClientActionType rotationType;
        private ShipConfig shipConfig;
        private ShipState shipState;
        private LocationObject locationObject;

        private RotatingTask(final PlayerInfoHolder playerInfoHolder) {
            super(CLIENT_ACTION_ROTATION_DELAY_MILLISECONDS, null, true, true);
            setRunnable(this);

            shipConfig = playerInfoHolder.getShipConfig();
            shipState = playerInfoHolder.getShipState();
            locationObject = playerInfoHolder.getLocationObject();
        }

        @Override
        public void run() {
            float currentRotationSpeed = shipState.getShipStateRotationSpeed();

            float rotationAcceleration = shipConfig.getShipConfigRotationAcceleration();
            float maxRotationSpeed = shipConfig.getShipConfigRotationMaxSpeed();

            switch (rotationType) {
                case ROTATE_RIGHT:
                    currentRotationSpeed -= rotationAcceleration;
                    break;
                case ROTATE_LEFT:
                    currentRotationSpeed += rotationAcceleration;
                    break;
            }

            if (Math.abs(currentRotationSpeed) != maxRotationSpeed) {
                if (Math.abs(currentRotationSpeed) > maxRotationSpeed) {
                    currentRotationSpeed = Math.signum(currentRotationSpeed) * maxRotationSpeed;
                }
            }
            shipState.setShipStateRotationSpeed(currentRotationSpeed);

            float currentRotationAngle = locationObject.getRotationAngle();
            currentRotationAngle += currentRotationSpeed;
            if (currentRotationAngle > MAX_ANGLE) {
                currentRotationAngle -= MAX_ANGLE;
            } else if (currentRotationAngle < 0) {
                currentRotationAngle = MAX_ANGLE + currentRotationAngle;
            }

            locationObject.setRotationAngle(currentRotationAngle);

            if (log.isDebugEnabled()) {
                log.debug(rotationType.toString());
                log.debug("Rotation speed " + currentRotationSpeed);
                log.debug("Rotation angle " + currentRotationAngle);
            }

            new UnitOfWork() {
                @Override
                protected void doWork(Session session) {
                    session.merge(shipState);
                    session.merge(locationObject);
                }
            }.execute();
        }
    }

    @Data
    private static class PostRotatingTask extends TaskRunnablePair implements Runnable {
        public static final int MAX_ANGLE = 360;

        private GeProtocol.ClientAction.ClientActionType rotationType;
        private ShipConfig shipConfig;
        private ShipState shipState;
        private LocationObject locationObject;

        private PostRotatingTask(final PlayerInfoHolder playerInfoHolder) {
            super(CLIENT_ACTION_ROTATION_DELAY_MILLISECONDS, null, true, true);
            setRunnable(this);

            shipConfig = playerInfoHolder.getShipConfig();
            shipState = playerInfoHolder.getShipState();
            locationObject = playerInfoHolder.getLocationObject();
        }

        @Override
        public void run() {
            float currentRotationSpeed = shipState.getShipStateRotationSpeed();
            float rotationAcceleration = shipConfig.getShipConfigRotationAcceleration();

            switch (rotationType) {
                case ROTATE_RIGHT:
                    currentRotationSpeed += rotationAcceleration;
                    if (currentRotationSpeed > 0) {
                        currentRotationSpeed = 0;
                        stop();
                    }
                    break;
                case ROTATE_LEFT:
                    currentRotationSpeed -= rotationAcceleration;
                    if (currentRotationSpeed < 0) {
                        currentRotationSpeed = 0;
                        stop();
                    }
                    break;
            }

            shipState.setShipStateRotationSpeed(currentRotationSpeed);

            float currentRotationAngle = locationObject.getRotationAngle();
            currentRotationAngle += currentRotationSpeed;
            if (currentRotationAngle > MAX_ANGLE) {
                currentRotationAngle -= MAX_ANGLE;
            } else if (currentRotationAngle < 0) {
                currentRotationAngle = MAX_ANGLE + currentRotationAngle;
            }

            locationObject.setRotationAngle(currentRotationAngle);

            if (log.isDebugEnabled()) {
                log.debug(rotationType.toString());
                log.debug("Rotation speed " + currentRotationSpeed);
                log.debug("Rotation angle " + currentRotationAngle);
            }

            new UnitOfWork() {
                @Override
                protected void doWork(Session session) {
                    session.merge(shipState);
                    session.merge(locationObject);
                }
            }.execute();
        }
    }
}
