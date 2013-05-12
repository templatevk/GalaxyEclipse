package arch.galaxyeclipse.server.network.handler;

import arch.galaxyeclipse.server.data.DynamicObjectsHolder.LocationObjectsHolder;
import arch.galaxyeclipse.server.data.PlayerInfoHolder;
import arch.galaxyeclipse.server.data.model.ShipConfig;
import arch.galaxyeclipse.shared.common.LogUtils;
import arch.galaxyeclipse.shared.common.MathUtils;
import arch.galaxyeclipse.shared.protocol.GeProtocol;
import arch.galaxyeclipse.shared.protocol.GeProtocol.ClientActionPacket;
import arch.galaxyeclipse.shared.protocol.GeProtocol.ClientActionPacket.ClientActionType;
import arch.galaxyeclipse.shared.protocol.GeProtocol.LocationInfoPacket.LocationObjectPacket;
import arch.galaxyeclipse.shared.protocol.GeProtocol.ShipStateResponse;
import arch.galaxyeclipse.shared.thread.TaskRunnablePair;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentLinkedQueue;

import static arch.galaxyeclipse.shared.GeConstants.*;

/**
 *
 */
@Slf4j
class ClientActionHandler extends PacketHandlerDecorator {
    private PlayerInfoHolder playerInfoHolder;

    private MoveHandler moveHandler;
    private RotationHandler rotationHandler;

    public ClientActionHandler(IChannelAwarePacketHandler decoratedPacketHandler) {
        super(decoratedPacketHandler);

        playerInfoHolder = getServerChannelHandler().getPlayerInfoHolder();

        moveHandler = new MoveHandler(playerInfoHolder);
        rotationHandler = new RotationHandler(playerInfoHolder);
    }

