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

package de.kaiserpfalzedv.rpg.bot.discord;

import de.kaiserpfalzedv.rpg.integrations.discord.testsupport.TestJDA;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * A fake message channel for tests.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0  2021-02-04
 */
public class FakeMessageChannel implements MessageChannel {
    private final long id;
    private final String name;
    private final long latestMessageId;
    private final ChannelType type;

    @SuppressWarnings("CdiInjectionPointsInspection")
    public FakeMessageChannel(
            final long id,
            final String name,
            final ChannelType type,
            final long latestMessageId
    ) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.latestMessageId = latestMessageId;
    }

    public FakeMessageChannel(
            final String name,
            final ChannelType type
    ) {
        this(0L, name, type, 0L);
    }


    @Override
    public long getLatestMessageIdLong() {
        return latestMessageId;
    }

    @Override
    public boolean hasLatestMessage() {
        return latestMessageId > 0L;
    }

    @NotNull
    @Override
    public String getName() {
        return name;
    }

    @NotNull
    @Override
    public ChannelType getType() {
        return type;
    }

    @NotNull
    @Override
    public JDA getJDA() {
        return TestJDA.JDA;
    }

    @Override
    public long getIdLong() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FakeMessageChannel)) return false;
        FakeMessageChannel that = (FakeMessageChannel) o;
        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", FakeMessageChannel.class.getSimpleName() + "[", "]")
                .add("identity=" + System.identityHashCode(this))
                .add("id=" + id)
                .add("name='" + name + "'")
                .add("latestMessageId=" + latestMessageId)
                .toString();
    }
}
