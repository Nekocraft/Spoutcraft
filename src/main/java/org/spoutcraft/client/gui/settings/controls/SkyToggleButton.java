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

public class SkyToggleButton extends GenericCheckBox {
	public SkyToggleButton() {
		super("天空");
		setChecked(Configuration.isSky());
		setEnabled(SpoutClient.getInstance().isSkyCheat());
		setTooltip("天空\n" +
				"开 - 天空是可见的，慢\n" +
				"关 - 天空是不可见的，速度更快\n" +
				"当天空是关月亮和太阳仍可见.");
	}

	@Override
	public void onButtonClick() {
		Configuration.setSky(!Configuration.isSky());
		Configuration.write();
	}
}
