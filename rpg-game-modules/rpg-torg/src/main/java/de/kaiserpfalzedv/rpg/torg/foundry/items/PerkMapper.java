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

import de.kaiserpfalzedv.rpg.torg.foundry.PriceMapper;
import de.kaiserpfalzedv.rpg.torg.model.items.ItemData;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.Dependent;

/**
 * PerkMapper --
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0  2021-06-05
 */
@Slf4j
@Dependent
public class PerkMapper extends BaseItemMapper {

    public PerkMapper(PriceMapper priceMapper) {
        super(priceMapper);
    }

    @Override
    public ItemData convertItemSpec(
            @NotNull final ItemData.ItemDataBuilder result,
            @NotNull final FoundryItem orig
    ) {
        return result.build();
    }
}