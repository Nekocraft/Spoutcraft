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

public class AdvancedOpenGLButton extends AutomatedButton {
	public AdvancedOpenGLButton() {
		setTooltip("检测可见的几何体\n关闭 - 所以几何体会被渲染 (慢)\n速度优先 - 只有可见的几何体 (最快)\n高级 - 更保守的渲染 (中等)\n这个选项值在显卡支持的情况\n下被启用.");
	}

	@Override
	public String getText() {
		switch (Configuration.getAdvancedOpenGL()) {
			case 0:
				return "高级 OpenGL: 关";

			case 1:
				return "高级 OpenGL: 速度优先";

			case 2:
				return "高级 OpenGL: 高级";
		}

		return "未知选项: " + Configuration.getAdvancedOpenGL();
	}

	@Override
	public void onButtonClick() {
		Configuration.setAdvancedOpenGL(Configuration.getAdvancedOpenGL() + 1);

		if (Configuration.getAdvancedOpenGL() > 2) {
			Configuration.setAdvancedOpenGL(0);
		}

		Configuration.write();
		Minecraft.theMinecraft.gameSettings.advancedOpengl = Configuration.getAdvancedOpenGL() != 0;
		Minecraft.theMinecraft.renderGlobal.setAllRenderesVisible();
	}
}
