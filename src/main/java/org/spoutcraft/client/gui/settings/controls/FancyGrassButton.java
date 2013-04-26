/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011 Spout LLC <http://www.spout.org/>
 * Spoutcraft is licensed under the GNU Lesser General Public License.
 *
 * Spoutcraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Spoutcraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.spoutcraft.client.gui.settings.controls;

import java.util.UUID;

import net.minecraft.client.Minecraft;

import org.spoutcraft.client.config.Configuration;

public class FancyGrassButton extends AutomatedCheckBox {
	UUID fancyGraphics;
	public FancyGrassButton(UUID fancyGraphics) {
		super("草渲染");
		this.fancyGraphics = fancyGraphics;
		setChecked(Configuration.isFancyGrass());
		setTooltip("草\n" +
				"快 - 低质量，更快\n" +
				"质量 - 质量更高，速度较慢\n" +
				"使用默认的快速草地边纹理。\n" +
				"花式草使用生物群落侧纹理.");
	}

	@Override
	public void onButtonClick() {
		Configuration.setFancyGrass(!Configuration.isFancyGrass());
		Configuration.write();
		((FancyGraphicsButton)getScreen().getWidget(fancyGraphics)).custom = true;

		if (Minecraft.theMinecraft.theWorld != null) {
			Minecraft.theMinecraft.renderGlobal.updateAllRenderers();
		}
	}
}
