package reifnsk.gui;

import java.awt.Point;

public class JMouseEvent {
	public static final int MOUSE_PRESSED = 501;
	public static final int MOUSE_RELEASED = 502;
	public static final int MOUSE_DRAGGED = 506;
	public static final int MOUSE_MOVED = 503;
	public static final int MOUSE_EXITED = 505;
	public static final int MOUSE_ENTERED = 504;
	public static final int MOUSE_WHEEL = 507;
	private JGuiComponent source;
	private long when;
	private int id;
	private int x;
	private int y;
	private int button;
	private int wheel;
	private boolean consumed;

	public JMouseEvent(JGuiComponent var1, int var2, long var3, int var5, int var6, int var7, int var8) {
		this.source = var1;
		this.id = var2;
		this.when = var3;
		this.x = var5;
		this.y = var6;
		this.button = var7;
		this.wheel = var8;
	}

	public JGuiComponent getSource() {
		return this.source;
	}

	public int getID() {
		return this.id;
	}

	public long getWhen() {
		return this.when;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public Point getPoint() {
		return new Point(this.x, this.y);
	}

	public int getButton() {
		return this.button;
	}

	public int getWheelScroll() {
		return this.wheel;
	}

	public void consume() {
		this.consumed = true;
	}

	public boolean isConsumed() {
		return this.consumed;
	}
}
