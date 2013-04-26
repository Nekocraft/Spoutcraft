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

import com.prupe.mcpatcher.TexturePackChangeHandler;

import org.spoutcraft.client.config.Configuration;

public class ConnectedTexturesButton extends AutomatedCheckBox {
	public ConnectedTexturesButton() {
		super("连续材质");
		setChecked(Configuration.isConnectedTextures());
		setTooltip("连接对方美学相同的块的纹理。\n" +
				"\n某些纹理包可能会增加额外的增强功能");
	}

	@Override
	public void onButtonClick() {
		Configuration.setConnectedTextures(!Configuration.isConnectedTextures());
		Configuration.write();

		TexturePackChangeHandler.scheduleTexturePackRefresh();
		if (Minecraft.theMinecraft.theWorld != null) {
			Minecraft.theMinecraft.renderGlobal.updateAllRenderers();
		}
	}
}
