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
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import de.kaiserpfalzedv.rpg.torg.model.MapperEnum;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Damage -- The damage of an {@link Attack}.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 0.3.0  2021-05-23
 */
@Builder(setterPrefix = "with", toBuilder = true)
@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
@JsonDeserialize(builder = Damage.DamageBuilder.class)
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@Schema(description = "Type of damage and the value")
@Slf4j
public class Damage {

    @AllArgsConstructor
    @Getter
    public enum DamageType implements MapperEnum<DamageType> {
        fixed("flat", "fixed"),
        cha("charismaPlus", "cha"),
        dex("dexterityPlus", "dex"),
        min("mindPlus", "min"),
        spi("spiritPlus", "spi"),
        str("strengthPlus", "str");

        private final String foundry;
        private final String roll20;

        public static Optional<DamageType> mapFoundry(@NotNull final String name) {
            return fixed.mapFromFoundry(name);
        }

        @Override
        public Optional<DamageType> mapFromFoundry(final String name) {
            log.trace("Mapping damage type. name='{}'", name);

            if (name == null || "null".equals(name)) {
                return Optional.empty();
            }

            return all().stream()
                            .filter(e -> e.foundry.equals(name)).distinct()
                            .findFirst();
        }

        @Override
        public Optional<DamageType> mapFromRoll20(String name) {
            return all().stream()
                            .filter(e -> e.roll20.equals(name)).distinct()
                            .findFirst();
        }

        public Set<DamageType> all() {
            return Set.of(fixed, cha, dex, min, spi, str);
        }
    }

    @Schema(description = "Type of the damage. Either 'fixed' or the base attribute.", defaultValue = "fixed")
    @Builder.Default
    private final DamageType type = DamageType.fixed;

    @Schema(description = "The adds for damage calculated. Will be ignored when type is 'fixed'.")
    private final Integer adds;

    @Schema(description = "The total value of damage or the adds for the damage (to be added to the attribute specified in type.")
    private final Integer value;
}
