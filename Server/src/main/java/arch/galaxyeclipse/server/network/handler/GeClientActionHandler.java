package arch.galaxyeclipse.server.network.handler;

import arch.galaxyeclipse.server.data.GeDynamicObjectsHolder.GeLocationObjectsHolder;
import arch.galaxyeclipse.server.data.GePlayerInfoHolder;
import arch.galaxyeclipse.server.data.model.GeShipConfig;
import arch.galaxyeclipse.shared.common.GeLogUtils;
import arch.galaxyeclipse.shared.common.GeMathUtils;
import arch.galaxyeclipse.shared.protocol.GeProtocol.GeClientActionPacket;
import arch.galaxyeclipse.shared.protocol.GeProtocol.GeClientActionPacket.ClientActionType;
import arch.galaxyeclipse.shared.protocol.GeProtocol.GeLocationInfoPacket.GeLocationObjectPacket;
import arch.galaxyeclipse.shared.protocol.GeProtocol.GePacket;
import arch.galaxyeclipse.shared.protocol.GeProtocol.GeShipStateResponse;
import arch.galaxyeclipse.shared.thread.GeTaskRunnablePair;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentLinkedQueue;

import static arch.galaxyeclipse.shared.GeConstants.*;

/**
 *
 */
@Slf4j
class GeClientActionHandler extends GePacketHandlerDecorator {

    private GePlayerInfoHolder playerInfoHolder;

    private MoveHandler moveHandler;
    private RotationHandler rotationHandler;

    public GeClientActionHandler(IGeChannelAwarePacketHandler decoratedPacketHandler) {
        super(decoratedPacketHandler);

        playerInfoHolder = getServerChannelHandler().getPlayerInfoHolder();

        moveHandler = new MoveHandler(playerInfoHolder);
        rotationHandler = new RotationHandler(playerInfoHolder);
    }

