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

public class VoidFogButton extends GenericCheckBox {
	public VoidFogButton() {
		super("虚空迷雾");
		setChecked(Configuration.isVoidFog());
		setEnabled(SpoutClient.getInstance().isVoidFogCheat());
		setTooltip("虚空迷雾\n" +
				"开 - 黑暗雾掩盖视力出现在低\n" +
				"水平的地图。\n" +
				"关 - 正常观看距离在所有高度.");
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
		Configuration.setVoidFog(!Configuration.isVoidFog());
		Configuration.write();
	}
}
