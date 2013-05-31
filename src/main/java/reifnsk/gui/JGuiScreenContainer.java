package reifnsk.gui;

final class JGuiScreenContainer extends JGuiContainer {
	private final JGuiScreen screen;

	JGuiScreenContainer(JGuiScreen var1) {
		this.screen = var1;
	}

	public JGuiScreen getJGuiScreen() {
		return this.screen;
	}
}
