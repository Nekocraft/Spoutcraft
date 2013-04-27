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

public class AutosaveButton extends AutomatedButton {
	public AutosaveButton() {
		setTooltip("自动保存间隔\n" +
				"默认频率 (2s) 不推荐.\n" +
				"会导致卡顿.");
	}

	@Override
	public String getText() {
		switch(Configuration.getAutosave()) {
			case 0: return "自动保存: 30 min";
			case 1: return "自动保存: 3 min";
			case 2: return "自动保存: 1 min";
			case 3: return "自动保存: 30 sec";
			case 4: return "自动保存: 10 sec";
			case 5: return "自动保存: 2 sec";
		}
		return "未知选项: " + Configuration.getAutosave();
	}

	@Override
	public void onButtonClick() {
		Configuration.setAutosave(Configuration.getAutosave() + 1);
		if (Configuration.getAutosave() > 5) {
			Configuration.setAutosave(0);
		}
		Configuration.write();

		if (Minecraft.theMinecraft.theWorld != null) {
			//Minecraft.theMinecraft.theWorld.autosavePeriod = getAutosaveTicks();
		}
	}

	public static int getAutosaveTicks() {
		switch(Configuration.getAutosave()) {
			case 0: return 30 * 60 * 20;
			case 1: return 3 * 60 * 20;
			case 2: return 60 * 20;
			case 3: return 30 * 20;
			case 4: return 10 * 20;
			case 5: return 2 * 20;
			default: return 40;
		}
	}
}
