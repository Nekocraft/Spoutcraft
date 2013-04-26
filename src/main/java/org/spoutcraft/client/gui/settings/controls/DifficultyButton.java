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

import org.spoutcraft.api.gui.GenericButton;

public class DifficultyButton extends GenericButton {
	public DifficultyButton() {
		super("难度");
		setTooltip("难度\n" +
				"控制游戏的难度.");
	}

	@Override
	public String getText() {
		if (Minecraft.theMinecraft.theWorld != null && Minecraft.theMinecraft.theWorld.getWorldInfo().isHardcoreModeEnabled()) {
			return "难度: 极限";
		}
		String difficulty;
		switch(Minecraft.theMinecraft.gameSettings.difficulty) {
			case 0: difficulty = "和平"; break;
			case 1: difficulty = "简单"; break;
			case 2: difficulty = "普通"; break;
			case 3: difficulty = "困难"; break;
			default: difficulty = "未知"; break;
		}
		return "难度: " + difficulty;
	}

	@Override
	public String getTooltip() {
		if (Minecraft.theMinecraft.theWorld == null) {
			return "不能改变游戏之外的难度";
		}
		if (!Minecraft.theMinecraft.isSingleplayer()) {
			return "无法更改多人难度";
		}
		if (Minecraft.theMinecraft.theWorld.getWorldInfo().isHardcoreModeEnabled()) {
			return "在极限模式无法改变难度";
		}
		return super.getTooltip();
	}

	@Override
	public boolean isEnabled() {
		if (Minecraft.theMinecraft.theWorld == null) {
			return false;
		}
		if (!Minecraft.theMinecraft.isSingleplayer()) {
			return false;
		}
		if (Minecraft.theMinecraft.theWorld.getWorldInfo().isHardcoreModeEnabled()) {
			return false;
		}
		return true;
	}

	@Override
	public void onButtonClick() {
		Minecraft.theMinecraft.gameSettings.difficulty++;
		if (Minecraft.theMinecraft.gameSettings.difficulty > 3) {
			Minecraft.theMinecraft.gameSettings.difficulty = 0;
		}
		Minecraft.theMinecraft.gameSettings.saveOptions();
	}
}
