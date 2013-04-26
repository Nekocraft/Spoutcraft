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

import org.spoutcraft.client.config.Configuration;

public class FancyWeatherButton extends AutomatedCheckBox {
	UUID fancyGraphics;
	public FancyWeatherButton(UUID fancyGraphics) {
		super("天气渲染");
		this.fancyGraphics = fancyGraphics;
		setChecked(Configuration.isFancyWeather());
		setTooltip("天气渲染\n" +
				"默认值 - 通过图形设置\n" +
				"快速 - 光雨/雪，快\n" +
				"质量 - 暴雨/雪，慢\n" +
				"关 - 无雨/雪，最快\n" +
				"当雨水飞溅和雨的声音\n" +
				"仍然活跃.");
	}

	@Override
	public void onButtonClick() {
		Configuration.setFancyWeather(!Configuration.isFancyWeather());
		Configuration.write();
		((FancyGraphicsButton)getScreen().getWidget(fancyGraphics)).custom = true;
	}
}
