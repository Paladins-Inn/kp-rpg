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

import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.hooks.IEventManager;
import net.dv8tion.jda.api.managers.AudioManager;
import net.dv8tion.jda.api.managers.DirectAudioController;
import net.dv8tion.jda.api.managers.Presence;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.GuildAction;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.cache.CacheView;
import net.dv8tion.jda.api.utils.cache.SnowflakeCacheView;
import okhttp3.OkHttpClient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

/**
 * A fake JDA implementation for test support.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0  2021-02-05
 */
public class TestJDA implements JDA {
    public static final JDA JDA = new TestJDA();

    @NotNull
    @Override
    public JDA.Status getStatus() {
        return null;
    }

    @NotNull
    @Override
    public EnumSet<GatewayIntent> getGatewayIntents() {
        return null;
    }

    @Override
    public boolean unloadUser(long userId) {
        return false;
    }

    @Override
    public long getGatewayPing() {
        return 0;
    }

    @NotNull
    @Override
    public JDA awaitStatus(@NotNull JDA.Status status, @NotNull JDA.Status... failOn) throws InterruptedException {
        return null;
    }

    @Override
    public int cancelRequests() {
        return 0;
    }

    @NotNull
    @Override
    public ScheduledExecutorService getRateLimitPool() {
        return null;
    }

    @NotNull
    @Override
    public ScheduledExecutorService getGatewayPool() {
        return null;
    }

    @NotNull
    @Override
    public ExecutorService getCallbackPool() {
        return null;
    }

    @NotNull
    @Override
    public OkHttpClient getHttpClient() {
        return null;
    }

    @NotNull
    @Override
    public DirectAudioController getDirectAudioController() {
        return null;
    }

    @Override
    public void addEventListener(@NotNull Object... listeners) {

    }

    @Override
    public void removeEventListener(@NotNull Object... listeners) {

    }

    @NotNull
    @Override
    public List<Object> getRegisteredListeners() {
        return null;
    }

    @NotNull
    @Override
    public GuildAction createGuild(@NotNull String name) {
        return null;
    }

    @NotNull
    @Override
    public CacheView<AudioManager> getAudioManagerCache() {
        return null;
    }

    @NotNull
    @Override
    public SnowflakeCacheView<User> getUserCache() {
        return null;
    }

    @NotNull
    @Override
    public List<net.dv8tion.jda.api.entities.Guild> getMutualGuilds(@NotNull User... users) {
        return null;
    }

    @NotNull
    @Override
    public List<net.dv8tion.jda.api.entities.Guild> getMutualGuilds(@NotNull Collection<User> users) {
        return null;
    }

    @NotNull
    @Override
    public RestAction<User> retrieveUserById(long id, boolean update) {
        return null;
    }

    @NotNull
    @Override
    public SnowflakeCacheView<net.dv8tion.jda.api.entities.Guild> getGuildCache() {
        return null;
    }

    @NotNull
    @Override
    public Set<String> getUnavailableGuilds() {
        return null;
    }

    @Override
    public boolean isUnavailable(long guildId) {
        return false;
    }

    @NotNull
    @Override
    public SnowflakeCacheView<Role> getRoleCache() {
        return null;
    }

    @NotNull
    @Override
    public SnowflakeCacheView<Category> getCategoryCache() {
        return null;
    }

    @NotNull
    @Override
    public SnowflakeCacheView<StoreChannel> getStoreChannelCache() {
        return null;
    }

    @NotNull
    @Override
    public SnowflakeCacheView<TextChannel> getTextChannelCache() {
        return null;
    }

    @NotNull
    @Override
    public SnowflakeCacheView<VoiceChannel> getVoiceChannelCache() {
        return null;
    }

    @NotNull
    @Override
    public SnowflakeCacheView<PrivateChannel> getPrivateChannelCache() {
        return null;
    }

    @NotNull
    @Override
    public RestAction<PrivateChannel> openPrivateChannelById(long userId) {
        return null;
    }

    @NotNull
    @Override
    public SnowflakeCacheView<Emote> getEmoteCache() {
        return null;
    }

    @NotNull
    @Override
    public IEventManager getEventManager() {
        return null;
    }

    @Override
    public void setEventManager(@Nullable IEventManager manager) {

    }

    @NotNull
    @Override
    public SelfUser getSelfUser() {
        return null;
    }

    @NotNull
    @Override
    public Presence getPresence() {
        return null;
    }

    @NotNull
    @Override
    public ShardInfo getShardInfo() {
        return null;
    }

    @NotNull
    @Override
    public String getToken() {
        return null;
    }

    @Override
    public long getResponseTotal() {
        return 0;
    }

    @Override
    public int getMaxReconnectDelay() {
        return 0;
    }

    @Override
    public void setRequestTimeoutRetry(boolean retryOnTimeout) {

    }

    @Override
    public boolean isAutoReconnect() {
        return false;
    }

    @Override
    public void setAutoReconnect(boolean reconnect) {

    }

    @Override
    public boolean isBulkDeleteSplittingEnabled() {
        return false;
    }

    @Override
    public void shutdown() {

    }

    @Override
    public void shutdownNow() {

    }

    @NotNull
    @Override
    public AccountType getAccountType() {
        return null;
    }

    @NotNull
    @Override
    public RestAction<ApplicationInfo> retrieveApplicationInfo() {
        return null;
    }

    @NotNull
    @Override
    public String getInviteUrl(@Nullable Permission... permissions) {
        return null;
    }

    @NotNull
    @Override
    public String getInviteUrl(@Nullable Collection<Permission> permissions) {
        return null;
    }

    @Nullable
    @Override
    public ShardManager getShardManager() {
        return null;
    }

    @NotNull
    @Override
    public RestAction<Webhook> retrieveWebhookById(@NotNull String webhookId) {
        return null;
    }
}
