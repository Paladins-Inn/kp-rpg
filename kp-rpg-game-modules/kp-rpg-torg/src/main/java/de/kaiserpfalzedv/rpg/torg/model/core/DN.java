/*
 * Copyright (c) 2021 Kaiserpfalz EDV-Service, Roland T. Lichti.
 *
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
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package de.kaiserpfalzedv.rpg.torg.model.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * DN --
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 0.3.0  2021-05-23
 */
@AllArgsConstructor
@Getter
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public enum DN {
    VERY_EASY("Very Easy", 6, +4),
    EASY("Easy", 8, +2),
    STANDARD("Standard", 10, 0),
    CHALLENGING("Challenging", 12, -2),
    HARD("Hard", 14, -4),
    VERY_HARD("Very Hard", 16, -6),
    HEROIC("Heroic", 18, -8),
    NEAR_IMPOSSIBLE("Near Impossible", 20, -10);

    private final String name;
    private final Integer dn;
    private final Integer modifier;
}
