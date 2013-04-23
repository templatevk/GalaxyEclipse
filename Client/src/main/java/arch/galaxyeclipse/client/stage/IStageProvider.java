package arch.galaxyeclipse.client.stage;

/**
 *
 */
public interface IStageProvider {
    AbstractGameStage getGameStage();

    void detach();
}
