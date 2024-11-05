/*
 * Copyright (c) &today.year Kaiserpfalz EDV-Service, Roland T. Lichti
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.kaiserpfalzedv.rpg.torg.model.actors;

import de.kaiserpfalzedv.rpg.torg.model.MapperEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Attribute -- The attributes defined in Torg.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 0.3.0  2021-05-23
 */
@AllArgsConstructor
@Getter
public enum Attribute implements MapperEnum<Attribute> {
    CHARISMA("CHA", "charisma", "attr_CHA", "charisma"),
    DEXTERITY("DEX", "dexterity", "attr_DEX", "dexterity"),
    MIND("MIN", "mind", "attr_MIN", "mind"),
    SPIRIT("SPI", "spirit", "attr_SPI", "spirit"),
    STRENGTH("STR", "strength", "attr_STR", "strength");

    private final String shortName;
    private final String name;
    private final String roll20;
    private final String foundry;

    public Optional<Attribute> mapFromFoundry(@NotNull final String name) {
        return Optional.ofNullable(
                allAttributes().stream()
                        .filter(e -> e.foundry.equals(name)).distinct()
                        .collect(Collectors.toList()).get(0)
        );
    }

    public Optional<Attribute> mapFromRoll20(@NotNull final String name) {
        return Optional.ofNullable(
                allAttributes().stream()
                        .filter(e -> e.roll20.equals(name)).distinct()
                        .collect(Collectors.toList()).get(0)
        );
    }

    public List<Attribute> allAttributes() {
        return List.of(
                CHARISMA,
                DEXTERITY,
                MIND,
                SPIRIT,
                STRENGTH
        );
    }
}