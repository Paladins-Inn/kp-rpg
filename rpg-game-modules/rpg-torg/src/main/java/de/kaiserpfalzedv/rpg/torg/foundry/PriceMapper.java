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

package de.kaiserpfalzedv.rpg.torg.foundry;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import net.objecthunter.exp4j.ExpressionBuilder;

/**
 * PriceMapper --
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 2.0.0  2021-06-06
 */
@Slf4j
public class PriceMapper {
    public Integer parse(@NotNull final String input) {
        if (input == null || input.isBlank() || "null".equals(input)) {
            log.debug("Price can't be parsed. input='{}'", input);
            return null;
        } else {
            log.trace("Parsing price: input='{}'", input);
        }

        return (int) new ExpressionBuilder(input
                .replaceAll("B", "*1000000000")
                .replaceAll("M", "*1000000")
                .replaceAll("K", "*1000")
                .replace(",", ".")
        ).build().evaluate();
    }
}
