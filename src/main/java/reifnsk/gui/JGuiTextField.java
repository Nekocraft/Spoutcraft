package reifnsk.gui;

import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputMethodEvent;
import java.awt.font.TextAttribute;
import java.awt.font.TextHitInfo;
import java.awt.im.InputMethodHighlight;
import java.text.AttributedCharacterIterator;
import java.text.AttributedCharacterIterator.Attribute;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.src.ScaledResolution;
import net.minecraft.src.Tessellator;
import org.lwjgl.opengl.GL11;

public class JGuiTextField extends JGuiComponent implements JGuiInputMethod {
	private static final int MAX_LENGTH = 65535;
	private static final String dottedline = "/reifnsk/gui/dottedline.png";
	private static final String singleline = "/reifnsk/gui/singleline.png";
	private static final String doubleline = "/reifnsk/gui/doubleline.png";
	private String prefix;
	private int prefixSize;
	private StringBuffer committedText;
	private int committedTextCaret;
	private int selectionStart;
	private int selectionEnd;
	private int selectionColor;
	private int selectedTextColor;
	private AttributedCharacterIterator composedText;
	private int composedTextCaret;
	private int committedCharacterCount;
	private int scrollX;
	private int limit;
	private int count;
	private Rectangle textLocation;
	private boolean shadow;
	private List actionListeners;
	private boolean colorCodeAccessible;

	public JGuiTextField() {
		this("", 0, 0, 0, 0);
	}

	public JGuiTextField(String var1) {
		this(var1, 0, 0, 0, 0);
	}

	public JGuiTextField(int var1, int var2, int var3, int var4) {
		this("", var1, var2, var3, var4);
	}

	public JGuiTextField(String var1, int var2, int var3, int var4, int var5) {
		super(var2, var3, var4, var5);
		this.prefix = "";
		this.prefixSize = 0;
		this.selectionColor = -13410648;
		this.selectedTextColor = -1;
		this.limit = 256;
		this.textLocation = new Rectangle();
		this.actionListeners = new CopyOnWriteArrayList();
		this.colorCodeAccessible = false;
		this.setBorder(new JEmptyBorder(1, 1, 1, 1));
		this.setFocusable(true);
		this.committedText = new StringBuffer(var1);
		this.committedTextCaret = this.committedText.length();
	}

	protected void updateComponent() {
		++this.count;
		super.updateComponent();
	}

	protected void keyTyped(char var1, int var2, int var3) {
		synchronized (this) {
			this.count = 0;

			if (var2 == 28 && var3 == 0) {
				Iterator var5 = this.actionListeners.iterator();

				while (var5.hasNext()) {
					ActionListener var6 = (ActionListener)var5.next();
					var6.actionPerformed(new ActionEvent(this, 1001, this.getText()));
				}
			}

			if (var2 == 14) {
				if (!this.delete() && this.committedTextCaret > this.prefixSize) {
					this.committedText.delete(this.committedTextCaret - 1, this.committedTextCaret);
					--this.committedTextCaret;
				}
			} else if (var2 == 211) {
				if (!this.delete() && this.committedTextCaret < this.committedText.length()) {
					this.committedText.delete(this.committedTextCaret, this.committedTextCaret + 1);
				}
			} else if (var2 == 203) {
				if (this.committedTextCaret > this.prefixSize) {
					--this.committedTextCaret;
				}

				this.selectionEnd = this.committedTextCaret;

				if (!this.isShiftDown(var3)) {
					this.selectionStart = this.committedTextCaret;
				}
			} else if (var2 == 205) {
				if (this.committedTextCaret < this.committedText.length()) {
					++this.committedTextCaret;
				}

				this.selectionEnd = this.committedTextCaret;

				if (!this.isShiftDown(var3)) {
					this.selectionStart = this.committedTextCaret;
				}
			} else if (var2 == 199) {
				this.committedTextCaret = this.prefixSize;
				this.scrollX = 0;
				this.selectionEnd = this.committedTextCaret;

				if (!this.isShiftDown(var3)) {
					this.selectionStart = this.committedTextCaret;
				}
			} else if (var2 == 207) {
				this.committedTextCaret = this.committedText.length();
				this.selectionEnd = this.committedTextCaret;

				if (!this.isShiftDown(var3)) {
					this.selectionStart = this.committedTextCaret;
				}
			} else if (var2 == 30 && this.isControlDown(var3)) {
				this.selectionStart = this.prefixSize;
				this.selectionEnd = this.committedText.length();
			} else if (var2 == 46 && this.isControlDown(var3)) {
				this.copy();
			} else if (var2 == 45 && this.isControlDown(var3)) {
				this.cut();
			} else if (var2 == 47 && this.isControlDown(var3)) {
				String var11 = JGuiScreen.getClipboardString();

				if (var11 != null && !var11.isEmpty()) {
					this.delete();
					int var12 = 0;

					for (int var7 = var11.length(); var12 < var7 && this.committedText.length() - this.prefixSize < this.limit; ++var12) {
						char var8 = var11.charAt(var12);

						if (var8 >= 32 && (this.colorCodeAccessible || var8 != 167)) {
							this.committedText.insert(this.committedTextCaret, var8);
							++this.committedTextCaret;
						}
					}
				}

				this.selectionStart = this.selectionEnd = this.committedTextCaret;
			} else if (var1 >= 32 && (this.colorCodeAccessible || var1 != 167)) {
				this.delete();

				if (this.committedText.length() - this.prefixSize < this.limit) {
					this.committedText.insert(this.committedTextCaret, var1);
					++this.committedTextCaret;
					this.selectionStart = this.selectionEnd = this.committedTextCaret;
				}
			}
		}
	}

