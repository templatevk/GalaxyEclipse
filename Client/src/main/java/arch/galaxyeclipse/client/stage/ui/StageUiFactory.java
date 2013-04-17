package arch.galaxyeclipse.client.stage.ui;

/**
 *
 */
public class StageUiFactory {
    private StageUiFactory() {
    }

    public static IButtonBuilder createButtonBuilder() {
        return new DefaultButtonBuilder();
    }

    public static  ITextFieldBuilder createTextFieldBuilder() {
        return new DefaultTextFieldBuilder();
    }
}
