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

import com.cheatbreaker.api.CheatBreakerAPI;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.bukkit.Location;

@EqualsAndHashCode
@AllArgsConstructor
public final class CBWaypoint {

    @Getter private final String name;
    @Getter private final int x;
    @Getter private final int y;
    @Getter private final int z;
    @Getter private final String world;
    @Getter private final int color;
    @Getter private final boolean forced;
    @Getter private boolean visible = true;

    public CBWaypoint(String name, Location location, int color, boolean forced, boolean visible) {
        this(
            name,
            location.getBlockX(),
            location.getBlockY(),
            location.getBlockZ(),
            CheatBreakerAPI.getInstance().getWorldIdentifier(location.getWorld()),
            color,
            forced, visible
        );
    }

    public CBWaypoint(String name, Location location, int color, boolean forced) {
        this(
                name,
                location.getBlockX(),
                location.getBlockY(),
                location.getBlockZ(),
                CheatBreakerAPI.getInstance().getWorldIdentifier(location.getWorld()),
                color,
                forced,
                true
        );
    }

}