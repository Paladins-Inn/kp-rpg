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

package de.kaiserpfalzedv.rpg.integrations.discord.JDA;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.requests.RestAction;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class NullDiscordUser implements User {
    @NotNull
    @Override
    public String getName() {
        return "-no-user-";
    }

    @NotNull
    @Override
    public String getDiscriminator() {
        return "#";
    }

    @Nullable
    @Override
    public String getAvatarId() {
        return "0";
    }

    @NotNull
    @Override
    public String getDefaultAvatarId() {
        return "0";
    }

    @NotNull
    @Override
    public String getAsTag() {
        return getName();
    }

    @Override
    public boolean hasPrivateChannel() {
        return false;
    }

    @NotNull
    @Override
    public RestAction<PrivateChannel> openPrivateChannel() {
        throw new UnsupportedOperationException();
    }

    @NotNull
    @Override
    public List<Guild> getMutualGuilds() {
        return new ArrayList<>();
    }

    @Override
    public boolean isBot() {
        return false;
    }

    @NotNull
    @Override
    public JDA getJDA() {
        unsupported();

        return null;
    }

    @NotNull
    @Override
    public EnumSet<UserFlag> getFlags() {
        unsupported();

        return null;
    }

    @Override
    public int getFlagsRaw() {
        return 0;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isFake() {
        return false;
    }

    @NotNull
    @Override
    public String getAsMention() {
        return getName();
    }

    @Override
    public long getIdLong() {
        return 0;
    }

    @Contract("->fail")
    private void unsupported() {
        throw new UnsupportedOperationException("This User instance only wraps an ID. Other operations are unsupported");
    }
}
