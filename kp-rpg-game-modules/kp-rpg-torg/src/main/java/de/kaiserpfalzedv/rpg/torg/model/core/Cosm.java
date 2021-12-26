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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import de.kaiserpfalzedv.rpg.torg.model.Book.Publication;
import de.kaiserpfalzedv.rpg.torg.model.MapperEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static de.kaiserpfalzedv.rpg.torg.data.Publications.CORE_RULES;

/**
 * Realm -- The realms of the possibility wars with their axioms.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0  2021-03-26
 */
@SuppressWarnings({"unused", "SpellCheckingInspection", "CdiInjectionPointsInspection"})
@Getter
@ToString
@Slf4j
public enum Cosm implements MapperEnum<Cosm> {
    AYSLE("Aysle", "Aysle", 24, 16, 18, 14, Type.MAIN, CORE_RULES),
    CORE_EARTH("Core Earth", "Core Earth", 9, 23, 10, 23, Type.MAIN, CORE_RULES),
    CYBERPAPACY("Cyberpapacy", "Cyberpapacy", 14, 18, 16, 26, Type.MAIN, CORE_RULES),
    LIVING_LAND("Living Land", "Living Land", 1, 7, 24, 6, Type.MAIN, CORE_RULES),
    NILE_EMPIRE("Nile Empire", "Nile Empire", 14, 20, 18, 20, Type.MAIN, CORE_RULES),
    ORRORSH("Orrorsh", "Orrorsh", 16, 18, 16, 18, Type.MAIN, CORE_RULES),
    PAN_PACIFICA("Pan-Pacifica", "Pan-Pacifica", 4, 24, 8, 24, Type.MAIN, CORE_RULES),
    THARKOLD("Tharkold", "Tharkold", 12, 25, 4, 25, Type.MAIN, CORE_RULES),

    AKASHA("akasha", "Akasha", 1, 26, 1, 28, Type.MINOR, null),
    UKHAAN("ukhaan", "Ukhaan", 8, 27, 5, 27, Type.MINOR, null),

    AYSLE_THARKOLD("aysleTharkold", "Aysle/Tharkold", 24, 25, 18, 25, Type.UNKNOWN, null),
    ELFAME("elfame", "Elfame", 24, 11, 18, 14, Type.UNKNOWN, null),
    FAIRY_TALE_AYSLE("fairyTaleAysle", "Fairy Tale Aysle", 24, 16, 18, 14, Type.UNKNOWN, null),
    MECHOPOTAMIA("mechopotamia", "Mechopotamia", 9, 9, 12, 8, Type.UNKNOWN, null),
    THE_DEAD_WORLD("theDeadWorld", "The Dead World", 3, 7, 4, 8, Type.UNKNOWN, null),
    TOMBSPACE("tombSpace", "Tomb Space", 0, 0, 0, 0, Type.UNKNOWN, null),
    WALDECK("waldeck", "Waldeck", 14, 11, 2, 12, Type.UNKNOWN, null);

    public enum Type {
        MAIN,
        MINOR,
        UNKNOWN
    }


    private final String foundry;

    @JsonValue
    private final String roll20;
    private final Publication publication;
    private final Type type;
    private final HashMap<Axiom.AxiomName, Axiom> axioms = new HashMap<>();
    private final String[] laws;


    Cosm(
            @NotNull final String foundry,
            @NotNull final String roll20,
            @NotNull final int magic,
            @NotNull final int social,
            @NotNull final int spirit,
            @NotNull final int tech,
            @NotNull final Type cosmType,
            @NotNull final Publication publication,
            String... laws
    ) {
        axioms.put(Axiom.AxiomName.Magic, Axiom.builder().withName(Axiom.AxiomName.Magic).withValue(tech).build());
        axioms.put(Axiom.AxiomName.Social, Axiom.builder().withName(Axiom.AxiomName.Social).withValue(social).build());
        axioms.put(Axiom.AxiomName.Spirit, Axiom.builder().withName(Axiom.AxiomName.Spirit).withValue(spirit).build());
        axioms.put(Axiom.AxiomName.Tech, Axiom.builder().withName(Axiom.AxiomName.Tech).withValue(tech).build());

        this.foundry = foundry;
        this.roll20 = roll20;
        this.publication = publication;
        this.type = cosmType;
        this.laws = laws;
    }

    @JsonIgnore
    public int[] axioms() {
        return new int[]{
                getMagic(),
                getSocial(),
                getSpirit(),
                getTech()
        };
    }

    @JsonIgnore
    public int getMagic() {
        return axioms.get(Axiom.AxiomName.Magic).getValue();
    }

    @JsonIgnore
    public int getSocial() {
        return axioms.get(Axiom.AxiomName.Social).getValue();
    }

    @JsonIgnore
    public int getSpirit() {
        return axioms.get(Axiom.AxiomName.Spirit).getValue();
    }

    @JsonIgnore
    public int getTech() {
        return axioms.get(Axiom.AxiomName.Tech).getValue();
    }


    @Override
    public Optional<Cosm> mapFromFoundry(@NotNull final String name) {
        log.trace("Mapping cosm. name='{}'", name);

        if ("(None)".equals(name) || "Universal".equals(name)) {
            return Optional.empty();
        }

        return Optional.of(
                allCosms().stream()
                        .filter(e -> e.foundry.equals(name)).distinct()
                        .collect(Collectors.toList()).get(0)
        );
    }

    public static Optional<Cosm> mapFoundry(@NotNull final String name) {
        return Cosm.CORE_EARTH.mapFromFoundry(name);
    }

    @Override
    public Optional<Cosm> mapFromRoll20(@NotNull final String name) {
        return Optional.of(
                allCosms().stream()
                        .filter(e -> e.roll20.equals(name)).distinct()
                        .collect(Collectors.toList()).get(0)
        );
    }


    public Set<Cosm> allCosms() {
        return Set.of(
                AYSLE,
                CORE_EARTH,
                CYBERPAPACY,
                LIVING_LAND,
                NILE_EMPIRE,
                ORRORSH,
                PAN_PACIFICA,
                THARKOLD,

                AKASHA,
                UKHAAN,

                AYSLE_THARKOLD,
                ELFAME,
                FAIRY_TALE_AYSLE,
                MECHOPOTAMIA,
                THE_DEAD_WORLD,
                TOMBSPACE,
                WALDECK
        );
    }
}
