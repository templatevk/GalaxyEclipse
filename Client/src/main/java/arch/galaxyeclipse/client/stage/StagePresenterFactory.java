package arch.galaxyeclipse.client.stage;

/**
 *
 */
public class StagePresenterFactory {
    public StagePresenterFactory() {

    }

    public IStagePresenter createStagePresenter(StagePresenterType stagePresenterType) {
        switch (stagePresenterType) {
            case MAIN_MENU_PRESENTER:
                return new MainMenuPresenter();
        }
        return null;
    }

    public enum StagePresenterType {
        MAIN_MENU_PRESENTER;
    }
}
