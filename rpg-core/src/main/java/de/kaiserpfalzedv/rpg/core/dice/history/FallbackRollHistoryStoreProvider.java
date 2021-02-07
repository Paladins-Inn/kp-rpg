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

package de.kaiserpfalzedv.rpg.core.dice.history;

import de.kaiserpfalzedv.rpg.core.cards.CardStoreService;
import de.kaiserpfalzedv.rpg.core.store.GenericStoreService;
import io.quarkus.arc.DefaultBean;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;

/**
 * The fallback provider for a memory based roll history store.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0  2021-02-05
 */
@Dependent
public class FallbackRollHistoryStoreProvider {
    /**
     * Produces a card store if no other bean is defined.
     *
     * @return The memory implementation of the {@link CardStoreService}.
     */
    @Produces
    @DefaultBean
    public RollHistoryStoreService memoryRollHistoryStore() {
        return new MemoryRollHistoryStore();
    }
}

class MemoryRollHistoryStore extends GenericStoreService<RollHistory> implements RollHistoryStoreService {
    @Override
    public RollHistory increaseGeneration(final RollHistory data) {
        return ImmutableRollHistory.builder()
                .from(data)
                .metadata(
                        increaseGeneration(data.getMetadata())
                )
                .build();
    }
}

