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

package de.kaiserpfalzedv.rpg.core.cards;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.kaiserpfalzedv.rpg.core.resources.ResourceAddress;
import org.immutables.value.Value;

import java.io.Serializable;
import java.util.List;

/**
 * The basic data for every card deck.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.0.0 2021-01-07
 */
@Value.Immutable
@Value.Modifiable
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonSerialize(as = ImmutableBasicCardDeckData.class)
@JsonDeserialize(builder = ImmutableBasicCardDeckData.Builder.class)
public interface BasicCardDeckData extends Serializable {
    /**
     * @return The width of the cards of this deck.
     */
    int getWidth();

    /**
     * @return The height of the cards of this deck.
     */
    int getHeight();

    /**
     * @return A picture of the backside of the cards.
     */
    ResourceAddress getBackOfCard();

    /**
     * @return All cards of this deck.
     */
    List<ResourceAddress> getCards();
}
