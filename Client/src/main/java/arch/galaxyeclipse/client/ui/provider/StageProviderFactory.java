package arch.galaxyeclipse.client.ui.provider;

/**
 *
 */
public class StageProviderFactory {
    StageProviderFactory() {

    }

    public static IStageProvider createStageProvider(StageProviderType stageProviderType) {
        switch (stageProviderType) {
            case MAIN_MENU:
                return new MainMenuPresenter();
        }
        return null;
    }
}
