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
import net.dv8tion.jda.api.Region;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.managers.AudioManager;
import net.dv8tion.jda.api.managers.GuildManager;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;
import net.dv8tion.jda.api.requests.restaction.ChannelAction;
import net.dv8tion.jda.api.requests.restaction.MemberAction;
import net.dv8tion.jda.api.requests.restaction.RoleAction;
import net.dv8tion.jda.api.requests.restaction.order.CategoryOrderAction;
import net.dv8tion.jda.api.requests.restaction.order.ChannelOrderAction;
import net.dv8tion.jda.api.requests.restaction.order.RoleOrderAction;
import net.dv8tion.jda.api.requests.restaction.pagination.AuditLogPaginationAction;
import net.dv8tion.jda.api.utils.cache.MemberCacheView;
import net.dv8tion.jda.api.utils.cache.SnowflakeCacheView;
import net.dv8tion.jda.api.utils.cache.SortedSnowflakeCacheView;
import net.dv8tion.jda.api.utils.concurrent.Task;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * A fake Guild implementation for test support.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0  2021-02-05
 */
public class TestGuild implements Guild {
    private final String name;


    public TestGuild(final String name) {
        this.name = name;
    }


    @NotNull
    @Override
    public RestAction<EnumSet<Region>> retrieveRegions(boolean includeDeprecated) {
        return null;
    }

    @NotNull
    @Override
    public MemberAction addMember(@NotNull String accessToken, @NotNull String userId) {
        return null;
    }

    @Override
    public boolean isLoaded() {
        return false;
    }

    @Override
    public void pruneMemberCache() {

    }

    @Override
    public boolean unloadMember(long userId) {
        return false;
    }

    @Override
    public int getMemberCount() {
        return 0;
    }

    @NotNull
    @Override
    public String getName() {
        return name;
    }

    @Nullable
    @Override
    public String getIconId() {
        return name + "/icon";
    }

    @NotNull
    @Override
    public Set<String> getFeatures() {
        return null;
    }

    @Nullable
    @Override
    public String getSplashId() {
        return null;
    }

    @NotNull
    @Override
    public RestAction<String> retrieveVanityUrl() {
        return null;
    }

    @Nullable
    @Override
    public String getVanityCode() {
        return null;
    }

    @Nullable
    @Override
    public String getDescription() {
        return null;
    }

    @NotNull
    @Override
    public Locale getLocale() {
        return Locale.getDefault();
    }

    @Nullable
    @Override
    public String getBannerId() {
        return name + "/banner";
    }

    @NotNull
    @Override
    public BoostTier getBoostTier() {
        return null;
    }

    @Override
    public int getBoostCount() {
        return 0;
    }

    @NotNull
    @Override
    public List<Member> getBoosters() {
        return null;
    }

    @Override
    public int getMaxMembers() {
        return 0;
    }

    @Override
    public int getMaxPresences() {
        return 0;
    }

    @NotNull
    @Override
    public RestAction<MetaData> retrieveMetaData() {
        return null;
    }

    @Nullable
    @Override
    public VoiceChannel getAfkChannel() {
        return null;
    }

    @Nullable
    @Override
    public TextChannel getSystemChannel() {
        return null;
    }

    @Nullable
    @Override
    public Member getOwner() {
        return null;
    }

    @Override
    public long getOwnerIdLong() {
        return 0;
    }

    @NotNull
    @Override
    public Timeout getAfkTimeout() {
        return null;
    }

    @NotNull
    @Override
    public String getRegionRaw() {
        return null;
    }

    @Override
    public boolean isMember(@NotNull User user) {
        return false;
    }

    @NotNull
    @Override
    public Member getSelfMember() {
        return null;
    }

    @Nullable
    @Override
    public Member getMember(@NotNull User user) {
        return null;
    }

    @NotNull
    @Override
    public MemberCacheView getMemberCache() {
        return null;
    }

    @NotNull
    @Override
    public SortedSnowflakeCacheView<Category> getCategoryCache() {
        return null;
    }

    @NotNull
    @Override
    public SortedSnowflakeCacheView<StoreChannel> getStoreChannelCache() {
        return null;
    }

    @NotNull
    @Override
    public SortedSnowflakeCacheView<TextChannel> getTextChannelCache() {
        return null;
    }

    @NotNull
    @Override
    public SortedSnowflakeCacheView<VoiceChannel> getVoiceChannelCache() {
        return null;
    }

    @NotNull
    @Override
    public List<GuildChannel> getChannels(boolean includeHidden) {
        return null;
    }

    @NotNull
    @Override
    public SortedSnowflakeCacheView<Role> getRoleCache() {
        return null;
    }

    @NotNull
    @Override
    public SnowflakeCacheView<Emote> getEmoteCache() {
        return null;
    }

    @NotNull
    @Override
    public RestAction<List<ListedEmote>> retrieveEmotes() {
        return null;
    }

    @NotNull
    @Override
    public RestAction<ListedEmote> retrieveEmoteById(@NotNull String id) {
        return null;
    }

    @NotNull
    @Override
    public RestAction<List<Ban>> retrieveBanList() {
        return null;
    }

    @NotNull
    @Override
    public RestAction<Ban> retrieveBanById(@NotNull String userId) {
        return null;
    }

    @NotNull
    @Override
    public RestAction<Integer> retrievePrunableMemberCount(int days) {
        return null;
    }

    @NotNull
    @Override
    public Role getPublicRole() {
        return null;
    }

    @Nullable
    @Override
    public TextChannel getDefaultChannel() {
        return null;
    }

    @NotNull
    @Override
    public GuildManager getManager() {
        return null;
    }

    @NotNull
    @Override
    public AuditLogPaginationAction retrieveAuditLogs() {
        return null;
    }

