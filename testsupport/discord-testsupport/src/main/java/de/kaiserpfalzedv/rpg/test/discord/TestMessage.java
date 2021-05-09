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

package de.kaiserpfalzedv.rpg.test.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import net.dv8tion.jda.api.requests.restaction.pagination.ReactionPaginationAction;
import org.apache.commons.collections4.Bag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.OffsetDateTime;
import java.util.EnumSet;
import java.util.Formatter;
import java.util.List;

public class TestMessage implements Message {
    final JDA jda;
    final net.dv8tion.jda.api.entities.Guild guild;
    final TextChannel channel;
    final User user;
    final String message;

    @SuppressWarnings("CdiInjectionPointsInspection")
    public TestMessage(
            final net.dv8tion.jda.api.entities.Guild guild,
            final TextChannel channel,
            final User user,
            final String message
    ) {
        this.jda = new TestJDA();
        this.guild = guild;
        this.channel = channel;
        this.user = user;
        this.message = message;
    }

    @Nullable
    @Override
    public Message getReferencedMessage() {
        return null;
    }

    @NotNull
    @Override
    public List<User> getMentionedUsers() {
        return null;
    }

    @NotNull
    @Override
    public Bag<User> getMentionedUsersBag() {
        return null;
    }

    @NotNull
    @Override
    public List<TextChannel> getMentionedChannels() {
        return null;
    }

    @NotNull
    @Override
    public Bag<TextChannel> getMentionedChannelsBag() {
        return null;
    }

    @NotNull
    @Override
    public List<Role> getMentionedRoles() {
        return null;
    }

    @NotNull
    @Override
    public Bag<Role> getMentionedRolesBag() {
        return null;
    }

    @NotNull
    @Override
    public List<Member> getMentionedMembers(@NotNull net.dv8tion.jda.api.entities.Guild guild) {
        return null;
    }

    @NotNull
    @Override
    public List<Member> getMentionedMembers() {
        return null;
    }

    @NotNull
    @Override
    public List<IMentionable> getMentions(@NotNull MentionType... types) {
        return null;
    }

    @Override
    public boolean isMentioned(@NotNull IMentionable mentionable, @NotNull MentionType... types) {
        return false;
    }

    @Override
    public boolean mentionsEveryone() {
        return false;
    }

    @Override
    public boolean isEdited() {
        return false;
    }

    @Nullable
    @Override
    public OffsetDateTime getTimeEdited() {
        return null;
    }

    @NotNull
    @Override
    public User getAuthor() {
        return user;
    }

    @Nullable
    @Override
    public Member getMember() {
        return null;
    }

    @NotNull
    @Override
    public String getJumpUrl() {
        return null;
    }

    @NotNull
    @Override
    public String getContentDisplay() {
        return message;
    }

    @NotNull
    @Override
    public String getContentRaw() {
        return message;
    }

    @NotNull
    @Override
    public String getContentStripped() {
        return message;
    }

    @NotNull
    @Override
    public List<String> getInvites() {
        return null;
    }

    @Nullable
    @Override
    public String getNonce() {
        return null;
    }

    @Override
    public boolean isFromType(@NotNull ChannelType type) {
        return false;
    }

    @NotNull
    @Override
    public ChannelType getChannelType() {
        return channel.getType();
    }

    @Override
    public boolean isWebhookMessage() {
        return false;
    }

    @NotNull
    @Override
    public MessageChannel getChannel() {
        return channel;
    }

    @NotNull
    @Override
    public PrivateChannel getPrivateChannel() {
        return null;
    }

    @NotNull
    @Override
    public TextChannel getTextChannel() {
        return channel;
    }

    @Nullable
    @Override
    public Category getCategory() {
        return null;
    }

    @NotNull
    @Override
    public net.dv8tion.jda.api.entities.Guild getGuild() {
        return guild;
    }

    @NotNull
    @Override
    public List<Attachment> getAttachments() {
        return null;
    }

