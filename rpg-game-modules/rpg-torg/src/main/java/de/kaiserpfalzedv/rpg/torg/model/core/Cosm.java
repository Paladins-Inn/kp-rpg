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
import de.kaiserpfalzedv.rpg.torg.model.Book.Publication;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

import java.util.HashSet;

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
public enum Cosm {
    AYSLE(24, 16, 18, 14, Type.MAIN, CORE_RULES),
    CORE_EARTH(9, 23, 10, 23, Type.MAIN, CORE_RULES),
    CYBERPAPACY(14, 18, 16, 26, Type.MAIN, CORE_RULES),
    LIVING_LAND(1, 7, 24, 6, Type.MAIN, CORE_RULES),
    NILE_EMPIRE(14, 20, 18, 20, Type.MAIN, CORE_RULES),
    ORRORSH(16, 18, 16, 18, Type.MAIN, CORE_RULES),
    PAN_PACIFICA(4, 24, 8, 24, Type.MAIN, CORE_RULES),
    THARKOLD(12, 25, 4, 25, Type.MAIN, CORE_RULES),

    AKASHA(1, 26, 1, 28, Type.MINOR, null),
    UKHAAN(8, 27, 5, 27, Type.MINOR, null),

    AYSLE_THARKOLD(24, 25, 18, 25, Type.UNKNOWN, null),
    ELFAME(24, 11, 18, 14, Type.UNKNOWN, null),
    FAIRY_TALE_AYSLE(24, 16, 18, 14, Type.UNKNOWN, null),
    MECHOPOTAMIA(9, 9, 12, 8, Type.UNKNOWN, null),
    THE_DEAD_WORLD(3, 7, 4, 8, Type.UNKNOWN, null),
    TOMBSPACE(0, 0, 0, 0, Type.UNKNOWN, null),
    WALDECK(14, 11, 2, 12, Type.UNKNOWN, null);

    public enum Type {
        MAIN,
        MINOR,
        UNKNOWN
    }

    private final Publication publication;
    private final Type type;
    private final HashSet<Axiom> axioms = new HashSet<>();
    private final String[] laws;

    Cosm(
            @NotNull final int magic,
            @NotNull final int social,
            @NotNull final int spirit,
            @NotNull final int tech,
            @NotNull final Type cosmType,
            @NotNull final Publication publication,
            String... laws
    ) {
        axioms.add(Axiom.builder().withName(Axiom.AxiomName.Magic).withValue(tech).build());
        axioms.add(Axiom.builder().withName(Axiom.AxiomName.Social).withValue(social).build());
        axioms.add(Axiom.builder().withName(Axiom.AxiomName.Spirit).withValue(spirit).build());
        axioms.add(Axiom.builder().withName(Axiom.AxiomName.Tech).withValue(tech).build());

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
        return axioms.stream()
                .filter(a -> Axiom.AxiomName.Magic.equals(a.getName()))
                .findFirst().orElseThrow()
                .getValue();
    }

    @JsonIgnore
    public int getSocial() {
        return axioms.stream()
                .filter(a -> Axiom.AxiomName.Social.equals(a.getName()))
                .findFirst().orElseThrow()
                .getValue();
    }

    @JsonIgnore
    public int getSpirit() {
        return axioms.stream()
                .filter(a -> Axiom.AxiomName.Spirit.equals(a.getName()))
                .findFirst().orElseThrow()
                .getValue();
    }

    @JsonIgnore
    public int getTech() {
        return axioms.stream()
                .filter(a -> Axiom.AxiomName.Tech.equals(a.getName()))
                .findFirst().orElseThrow()
                .getValue();
    }
}