	protected void drawComponent(float var1) {
		if (!JGuiScreen.exitMinecraft) {
			super.drawComponent(var1);

			synchronized (this) {
				this.normalize();
				minecraft.renderEngine.resetBoundTexture();
				int var3 = this.getForeground();
				Insets var4 = this.getInsets();
				boolean var5 = this.isFocusOwner();
				Minecraft var6 = JGuiScreen.theMinecraft;

				if (this.composedText == null) {
					float var7 = (float)this.getStringWidth(this.committedText.substring(0, this.committedTextCaret));

					if (var7 < (float)this.scrollX) {
						this.scrollX = (int)Math.max(var7 - (float)(this.getWidth() / 3), 0.0F);
					} else if (var7 > (float)(this.scrollX + this.getWidth() - var4.left - var4.right)) {
						this.scrollX = (int)(var7 - (float)this.getWidth() + (float)var4.left + (float)var4.right);
					}

					GL11.glTranslatef((float)(var4.left - this.scrollX), (float)var4.top, 0.0F);

					if (this.selectionStart == this.selectionEnd) {
						this.drawString(this.shadow, this.committedText.toString(), 0, 0, var3);
					} else {
						int var8 = Math.min(this.selectionStart, this.selectionEnd);
						int var9 = Math.max(this.selectionEnd, this.selectionStart);
						String var10 = this.committedText.substring(0, var8);
						String var11 = this.committedText.substring(var8, var9);
						String var12 = this.committedText.substring(var9, this.committedText.length());
						int var13 = this.getStringWidth(var10);
						int var14 = this.getStringWidth(var11);
						this.drawRect((float)var13 - 0.5F, 0.0F, (float)(var13 + var14) - 0.5F, (float)var6.fontRenderer.FONT_HEIGHT, this.selectionColor, 770, 771);
						this.drawString(this.shadow, var10, 0, 0, var3);
						this.drawString(false, var11, var13, 0, this.selectedTextColor);
						this.drawString(this.shadow, var12, var13 + var14, 0, this.selectedTextColor);
					}

					if (var5 && this.isVisibleCaretCount()) {
						this.drawRect(var7 - 0.5F, 0.0F, var7 + 0.5F, (float)var6.fontRenderer.FONT_HEIGHT, -1, 775, 769);
					}
				} else {
					String var48 = this.committedText.substring(0, this.committedTextCaret);
					String var46 = this.committedText.substring(this.committedTextCaret, this.committedText.length());
					float var47 = (float)this.getStringWidth(var48);
					float var43 = (float)this.getStringWidth(var46);
					LinkedHashMap var44 = new LinkedHashMap();
					StringBuilder var45 = new StringBuilder();

					for (char var40 = this.composedText.setIndex(this.committedCharacterCount); var40 != 65535; var40 = this.composedText.next()) {
						if (var40 >= 32) {
							var45.append(var40);
						}

						if (this.composedText.getRunStart() == this.composedText.getIndex()) {
							var44.put(Integer.valueOf(this.composedText.getRunLimit() - this.committedCharacterCount), (InputMethodHighlight)this.composedText.getAttribute(TextAttribute.INPUT_METHOD_HIGHLIGHT));
						}
					}

					String var41 = var45.toString();
					float var42 = (float)this.getStringWidth(var41);
					int var15 = Math.max(0, Math.min(var41.length(), this.composedTextCaret));
					float var16 = (float)this.getStringWidth(var48 + var41.substring(0, var15));
					float var17;

					if (var16 < (float)this.scrollX) {
						this.scrollX = (int)Math.max(var16 - (float)(this.getWidth() / 3), 0.0F);
					} else if (var16 > (float)(this.scrollX + this.getWidth() - var4.left - var4.right)) {
						var17 = Math.min(var16 + (float)(this.getWidth() / 3), var47 + var43 + var42);
						this.scrollX = (int)(var17 - (float)this.getWidth() + (float)var4.left + (float)var4.right);
					}

					GL11.glTranslatef((float)(var4.left - this.scrollX), (float)var4.top, 0.0F);
					var17 = 0.0F;

					if (this.shadow) {
						this.drawStringWithShadow(var48, Math.round(var17), 0, var3);
					} else {
						this.drawString(var48, Math.round(var17), 0, var3);
					}

					var17 += var47;

					if (this.shadow) {
						this.drawStringWithShadow(var41, Math.round(var17), 0, var3);
					} else {
						this.drawString(var41, Math.round(var17), 0, var3);
					}

					var17 += var42;

					if (this.shadow) {
						this.drawStringWithShadow(var46, Math.round(var17), 0, var3);
					} else {
						this.drawString(var46, Math.round(var17), 0, var3);
					}

					Point var18 = this.getAbsoluteLocation();
					int var19 = (new ScaledResolution(var6.gameSettings, var6.displayWidth, var6.displayHeight)).getScaleFactor();
					int var20 = (int)((float)(var18.x + var4.left) + var16 - (float)this.scrollX) * var19;
					this.textLocation.setBounds(JGuiScreen.mcCanvasLocationOnScreen.x + var20, JGuiScreen.mcCanvasLocationOnScreen.y + (var18.y + var4.top - var6.fontRenderer.FONT_HEIGHT - 1) * var19, 0, (var6.fontRenderer.FONT_HEIGHT + 1) * var19);

					if (var5 && this.isVisibleCaretCount()) {
						this.drawRect(var16 - 0.5F, 0.0F, var16 + 0.5F, (float)var6.fontRenderer.FONT_HEIGHT, -1, 775, 769);
					}

					int var21 = 0;
					float var22 = var47;
					Tessellator var23 = Tessellator.instance;
					float var29;

					try {
						for (Iterator var24 = var44.entrySet().iterator(); var24.hasNext(); var22 = var29) {
							Entry var25 = (Entry)var24.next();
							int var26 = ((Integer)var25.getKey()).intValue();
							InputMethodHighlight var27 = (InputMethodHighlight)var25.getValue();
							String var28 = var41.substring(var21, var26);
							var29 = var22 + (float)this.getStringWidth(var28);
							String var30 = null;

							if (var27 == InputMethodHighlight.SELECTED_CONVERTED_TEXT_HIGHLIGHT) {
								var30 = "/reifnsk/gui/doubleline.png";
							}

							if (var27 == InputMethodHighlight.SELECTED_RAW_TEXT_HIGHLIGHT) {
								var30 = "/reifnsk/gui/doubleline.png";
							}

							if (var27 == InputMethodHighlight.UNSELECTED_CONVERTED_TEXT_HIGHLIGHT) {
								var30 = "/reifnsk/gui/singleline.png";
							}

							if (var27 == InputMethodHighlight.UNSELECTED_RAW_TEXT_HIGHLIGHT) {
								var30 = "/reifnsk/gui/dottedline.png";
							}

							float var32 = var29 - 1.0F;
							float var33 = (float)var6.fontRenderer.FONT_HEIGHT - 1.0F;
							float var34 = var33 + 1.0F;
							float var35 = 0.0F;
							float var36 = var29 - var22 - 1.0F;
							GL11.glEnable(GL11.GL_TEXTURE_2D);
							var6.renderEngine.bindTexture(var30);
							GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
							GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
							GL11.glEnable(GL11.GL_BLEND);
							GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
							GL11.glColor3f(1.0F, 1.0F, 1.0F);
							var23.startDrawingQuads();
							var23.addVertexWithUV((double)var22, (double)var33, 0.0D, (double)var35, 0.0D);
							var23.addVertexWithUV((double)var22, (double)var34, 0.0D, (double)var35, 1.0D);
							var23.addVertexWithUV((double)var32, (double)var34, 0.0D, (double)var36, 1.0D);
							var23.addVertexWithUV((double)var32, (double)var33, 0.0D, (double)var36, 0.0D);
							var23.draw();
							var21 = var26;
						}
					} catch (Exception var38) {
						System.out.println(var41);
						var38.printStackTrace();
					}
				}
			}
		}
	}