    @NotNull
    @Override
    public RestAction<Void> leave() {
        return null;
    }

    @NotNull
    @Override
    public RestAction<Void> delete() {
        return null;
    }

    @NotNull
    @Override
    public RestAction<Void> delete(@Nullable String mfaCode) {
        return null;
    }

    @NotNull
    @Override
    public AudioManager getAudioManager() {
        return null;
    }

    @NotNull
    @Override
    public JDA getJDA() {
        return new TestJDA();
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
    public List<GuildVoiceState> getVoiceStates() {
        return null;
    }

    @NotNull
    @Override
    public VerificationLevel getVerificationLevel() {
        return null;
    }

    @NotNull
    @Override
    public NotificationLevel getDefaultNotificationLevel() {
        return null;
    }

    @NotNull
    @Override
    public MFALevel getRequiredMFALevel() {
        return null;
    }

    @NotNull
    @Override
    public ExplicitContentLevel getExplicitContentLevel() {
        return null;
    }

    @Override
    public boolean checkVerification() {
        return false;
    }

    @Override
    public boolean isAvailable() {
        return false;
    }

    @NotNull
    @Override
    public CompletableFuture<Void> retrieveMembers() {
        return null;
    }

    @NotNull
    @Override
    public Task<Void> loadMembers(@NotNull Consumer<Member> callback) {
        return null;
    }

    @NotNull
    @Override
    public RestAction<Member> retrieveMemberById(long id, boolean update) {
        return null;
    }

    @NotNull
    @Override
    public Task<List<Member>> retrieveMembersByIds(boolean includePresence, @NotNull long... ids) {
        return null;
    }

    @NotNull
    @Override
    public Task<List<Member>> retrieveMembersByPrefix(@NotNull String prefix, int limit) {
        return null;
    }

    @NotNull
    @Override
    public RestAction<Void> moveVoiceMember(@NotNull Member member, @Nullable VoiceChannel voiceChannel) {
        return null;
    }

    @NotNull
    @Override
    public AuditableRestAction<Void> modifyNickname(@NotNull Member member, @Nullable String nickname) {
        return null;
    }

    @NotNull
    @Override
    public AuditableRestAction<Integer> prune(int days, boolean wait, @NotNull Role... roles) {
        return null;
    }

    @NotNull
    @Override
    public AuditableRestAction<Void> kick(@NotNull Member member, @Nullable String reason) {
        return null;
    }

    @NotNull
    @Override
    public AuditableRestAction<Void> kick(@NotNull String userId, @Nullable String reason) {
        return null;
    }

    @NotNull
    @Override
    public AuditableRestAction<Void> ban(@NotNull User user, int delDays, @Nullable String reason) {
        return null;
    }

    @NotNull
    @Override
    public AuditableRestAction<Void> ban(@NotNull String userId, int delDays, @Nullable String reason) {
        return null;
    }

    @NotNull
    @Override
    public AuditableRestAction<Void> unban(@NotNull String userId) {
        return null;
    }

    @NotNull
    @Override
    public AuditableRestAction<Void> deafen(@NotNull Member member, boolean deafen) {
        return null;
    }

    @NotNull
    @Override
    public AuditableRestAction<Void> mute(@NotNull Member member, boolean mute) {
        return null;
    }

    @NotNull
    @Override
    public AuditableRestAction<Void> addRoleToMember(@NotNull Member member, @NotNull Role role) {
        return null;
    }

    @NotNull
    @Override
    public AuditableRestAction<Void> removeRoleFromMember(@NotNull Member member, @NotNull Role role) {
        return null;
    }

    @NotNull
    @Override
    public AuditableRestAction<Void> modifyMemberRoles(@NotNull Member member, @Nullable Collection<Role> rolesToAdd, @Nullable Collection<Role> rolesToRemove) {
        return null;
    }

    @NotNull
    @Override
    public AuditableRestAction<Void> modifyMemberRoles(@NotNull Member member, @NotNull Collection<Role> roles) {
        return null;
    }

    @NotNull
    @Override
    public AuditableRestAction<Void> transferOwnership(@NotNull Member newOwner) {
        return null;
    }

    @NotNull
    @Override
    public ChannelAction<TextChannel> createTextChannel(@NotNull String name, @Nullable Category parent) {
        return null;
    }

    @NotNull
    @Override
    public ChannelAction<VoiceChannel> createVoiceChannel(@NotNull String name, @Nullable Category parent) {
        return null;
    }

    @NotNull
    @Override
    public ChannelAction<Category> createCategory(@NotNull String name) {
        return null;
    }

    @NotNull
    @Override
    public RoleAction createRole() {
        return null;
    }

    @NotNull
    @Override
    public AuditableRestAction<Emote> createEmote(@NotNull String name, @NotNull Icon icon, @NotNull Role... roles) {
        return null;
    }

    @NotNull
    @Override
    public ChannelOrderAction modifyCategoryPositions() {
        return null;
    }

    @NotNull
    @Override
    public ChannelOrderAction modifyTextChannelPositions() {
        return null;
    }

    @NotNull
    @Override
    public ChannelOrderAction modifyVoiceChannelPositions() {
        return null;
    }

    @NotNull
    @Override
    public CategoryOrderAction modifyTextChannelPositions(@NotNull Category category) {
        return null;
    }

    @NotNull
    @Override
    public CategoryOrderAction modifyVoiceChannelPositions(@NotNull Category category) {
        return null;
    }

    @NotNull
    @Override
    public RoleOrderAction modifyRolePositions(boolean useAscendingOrder) {
        return null;
    }

    @Override
    public long getIdLong() {
        return 1L;
    }
}
