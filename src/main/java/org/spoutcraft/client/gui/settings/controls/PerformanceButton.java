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

import org.lwjgl.opengl.Display;

import net.minecraft.client.Minecraft;

import org.spoutcraft.client.config.Configuration;

public class PerformanceButton extends AutomatedButton {
	public PerformanceButton() {
		setTooltip("FPS限制\n" +
				"最多的FPS - 没有限制（最快）\n" +
				"平衡 - 限制120 FPS（较慢）\n" +
				"节电保护 - 限制40 FPS（最慢）\n" +
				"垂直同步 - 限制监视帧率（60，30，20）\n" +
				"即使平衡和省电降低FPS\n" +
				"限制值还没有达到.");
	}

	@Override
	public String getText() {
		switch (Configuration.getPerformance()) {
			case 0: return "性能: 最大 FPS";
			case 1: return "性能: 平衡";
			case 2: return "性能: 节能";
			case 3: return "性能: 显示器";
		}
		return "未知选项: " + Configuration.getPerformance();
	}

	@Override
	public void onButtonClick() {
		Configuration.setPerformance(Configuration.getPerformance() + 1);
		Configuration.setPerformance(Configuration.getPerformance() & 3);
		Configuration.write();
		Minecraft.theMinecraft.gameSettings.limitFramerate = Configuration.getPerformance();
		Display.setVSyncEnabled(Configuration.getPerformance() == 3);
	}
}
