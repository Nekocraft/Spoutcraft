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

import org.spoutcraft.api.gui.GenericCheckBox;
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.config.Configuration;

public class WeatherToggleButton extends GenericCheckBox {
	public WeatherToggleButton() {
		super("天气");
		setChecked(Configuration.isWeather());
		setEnabled(SpoutClient.getInstance().isWeatherCheat());
		setTooltip("天气\n" +
				"开 - 天气活跃，慢\n" +
				"关 - 天气不活跃，速度更快\n" +
				"天气控制雨，雪和雷暴.");
	}

	@Override
	public String getTooltip() {
		if (!isEnabled()) {
			return "此选项在服务器不被允许，它被视为作弊.";
		}
		return super.getTooltip();
	}

	@Override
	public void onButtonClick() {
		Configuration.setWeather(!Configuration.isWeather());
		Configuration.write();
	}
}
