package reifnsk.gui;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.Gui;
import net.minecraft.src.Tessellator;
import org.lwjgl.opengl.GL11;

public class JGuiComponent extends Gui {
	protected static final Minecraft minecraft = JGuiScreen.theMinecraft;
	private static int stencilValue = 256;
	JGuiContainer parent;
	private int x;
	private int y;
	private int width;
	private int height;
	private int color = -1;
	private int bgColor = Integer.MIN_VALUE;
	private boolean enabled = true;
	private boolean visible = true;
	private boolean focusable = false;
	private boolean mouseThrough = false;
	private JBorder border;
	private int insetTop = 0;
	private int insetLeft = 0;
	private int insetBottom = 0;
	private int insetRight = 0;
	private List mouseListeners = new CopyOnWriteArrayList();

	public JGuiComponent() {}

	public JGuiComponent(int var1, int var2, int var3, int var4) {
		this.setBounds(var1, var2, var3, var4);
	}

	public JGuiComponent setX(int var1) {
		this.x = var1;
		return this;
	}

	public int getX() {
		return this.x;
	}

	public JGuiComponent setY(int var1) {
		this.y = var1;
		return this;
	}

	public int getY() {
		return this.y;
	}

	public JGuiComponent setLocation(int var1, int var2) {
		this.x = var1;
		this.y = var2;
		return this;
	}

	public JGuiComponent setLocation(Point var1) {
		this.x = var1.x;
		this.y = var1.y;
		return this;
	}

	public Point getLocation() {
		return new Point(this.x, this.y);
	}

	public Point getAbsoluteLocation() {
		Point var1 = new Point(this.x, this.y);

		for (JGuiContainer var2 = this.parent; var2 != null; var2 = var2.parent) {
			var1.x += var2.getX();
			var1.y += var2.getY();
		}

		return var1;
	}

	public JGuiComponent setWidth(int var1) {
		this.width = var1;
		this.resizeEvent();
		return this;
	}

	public int getWidth() {
		return this.width;
	}

	public JGuiComponent setHeight(int var1) {
		this.height = var1;
		this.resizeEvent();
		return this;
	}

	public int getHeight() {
		return this.height;
	}

	public JGuiComponent setSize(int var1, int var2) {
		this.width = var1;
		this.height = var2;
		this.resizeEvent();
		return this;
	}

	public JGuiComponent setSize(Dimension var1) {
		this.width = var1.width;
		this.height = var1.height;
		this.resizeEvent();
		return this;
	}

	public Dimension getSize() {
		return new Dimension(this.width, this.height);
	}

	public JGuiComponent setBounds(int var1, int var2, int var3, int var4) {
		this.x = var1;
		this.y = var2;
		this.width = var3;
		this.height = var4;
		this.resizeEvent();
		return this;
	}

	public JGuiComponent setBounds(Rectangle var1) {
		this.x = var1.x;
		this.y = var1.y;
		this.width = var1.width;
		this.height = var1.height;
		this.resizeEvent();
		return this;
	}

	public Rectangle getBounds() {
		return new Rectangle(this.x, this.y, this.width, this.height);
	}

	public JGuiComponent setEnabled(boolean var1) {
		this.enabled = var1;
		return this;
	}

	public boolean isEnabled() {
		return this.enabled;
	}

	public JGuiComponent setVisible(boolean var1) {
		this.visible = var1;
		return this;
	}

	public boolean isVisible() {
		return this.visible;
	}

	public JGuiComponent setFocusable(boolean var1) {
		this.focusable = var1;
		return this;
	}

	public boolean isFocusable() {
		return this.focusable;
	}

	public JGuiComponent setInsets(int var1, int var2, int var3, int var4) {
		this.insetTop = var1;
		this.insetLeft = var2;
		this.insetBottom = var3;
		this.insetRight = var4;
		this.resizeEvent();
		return this;
	}

	public JGuiComponent setInsets(Insets var1) {
		this.insetTop = var1.top;
		this.insetLeft = var1.left;
		this.insetBottom = var1.bottom;
		this.insetRight = var1.right;
		this.resizeEvent();
		return this;
	}

	public Insets getInsets() {
		return this.border != null ? this.border.getBorderInsets(this) : new Insets(this.insetTop, this.insetLeft, this.insetBottom, this.insetRight);
	}

	public Insets getInsets(Insets var1) {
		if (var1 == null) {
			return this.getInsets();
		} else if (this.border != null) {
			return this.border.getBorderInsets(this, var1);
		} else {
			var1.top = this.insetTop;
			var1.left = this.insetLeft;
			var1.bottom = this.insetBottom;
			var1.right = this.insetRight;
			return var1;
		}
	}

	public JGuiComponent setBorder(JBorder var1) {
		this.border = var1;
		this.resizeEvent();
		return this;
	}

	public JBorder getBorder() {
		return this.border;
	}

	public JGuiComponent setMouseThrough(boolean var1) {
		this.mouseThrough = var1;
		return this;
	}

	public boolean isMouseThrough() {
		return this.mouseThrough;
	}

