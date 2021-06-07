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
import de.kaiserpfalzedv.rpg.torg.foundry.FoundryMapper;
import de.kaiserpfalzedv.rpg.torg.foundry.PriceMapper;
import de.kaiserpfalzedv.rpg.torg.model.core.Cosm;
import de.kaiserpfalzedv.rpg.torg.model.items.Item;
import de.kaiserpfalzedv.rpg.torg.model.items.ItemData;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.Set;

/**
 * BaseItemMapper -- The strategy base for converting items.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0  2021-06-05
 */
@RequiredArgsConstructor
@Slf4j
public abstract class BaseItemMapper implements FoundryMapper<FoundryItem, Item> {

    private final PriceMapper priceMapper;

    public Item convert(@NotNull final FoundryItem orig) {
        Item.ItemBuilder result = createCommonItemData(orig);

        ItemData.ItemDataBuilder spec = ItemData.builder();

        convertNotes(orig, spec);
        convertDescription(orig, spec);
        convertCosm(orig, spec);
        convertPrice(orig, spec);

        result.withSpec(convertItemSpec(spec, orig));

        return result.build();
    }

    private Item.ItemBuilder createCommonItemData(FoundryItem orig) {
        Item.ItemBuilder result = Item.builder()
                .withKind(Item.KIND)
                .withApiVersion(Item.VERSION)

                .withName(orig.getName())
                .withNamespace(orig.getType().getTitle())

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
        return result;
    }

    private void convertNotes(@NotNull final FoundryItem orig, @NotNull final ItemData.ItemDataBuilder spec) {
        if (orig.getData().getNotes() != null) {
            spec.withNotes(Set.of(orig.getData().getNotes()));
        }
    }

    private void convertDescription(@NotNull final FoundryItem orig, @NotNull final ItemData.ItemDataBuilder spec) {
        if (orig.getData().getDescription() != null
                && !"null".equals(orig.getData().getDescription())
        ) {
            spec.withDescription(orig.getData().getDescription()
                    .replaceAll("<img[^>]+alt='([^'>]+)'[^>]+>", "$1")
                    .replaceAll("<a href=[^>]+>([^<]+)</a>", "$1"));
        }
    }

    private void convertPrice(@NotNull final FoundryItem orig, @NotNull final ItemData.ItemDataBuilder spec) {
        if (orig.getData().getPrice() != null
                && !"null".equals(orig.getData().getPrice())
                && !orig.getData().getPrice().isBlank()
        ) {
            convertPrice(spec, orig);
        }
    }

    public abstract ItemData convertItemSpec(
            @NotNull final ItemData.ItemDataBuilder spec,
            @NotNull final FoundryItem orig
    );

    protected void convertCosm(@NotNull final FoundryItem orig, @NotNull ItemData.ItemDataBuilder result) {
        if (orig.getData().getCosm() != null
                && !orig.getData().getCosm().isBlank()
                && !"(None)".equals(orig.getData().getCosm())
                && !"Universal".equals(orig.getData().getCosm())
        ) {
            Cosm.mapFoundry(orig.getData().getCosm()).ifPresentOrElse(
                    c -> result.withCosms(Collections.singleton(c)),
                    () -> {
                    }
            );
        }
    }

    protected void convertPrice(@NotNull ItemData.ItemDataBuilder result, @NotNull final FoundryItem orig) {
        try {
            result.withPrice(priceMapper.parse(orig.getData().getPrice()));
        } catch (NullPointerException e) {
            log.trace(
                    "No valid price for item. item='{}', id={}, price:'{}'",
                    orig.getName(), orig.get_id(), orig.getData().getPrice()
            );
        }
        result.withDelphiDN(orig.getData().getValue());
    }
}
