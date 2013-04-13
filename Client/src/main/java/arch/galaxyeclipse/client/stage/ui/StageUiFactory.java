package arch.galaxyeclipse.client.stage.ui;

/**
 *
 */
class StageUiFactory implements IStageUiFactory {
    StageUiFactory() {
    }

    @Override
    public IButtonBuilder createButtonBuilder() {
        return new DefaultButtonBuilder();
    }

    @Override
    public ITextFieldBuilder createTextFieldBuilder() {
        return new DefaultTextFieldBuilder();
    }
}