	public void caretPositionChanged(InputMethodEvent var1) {
		synchronized (this) {
			this.composedTextCaret = var1.getCaret() == null ? 0 : var1.getCaret().getCharIndex();
			var1.consume();
		}
	}

	public void inputMethodTextChanged(InputMethodEvent var1) {
		synchronized (this) {
			this.composedTextCaret = var1.getCaret() == null ? 0 : var1.getCaret().getCharIndex();
			this.composedText = var1.getText();
			this.committedCharacterCount = var1.getCommittedCharacterCount();
			this.delete();

			for (int var3 = 0; var3 < this.committedCharacterCount && this.committedText.length() - this.prefixSize < this.limit; ++var3) {
				char var4 = this.composedText.setIndex(var3);

				if (var4 >= 32 && (this.colorCodeAccessible || var4 != 167)) {
					this.committedText.insert(this.committedTextCaret, var4);
					++this.committedTextCaret;
				}
			}

			this.selectionStart = this.selectionEnd = this.committedTextCaret;
			var1.consume();
		}
	}

	public AttributedCharacterIterator cancelLatestCommittedText(Attribute[] var1) {
		return null;
	}

	public AttributedCharacterIterator getCommittedText(int var1, int var2, Attribute[] var3) {
		return null;
	}

	public int getCommittedTextLength() {
		return 0;
	}

