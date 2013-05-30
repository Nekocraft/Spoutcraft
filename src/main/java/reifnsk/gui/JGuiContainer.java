package reifnsk.gui;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.CopyOnWriteArrayList;
import org.lwjgl.opengl.GL11;

public class JGuiContainer extends JGuiComponent {
	private List components = new CopyOnWriteArrayList();

	public JGuiContainer() {}

	public JGuiContainer(int var1, int var2, int var3, int var4) {
		super(var1, var2, var3, var4);
	}

	final void updateComponents() {
		Iterator var1 = this.components.iterator();

		while (var1.hasNext()) {
			JGuiComponent var2 = (JGuiComponent)var1.next();
			var2.updateComponent();

			if (var2 instanceof JGuiContainer) {
				((JGuiContainer)var2).updateComponents();
			}
		}
	}

	final void drawComponents(float var1) {
		if (this.isVisible() && !JGuiScreen.exitMinecraft) {
			GL11.glPushMatrix();
			GL11.glTranslatef((float)this.getX(), (float)this.getY(), 0.0F);

			try {
				Iterator var2 = this.components.iterator();

				while (var2.hasNext()) {
					JGuiComponent var3 = (JGuiComponent)var2.next();

					if (var3.isVisible()) {
						var3.renderProcess(var1);

						if (var3 instanceof JGuiContainer) {
							((JGuiContainer)var3).drawComponents(var1);
						}
					}
				}
			} finally {
				GL11.glPopMatrix();
			}
		}
	}

	JGuiComponent locationToComponent(int var1, int var2, boolean var3) {
		JGuiComponent var4 = super.locationToComponent(var1, var2, var3);

		if (var4 != null) {
			ListIterator var5 = this.components.listIterator(this.components.size());

			while (var5.hasPrevious()) {
				JGuiComponent var6 = (JGuiComponent)var5.previous();
				var6 = var6.locationToComponent(var1 - var6.getX(), var2 - var6.getY(), var3);

				if (var6 != null) {
					var4 = var6;
					break;
				}
			}
		}

		return var4;
	}

	public final JGuiComponent add(JGuiComponent var1) {
		if (var1 == null) {
			throw new NullPointerException();
		} else if (var1.parent == this) {
			return var1;
		} else if (var1.parent != null) {
			throw new IllegalArgumentException();
		} else if (var1 instanceof JGuiScreenContainer) {
			throw new IllegalArgumentException("top level container");
		} else {
			for (JGuiContainer var2 = var1.parent; var2 != null; var2 = var1.parent) {
				if (var1 == var2) {
					throw new IllegalArgumentException("adding container\'s parent to itself");
				}
			}

			this.components.add(var1);
			var1.parent = this;
			return var1;
		}
	}

	public final void remove(JGuiComponent var1) {
		if (var1 != null) {
			if (var1.parent == this) {
				this.components.remove(var1);
				var1.parent = null;
			}
		}
	}
}
