package arch.galaxyeclipse.client.stage;

/**
 *
 */
public class StageProviderFactory {
    StageProviderFactory() {

    }

    public static IStageProvider createStageProvider(StagePresenterType stagePresenterType) {
        switch (stagePresenterType) {
            case MAIN_MENU_PROVIDER:
                return new MainMenuPresenter();
        }
        return null;
    }

    public enum StagePresenterType {
        MAIN_MENU_PROVIDER;
    }
}
