package reifnsk.gui;

import java.awt.Canvas;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.im.InputContext;
import java.awt.im.InputMethodRequests;
import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import javax.swing.SwingUtilities;
import net.minecraft.client.Minecraft;
import net.minecraft.src.ScreenShotHelper;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import net.minecraft.src.GuiScreen;

public class JGuiScreen extends GuiScreen {
	static boolean exitMinecraft;
	public boolean immediateEvent = true;
	private final JGuiScreenContainer container = new JGuiScreenContainer(this);
	private JGuiComponent focusOwner;
	private JGuiComponent mouseOwner;
	private JGuiComponent mouseOver;
	private int mouseX;
	private int mouseY;
	private int tickCount;
	private boolean closed = false;
	private boolean closeScreen;
	private boolean isTakingScreenshot;
	private ConcurrentLinkedQueue keyEventQueue = new ConcurrentLinkedQueue();
	private static InputMethodComponent imComponent;
	public static final Minecraft theMinecraft;
	static final Container mcContainer;
	static final Frame mcFrame;
	static final Canvas mcCanvas;
	static Point mcCanvasLocationOnScreen = new Point();
	private static int[] KEY_MAP = new int[65535];
	private static int[] KEY_MAP2 = new int[65535];

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	public void initGui() {
		this.closed = false;
		this.closeScreen = false;
		imComponent.enableInputMethods(this.focusOwner instanceof InputMethodRequests);
	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	public final void updateScreen() {
		if (!this.closed) {
			this.container.updateComponents();
			this.updateScreenImpl();
			++this.tickCount;

			if (this.isTakingScreenshot) {
				theMinecraft.ingameGUI.getChatGUI().printChatMessage(ScreenShotHelper.saveScreenshot(Minecraft.getMinecraftDir(), theMinecraft.displayWidth, theMinecraft.displayHeight));
				this.isTakingScreenshot = false;
			}

			if (this.closeScreen) {
				this.mc.setIngameFocus();
			} else {
				if (Display.isActive() || isWindowActive() && !imComponent.isFocusOwner()) {
					this.moveFocus(true);
				}

				super.updateScreen();
			}
		}
	}

	protected void updateScreenImpl() {}

	/**
	 * Draws the screen and all the components in it.
	 */
	public void drawScreen(int var1, int var2, float var3) {
		if (!exitMinecraft) {
			if (this.immediateEvent) {
				this.handleInput();
			}

			this.drawScreenImpl();
			this.container.drawComponents(var3);
		}
	}

	protected void drawScreenImpl() {}

	/**
	 * Called when the screen is unloaded. Used to disable keyboard repeat events
	 */
	public final void onGuiClosed() {
		if (!this.closed) {
			this.closed = true;
			endComposition();
			this.closeScreen = true;
			this.moveFocus(false);
			this.onGuiClosedImpl();
			super.onGuiClosed();
			this.mc.inGameHasFocus = true;
			this.mc.mouseHelper.grabMouseCursor();

			try {
				Keyboard.destroy();
				Keyboard.create();
			} catch (LWJGLException var2) {
				var2.printStackTrace();
			}
		}
	}

	public void onGuiClosedImpl() {}

	public void closeScreen() {
		this.moveFocus(false);
		this.closeScreen = true;
	}

	/**
	 * Causes the screen to lay out its subcomponents again. This is the equivalent of the Java call Container.validate()
	 */
	public void setWorldAndResolution(Minecraft var1, int var2, int var3) {
		super.setWorldAndResolution(var1, var2, var3);
		this.container.setBounds(0, 0, var2, var3);
	}

	/**
	 * Delegates mouse and keyboard input.
	 */
	public void handleInput() {
		super.handleInput();

		while (true) {
			Long var1 = (Long)this.keyEventQueue.poll();

			if (var1 == null) {
				if (!Mouse.isButtonDown(0) && !Mouse.isButtonDown(1) && !Mouse.isButtonDown(2)) {
					this.mouseOwner = null;
				}

				return;
			}

			if ((var1.longValue() & -1L) == 87L) {
				theMinecraft.toggleFullscreen();
			} else {
				long o = var1.longValue();
				int modifiersEx = (int)(o >>> 48);
				char keyChar = (char)(o >>> 32);
				int mappedKeyCode = (int)(o & 0xFFFFFFFF);
				this.key(keyChar, mappedKeyCode, modifiersEx);
			}
		}
	}

	/**
	 * Handles keyboard input.
	 */
	public void handleKeyboardInput() {
		if (Keyboard.getEventKeyState()) {
			if (Keyboard.getEventKey() == 87) {
				this.mc.toggleFullscreen();
				return;
			}

			this.key(Keyboard.getEventCharacter(), Keyboard.getEventKey(), 0);
		}
	}

	/**
	 * Handles mouse input.
	 */
	public void handleMouseInput() {
		long var1 = Mouse.getEventNanoseconds() / 1000000L;
		int var3 = Mouse.getEventX() * this.width / this.mc.displayWidth;
		int var4 = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
		int var5 = Mouse.getEventButton();
		int var6 = Mouse.getEventDWheel() / 120;

		if (Mouse.getEventButtonState()) {
			this.mouseClicked(var3, var4, var5);
		} else {
			this.mouseMovedOrUp(var3, var4, var5);
		}

		if (var3 != this.mouseX || var4 != this.mouseY || var5 != -1 || var6 != 0) {
			JGuiComponent var7 = this.container.locationToComponent(var3, var4, true);
			JMouseEvent var9;
			Point var8;

			if (var7 != this.mouseOver) {
				if (this.mouseOver != null) {
					var8 = this.mouseOver.getAbsoluteLocation();
					var9 = new JMouseEvent(this.mouseOver, 505, var1, var3 - var8.x, var4 - var8.y, -1, 0);
					this.mouseOver.fireMouseEvent(var9);
				}

				if (var7 != null) {
					var8 = var7.getAbsoluteLocation();
					var9 = new JMouseEvent(var7, 504, var1, var3 - var8.x, var4 - var8.y, -1, 0);
					var7.fireMouseEvent(var9);
				}

				this.mouseOver = var7;
			}

			if (var3 != this.mouseX || var4 != this.mouseY) {
				this.mouseX = var3;
				this.mouseY = var4;

				if (this.mouseOwner != null) {
					var8 = this.mouseOwner.getAbsoluteLocation();
					var9 = new JMouseEvent(this.mouseOwner, 506, var1, var3 - var8.x, var4 - var8.y, -1, 0);
					this.mouseOwner.fireMouseEvent(var9);
				} else if (var7 != null) {
					var8 = var7.getAbsoluteLocation();
					var9 = new JMouseEvent(var7, 503, var1, var3 - var8.x, var4 - var8.y, -1, 0);
					var7.fireMouseEvent(var9);
				}
			}

			if (var5 != -1) {
				if (Mouse.getEventButtonState()) {
					if (this.mouseOwner != null) {
						var8 = this.mouseOwner.getAbsoluteLocation();
						var9 = new JMouseEvent(this.mouseOwner, 501, var1, var3 - var8.x, var4 - var8.y, var5, 0);
						this.mouseOwner.fireMouseEvent(var9);
					} else if (var7 != null) {
						this.mouseOwner = var7;
						var8 = var7.getAbsoluteLocation();
						var9 = new JMouseEvent(var7, 501, var1, var3 - var8.x, var4 - var8.y, var5, 0);
						var7.fireMouseEvent(var9);
						this.setFocus(var7);
					}
				} else {
					assert this.mouseOwner != null;

					if (this.mouseOwner != null) {
						var8 = this.mouseOwner.getAbsoluteLocation();
						var9 = new JMouseEvent(this.mouseOwner, 502, var1, var3 - var8.x, var4 - var8.y, var5, 0);
						this.mouseOwner.fireMouseEvent(var9);
					}
				}
			}

			if (var6 != 0) {
				this.mouseWheelMoved(var3, var4, var6);

				if (this.mouseOwner != null) {
					var8 = this.mouseOwner.getAbsoluteLocation();
					var9 = new JMouseEvent(this.mouseOwner, 507, var1, var3 - var8.x, var4 - var8.y, 0, var6);
					this.mouseOwner.fireMouseEvent(var9);
				} else if (var7 != null) {
					var8 = var7.getAbsoluteLocation();
					var9 = new JMouseEvent(var7, 507, var1, var3 - var8.x, var4 - var8.y, 0, var6);
					var7.fireMouseEvent(var9);
				}
			}
		}
	}

	private final void key(char c, int i, int modifies) {
		this.keyTyped(c, i);

		if (this.focusOwner != null) {
			this.focusOwner.keyTyped(c, i, modifies);
		}
	}

	/**
	 * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
	 */
	protected void keyTyped(char c, int i) {
		if (i == 1) {
			closeScreen();
		}

		if (i == 60) {
			saveScreenshot();
		}
	}

	protected void mouseWheelMoved(int x, int y, int wheel) {}

	public final JGuiComponent locationToComponent(int x, int y) {
		return this.container.locationToComponent(x, y, false);
	}

	public JGuiComponent add(JGuiComponent component) {
		return this.container.add(component);
	}

	public void remove(JGuiComponent component) {
		this.container.remove(component);
	}

	protected void moveFocus(final boolean moveToComponent) {
		final CountDownLatch cdl = new CountDownLatch(1);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwner();

				if (moveToComponent) {
					JGuiScreen.imComponent.setVisible(true);
					JGuiScreen.imComponent.requestFocusInWindow();
				} else {
					JGuiScreen.imComponent.setVisible(false);
					JGuiScreen.mcCanvas.requestFocusInWindow();
				}

				cdl.countDown();
			}
		});

		try {
			cdl.await(10L, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
		}
	}

	public void setFocus(final JGuiComponent component) {
		if ((this.focusOwner != component) && ((component == null) || (component.isFocusable()))) {
			InputContext inputContext = imComponent.getInputContext();

			if (inputContext != null) {
				final CountDownLatch cdl = new CountDownLatch(1);
				inputContext.endComposition();
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						JGuiScreen.this.focusOwner = component;
						JGuiScreen.imComponent.enableInputMethods(JGuiScreen.this.focusOwner instanceof JGuiInputMethod);
						cdl.countDown();
					}
				});

				try {
					cdl.await(10L, TimeUnit.MILLISECONDS);
				} catch (Exception e) {
				}
			}
		}
	}

	public boolean isFocusOwner(JGuiComponent component) {
		return this.focusOwner == component;
	}

	public boolean isMouseOver(JGuiComponent component) {
		return this.mouseOver == component;
	}

	public boolean isMouseOwner(JGuiComponent component) {
		return this.mouseOwner == component;
	}

	public int getTickCount() {
		return this.tickCount;
	}

	static void getLocationOnScreen() {
		mcCanvasLocationOnScreen = mcContainer.getLocationOnScreen();
	}

	public static int getMappedKeyCode(KeyEvent e) {
		return getMappedKeyCode(e.getKeyCode(), e.getKeyLocation());
	}

	public static int getMappedKeyCode(int key_code, int position) {
		switch (key_code) {
			case 18:
				return position == 3 ? 184 : 56;

			case 157:
				return position == 3 ? 220 : 219;

			case 16:
				return position == 3 ? 54 : 42;

			case 17:
				return position == 3 ? 157 : 29;
		}

		return KEY_MAP[key_code];
	}

	public static int getMappedKeyCode2(int key) {
		return KEY_MAP2[key];
	}

	public static void endComposition() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				InputContext inputContext = JGuiScreen.imComponent.getInputContext();

				if (inputContext != null) {
					inputContext.endComposition();
				}
			}
		});
	}

	private static boolean isWindowActive() {
		return (mcFrame != null) && (mcFrame.isActive());
	}

	public void saveScreenshot() {
		this.isTakingScreenshot = true;
	}

	public static boolean isControlPressed() {
		return InputMethodComponent.pressed[17] || GuiScreen.isCtrlKeyDown();
	}

	public static boolean isShiftPressed() {
		return InputMethodComponent.pressed[16] || GuiScreen.isShiftKeyDown();
	}

	static {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				JGuiScreen.exitMinecraft = true;
			}
		});
		theMinecraft = Minecraft.getMinecraft();
		mcCanvas = theMinecraft.mcCanvas;
		Container tempContainer = mcCanvas.getParent();

		while (tempContainer.getParent() != null) {
			tempContainer = tempContainer.getParent();
		}

		mcContainer = tempContainer;
		mcFrame = (Frame)((mcContainer instanceof Frame) ? mcContainer : null);
		mcContainer.addComponentListener(new ComponentListener() {
			public void componentHidden(ComponentEvent e) {
				JGuiScreen.getLocationOnScreen();
			}
			public void componentMoved(ComponentEvent e) {
				JGuiScreen.getLocationOnScreen();
			}
			public void componentResized(ComponentEvent e) {
				JGuiScreen.getLocationOnScreen();
			}
			public void componentShown(ComponentEvent e) {
				JGuiScreen.getLocationOnScreen();
			}
		});
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JGuiScreen.getLocationOnScreen();
			}
		});
		imComponent = new InputMethodComponent();
		mcContainer.add(imComponent, "South");
		mcContainer.addMouseWheelListener(imComponent);
		KEY_MAP[48] = 11;
		KEY_MAP[49] = 2;
		KEY_MAP[50] = 3;
		KEY_MAP[51] = 4;
		KEY_MAP[52] = 5;
		KEY_MAP[53] = 6;
		KEY_MAP[54] = 7;
		KEY_MAP[55] = 8;
		KEY_MAP[56] = 9;
		KEY_MAP[57] = 10;
		KEY_MAP[65] = 30;
		KEY_MAP[107] = 78;
		KEY_MAP[65406] = 184;
		KEY_MAP[512] = 145;
		KEY_MAP[66] = 48;
		KEY_MAP[92] = 43;
		KEY_MAP[8] = 14;
		KEY_MAP[67] = 46;
		KEY_MAP[20] = 58;
		KEY_MAP[514] = 144;
		KEY_MAP[93] = 27;
		KEY_MAP[513] = 146;
		KEY_MAP[44] = 51;
		KEY_MAP[28] = 121;
		KEY_MAP[68] = 32;
		KEY_MAP[110] = 83;
		KEY_MAP[127] = 211;
		KEY_MAP[111] = 181;
		KEY_MAP[40] = 208;
		KEY_MAP[69] = 18;
		KEY_MAP[35] = 207;
		KEY_MAP[10] = 28;
		KEY_MAP[61] = 13;
		KEY_MAP[27] = 1;
		KEY_MAP[70] = 33;
		KEY_MAP[112] = 59;
		KEY_MAP[121] = 68;
		KEY_MAP[122] = 87;
		KEY_MAP[123] = 88;
		KEY_MAP[61440] = 100;
		KEY_MAP[61441] = 101;
		KEY_MAP[61442] = 102;
		KEY_MAP[113] = 60;
		KEY_MAP[114] = 61;
		KEY_MAP[115] = 62;
		KEY_MAP[116] = 63;
		KEY_MAP[117] = 64;
		KEY_MAP[118] = 65;
		KEY_MAP[119] = 66;
		KEY_MAP[120] = 67;
		KEY_MAP[71] = 34;
		KEY_MAP[72] = 35;
		KEY_MAP[36] = 199;
		KEY_MAP[73] = 23;
		KEY_MAP[''] = 210;
		KEY_MAP[74] = 36;
		KEY_MAP[75] = 37;
		KEY_MAP[21] = 112;
		KEY_MAP[25] = 148;
		KEY_MAP[76] = 38;
		KEY_MAP[37] = 203;
		KEY_MAP[77] = 50;
		KEY_MAP[45] = 12;
		KEY_MAP[106] = 55;
		KEY_MAP[78] = 49;
		KEY_MAP[''] = 69;
		KEY_MAP[96] = 82;
		KEY_MAP[97] = 79;
		KEY_MAP[98] = 80;
		KEY_MAP[99] = 81;
		KEY_MAP[100] = 75;
		KEY_MAP[101] = 76;
		KEY_MAP[102] = 77;
		KEY_MAP[103] = 71;
		KEY_MAP[104] = 72;
		KEY_MAP[105] = 73;
		KEY_MAP[79] = 24;
		KEY_MAP[91] = 26;
		KEY_MAP[80] = 25;
		KEY_MAP[34] = 209;
		KEY_MAP[33] = 201;
		KEY_MAP[19] = 197;
		KEY_MAP[46] = 52;
		KEY_MAP[81] = 16;
		KEY_MAP[82] = 19;
		KEY_MAP[39] = 205;
		KEY_MAP[83] = 31;
		KEY_MAP[''] = 70;
		KEY_MAP[59] = 39;
		KEY_MAP[108] = 83;
		KEY_MAP[47] = 53;
		KEY_MAP[32] = 57;
		KEY_MAP[65480] = 149;
		KEY_MAP[109] = 74;
		KEY_MAP[84] = 20;
		KEY_MAP[9] = 15;
		KEY_MAP[85] = 22;
		KEY_MAP[38] = 200;
		KEY_MAP[86] = 47;
		KEY_MAP[87] = 17;
		KEY_MAP[88] = 45;
		KEY_MAP[89] = 21;
		KEY_MAP[90] = 44;
		int i = 0;

		for (int j = KEY_MAP.length; i < j; i++) {
			KEY_MAP2[KEY_MAP[i]] = i;
		}
	}

	private static class InputMethodComponent extends Component
		implements KeyListener, InputMethodListener, MouseWheelListener {
		private static GuiScreen currentScreen;
		private static boolean[] pressed = new boolean[65535];

		private InputMethodComponent() {
			setPreferredSize(new Dimension(0, 0));
			addKeyListener(this);
			addInputMethodListener(this);
			enableInputMethods(true);
			setFocusable(true);
			setFocusTraversalKeysEnabled(false);
		}

		public InputMethodRequests getInputMethodRequests() {
			return getJGuiInputMethod();
		}

		public void caretPositionChanged(InputMethodEvent event) {
			JGuiInputMethod im = getJGuiInputMethod();

			if (im != null) {
				im.caretPositionChanged(event);
			}
		}

		public void inputMethodTextChanged(InputMethodEvent event) {
			JGuiInputMethod im = getJGuiInputMethod();

			if (im != null) {
				im.inputMethodTextChanged(event);
			}
		}

		public void keyPressed(KeyEvent e) {
			JGuiScreen screen = getCurrentScreen();

			if (screen == null) {
				return;
			}

			char keyChar = e.getKeyChar();
			int keyCode = e.getKeyCode();

			if ((!Keyboard.areRepeatEventsEnabled()) && pressed[keyCode]) {
				return;
			}

			pressed[keyCode] = true;

			if (keyChar == 65535) {
				keyChar = '\000';
			}

			screen.keyEventQueue.add(((long)(e.getModifiersEx() & 0xFFFF) << 48) |
									((long)(keyChar & 0xFFFF) << 32) |
									((long)JGuiScreen.getMappedKeyCode(e) & 0xFFFFFFFF));
		}

		public void keyReleased(KeyEvent e) {
			pressed[e.getKeyCode()] = false;
		}

		public void keyTyped(KeyEvent e) {
		}

		public void mouseWheelMoved(MouseWheelEvent e) {
			JGuiScreen current = getCurrentScreen();

			if (current != null) {
				Insets insets = JGuiScreen.mcContainer.getInsets();
				int x = e.getX() - insets.left;
				int y = e.getY() - insets.top;
				int mx = x * current.width / JGuiScreen.theMinecraft.displayWidth;
				int my = y * current.height / JGuiScreen.theMinecraft.displayHeight;
				current.mouseWheelMoved(mx, my, -e.getWheelRotation());
			}
		}

		private JGuiScreen getCurrentScreen() {
			GuiScreen screen = JGuiScreen.theMinecraft.currentScreen;

			if (currentScreen != screen) {
				Arrays.fill(pressed, false);
				currentScreen = screen;
			}

			return (screen instanceof JGuiScreen) ? (JGuiScreen)screen : null;
		}

		private JGuiComponent getFocusOwner() {
			JGuiScreen screen = getCurrentScreen();
			return screen == null ? null : screen.focusOwner;
		}

		private JGuiInputMethod getJGuiInputMethod() {
			JGuiComponent component = getFocusOwner();
			return (component instanceof JGuiInputMethod) ? (JGuiInputMethod)component : null;
		}
	}
}