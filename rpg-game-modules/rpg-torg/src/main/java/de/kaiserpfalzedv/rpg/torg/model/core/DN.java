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

package de.kaiserpfalzedv.rpg.torg.model.core;

<<<<<<< HEAD:rpg-game-modules/rpg-torg/src/main/java/de/kaiserpfalzedv/rpg/torg/model/core/DN.java
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
=======
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
>>>>>>> main:rpg-server/tomb/src/main/java/de/kaiserpfalzedv/rpg/bot/discord/DiscordPlugin.java

/**
 * DN --
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 0.3.0  2021-05-23
 */
<<<<<<< HEAD:rpg-game-modules/rpg-torg/src/main/java/de/kaiserpfalzedv/rpg/torg/model/core/DN.java
@AllArgsConstructor
@Getter
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public enum DN {
    VERY_EASY("Very Easy", 6, +4),
    EASY("Easy", 8, +2),
    STANDARD("Standard", 10, 0),
    CHALLENGING("Challenging", 12, -2),
    HARD("Hard", 14, -4),
    VERY_HARD("Very Hard", 16, -6),
    HEROIC("Heroic", 18, -8),
    NEAR_IMPOSSIBLE("Near Impossible", 20, -10);

    private final String name;
    private final Integer dn;
    private final Integer modifier;
=======
public interface DiscordPlugin {
    /**
     * The command execution of this plugin. All plugins get all events and have to decide to react on it or not.
     *
     * @param event The event to work on.
     * @throws DiscordPluginException If any problem occurred.
     */
    void work(MessageReceivedEvent event) throws DiscordPluginException;

    /**
     *
     * @param event
     * @throws DiscordPluginException
     */
    void work(GuildMessageReactionAddEvent event) throws DiscordPluginException;

    /**
     *
     * @param event
     * @throws DiscordPluginException
     */
    void work(GuildMessageReactionRemoveEvent event) throws DiscordPluginException;
>>>>>>> main:rpg-server/tomb/src/main/java/de/kaiserpfalzedv/rpg/bot/discord/DiscordPlugin.java
}
