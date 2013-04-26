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

import org.spoutcraft.client.config.Configuration;

public class FarViewButton extends AutomatedCheckBox {
	public FarViewButton() {
		super("距离设置");
		setChecked(Configuration.isFarView());
		setTooltip("距离设置\n" +
				"OFF - （默认）标准视图距离\n" +
				"开 - 3X视图距离\n" +
				"资源要求非常苛刻！\n" +
				"3倍视距=>9x中要加载块=> FPS/ 9\n" +
				"标准视图距离：32，64，128，256\n" +
				"远观距离：96，192，384，512");
	}

	@Override
	public void onButtonClick() {
		Configuration.setFarView(!Configuration.isFarView());
		Configuration.write();

		if (Minecraft.theMinecraft.theWorld != null) {
			Minecraft.theMinecraft.renderGlobal.updateAllRenderers();
		}
	}
}
