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

import net.minecraft.client.Minecraft;

import org.spoutcraft.api.gui.GenericSlider;
import org.spoutcraft.client.config.Configuration;

public class SmoothLightingSlider extends GenericSlider {
	public SmoothLightingSlider() {
		super("平滑光照");
		setSliderPosition(Configuration.getSmoothLighting());
		setTooltip("平滑光照\n" +
				"关 - 没有平滑光照（更快）\n" +
				"1％ - 光线平滑光照（较慢）\n" +
				"100％ - 暗的平滑光照（较慢）");
	}

	@Override
	public void onSliderDrag(float old, float newPos) {
		Configuration.setSmoothLighting(newPos);
		Minecraft.theMinecraft.gameSettings.ambientOcclusion = Configuration.getSmoothLighting() > 0F ? 0 : 2;
		if (Minecraft.theMinecraft.theWorld != null) {
			Minecraft.theMinecraft.renderGlobal.loadRenderers();
		}
		Configuration.write();
	}

	public String getText() {
		return "平滑光照: " + (int)(this.getSliderPosition() * 100) + "%";
	}
}
