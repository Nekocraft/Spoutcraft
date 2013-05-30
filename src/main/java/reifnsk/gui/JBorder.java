package reifnsk.gui;

import java.awt.Insets;

public abstract class JBorder {
	public abstract Insets getBorderInsets(JGuiComponent var1);

	public abstract Insets getBorderInsets(JGuiComponent var1, Insets var2);

	public abstract void drawBorder(JGuiComponent var1, float var2);
}
