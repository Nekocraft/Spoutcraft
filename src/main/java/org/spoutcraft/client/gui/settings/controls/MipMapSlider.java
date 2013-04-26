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

import org.spoutcraft.api.gui.GenericSlider;
import org.spoutcraft.client.config.Configuration;
import org.spoutcraft.client.config.MipMapUtils;

public class MipMapSlider extends GenericSlider {
	public MipMapSlider() {
		super("地形的贴图");
		this.setSliderPosition(Configuration.getMipmapsPercent());
		setTooltip("地形的贴图\n" +
				"ON - 降低像素化为期不远的地形。然而，并非所有\n" +
				"显卡支持，和一些纹理包处理不善。\n" +
				"OFF - 正常MINECRAFT地形的.");
	}

	@Override
	public String getText() {
		if (this.getSliderPosition() == 0F) {
			return "地形的贴图: OFF";
		}
		return "地形的贴图: " + (int)(this.getSliderPosition() * 100) + "%";
	}

	@Override
	public void onSliderDrag(float old, float newPos) {
		Configuration.setMipmapsPercent(newPos);
		Configuration.write();
		MipMapUtils.update();
	}
}
