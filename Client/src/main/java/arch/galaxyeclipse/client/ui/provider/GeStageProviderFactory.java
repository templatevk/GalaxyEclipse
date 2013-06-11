package arch.galaxyeclipse.client.ui.provider;

/**
 *
 */
public class GeStageProviderFactory {
    GeStageProviderFactory() {

    }

    public static IGeStageProvider createStageProvider(StageProviderType stagePresenterType) {
        switch (stagePresenterType) {
            case MAIN_MENU:
                return new GeMainMenuPresenter();
        }
        return null;
    }

    public enum StageProviderType {
        MAIN_MENU;
    }
}
