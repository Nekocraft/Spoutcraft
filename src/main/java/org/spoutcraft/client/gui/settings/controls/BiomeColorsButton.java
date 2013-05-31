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

public class BiomeColorsButton extends AutomatedCheckBox {
	UUID fancyGraphics;
	public BiomeColorsButton(UUID fancyGraphics) {
		super("绚丽生物群落颜色");
		this.fancyGraphics = fancyGraphics;
		this.setChecked(Configuration.isFancyBiomeColors());
		setTooltip("生物群落颜色\n" +
				"快速 - 缓存为每个块的草和水的颜色。\n" +
				"可能会导致颜色块边缘附近的急剧变化。\n" +
				"花式 - 草和水的正常着色。\n" +
				"计算水和草对每个块的颜色。");
	}

	@Override
	public void onButtonClick() {
		Configuration.setFancyBiomeColors(!Configuration.isFancyBiomeColors());
		Configuration.write();
		((FancyGraphicsButton)getScreen().getWidget(fancyGraphics)).custom = true;

		if (Minecraft.theMinecraft.theWorld != null) {
			Minecraft.theMinecraft.renderGlobal.updateAllRenderers();
		}
	}
}