	public int getInsertPositionOffset() {
		return 0;
	}

	public TextHitInfo getLocationOffset(int var1, int var2) {
		return null;
	}

	public AttributedCharacterIterator getSelectedText(Attribute[] var1) {
		return null;
	}

	public Rectangle getTextLocation(TextHitInfo var1) {
		return this.textLocation;
	}

	protected void resizeEvent() {}

	public void setPrefix(String var1) {
		if (var1 == null) {
			var1 = "";
		}

		synchronized (this) {
			this.committedText.replace(0, this.prefixSize, var1);
			this.committedTextCaret -= this.prefixSize;
			this.selectionStart -= this.prefixSize;
			this.selectionEnd -= this.prefixSize;
			this.prefixSize = var1.length();
			this.committedTextCaret += this.prefixSize;
			this.selectionStart += this.prefixSize;
			this.selectionEnd += this.prefixSize;
			this.prefix = var1;
		}
	}

	public String getPrefix() {
		return this.committedText.substring(0, this.prefixSize);
	}

	public void setText(String var1) {
		synchronized (this) {
			if (var1 == null) {
				var1 = "";
			}

			this.committedText.setLength(0);
			this.committedText.append(this.prefix);
			this.committedText.append(var1, 0, Math.min(var1.length(), this.prefixSize + this.limit));
			this.committedTextCaret = this.committedText.length();
			this.selectionStart = this.committedTextCaret;
			this.selectionEnd = this.committedTextCaret;
		}
	}

