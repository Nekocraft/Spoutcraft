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

import org.spoutcraft.client.config.Configuration;

public class SmoothFPSButton extends AutomatedCheckBox {
	public SmoothFPSButton() {
		super("流畅的FPS");
		setChecked(Configuration.isSmoothFPS());
		setTooltip("稳定FPS冲洗图形驱动程序缓冲区\n" +
				"OFF - 没有稳定，FPS可能波动\n" +
				"ON - FPS稳定\n" +
				"此选项图形驱动程序的依赖性，其效果\n" +
				"不总是可用");
	}

	@Override
	public void onButtonClick() {
		Configuration.setSmoothFPS(!Configuration.isSmoothFPS());
		Configuration.write();
	}
}
