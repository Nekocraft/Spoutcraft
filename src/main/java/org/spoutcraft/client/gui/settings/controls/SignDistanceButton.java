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

public class SignDistanceButton extends AutomatedButton {
	public SignDistanceButton() {
		setTooltip("你可以看到一个牌子上的文字的距离\n" +
				"更远的距离可以降低FPS.");
	}

	@Override
	public String getText() {
		return "牌子距离: " + (Configuration.getSignDistance() != Integer.MAX_VALUE ? Configuration.getSignDistance() : "无限");
	}

	@Override
	public void onButtonClick() {
		if (Configuration.getSignDistance() < 128) {
			Configuration.setSignDistance(Configuration.getSignDistance() * 2);
		} else if (Configuration.getSignDistance() == 128) {
			Configuration.setSignDistance(Integer.MAX_VALUE);
		} else {
			Configuration.setSignDistance(8);
		}

		Configuration.write();
	}
}
