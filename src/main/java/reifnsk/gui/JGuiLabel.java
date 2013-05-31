package reifnsk.gui;

import java.awt.Insets;

public class JGuiLabel extends JGuiComponent {
	public static final int CENTER = 0;
	public static final int TOP = 1;
	public static final int LEFT = 2;
	public static final int BOTTOM = 3;
	public static final int RIGHT = 4;
	private String text;
	private int verticalAlignment;
	private int horizontalAlignment;

	public JGuiLabel(String var1) {
		this.setText(var1);
	}

	public JGuiLabel(String var1, int var2, int var3, int var4, int var5) {
		super(var2, var3, var4, var5);
		this.setText(var1);
	}

	public JGuiLabel setHorizontalAlignment(int var1) {
		if (this.horizontalAlignment == var1) {
			return this;
		} else if (var1 != 0 && var1 != 2 && var1 != 4) {
			throw new IllegalArgumentException("horizontalAlignment");
		} else {
			this.horizontalAlignment = var1;
			return this;
		}
	}

	public int getHorizontalAlignment() {
		return this.horizontalAlignment;
	}

	public JGuiLabel setVerticalAlignment(int var1) {
		if (this.verticalAlignment == var1) {
			return this;
		} else if (var1 != 0 && var1 != 1 && var1 != 3) {
			throw new IllegalArgumentException("verticalAlignment");
		} else {
			this.verticalAlignment = var1;
			return this;
		}
	}

	public int getVerticalAlignment() {
		return this.verticalAlignment;
	}

	protected void drawComponent(float var1) {
		super.drawComponent(var1);
		Insets var2 = this.getInsets();
		int var3 = var2.left;
		int var4 = var2.top;

		if (this.horizontalAlignment == 0) {
			var3 = var2.left + this.getWidth() - var2.right - this.getStringWidth(this.text) >> 1;
		} else if (this.horizontalAlignment == 4) {
			var3 = var2.left + this.getWidth() - var2.right - this.getStringWidth(this.text);
		}

		if (this.verticalAlignment == 0) {
			var4 = var2.top + this.getHeight() - var2.bottom - JGuiScreen.theMinecraft.fontRenderer.FONT_HEIGHT >> 1;
		} else if (this.verticalAlignment == 3) {
			var4 = var2.top + this.getHeight() - var2.bottom - JGuiScreen.theMinecraft.fontRenderer.FONT_HEIGHT;
		}

		this.drawStringWithShadow(this.text, var3, var4, this.getForeground());
	}

	public JGuiLabel setText(String var1) {
		this.text = var1 == null ? "" : var1;
		return this;
	}

	public String getText() {
		return this.text;
	}
}