    @Override
    protected boolean handleImp(GePacket packet) {
        switch (packet.getType()) {
            case CLIENT_ACTION:
                GeClientActionPacket clientAction = packet.getClientAction();
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

    private static class RotationHandler extends GeTaskRunnablePair<Runnable> {

        public static final int MAX_ANGLE = 360;

        private ClientActionType rotationType;
        private GeShipConfig shipConfig;
        private GeShipStateResponse.Builder ssrBuilder;
        private GeLocationObjectPacket.Builder lopBuilder;
        private RotatingRunnable rotatingRunnable;
        private PostRotatingRunnable postRotatingRunnable;

        private RotationHandler(GePlayerInfoHolder playerInfoHolder) {
            super(DELAY_OBJECT_ROTATION_UPDATE, null, true, true);

            shipConfig = playerInfoHolder.getShipConfig();
            ssrBuilder = playerInfoHolder.getSsrBuilder();
            lopBuilder = playerInfoHolder.getLopBuilder();
            rotatingRunnable = new RotatingRunnable();
            postRotatingRunnable = new PostRotatingRunnable();
        }

        public void setRotationType(ClientActionType rotationType) {
            this.rotationType = rotationType;
            if (GeClientActionHandler.log.isTraceEnabled()) {
                GeClientActionHandler.log.trace("Setting rotation type " + rotationType);
            }

            switch (rotationType) {
                case ROTATE_LEFT_DOWN:
                case ROTATE_RIGHT_DOWN:
                    if (GeClientActionHandler.log.isTraceEnabled()) {
                        GeClientActionHandler.log.trace("Starting " + GeLogUtils.getObjectInfo(this));
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

                if (GeClientActionHandler.log.isTraceEnabled()) {
                    GeClientActionHandler.log.trace(rotationType.toString());
                    GeClientActionHandler.log.trace("Rotation speed " + currentRotationSpeed
                            + " object " + lopBuilder.getObjectId());
                    GeClientActionHandler.log.trace("Rotation angle " + currentRotationAngle
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

                if (GeClientActionHandler.log.isTraceEnabled()) {
                    GeClientActionHandler.log.trace(rotationType.toString());
                    GeClientActionHandler.log.trace("Rotation speed " + currentRotationSpeed
                            + " object " + lopBuilder.getObjectId());
                    GeClientActionHandler.log.trace("Rotation angle " + currentRotationAngle
                            + " object " + lopBuilder.getObjectId());
                }
            }
        }
    }

    private static class MoveHandler {

        private GeShipConfig shipConfig;
        private GeShipStateResponse.Builder ssrBuilder;
        private GeLocationObjectPacket.Builder lopBuilder;
        private SpeedTask speedTask;
        private PositionTask positionTask;
        private GeLocationObjectsHolder locationObjectsHolder;
        private ConcurrentLinkedQueue<ClientActionType> moveActions;

        private MoveHandler(GePlayerInfoHolder playerInfoHolder) {
            shipConfig = playerInfoHolder.getShipConfig();
            ssrBuilder = playerInfoHolder.getSsrBuilder();
            lopBuilder = playerInfoHolder.getLopBuilder();
            locationObjectsHolder = playerInfoHolder.getLocationObjectsHolder();

            speedTask = new SpeedTask();
            positionTask = new PositionTask();
            moveActions = new ConcurrentLinkedQueue<>();
        }

        public void setMoveType(ClientActionType moveType) {
            if (GeClientActionHandler.log.isTraceEnabled()) {
                GeClientActionHandler.log.trace("Setting move type " + moveType);
            }
            moveActions.add(moveType);

            switch (moveType) {
                case MOVE_DOWN:
                case STOP_DOWN:
                    if (!positionTask.isAlive()) {
                        if (GeClientActionHandler.log.isTraceEnabled()) {
                            GeClientActionHandler.log.trace("Starting " + GeLogUtils.getObjectInfo(positionTask));
                        }
                        positionTask.start();
                    }
                    if (!speedTask.isAlive()) {
                        if (GeClientActionHandler.log.isTraceEnabled()) {
                            GeClientActionHandler.log.trace("Starting " + GeLogUtils.getObjectInfo(speedTask));
                        }
                        speedTask.start();
                    }
                    break;
            }
        }

        public void stop() {
            if (GeClientActionHandler.log.isTraceEnabled()) {
                GeClientActionHandler.log.trace("Stopping " + GeLogUtils.getObjectInfo(speedTask));
                GeClientActionHandler.log.trace("Stopping " + GeLogUtils.getObjectInfo(positionTask));
            }
            speedTask.stop();
            positionTask.stop();
        }

        private class SpeedTask extends GeTaskRunnablePair<Runnable> implements Runnable {

            private ClientActionType moveType;

            private SpeedTask() {
                super(DELAY_OBJECT_SPEED_UPDATE, null, true, true);
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
                    if (GeClientActionHandler.log.isTraceEnabled()) {
                        GeClientActionHandler.log.trace("Stopping " + GeLogUtils.getObjectInfo(this));
                        GeClientActionHandler.log.trace("Stopping " + GeLogUtils.getObjectInfo(positionTask));
                    }
                    speedTask.stop();
                    positionTask.stop();
                } else if (currentMoveSpeed > maxMoveSpeed) {
                    currentMoveSpeed = maxMoveSpeed;
                }
                if (GeClientActionHandler.log.isTraceEnabled()) {
                    GeClientActionHandler.log.trace("Move speed " + currentMoveSpeed
                            + " object " + lopBuilder.getObjectId());
                }
                ssrBuilder.setMoveSpeed(currentMoveSpeed);
            }
        }

        private class PositionTask extends GeTaskRunnablePair<Runnable> implements Runnable {

            private PositionTask() {
                super(DELAY_OBJECT_POSITION_UPDATE, null, true, true);
                setRunnable(this);
            }

            @Override
            public void run() {
                float currentMoveSpeed = ssrBuilder.getMoveSpeed();
                float rotationAngle = lopBuilder.getRotationAngle();
                float positionX = lopBuilder.getPositionX();
                float positionY = lopBuilder.getPositionY();
                float xDiff = currentMoveSpeed * GeMathUtils.sinDeg(rotationAngle);
                float yDiff = currentMoveSpeed * GeMathUtils.cosDeg(rotationAngle);

                positionX += xDiff;
                // -= here because of client rendering
                positionY -= yDiff;

                locationObjectsHolder.updateLopBuilderX(lopBuilder, positionX);
                locationObjectsHolder.updateLopBuilderY(lopBuilder, positionY);
                ssrBuilder.setPositionX(positionX);
                ssrBuilder.setPositionY(positionY);

                if (GeClientActionHandler.log.isTraceEnabled()) {
                    GeClientActionHandler.log.trace("Position x " + positionX
                            + " object " + lopBuilder.getObjectId());
                    GeClientActionHandler.log.trace("Position y " + positionY
                            + " object " + lopBuilder.getObjectId());
                }
            }
        }
    }
}