    @NotNull
    @Override
    public List<MessageEmbed> getEmbeds() {
        return null;
    }

    @NotNull
    @Override
    public List<Emote> getEmotes() {
        return null;
    }

    @NotNull
    @Override
    public Bag<Emote> getEmotesBag() {
        return null;
    }

    @NotNull
    @Override
    public List<MessageReaction> getReactions() {
        return null;
    }

    @Override
    public boolean isTTS() {
        return false;
    }

    @Nullable
    @Override
    public MessageActivity getActivity() {
        return null;
    }

    @NotNull
    @Override
    public MessageAction editMessage(@NotNull CharSequence newContent) {
        return null;
    }

    @NotNull
    @Override
    public MessageAction editMessage(@NotNull MessageEmbed newContent) {
        return null;
    }

    @NotNull
    @Override
    public MessageAction editMessageFormat(@NotNull String format, @NotNull Object... args) {
        return null;
    }

    @NotNull
    @Override
    public MessageAction editMessage(@NotNull Message newContent) {
        return null;
    }

    @NotNull
    @Override
    public AuditableRestAction<Void> delete() {
        return null;
    }

    @NotNull
    @Override
    public JDA getJDA() {
        return new TestJDA();
    }

    @Override
    public boolean isPinned() {
        return false;
    }

    @NotNull
    @Override
    public RestAction<Void> pin() {
        return null;
    }

    @NotNull
    @Override
    public RestAction<Void> unpin() {
        return null;
    }

    @NotNull
    @Override
    public RestAction<Void> addReaction(@NotNull Emote emote) {
        return null;
    }

    @NotNull
    @Override
    public RestAction<Void> addReaction(@NotNull String unicode) {
        return null;
    }

    @NotNull
    @Override
    public RestAction<Void> clearReactions() {
        return null;
    }

    @NotNull
    @Override
    public RestAction<Void> clearReactions(@NotNull String unicode) {
        return null;
    }

    @NotNull
    @Override
    public RestAction<Void> clearReactions(@NotNull Emote emote) {
        return null;
    }

    @NotNull
    @Override
    public RestAction<Void> removeReaction(@NotNull Emote emote) {
        return null;
    }

    @NotNull
    @Override
    public RestAction<Void> removeReaction(@NotNull Emote emote, @NotNull User user) {
        return null;
    }

    @NotNull
    @Override
    public RestAction<Void> removeReaction(@NotNull String unicode) {
        return null;
    }

    @NotNull
    @Override
    public RestAction<Void> removeReaction(@NotNull String unicode, @NotNull User user) {
        return null;
    }

    @NotNull
    @Override
    public ReactionPaginationAction retrieveReactionUsers(@NotNull Emote emote) {
        return null;
    }

    @NotNull
    @Override
    public ReactionPaginationAction retrieveReactionUsers(@NotNull String unicode) {
        return null;
    }

    @Nullable
    @Override
    public MessageReaction.ReactionEmote getReactionByUnicode(@NotNull String unicode) {
        return null;
    }

    @Nullable
    @Override
    public MessageReaction.ReactionEmote getReactionById(@NotNull String id) {
        return null;
    }

    @Nullable
    @Override
    public MessageReaction.ReactionEmote getReactionById(long id) {
        return null;
    }

    @NotNull
    @Override
    public AuditableRestAction<Void> suppressEmbeds(boolean suppressed) {
        return null;
    }

    @NotNull
    @Override
    public RestAction<Message> crosspost() {
        return null;
    }

    @Override
    public boolean isSuppressedEmbeds() {
        return false;
    }

    @NotNull
    @Override
    public EnumSet<MessageFlag> getFlags() {
        return null;
    }

    @NotNull
    @Override
    public MessageType getType() {
        return null;
    }

    @Override
    public void formatTo(Formatter formatter, int flags, int width, int precision) {

    }

    @Override
    public long getIdLong() {
        return 0;
    }
}
