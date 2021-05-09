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
import de.kaiserpfalzedv.rpg.core.resources.ResourceStatus;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class MongoResourceStatus {
    public Long observedGeneration;
    public List<MongoResourceHistory> history;

    public MongoResourceStatus() {
        history = new ArrayList<>();
    }

    public MongoResourceStatus(ResourceStatus orig) {
        observedGeneration = orig.getObservedGeneration();

        if (!orig.getHistory().isEmpty()) {
            history = new ArrayList<>(orig.getHistory().size());

            for (ResourceHistory h : orig.getHistory()) {
                history.add(new MongoResourceHistory(h));
            }
        } else {
            history = new ArrayList<>();
        }
    }

    /**
     * Update history entry
     */
    public void updateHistory(MongoMetaData metaData) {
        if (observedGeneration == null || observedGeneration <= 0L) {
            saved(metaData);
        } else {
            updated(metaData);
        }
    }

    private void saved(MongoMetaData metaData) {
        metaData.generation = 1L;
        observedGeneration = metaData.generation;

        addHistory("saved", null);
    }

    private void updated(MongoMetaData metaData) {
        metaData.generation++;
        observedGeneration = metaData.generation;

        addHistory("updated", null);
    }

    private void addHistory(final String status, final String message) {
        MongoResourceHistory entry = new MongoResourceHistory();

        entry.status = status;
        entry.message = message;
        entry.timeStamp = new MongoOffsetDateTime(OffsetDateTime.now(Clock.systemUTC()));

        history.add(entry);
    }

    public ResourceStatus data() {
        return ResourceStatus.builder()

                .observedGeneration(observedGeneration)
                .history(history())

                .build();
    }

    public List<ResourceHistory> history() {
        return history.stream()
                .map(MongoResourceHistory::history)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public String toString() {

        return new StringJoiner(", ", MongoResourceStatus.class.getSimpleName() + "[", "]")
                .add("observedGeneration=" + observedGeneration)
                .add("history=" + joinHistory())
                .toString();
    }

    private String joinHistory() {
        StringJoiner result = new StringJoiner(",", "[", "]");
        history.forEach(e -> result.add(e.toString()));

        return result.toString();
    }
}
