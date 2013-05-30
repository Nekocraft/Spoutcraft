package net.minecraft.src;

import java.awt.event.ActionEvent;
import reifnsk.gui.JGuiMCButton;

public class GuiSleepMP extends GuiChat {
	private JGuiMCButton exitButton;

	public GuiSleepMP() {
		StringTranslate var1 = StringTranslate.getInstance();
		this.exitButton = new JGuiMCButton(var1.translateKey("multiplayer.stopSleeping"), this.width / 2 - 100, this.height - 40, 200, 20);
		this.exitButton.addActionListener(this);
		this.add(this.exitButton);
	}

	public void updateScreenImpl() {
		super.updateScreenImpl();
		this.exitButton.setLocation(this.width / 2 - 100, this.height - 40);
	}

	/**
	 * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
	 */
	protected void keyTyped(char par1, int par2) {
		if (par2 == 1) {
			this.wakeEntity();
		} else {
			super.keyTyped(par1, par2);
		}
	}

	public void actionPerformed(ActionEvent var1) {
		if (var1.getSource() == this.exitButton) {
			this.wakeEntity();
		} else if (var1.getSource() == this.chatInput) {
			this.sendChatMessage(var1.getActionCommand());
			this.chatInput.setText("");
			this.mc.ingameGUI.getChatGUI().resetScroll();
		} else {
			super.actionPerformed(var1);
		}
	}

	/**
	 * Wakes the entity from the bed
	 */
	private void wakeEntity() {
		NetClientHandler var1 = this.mc.thePlayer.sendQueue;
		var1.addToSendQueue(new Packet19EntityAction(this.mc.thePlayer, 3));
		this.onGuiClosed();
	}
}
