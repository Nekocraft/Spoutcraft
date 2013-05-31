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

import org.spoutcraft.api.gui.GenericButton;
import org.spoutcraft.client.config.Configuration;

public class FastDebugInfoButton extends GenericButton {
	public FastDebugInfoButton() {
		setTooltip("快速调试信息\n" +
				"完整 - 默认调试信息屏幕，速度较慢\n" +
				"快速 - 调试信息屏没有lagmeter，更快\n" +
				"FPS - 仅显示帧每秒，隐藏的调试信息.");
	}

	@Override
	public String getText() {
		switch (Configuration.getFastDebug()) {
			case 0:
				return "调试信息: 完整";

			case 1:
				return "调试信息: 快速";

			case 2:
				return "调试信息: FPS";
		}

		return "Unknown State: " + Configuration.getFastDebug();
	}

	@Override
	public void onButtonClick() {
		Configuration.setFastDebug(Configuration.getFastDebug() + 1);

		if (Configuration.getFastDebug() > 2) {
			Configuration.setFastDebug(0);
		}

		Configuration.write();
	}
}
