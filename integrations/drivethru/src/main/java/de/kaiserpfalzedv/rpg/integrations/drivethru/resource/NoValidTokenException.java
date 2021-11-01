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

package de.kaiserpfalzedv.rpg.integrations.drivethru.resource;

<<<<<<< HEAD:integrations/drivethru/src/main/java/de/kaiserpfalzedv/rpg/integrations/drivethru/resource/NoValidTokenException.java
import de.kaiserpfalzedv.commons.core.user.User;
import de.kaiserpfalzedv.rpg.core.api.RPGBaseException;
=======
import de.kaiserpfalzedv.rpg.bot.text.MarkdownConverter;
import net.dv8tion.jda.api.entities.MessageChannel;
>>>>>>> main:rpg-server/tomb/src/main/java/de/kaiserpfalzedv/rpg/bot/discord/DiscordMessageSender.java


/**
 * NoDriveThruRPGAPIKeyDefinedException -- The user has no registered API Key for DriveThruRPG.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0  2021-02-04
 */
public class NoValidTokenException extends RPGBaseException {
    public NoValidTokenException() {
        super("No valid token for DriveThruRPG.");
    }

    public NoValidTokenException(final User user) {
        super(String.format("Could not create a DriveThruRPG token for user '%s/%s'.", user.getNamespace(), user.getName()));
    }

}
