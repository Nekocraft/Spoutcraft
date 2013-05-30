package reifnsk.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.src.FontRenderer;
import org.lwjgl.opengl.GL11;

public class JGuiMCButton extends JGuiLabel {
	private List actionListeners = new CopyOnWriteArrayList();
	private String actionCommand;

	public JGuiMCButton(String text, int x, int y, int width, int height) {
		super(text, x, y, width, height);
		this.setBackground(0);
	}

	public JGuiMCButton(String text) {
		this(text, 0, 0, 0, 0);
	}

	protected void drawComponent(float var1) {
		FontRenderer var2 = minecraft.fontRenderer;
		minecraft.renderEngine.bindTexture("/gui/gui.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		boolean var3 = this.isMouseOver();
		int var4 = this.isEnabled() ? (var3 ? 2 : 1) : 0;
		this.drawTexturedModalRect(0, 0, 0, 46 + var4 * 20, this.getWidth() / 2, this.getHeight());
		this.drawTexturedModalRect(0 + this.getWidth() / 2, 0, 200 - this.getWidth() / 2, 46 + var4 * 20, this.getWidth() / 2, this.getHeight());
		this.setForeground(this.isEnabled() ? (var3 ? 16777120 : 14737632) : 10526880);
		super.drawComponent(var1);
	}

	boolean isMouseOver() {
		JGuiScreen var1 = this.getJGuiScreen();
		return var1 != null && var1.isMouseOver(this);
	}

	protected void mousePress(JMouseEvent var1) {
		if (var1.getButton() == 0) {
			minecraft.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
			ActionEvent var2 = new ActionEvent(this, 1001, this.actionCommand != null ? this.actionCommand : this.getText());
			Iterator var3 = this.actionListeners.iterator();

			while (var3.hasNext()) {
				ActionListener var4 = (ActionListener)var3.next();
				var4.actionPerformed(var2);
			}
		}
	}

	public void addActionListener(ActionListener var1) {
		if (var1 != null) {
			this.actionListeners.add(var1);
		}
	}

	public void removeActionListener(ActionListener var1) {
		this.actionListeners.remove(var1);
	}

	public void setActionCommand(String var1) {
		this.actionCommand = var1;
	}

	public String getActionCommand() {
		return this.actionCommand;
	}
}
