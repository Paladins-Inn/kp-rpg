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

package de.kaiserpfalzedv.rpg.integrations.discord.testsupport;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.managers.ChannelManager;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

/**
 * A fake text channel for testing purposes.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0  2021-02-05
 */
public class TestTextChannel implements TextChannel {
    private final long id;
    private final Guild guild;
    private final String name;
    private final ChannelType type;

    public TestTextChannel(
            final long id,
            final Guild guild,
            final String name,
            final ChannelType type
    ) {
        this.id = id;
        this.guild = guild;
        this.name = name;
        this.type = type;
    }

    @Nullable
    @Override
    public String getTopic() {
        return null;
    }

    @Override
    public boolean isNSFW() {
        return false;
    }

    @Override
    public boolean isNews() {
        return false;
    }

    @Override
    public int getSlowmode() {
        return 0;
    }

    @NotNull
    @Override
    public ChannelType getType() {
        return type;
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
        return name;
    }

    @NotNull
    @Override
    public Guild getGuild() {
        return guild;
    }

    @Nullable
    @Override
    public Category getParent() {
        return null;
    }

    @NotNull
    @Override
    public List<Member> getMembers() {
        return null;
    }

    @Override
    public int getPosition() {
        return 0;
    }

    @Override
    public int getPositionRaw() {
        return 0;
    }

    @NotNull
    @Override
    public JDA getJDA() {
        return TestJDA.JDA;
    }

    @Nullable
    @Override
    public PermissionOverride getPermissionOverride(@NotNull IPermissionHolder permissionHolder) {
        return null;
    }

    @NotNull
    @Override
    public List<PermissionOverride> getPermissionOverrides() {
        return null;
    }

    @NotNull
    @Override
    public List<PermissionOverride> getMemberPermissionOverrides() {
        return null;
    }

    @NotNull
    @Override
    public List<PermissionOverride> getRolePermissionOverrides() {
        return null;
    }

    @Override
    public boolean isSynced() {
        return false;
    }

    @NotNull
    @Override
    public ChannelAction<TextChannel> createCopy(@NotNull Guild guild) {
        return null;
    }

    @NotNull
    @Override
    public ChannelAction<TextChannel> createCopy() {
        return null;
    }

    @NotNull
    @Override
    public ChannelManager getManager() {
        return null;
    }

    @NotNull
    @Override
    public AuditableRestAction<Void> delete() {
        return null;
    }

    @NotNull
    @Override
    public PermissionOverrideAction createPermissionOverride(@NotNull IPermissionHolder permissionHolder) {
        return null;
    }

    @NotNull
    @Override
    public PermissionOverrideAction putPermissionOverride(@NotNull IPermissionHolder permissionHolder) {
        return null;
    }

    @NotNull
    @Override
    public InviteAction createInvite() {
        return null;
    }

    @NotNull
    @Override
    public RestAction<List<Invite>> retrieveInvites() {
        return null;
    }

    @NotNull
    @Override
    public RestAction<List<Webhook>> retrieveWebhooks() {
        return null;
    }

    @NotNull
    @Override
    public WebhookAction createWebhook(@NotNull String name) {
        return null;
    }

    @NotNull
    @Override
    public RestAction<Void> deleteMessages(@NotNull Collection<Message> messages) {
        return null;
    }

    @NotNull
    @Override
    public RestAction<Void> deleteMessagesByIds(@NotNull Collection<String> messageIds) {
        return null;
    }

    @NotNull
    @Override
    public AuditableRestAction<Void> deleteWebhookById(@NotNull String id) {
        return null;
    }

    @NotNull
    @Override
    public RestAction<Void> clearReactionsById(@NotNull String messageId) {
        return null;
    }

    @NotNull
    @Override
    public RestAction<Void> clearReactionsById(@NotNull String messageId, @NotNull String unicode) {
        return null;
    }

    @NotNull
    @Override
    public RestAction<Void> clearReactionsById(@NotNull String messageId, @NotNull Emote emote) {
        return null;
    }

    @NotNull
    @Override
    public RestAction<Void> removeReactionById(@NotNull String messageId, @NotNull String unicode, @NotNull User user) {
        return null;
    }

    @Override
    public boolean canTalk() {
        return false;
    }

    @Override
    public boolean canTalk(@NotNull Member member) {
        return false;
    }

    @Override
    public int compareTo(@NotNull GuildChannel o) {
        return 0;
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
