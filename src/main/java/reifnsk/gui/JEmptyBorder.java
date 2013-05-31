package reifnsk.gui;

import java.awt.Insets;

public class JEmptyBorder extends JBorder {
	private int insetTop;
	private int insetLeft;
	private int insetBottom;
	private int insetRight;

	public JEmptyBorder() {}

	public JEmptyBorder(int var1, int var2, int var3, int var4) {
		this.insetTop = var1;
		this.insetLeft = var2;
		this.insetBottom = var3;
		this.insetRight = var4;
	}

	public Insets getBorderInsets(JGuiComponent var1) {
		return new Insets(this.insetTop, this.insetLeft, this.insetBottom, this.insetRight);
	}

	public Insets getBorderInsets(JGuiComponent var1, Insets var2) {
		if (var2 == null) {
			return this.getBorderInsets(var1);
		} else {
			var2.top = this.insetTop;
			var2.left = this.insetLeft;
			var2.bottom = this.insetBottom;
			var2.right = this.insetRight;
			return var2;
		}
	}

	public void drawBorder(JGuiComponent var1, float var2) {}
}
