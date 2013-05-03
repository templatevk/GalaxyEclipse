package arch.galaxyeclipse.client.ui.provider;

/**
 *
 */
public class StageProviderFactory {
    StageProviderFactory() {

    }

    public static IStageProvider createStageProvider(StagePresenterType stagePresenterType) {
        switch (stagePresenterType) {
            case MAIN_MENU:
                return new MainMenuPresenter();
        }
        return null;
    }

    public enum StagePresenterType {
        MAIN_MENU;
    }
}
