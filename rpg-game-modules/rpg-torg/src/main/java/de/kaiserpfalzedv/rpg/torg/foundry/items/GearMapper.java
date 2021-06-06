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

package de.kaiserpfalzedv.rpg.torg.foundry.items;

import de.kaiserpfalzedv.commons.core.resources.History;
import de.kaiserpfalzedv.commons.core.resources.Metadata;
import de.kaiserpfalzedv.commons.core.resources.Status;
import de.kaiserpfalzedv.rpg.torg.model.items.Item;
import de.kaiserpfalzedv.rpg.torg.model.items.ItemData;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.Dependent;
import java.util.Collections;

import static de.kaiserpfalzedv.rpg.torg.foundry.items.FoundryItemType.GEAR;

/**
 * ItemMapper --
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0  2021-06-05
 */
@Dependent
@Slf4j
public class GearMapper extends BaseItemMapper {
    public Item convert(@NotNull final FoundryItem orig) {
        if (!GEAR.equals(orig.getType())) {
            log.debug("Item is of wrong type. Will be ignored.");
            throw new IllegalArgumentException(String.format("Wrong type of item. supported=%s, actual=%s",
                    GEAR.getTitle(), orig.getType().getTitle()));
        }

        Item.ItemBuilder result = Item.builder()
                .withKind(Item.KIND)
                .withApiVersion(Item.VERSION)

                .withNamespace(orig.getName())

                .withMetadata(Metadata.builder()
                        .withLabel("foundry.id", orig.get_id())
                        .build()
                )

                .withStatus(Status.builder()
                        .withHistory(Collections.singletonList(
                                History.builder()
                                        .withStatus("converted")
                                        .withMessage("Converted from foundry data.")
                                        .build()
                        ))
                        .build()
                );

        try {
            result.withNamespace(orig.getFlags().getCore().getSourceId());
        } catch (NullPointerException e) {
            result.withNamespace("./.");
            log.info("No namespace for item. item={}", orig.getName());
        }

        result.withSpec(convertItemSpec(orig));

        return result.build();
    }

    private ItemData convertItemSpec(final FoundryItem orig) {
        ItemData.ItemDataBuilder result = ItemData.builder();

        result.withCosms(Collections.singleton(orig.getData().getCosm()));
        result.withDescription(orig.getData().getDescription());

        return result.build();
    }
}
