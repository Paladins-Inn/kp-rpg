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

package de.kaiserpfalzedv.rpg.torg.foundry.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import de.kaiserpfalzedv.rpg.torg.model.actors.Attribute;
import de.kaiserpfalzedv.rpg.torg.model.actors.AttributeValue;
import lombok.*;
import org.bson.codecs.pojo.annotations.BsonIgnore;

import java.beans.Transient;
import java.util.Set;

/**
 * Attributes --
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 2.0.0  2021-06-04
 */
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonDeserialize(builder = Attributes.AttributesBuilder.class)
@Builder(setterPrefix = "with", toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Attributes {
    private int charisma;
    private int dexterity;
    private int mind;
    private int spirit;
    private int strength;

    @JsonIgnore
    @BsonIgnore
    @Transient
    public Set<AttributeValue> getAttributes() {
        return Set.of(
                new AttributeValue(Attribute.CHARISMA, charisma, 0, charisma),
                new AttributeValue(Attribute.DEXTERITY, dexterity, 0, dexterity),
                new AttributeValue(Attribute.MIND, mind, 0, mind),
                new AttributeValue(Attribute.SPIRIT, spirit, 0, spirit),
                new AttributeValue(Attribute.STRENGTH, strength, 0, strength)
        );
    }
}
