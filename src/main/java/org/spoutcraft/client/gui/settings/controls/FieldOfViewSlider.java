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

public class FieldOfViewSlider extends GenericSlider {
	public FieldOfViewSlider() {
		super("视野");
		setSliderPosition(Minecraft.theMinecraft.gameSettings.fovSetting);
		setTooltip("视野\n" +
				"调整在游戏中的视场.");
	}

	@Override
	public void onSliderDrag(float old, float newPos) {
		Minecraft.theMinecraft.gameSettings.fovSetting = newPos;
		Minecraft.theMinecraft.gameSettings.saveOptions();
	}

	public String getText() {
		String message = String.valueOf((70 + (int)(this.getSliderPosition() * 40)));
		if (this.getSliderPosition() == 0) {
			message = "正常";
		}
		if (this.getSliderPosition() == 1) {
			message = "广角";
		}
		return "FOV: " + message;
	}
}
