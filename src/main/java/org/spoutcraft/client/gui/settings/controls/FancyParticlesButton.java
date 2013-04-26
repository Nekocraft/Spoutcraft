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

import java.util.UUID;

import org.spoutcraft.client.config.Configuration;

public class FancyParticlesButton extends AutomatedCheckBox {
	UUID fancyGraphics;
	public FancyParticlesButton(UUID fancyGraphics) {
		super("颗粒渲染");
		this.fancyGraphics = fancyGraphics;
		setChecked(Configuration.isFancyParticles());
		setTooltip("颗粒渲染\n" +
				"快 - 低质量，更快\n" +
				"质量 - 质量更高，速度较慢\n" +
				"快速粒子呈现更少的粒子，只有那些附近的。\n" +
				"质量粒子渲染的所有粒子.");
	}

	@Override
	public void onButtonClick() {
		Configuration.setFancyParticles(!Configuration.isFancyParticles());
		Configuration.write();
		((FancyGraphicsButton)getScreen().getWidget(fancyGraphics)).custom = true;
	}
}
