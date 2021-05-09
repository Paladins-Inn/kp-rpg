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

package de.kaiserpfalzedv.rpg.core.user;

import de.kaiserpfalzedv.rpg.core.store.GenericStoreService;
import io.quarkus.arc.AlternativePriority;

import javax.enterprise.context.ApplicationScoped;

/**
 * MemoryUserStore --
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 0.3.0  2021-05-09
 */
@ApplicationScoped
@AlternativePriority(100)
public class MemoryUserStore extends GenericStoreService<User> implements UserStoreService {
    @Override
    public User increaseGeneration(final User data) {
        return User.builder()
                .metadata(
                        increaseGeneration(data.getMetadata())
                )
                .spec(data.getSpec().orElse(null))
                .state(data.getStatus().orElse(null))
                .build();
    }
}
