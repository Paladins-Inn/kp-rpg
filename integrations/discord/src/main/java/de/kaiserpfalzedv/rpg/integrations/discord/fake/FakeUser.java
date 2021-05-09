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

package de.kaiserpfalzedv.rpg.integrations.discord.fake;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.exceptions.RateLimitedException;
import net.dv8tion.jda.api.requests.RestAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;


/**
 * A fake discord user for tests.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0  2021-02-04
 */
public class FakeUser implements User {
    private final long id;
    private final String name;

    private final String avatarId;

    public FakeUser(
            final long id,
            final String name
    ) {
        this.id = id;
        this.name = name;
        this.avatarId = name + "/avatar";
    }

    public FakeUser(
            final String name
    ) {
        this(1L, name);
    }

    @NotNull
    @Override
    public String getName() {
        return name;
    }

    @NotNull
    @Override
    public String getDiscriminator() {
        return null;
    }

    @Nullable
    @Override
    public String getAvatarId() {
        return avatarId;
    }

    @NotNull
    @Override
    public String getDefaultAvatarId() {
        return avatarId;
    }

    @NotNull
    @Override
    public String getAsTag() {
        return null;
    }

    @Override
    public boolean hasPrivateChannel() {
        return false;
    }

    @NotNull
    @Override
    public RestAction<PrivateChannel> openPrivateChannel() {
        return new RestAction<PrivateChannel>() {
            @NotNull
            @Override
            public JDA getJDA() {
                return null;
            }

            @NotNull
            @Override
            public RestAction<PrivateChannel> setCheck(@Nullable BooleanSupplier checks) {
                return null;
            }

            @Override
            public void queue(@Nullable Consumer<? super PrivateChannel> success, @Nullable Consumer<? super Throwable> failure) {

            }

            @Override
            public PrivateChannel complete(boolean shouldQueue) throws RateLimitedException {
                return new PrivateChannel() {
                    @NotNull
                    @Override
                    public User getUser() {
                        return null;
                    }

                    @NotNull
                    @Override
                    public JDA getJDA() {
                        return null;
                    }

                    @NotNull
                    @Override
                    public RestAction<Void> close() {
                        return null;
                    }

                    @Override
                    public boolean isFake() {
                        return false;
                    }

                    @Override
                    public long getLatestMessageIdLong() {
                        return 0;
                    }

                    @Override
                    public boolean hasLatestMessage() {
                        return false;
                    }

                    @NotNull
                    @Override
                    public String getName() {
                        return null;
                    }

                    @NotNull
                    @Override
                    public ChannelType getType() {
                        return null;
                    }

                    @Override
                    public long getIdLong() {
                        return 0;
                    }
                };
            }

            @NotNull
            @Override
            public CompletableFuture<PrivateChannel> submit(boolean shouldQueue) {
                return null;
            }
        };
    }

    @NotNull
    @Override
    public List<Guild> getMutualGuilds() {
        return null;
    }

    @Override
    public boolean isBot() {
        return false;
    }

    @NotNull
    @Override
    public JDA getJDA() {
        return null;
    }

    @NotNull
    @Override
    public EnumSet<UserFlag> getFlags() {
        return null;
    }

    @Override
    public int getFlagsRaw() {
        return 0;
    }

    @Override
    public boolean isFake() {
        return false;
    }

    @NotNull
    @Override
    public String getAsMention() {
        return null;
    }

    @Override
    public long getIdLong() {
        return id;
    }
}
