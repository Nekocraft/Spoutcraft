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

public class BetterGrassButton extends AutomatedButton {
	public BetterGrassButton() {
		setTooltip("更好的玻璃 / 雪\nOFF - 默认材质，最快的\n快速 - 边缘材质，慢的\nFancy - 动态边缘材质，最慢");
	}

	@Override
	public String getText() {
		switch (Configuration.getBetterGrass()) {
			case 0:
				return "更好的玻璃 / 雪: 关闭";

			case 1:
				return "更好的玻璃 / 雪: 快速";

			case 2:
				return "更好的玻璃 / 雪: 最好";
		}

		return "未知选项: " + Configuration.getBetterGrass();
	}

	@Override
	public void onButtonClick() {
		Configuration.setBetterGrass(Configuration.getBetterGrass() + 1);

		if (Configuration.getBetterGrass() > 2) {
			Configuration.setBetterGrass(0);
		}

		Configuration.write();

		if (Minecraft.theMinecraft.theWorld != null) {
			Minecraft.theMinecraft.renderGlobal.updateAllRenderers();
		}
	}
}
