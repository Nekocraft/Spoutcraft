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

public class BrightnessSlider extends GenericSlider {
	public BrightnessSlider() {
		super("亮度");
		setSliderPosition(Configuration.getBrightnessSlider());
		setTooltip("增加亮度较暗的物体\n" +
				"OFF - 标准亮度\n" +
				"100% - 最大亮度较暗的对象\n" +
				"此选项不改变\n" +
				"全黑色物体的亮度");
	}

	@Override
	public void onSliderDrag(float oldPos, float newPos) {
		Configuration.setBrightnessSlider(newPos);
		Configuration.write();
		Minecraft.theMinecraft.gameSettings.gammaSetting = Configuration.getBrightnessSlider();
	}

	public String getText() {
		if (getSliderPosition() == 0F) {
			return "亮度: 昏暗";
		}

		if (getSliderPosition() == 1F) {
			return "亮度: 明亮";
		}

		return "亮度: " + (int)(this.getSliderPosition() * 100) + "%";
	}
}
