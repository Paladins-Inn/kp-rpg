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

package de.kaiserpfalzedv.rpg.integrations.datastore.cards;

import de.kaiserpfalzedv.rpg.core.cards.BasicCardData;
import de.kaiserpfalzedv.rpg.core.cards.ImmutableBasicCardData;
import de.kaiserpfalzedv.rpg.integrations.datastore.resources.MongoResourcePointer;

import java.util.Optional;

public class MongoCardData {
    public String description;

    public MongoResourcePointer picture;

    public MongoCardData() {
    }

    public MongoCardData(final BasicCardData orig) {
        description = orig.getDescription().orElse(null);
        picture = new MongoResourcePointer(orig.getPicture());
    }


    public BasicCardData data() {
        ImmutableBasicCardData.Builder result = ImmutableBasicCardData.builder()
                .description(Optional.ofNullable(description))
                .picture(picture.data());

        return result.build();
    }
}
