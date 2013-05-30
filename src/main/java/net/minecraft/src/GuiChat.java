package net.minecraft.src;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import reifnsk.gui.JEmptyBorder;
import reifnsk.gui.JGuiScreen;
import reifnsk.gui.JGuiTextField;

// Spout Start
import org.bukkit.ChatColor;
import org.spoutcraft.client.config.Configuration;
// Spout End

public class GuiChat extends JGuiScreen implements ActionListener {
	protected final JGuiTextField chatInput;

	/**
	 * keeps position of which chat message you will select when you press up, (does not increase for duplicated messages
	 * sent immediately after each other)
	 */
	private int sentHistoryCursor;
	private String temporaryInputText;
	private URI openURI;
	private boolean completeTextFlag;
	private boolean field_73905_m;
	private int completeTextPosition;
	private ArrayList completeTextList;
	String inputText;

	public GuiChat() {
		this.chatInput = new JGuiTextField();
		this.temporaryInputText = "";
		this.completeTextPosition = 0;
		this.completeTextList = new ArrayList();
		this.inputText = null;
		this.add(this.chatInput);
		this.chatInput.addActionListener(this);
		this.chatInput.setBorder(new JEmptyBorder(2, 2, 2, 2));
		this.chatInput.setBackground(Integer.MIN_VALUE);
		this.chatInput.setShadow(true);
		this.chatInput.setLimit(100);
		this.chatInput.requestFocus();
		this.sentHistoryCursor = -1;
	}

