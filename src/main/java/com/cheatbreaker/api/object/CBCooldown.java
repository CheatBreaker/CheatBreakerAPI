/*
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.cheatbreaker.api.object;

import com.google.common.base.Preconditions;
import lombok.Getter;
import org.bukkit.Material;

import java.util.concurrent.TimeUnit;

public final class CBCooldown {

    @Getter private final String message;
    @Getter private final long durationMs;
    @Getter private final Material icon;

    public CBCooldown(String message, long unitCount, TimeUnit unit, Material icon) {
        this.message = Preconditions.checkNotNull(message, "message");
        this.durationMs = unit.toMillis(unitCount);
        this.icon = Preconditions.checkNotNull(icon, "icon");
    }

}