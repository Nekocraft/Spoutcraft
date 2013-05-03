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

public class ChunkRenderSpeed extends AutomatedButton {
	public ChunkRenderSpeed() {
		setTooltip("大块渲染速度\n" +
				"控制快速的块将如何呈现在新的领域。更快的渲染，可能会影响FPS.");
	}

	@Override
	public void onButtonClick() {
		Configuration.setChunkRenderPasses(Configuration.getChunkRenderPasses() * 2);

		if (Configuration.getChunkRenderPasses() > 16) {
			Configuration.setChunkRenderPasses(1);
		} else if (Configuration.getChunkRenderPasses() < 1) {
			Configuration.setChunkRenderPasses(1);
		}

		Configuration.write();
	}

	public String getText() {
		switch (Configuration.getChunkRenderPasses()) {
			case 16:
				return "渲染速度：非常快";

			case 8:
				return "渲染速度：快";

			case 4:
				return "渲染速度：平均";

			case 2:
				return "渲染速度：慢";

			case 1:
				return "渲染速度：最慢";
		}

		return "错误，不明设定！";
	}
}