	public GuiChat(String par1Str) {
		this();
		this.chatInput.setText(par1Str);
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	public void initGui() {
		super.initGui();
		Keyboard.enableRepeatEvents(true);
		this.sentHistoryCursor = this.mc.ingameGUI.getChatGUI().getSentMessages().size();
	}

	public void onGuiClosedImpl() {
		Keyboard.enableRepeatEvents(false);
		this.mc.ingameGUI.getChatGUI().resetScroll();
	}

	protected void updateScreenImpl() {
		this.chatInput.setBounds(2, this.height - 14, this.width - 4, 12);
	}

	public void actionPerformed(ActionEvent var1) {
		if (var1.getSource() == this.chatInput) {
			String var2 = var1.getActionCommand();
			this.sendChatMessage(var2);
			this.closeScreen();
		}
	}

	protected final void sendChatMessage(String var1) {
		if (var1 != null) {
			var1 = var1.trim();

			if (!var1.isEmpty()) {
				this.mc.ingameGUI.getChatGUI().addToSentMessages(var1);

				if (!this.mc.handleClientCommand(var1)) {
					this.mc.thePlayer.sendChatMessage(var1);
				}
			}
		}
	}

	protected void mouseWheelMoved(int var1, int var2, int var3) {
		var3 = var3 >> 31 | 1;

		if (!isShiftPressed()) {
			var3 *= 7;
		}

		this.mc.ingameGUI.getChatGUI().scroll(var3);
	}

	/**
	 * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
	 */
	protected void keyTyped(char par1, int par2) {
		this.field_73905_m = false;

		if (par2 == 15) {
			this.completePlayerName();
		} else {
			this.completeTextFlag = false;
		}

		switch (par2) {
			case 200:
				this.getSentHistory(-1);
				break;

			case 201:
				this.mc.ingameGUI.getChatGUI().scroll(19);

			case 202:
			case 203:
			case 204:
			case 205:
			case 206:
			case 207:
			default:
				break;

			case 208:
				this.getSentHistory(1);
				break;

			case 209:
				this.mc.ingameGUI.getChatGUI().scroll(-19);
		}

		super.keyTyped(par1, par2);
	}

	/**
	 * Called when the mouse is clicked.
	 */
	protected void mouseClicked(int par1, int par2, int par3) {
		if (par3 == 0) {
			ChatClickData var4 = this.mc.ingameGUI.getChatGUI().func_73766_a(Mouse.getX(), Mouse.getY());

			if (var4 != null) {
				URI var5 = var4.getURI();

				if (var5 != null) {
					this.openURI = var5;
					this.mc.displayGuiScreen(new GuiConfirmOpenLink(this, var4.getClickedUrl(), 0, false));
					return;
				}
			}
		}

		super.mouseClicked(par1, par2, par3);
	}

	public void confirmClicked(boolean par1, int par2) {
		if (par2 == 0) {
			if (par1) {
				try {
					Desktop.getDesktop().browse(this.openURI);
				} catch (IOException var4) {
					var4.printStackTrace();
				}
			}

			this.openURI = null;
			this.mc.displayGuiScreen(this);
		}
	}

	/**
	 * input is relative and is applied directly to the sentHistoryCursor so -1 is the previous message, 1 is the next
	 * message from the current cursor position
	 */
	public void getSentHistory(int par1) {
		int var2 = this.sentHistoryCursor + par1;
		int var3 = this.mc.ingameGUI.getChatGUI().getSentMessages().size();
		var2 = var2 < 0 ? 0 : (var2 > var3 ? var3 : var2);

		if (var2 != this.sentHistoryCursor) {
			if (var2 == var3) {
				this.sentHistoryCursor = var3;
				this.chatInput.setText(this.temporaryInputText);
			} else {
				if (this.sentHistoryCursor == var3) {
					this.temporaryInputText = this.chatInput.getText();
				}

				this.chatInput.setText((String)this.mc.ingameGUI.getChatGUI().getSentMessages().get(var2));
				this.sentHistoryCursor = var2;
			}
		}
	}

	/**
	 * Autocompletes player name
	 */
	public void completePlayerName() {
		endComposition();
		String var3;

		if (this.completeTextFlag) {
			if (this.completeTextPosition >= this.completeTextList.size()) {
				this.completeTextPosition = 0;
			}
		} else {
			this.completeTextList.clear();
			this.completeTextFlag = true;
			this.completeTextPosition = 0;
			String var1 = this.chatInput.getText();
			int var2;

			for (var2 = this.chatInput.getCommittedTextCaret(); var2 > 0 && var1.charAt(var2 - 1) != 32; --var2) {
				;
			}

			this.inputText = var1.substring(0, var2);
			var3 = var1.substring(var2).toLowerCase();
			String var4 = var1.substring(0, this.chatInput.getCommittedTextCaret());
			this.func_73893_a(var4, var3);
		}

		if (!this.completeTextList.isEmpty()) {
			if (this.completeTextList.size() > 1) {
				StringBuilder var6 = new StringBuilder();
				Iterator var5 = this.completeTextList.iterator();

				while (var5.hasNext()) {
					var3 = (String)var5.next();
					var6.append(var3).append(", ");
				}

				var6.setLength(var6.length() - 2);
				this.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(var6.toString(), 1);
			}

			this.chatInput.setText(this.inputText + (String)this.completeTextList.get(this.completeTextPosition++));
		}
	}

	private void func_73893_a(String par1Str, String par2Str) {
		if (par1Str.length() >= 1) {
			this.mc.thePlayer.sendQueue.addToSendQueue(new Packet203AutoComplete(par1Str));
			this.field_73905_m = true;
		}
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	public void drawScreen(int par1, int par2, float par3) {
		// Spout Start
		if (Configuration.isShowingChatColorAssist()) {
			for(int c = 0; c < 16; c++) {
				ChatColor value = ChatColor.getByCode(c);
				String name = value.name().toLowerCase();
				String parsedName = value.getName();
				char code = (char) ('0' + c);
				if(c >= 10) {
					code = (char) ('a' + c - 10);
				}
				fontRenderer.drawStringWithShadow("&" + code + " - " + value + parsedName, width - 90, 70 + c * 10, 0xffffffff);
			}
		}
		// Spout End
		super.drawScreen(par1, par2, par3);
	}

	public void func_73894_a(String[] par1ArrayOfStr) {
		if (this.field_73905_m) {
			this.completeTextList.clear();
			String[] var2 = par1ArrayOfStr;
			int var3 = par1ArrayOfStr.length;

			for (int var4 = 0; var4 < var3; ++var4) {
				String var5 = var2[var4];

				if (var5.length() > 0) {
					this.completeTextList.add(var5);
				}
			}

			if (this.completeTextList.size() > 0) {
				this.completeTextFlag = true;
				this.completePlayerName();
			}
		}
	}

	/**
	 * Returns true if this GUI should pause the game when it is displayed in single-player
	 */
	public boolean doesGuiPauseGame() {
		return false;
	}
}
