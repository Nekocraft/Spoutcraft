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

import java.util.List;

import net.minecraft.client.Minecraft;

import org.spoutcraft.api.gui.CheckBox;
import org.spoutcraft.client.config.Configuration;

public class FancyGraphicsButton extends AutomatedButton {
	public boolean custom = false;
	private List<CheckBox> linkedButtons = null;
	public FancyGraphicsButton() {
		setTooltip("视觉质量\n" +
				"速度 - 低质量，更快\n" +
				"质量 - 质量更高，速度较慢\n" +
				"改变外观的云，叶，水，\n" +
				"阴影草两侧");
	}

	@Override
	public String getText() {
		return "图像: " + (custom ? "自定义" : (Configuration.isFancyGraphics() ? "质量" : "速度"));
	}

	@Override
	public void onButtonClick() {
		Configuration.setFancyGraphics(!Configuration.isFancyGraphics());

		for (CheckBox check : linkedButtons) {
			if (check.isChecked() != Configuration.isFancyGraphics()) {
				check.setChecked(Configuration.isFancyGraphics());
				check.onButtonClick();
			}
		}

		Minecraft.theMinecraft.gameSettings.fancyGraphics = Configuration.isFancyGraphics();

		if (Minecraft.theMinecraft.theWorld != null) {
			Minecraft.theMinecraft.renderGlobal.updateAllRenderers();
		}

		custom = false;
	}

	public void setLinkedButtons(List<CheckBox> linked) {
		linkedButtons = linked;

		for (CheckBox check : linkedButtons) {
			if (check.isChecked() != Configuration.isFancyGraphics()) {
				custom = true;
				break;
			}
		}
	}
}