    @Override
    protected boolean handleImp(GeProtocol.Packet packet) {
        switch (packet.getType()) {
            case CLIENT_ACTION:
                ClientActionPacket clientAction = packet.getClientAction();
                ClientActionType clientActionType = clientAction.getType();

                switch (clientActionType) {
                    case ROTATE_LEFT_DOWN:
                    case ROTATE_LEFT_UP:
                    case ROTATE_RIGHT_DOWN:
                    case ROTATE_RIGHT_UP:
                        processRotation(clientActionType);
                        break;
                    case MOVE_UP:
                    case MOVE_DOWN:
                    case STOP_UP:
                    case STOP_DOWN:
                        processMoving(clientActionType);
                        processMoving(clientActionType);
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

    @Override
    protected void onChannelClosedImpl() {
        rotationHandler.stop();
        moveHandler.stop();
    }

    private void processMoving(ClientActionType moveType) {
        moveHandler.setMoveType(moveType);
    }

    private void processRotation(ClientActionType moveType) {
        rotationHandler.setRotationType(moveType);
    }

    private static class RotationHandler extends TaskRunnablePair<Runnable> {
        public static final int MAX_ANGLE = 360;

        private ClientActionType rotationType;
        private ShipConfig shipConfig;
        private ShipStateResponse.Builder ssrBuilder;
        private LocationObjectPacket.Builder lopBuilder;
        private RotatingRunnable rotatingRunnable;
        private PostRotatingRunnable postRotatingRunnable;

        private RotationHandler(PlayerInfoHolder playerInfoHolder) {
            super(CLIENT_ACTION_ROTATION_DELAY_MILLISECONDS, null, true, true);

            shipConfig = playerInfoHolder.getShipConfig();
            ssrBuilder = playerInfoHolder.getSsrBuilder();
            lopBuilder = playerInfoHolder.getLopBuilder();
            rotatingRunnable = new RotatingRunnable();
            postRotatingRunnable = new PostRotatingRunnable();
        }

        public void setRotationType(ClientActionType rotationType) {
            this.rotationType = rotationType;
            if (log.isTraceEnabled()) {
                log.trace("Setting rotation type " + rotationType);
            }

            switch (rotationType) {
                case ROTATE_LEFT_DOWN:
                case ROTATE_RIGHT_DOWN:
                    if (log.isTraceEnabled()) {
                        log.trace("Starting " + LogUtils.getObjectInfo(this));
                    }
                    setRunnable(rotatingRunnable);
                    start();
                    break;
                case ROTATE_LEFT_UP:
                case ROTATE_RIGHT_UP:
                    setRunnable(postRotatingRunnable);
                    break;
            }
        }

        private class RotatingRunnable implements Runnable {
            @Override
            public void run() {
                float currentRotationSpeed = ssrBuilder.getRotationSpeed();
                float rotationAcceleration = shipConfig.getShipConfigRotationAcceleration();
                float maxRotationSpeed = shipConfig.getShipConfigRotationMaxSpeed();

                switch (rotationType) {
                    case ROTATE_RIGHT_DOWN:
                        currentRotationSpeed -= rotationAcceleration;
                        break;
                    case ROTATE_LEFT_DOWN:
                        currentRotationSpeed += rotationAcceleration;
                        break;
                }
                if (Math.abs(currentRotationSpeed) > maxRotationSpeed) {
                    currentRotationSpeed = Math.signum(currentRotationSpeed) * maxRotationSpeed;
                }
                ssrBuilder.setRotationSpeed(currentRotationSpeed);

                float currentRotationAngle = lopBuilder.getRotationAngle();
                currentRotationAngle += currentRotationSpeed;
                if (currentRotationAngle > MAX_ANGLE) {
                    currentRotationAngle -= MAX_ANGLE;
                } else if (currentRotationAngle < 0) {
                    currentRotationAngle = MAX_ANGLE + currentRotationAngle;
                }
                lopBuilder.setRotationAngle(currentRotationAngle);

                if (ClientActionHandler.log.isTraceEnabled()) {
                    ClientActionHandler.log.trace(rotationType.toString());
                    ClientActionHandler.log.trace("Rotation speed " + currentRotationSpeed
                            + " object " + lopBuilder.getObjectId());
                    ClientActionHandler.log.trace("Rotation angle " + currentRotationAngle
                            + " object " + lopBuilder.getObjectId());
                }
            }
        }

        private class PostRotatingRunnable implements Runnable {
            @Override
            public void run() {
                float currentRotationSpeed = ssrBuilder.getRotationSpeed();
                float rotationAcceleration = shipConfig.getShipConfigRotationAcceleration();

                switch (rotationType) {
                    case ROTATE_RIGHT_UP:
                        currentRotationSpeed += rotationAcceleration;
                        if (currentRotationSpeed > 0) {
                            currentRotationSpeed = 0;
                            stop();
                        }
                        break;
                    case ROTATE_LEFT_UP:
                        currentRotationSpeed -= rotationAcceleration;
                        if (currentRotationSpeed < 0) {
                            currentRotationSpeed = 0;
                            stop();
                        }
                        break;
                }
                ssrBuilder.setRotationSpeed(currentRotationSpeed);

                float currentRotationAngle = lopBuilder.getRotationAngle();
                currentRotationAngle += currentRotationSpeed;
                if (currentRotationAngle > MAX_ANGLE) {
                    currentRotationAngle -= MAX_ANGLE;
                } else if (currentRotationAngle < 0) {
                    currentRotationAngle = MAX_ANGLE + currentRotationAngle;
                }
                lopBuilder.setRotationAngle(currentRotationAngle);

                if (ClientActionHandler.log.isTraceEnabled()) {
                    ClientActionHandler.log.trace(rotationType.toString());
                    ClientActionHandler.log.trace("Rotation speed " + currentRotationSpeed
                            + " object " + lopBuilder.getObjectId());
                    ClientActionHandler.log.trace("Rotation angle " + currentRotationAngle
                            + " object " + lopBuilder.getObjectId());
                }
            }
        }
    }

    private static class MoveHandler {
        private ShipConfig shipConfig;
        private ShipStateResponse.Builder ssrBuilder;
        private LocationObjectPacket.Builder lopBuilder;
        private SpeedTask speedTask;
        private PositionTask positionTask;
        private LocationObjectsHolder locationObjectsHolder;
        private ConcurrentLinkedQueue<ClientActionType> moveActions;

        private MoveHandler(PlayerInfoHolder playerInfoHolder) {
            shipConfig = playerInfoHolder.getShipConfig();
            ssrBuilder = playerInfoHolder.getSsrBuilder();
            lopBuilder = playerInfoHolder.getLopBuilder();
            locationObjectsHolder = playerInfoHolder.getLocationObjectsHolder();

            speedTask = new SpeedTask();
            positionTask = new PositionTask();
            moveActions = new ConcurrentLinkedQueue<>();
        }

        public void setMoveType(ClientActionType moveType) {
            if (log.isTraceEnabled()) {
                log.trace("Setting move type " + moveType);
            }
            moveActions.add(moveType);

            switch (moveType) {
                case MOVE_DOWN:
                case STOP_DOWN:
                    if (!positionTask.isAlive()) {
                        if (log.isTraceEnabled()) {
                            log.trace("Starting " + LogUtils.getObjectInfo(positionTask));
                        }
                        positionTask.start();
                    }
                    if (!speedTask.isAlive()) {
                        if (log.isTraceEnabled()) {
                            log.trace("Starting " + LogUtils.getObjectInfo(speedTask));
                        }
                        speedTask.start();
                    }
                    break;
            }
        }

        public void stop() {
            if (log.isTraceEnabled()) {
                log.trace("Stopping " + LogUtils.getObjectInfo(speedTask));
                log.trace("Stopping " + LogUtils.getObjectInfo(positionTask));
            }
            speedTask.stop();
            positionTask.stop();
        }

        private class SpeedTask extends TaskRunnablePair<Runnable> implements Runnable {
            private ClientActionType moveType;

            private SpeedTask() {
                super(CLIENT_ACTION_SPEED_DELAY_MILLISECONDS, null, true, true);
                setRunnable(this);
            }

            @Override
            public void run() {
                float currentMoveSpeed = ssrBuilder.getMoveSpeed();
                float moveAcceleration = shipConfig.getShipConfigMoveAcceleration();
                float maxMoveSpeed = shipConfig.getShipConfigMoveMaxSpeed();

                do {
                    moveType = moveActions.size() == 1 ? moveActions.peek() : moveActions.poll();
                    switch (moveType) {
                        case MOVE_DOWN:
                            currentMoveSpeed += moveAcceleration;
                            break;
                        case STOP_DOWN:
                            currentMoveSpeed -= moveAcceleration;
                            break;
                    }
                } while (moveActions.size() > 1);

                if (currentMoveSpeed <= 0) {
                    currentMoveSpeed = 0;
                    if (log.isTraceEnabled()) {
                        log.trace("Stopping " + LogUtils.getObjectInfo(this));
                        log.trace("Stopping " + LogUtils.getObjectInfo(positionTask));
                    }
                    speedTask.stop();
                    positionTask.stop();
                } else if (currentMoveSpeed > maxMoveSpeed) {
                    currentMoveSpeed = maxMoveSpeed;
                }
                if (ClientActionHandler.log.isTraceEnabled()) {
                    ClientActionHandler.log.trace("Move speed " + currentMoveSpeed
                            + " object " + lopBuilder.getObjectId());
                }
                ssrBuilder.setMoveSpeed(currentMoveSpeed);
            }
        }

        private class PositionTask extends TaskRunnablePair<Runnable> implements Runnable {
            private PositionTask() {
                super(CLIENT_ACTION_MOVE_DELAY_MILLISECONDS, null, true, true);
                setRunnable(this);
            }

            @Override
            public void run() {
                float currentMoveSpeed = ssrBuilder.getMoveSpeed();
                float rotationAngle = lopBuilder.getRotationAngle();
                float positionX = lopBuilder.getPositionX();
                float positionY = lopBuilder.getPositionY();
                float xDiff = currentMoveSpeed * MathUtils.sinDeg(rotationAngle);
                float yDiff = currentMoveSpeed * MathUtils.cosDeg(rotationAngle);
                positionX += xDiff * SHIP_MOVE_SPEED_TO_LOCATION_COORDS_COEF;
                // -= here because of client rendering
                positionY -= yDiff * SHIP_MOVE_SPEED_TO_LOCATION_COORDS_COEF;

                locationObjectsHolder.updateLopBuilderX(lopBuilder, positionX);
                locationObjectsHolder.updateLopBuilderY(lopBuilder, positionY);
                ssrBuilder.setPositionX(positionX);
                ssrBuilder.setPositionY(positionY);

                if (ClientActionHandler.log.isTraceEnabled()) {
                    ClientActionHandler.log.trace("Position x " + positionX
                            + " object " + lopBuilder.getObjectId());
                    ClientActionHandler.log.trace("Position y " + positionY
                            + " object " + lopBuilder.getObjectId());
                }
            }
        }
    }
}
