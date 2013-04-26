package arch.galaxyeclipse.client.stage;

import arch.galaxyeclipse.client.window.*;
import arch.galaxyeclipse.shared.context.*;
import com.badlogic.gdx.*;

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
//                return createLoadableStageProvider(new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//                                Thread.sleep(4000);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//                            }
//                            ContextHolder.getBean(IClientWindow.class)
//                                    .setStageProvider(new MainMenuPresenter());
//                        }
//                    });
        }
        return null;
    }

//    private static IStageProvider createLoadableStageProvider(final Runnable loadRunnable) {
//        Gdx.app.postRunnable(loadRunnable);
//        return new LoadingStageProvider();
//    }

    public enum StagePresenterType {
        MAIN_MENU;
    }
}