	public JGuiComponent setForeground(int var1) {
		this.color = var1;
		return this;
	}

	public int getForeground() {
		return this.color;
	}

	public JGuiComponent setBackground(int var1) {
		this.bgColor = var1;
		return this;
	}

	public int getBackgroundColor() {
		return this.bgColor;
	}

	protected void resizeEvent() {}

	protected void updateComponent() {}

	final void renderProcess(float var1) {
		if (this.width > 0 && this.height > 0 && -this.x < this.width && -this.y < this.height) {
			int var2 = this.x + this.width;
			int var3 = this.y + this.height;

			if (this.parent != null) {
				var2 = this.parent.getWidth();
				var3 = this.parent.getHeight();

				if (this.x >= var2 || this.y >= var3) {
					return;
				}
			}

			GL11.glPushMatrix();
			GL11.glPushAttrib(1048575);

			try {
				GL11.glTranslatef((float)this.x, (float)this.y, 0.0F);

				if (++stencilValue >= 256) {
					GL11.glClearStencil(0);
					GL11.glClear(1024);
					stencilValue = 1;
				}

				GL11.glEnable(GL11.GL_STENCIL_TEST);
				GL11.glStencilFunc(GL11.GL_ALWAYS, stencilValue, -1);
				GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_REPLACE, GL11.GL_REPLACE);
				GL11.glDisable(GL11.GL_DEPTH_TEST);
				GL11.glColorMask(false, false, false, false);
				GL11.glDepthMask(false);
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				int var4 = Math.max(0, -this.x);
				int var5 = Math.max(0, -this.y);
				int var6 = Math.min(this.width, var2 - this.x);
				int var7 = Math.min(this.height, var3 - this.y);
				drawRect(var4, var5, var6, var7, -1);
				GL11.glEnable(GL11.GL_TEXTURE_2D);
				GL11.glEnable(GL11.GL_DEPTH_TEST);
				GL11.glColorMask(true, true, true, true);
				GL11.glDepthMask(true);
				GL11.glStencilFunc(GL11.GL_EQUAL, stencilValue, -1);
				GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_KEEP);
				this.drawComponent(var1);

				if (this.border != null) {
					this.border.drawBorder(this, var1);
				}
			} finally {
				GL11.glPopAttrib();
				GL11.glPopMatrix();
			}
		}
	}

	protected void drawComponent(float var1) {
		drawRect(0, 0, this.width, this.height, this.bgColor);
	}

	JGuiComponent locationToComponent(int var1, int var2, boolean var3) {
		return this.visible && var1 >= 0 && var2 >= 0 && var1 < this.width && var2 < this.height && (!var3 || !this.mouseThrough) ? this : null;
	}

	public JGuiScreen getJGuiScreen() {
		for (JGuiContainer var1 = this.parent; var1 != null; var1 = var1.parent) {
			if (var1 instanceof JGuiScreenContainer) {
				return var1.getJGuiScreen();
			}
		}

		return null;
	}

	public JGuiComponent addMouseListener(JMouseListener var1) {
		if (var1 != null) {
			this.mouseListeners.add(var1);
		}

		return this;
	}

	public void removeMouseListener(JMouseListener var1) {
		this.mouseListeners.remove(var1);
	}

	final void fireMouseEvent(JMouseEvent var1) {
		switch (var1.getID()) {
			case 501:
				this.fireMousePress(var1);
				break;

			case 502:
				this.fireMouseRelease(var1);
				break;

			case 503:
				this.fireMouseMoved(var1);
				break;

			case 504:
				this.fireMouseEntered(var1);
				break;

			case 505:
				this.fireMouseExited(var1);
				break;

			case 506:
				this.fireMouseDragged(var1);
				break;

			case 507:
				this.fireMouseWheelMoved(var1);
		}
	}

	private void fireMousePress(JMouseEvent var1) {
		this.mousePress(var1);
		Iterator var2 = this.mouseListeners.iterator();

		while (var2.hasNext()) {
			JMouseListener var3 = (JMouseListener)var2.next();
			var3.mousePress(var1);
		}
	}

	private void fireMouseRelease(JMouseEvent var1) {
		this.mouseRelease(var1);
		Iterator var2 = this.mouseListeners.iterator();

		while (var2.hasNext()) {
			JMouseListener var3 = (JMouseListener)var2.next();
			var3.mouseRelease(var1);
		}
	}

	private void fireMouseDragged(JMouseEvent var1) {
		this.mouseDragged(var1);
		Iterator var2 = this.mouseListeners.iterator();

		while (var2.hasNext()) {
			JMouseListener var3 = (JMouseListener)var2.next();
			var3.mouseDragged(var1);
		}
	}

	private void fireMouseMoved(JMouseEvent var1) {
		this.mouseMoved(var1);
		Iterator var2 = this.mouseListeners.iterator();

		while (var2.hasNext()) {
			JMouseListener var3 = (JMouseListener)var2.next();
			var3.mouseMoved(var1);
		}
	}

	private void fireMouseExited(JMouseEvent var1) {
		this.mouseExited(var1);
		Iterator var2 = this.mouseListeners.iterator();

		while (var2.hasNext()) {
			JMouseListener var3 = (JMouseListener)var2.next();
			var3.mouseExited(var1);
		}
	}

	private void fireMouseEntered(JMouseEvent var1) {
		this.mouseEntered(var1);
		Iterator var2 = this.mouseListeners.iterator();

		while (var2.hasNext()) {
			JMouseListener var3 = (JMouseListener)var2.next();
			var3.mouseEntered(var1);
		}
	}

	private void fireMouseWheelMoved(JMouseEvent var1) {
		this.mouseWheelMoved(var1);
		Iterator var2 = this.mouseListeners.iterator();

		while (var2.hasNext()) {
			JMouseListener var3 = (JMouseListener)var2.next();
			var3.mouseWheelMoved(var1);
		}
	}

	protected void mousePress(JMouseEvent var1) {}

	protected void mouseRelease(JMouseEvent var1) {}

	protected void mouseDragged(JMouseEvent var1) {}

	protected void mouseMoved(JMouseEvent var1) {}

	protected void mouseExited(JMouseEvent var1) {}

	protected void mouseEntered(JMouseEvent var1) {}

	protected void mouseWheelMoved(JMouseEvent var1) {}

	protected void keyTyped(char var1, int var2, int var3) {}

	public final void drawString(String var1, int var2, int var3, int var4) {
		if (var1 != null) {
			FontRenderer var5 = JGuiScreen.theMinecraft.fontRenderer;
			var5.drawString(var1, var2, var3, var4);
		}
	}

	public final void drawCenteredString(String var1, int var2, int var3, int var4) {
		if (var1 != null) {
			FontRenderer var5 = JGuiScreen.theMinecraft.fontRenderer;
			var5.drawString(var1, (var2 << 1) - var5.getStringWidth(var1) >> 1, var3, var4);
		}
	}

	public final void drawStringWithShadow(String var1, int var2, int var3, int var4) {
		if (var1 != null) {
			FontRenderer var5 = JGuiScreen.theMinecraft.fontRenderer;
			var5.drawStringWithShadow(var1, var2, var3, var4);
		}
	}

	public final void drawCenteredStringWithShadow(String var1, int var2, int var3, int var4) {
		if (var1 != null) {
			FontRenderer var5 = JGuiScreen.theMinecraft.fontRenderer;
			var5.drawStringWithShadow(var1, (var2 << 1) - var5.getStringWidth(var1) >> 1, var3, var4);
		}
	}

	public final int getStringWidth(String var1) {
		if (var1 == null) {
			return 0;
		} else {
			FontRenderer var2 = JGuiScreen.theMinecraft.fontRenderer;
			return var2.getStringWidth(var1);
		}
	}

	public final void drawString(boolean var1, String var2, int var3, int var4, int var5) {
		if (var2 != null) {
			if (var1) {
				this.drawStringWithShadow(var2, var3, var4, var5);
			} else {
				this.drawString(var2, var3, var4, var5);
			}
		}
	}

	public final void drawCenteredString(boolean var1, String var2, int var3, int var4, int var5) {
		if (var2 != null) {
			if (var1) {
				this.drawCenteredStringWithShadow(var2, var3, var4, var5);
			} else {
				this.drawCenteredString(var2, var3, var4, var5);
			}
		}
	}

	protected void drawRect(float var1, float var2, float var3, float var4, int var5, int var6, int var7) {
		float var8;

		if (var1 < var3) {
			var8 = var1;
			var1 = var3;
			var3 = var8;
		}

		if (var2 < var4) {
			var8 = var2;
			var2 = var4;
			var4 = var8;
		}

		var8 = (float)(var5 >> 24 & 255) / 255.0F;
		float var9 = (float)(var5 >> 16 & 255) / 255.0F;
		float var10 = (float)(var5 >> 8 & 255) / 255.0F;
		float var11 = (float)(var5 & 255) / 255.0F;
		Tessellator var12 = Tessellator.instance;
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(var6, var7);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glColor4f(var9, var10, var11, var8);
		var12.startDrawingQuads();
		var12.addVertex((double)var1, (double)var4, 0.0D);
		var12.addVertex((double)var3, (double)var4, 0.0D);
		var12.addVertex((double)var3, (double)var2, 0.0D);
		var12.addVertex((double)var1, (double)var2, 0.0D);
		var12.draw();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
	}

	public boolean isFocusOwner() {
		JGuiScreen var1 = this.getJGuiScreen();
		return var1 != null ? var1.isFocusOwner(this) : false;
	}

	public void requestFocus() {
		if (this.isFocusable()) {
			JGuiScreen var1 = this.getJGuiScreen();

			if (var1 != null) {
				var1.setFocus(this);
			}
		}
	}

	public boolean isControlDown(int var1) {
		return (var1 & 128) != 0;
	}

	public boolean isAltDown(int var1) {
		return (var1 & 512) != 0;
	}

	public boolean isShiftDown(int var1) {
		return (var1 & 64) != 0;
	}

	public boolean isMetaDown(int var1) {
		return (var1 & 256) != 0;
	}
}
