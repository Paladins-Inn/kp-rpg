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

package de.kaiserpfalzedv.rpg.store.resources;

import de.kaiserpfalzedv.rpg.core.resources.ResourceHistory;
import lombok.*;

import java.util.Optional;


/**
 * A single history entry. Basic data is the timestamp, the status and the message.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0  2021-01-31
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class MongoResourceHistory {
    public String status;
    public MongoOffsetDateTime timeStamp;
    public String message;

    public MongoResourceHistory(final ResourceHistory orig) {
        status = orig.getStatus();
        timeStamp = new MongoOffsetDateTime(orig.getTimeStamp());

        if (orig.getMessage() != null && orig.getMessage().isPresent()) {
            message = orig.getMessage().get();
        }
    }

    public ResourceHistory history() {
        ResourceHistory.ResourceHistoryBuilder result = ResourceHistory.builder()
                .status(status)
                .timeStamp(timeStamp.timeStamp());

        if (message != null) result.message(Optional.of(message));

        return result.build();
    }
}
