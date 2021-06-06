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

package de.kaiserpfalzedv.rpg.torg.foundry.actors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import de.kaiserpfalzedv.rpg.torg.foundry.FoundryResource;
import de.kaiserpfalzedv.rpg.torg.foundry.items.FoundryItem;
import de.kaiserpfalzedv.rpg.torg.foundry.model.Effect;
import de.kaiserpfalzedv.rpg.torg.foundry.model.Flag;
import de.kaiserpfalzedv.rpg.torg.foundry.model.Permission;
import de.kaiserpfalzedv.rpg.torg.foundry.model.Token;
import de.kaiserpfalzedv.rpg.torg.model.actors.ActorType;
import lombok.*;

import java.util.ArrayList;

/**
 * FoundryActor -- A simple actor (archetype, nsc, threat)
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0  2021-06-04
 */
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonDeserialize(builder = FoundryActor.FoundryActorBuilder.class)
@Builder(setterPrefix = "with", toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class FoundryActor implements FoundryResource {
    private String _id;
    private String name;
    private Permission permission;
    private ActorType type;
    private ActorData data;
    private Flag flags;
    private int sort;
    private String img;
    private Token token;
    @Builder.Default
    private final ArrayList<FoundryItem> items = new ArrayList<>();
    @Builder.Default
    private final ArrayList<Effect> effects = new ArrayList<>();
}