	public String getText() {
		synchronized (this) {
			if (this.composedText == null) {
				return this.committedText.substring(this.prefixSize);
			} else {
				StringBuilder var2 = new StringBuilder();
				var2.append(this.committedText, this.prefixSize, this.committedTextCaret);
				int var3 = this.limit - this.committedText.length() + this.prefixSize;

				for (char var4 = this.composedText.setIndex(this.committedCharacterCount); var4 != 65535 && var3 > 0; var4 = this.composedText.next()) {
					if (var4 >= 32 && (this.colorCodeAccessible || var4 != 167)) {
						var2.append(var4);
						--var3;
					}
				}

				var2.append(this.committedText, this.committedTextCaret, this.committedText.length());
				return var2.toString();
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

	public void setShadow(boolean var1) {
		this.shadow = var1;
	}

	public boolean getShadow() {
		return this.shadow;
	}

	public void setLimit(int var1) {
		synchronized (this) {
			if (var1 < 0 || var1 > 65535) {
				var1 = 65535;
			}

			this.limit = var1;

			if (var1 < this.committedText.length() + this.prefixSize) {
				this.committedText.setLength(var1 + this.prefixSize);
			}

			this.normalize();
		}
	}

	public int getLimit() {
		return this.limit;
	}

	private void normalize() {
		this.selectionStart = Math.max(this.prefixSize, Math.min(this.committedText.length(), this.selectionStart));
		this.selectionEnd = Math.max(this.prefixSize, Math.min(this.committedText.length(), this.selectionEnd));
		this.committedTextCaret = Math.max(this.prefixSize, Math.min(this.committedText.length(), this.committedTextCaret));
	}

	private boolean delete() {
		synchronized (this) {
			this.normalize();

			if (this.selectionEnd == this.selectionStart) {
				return false;
			} else {
				int var2 = Math.min(this.selectionStart, this.selectionEnd);
				int var3 = Math.max(this.selectionStart, this.selectionEnd);
				this.committedText.delete(var2, var3);

				if (this.committedTextCaret > var2) {
					this.committedTextCaret = this.committedTextCaret < var3 ? var2 : this.committedTextCaret + var2 - var3;
				}

				this.selectionStart = this.committedTextCaret;
				this.selectionEnd = this.committedTextCaret;
				return true;
			}
		}
	}

	public String getSelectedText() {
		synchronized (this) {
			this.normalize();

			if (this.selectionStart == this.selectionEnd) {
				return null;
			} else {
				int var2 = Math.min(this.selectionStart, this.selectionEnd);
				int var3 = Math.max(this.selectionStart, this.selectionEnd);
				return this.committedText.substring(var2, var3);
			}
		}
	}

	public int getSelectedTextLength() {
		return Math.abs(this.selectionStart - this.selectionEnd);
	}

	public boolean isComposed() {
		return this.composedText != null;
	}

	public int getCommittedTextCaret() {
		return this.committedTextCaret - this.prefixSize;
	}

	public void setCommittedTextCaret(int var1) {
		this.committedTextCaret = this.selectionStart = this.selectionEnd = var1 + this.prefixSize;
		this.normalize();
	}

	private void copy() {
		synchronized (this) {
			this.normalize();

			if (this.selectionStart != this.selectionEnd) {
				int var2 = Math.min(this.selectionStart, this.selectionEnd);
				int var3 = Math.max(this.selectionStart, this.selectionEnd);
				StringSelection var4 = new StringSelection(this.committedText.substring(var2, var3));
				Toolkit.getDefaultToolkit().getSystemClipboard().setContents(var4, var4);
			}
		}
	}

	private void cut() {
		synchronized (this) {
			this.normalize();

			if (this.selectionStart != this.selectionEnd) {
				int var2 = Math.min(this.selectionStart, this.selectionEnd);
				int var3 = Math.max(this.selectionStart, this.selectionEnd);
				StringSelection var4 = new StringSelection(this.committedText.substring(var2, var3));
				Toolkit.getDefaultToolkit().getSystemClipboard().setContents(var4, var4);
				this.committedText.delete(var2, var3);

				if (this.committedTextCaret > var2) {
					this.committedTextCaret = this.committedTextCaret < var3 ? var2 : this.committedTextCaret + var2 - var3;
				}

				this.selectionStart = this.committedTextCaret;
				this.selectionEnd = this.committedTextCaret;
			}
		}
	}

	public boolean isVisibleCaretCount() {
		return (this.count / 6 & 1) == 0;
	}

	protected void mousePress(JMouseEvent var1) {
		JGuiScreen.endComposition();
		int var2 = minecraft.fontRenderer.trimStringToWidth(this.getPrefix() + this.getText(), this.scrollX + var1.getX()).length();
		this.selectionEnd = this.selectionStart = this.committedTextCaret = var2;
	}

	protected void mouseDragged(JMouseEvent var1) {
		int var2 = minecraft.fontRenderer.trimStringToWidth(this.getPrefix() + this.getText(), this.scrollX + var1.getX()).length();
		this.selectionEnd = this.committedTextCaret = var2;
	}

	public boolean getColorCodeAccesible() {
		return this.colorCodeAccessible;
	}

	public void setColorCodeAccessible(boolean var1) {
		this.colorCodeAccessible = var1;
	}
}
