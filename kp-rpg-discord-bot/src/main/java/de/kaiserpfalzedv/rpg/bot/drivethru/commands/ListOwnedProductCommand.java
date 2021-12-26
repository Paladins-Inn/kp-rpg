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

package de.kaiserpfalzedv.rpg.bot.drivethru.commands;

import de.kaiserpfalzedv.commons.core.discord.DiscordMessageHandler;
import de.kaiserpfalzedv.commons.core.user.InvalidUserException;
import de.kaiserpfalzedv.commons.core.user.User;
import de.kaiserpfalzedv.commons.core.user.UserStoreService;
import de.kaiserpfalzedv.commons.discord.DiscordPluginContext;
import de.kaiserpfalzedv.commons.discord.DiscordPluginException;
import de.kaiserpfalzedv.commons.discord.DontWorkOnDiscordEventException;
import de.kaiserpfalzedv.rpg.bot.drivethru.DriveThruRPGPluginCommand;
import de.kaiserpfalzedv.rpg.integrations.drivethru.DriveThruRPGService;
import de.kaiserpfalzedv.rpg.integrations.drivethru.model.OwnedProduct;
import de.kaiserpfalzedv.rpg.integrations.drivethru.resource.NoDriveThruRPGAPIKeyDefinedException;
import de.kaiserpfalzedv.rpg.integrations.drivethru.resource.NoValidTokenException;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.ChannelType;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.StringJoiner;

/**
 * ListOwnedProductCommand -- Lists the products the user has purchased at DriveThruRPG.
 */
@ApplicationScoped
@ToString
@EqualsAndHashCode
@Slf4j
public class ListOwnedProductCommand implements DriveThruRPGPluginCommand {
    /**
     * main service to do the job.
     */
    private final DriveThruRPGService service;
    /**
     * The user store to load userdata from.
     */
    private final UserStoreService userStore;
    /**
     * Sender for discord messages.
     */
    private final DiscordMessageHandler sender;

    @Inject
    public ListOwnedProductCommand(
            final DriveThruRPGService service,
            final UserStoreService userStore,
            final DiscordMessageHandler sender
    ) {
        this.service = service;
        this.userStore = userStore;
        this.sender = sender;
    }


    @Override
    public void execute(final DiscordPluginContext context) throws DiscordPluginException {
        User user = loadUserFromStoreOrCreateNewUser(context, userStore);

        validate(context, user);

        try {
            List<OwnedProduct> result = service.getOwnedProducts(user);

            StringJoiner sj = new StringJoiner(
                    "\n",
                    "You own the following products:\n",
                    "<<<end of list (" + result.size() + " products)>>>"
            );

            result.forEach(s -> sj.add(formatProduct(s)));

            log.debug("Listed {} products.", result.size());
            sender.sendTextMessage(context.getChannel(), sj.toString());
        } catch (NoValidTokenException | InvalidUserException | NoDriveThruRPGAPIKeyDefinedException e) {
            sender.sendDM(context.getUser(), "Sorry, your api-key is invalid. Please check it.");
            log.warn("Invalid api-key for driveThruRPG.");
        }
    }

    private void validate(final DiscordPluginContext context, final User user) throws DiscordPluginException {
        log.trace("Validating: context={}, user={}", context, user);

        if (!ChannelType.TEXT.equals(context.getChannel().getType())) {
            throw new DontWorkOnDiscordEventException(context.getPlugin());
        }

        try {
            user.getData().orElseThrow().getDriveThruRPGApiKey().orElseThrow();
        } catch (NoSuchElementException e) {
            sender.sendDM(context.getUser(), "Sorry, I found no api key for you. please set one via 'dtr token <api key>'.");

            throw new DontWorkOnDiscordEventException(context.getPlugin());
        }
    }

    private String formatProduct(final OwnedProduct data) {
        log.trace("Formatting product: id={}, name='{}'", data.getId(), data.getName());

        return data.getName() + " <"
                + data.getCoverURL() + "> ("
                + data.getId() + ", "
                + data.getDatePurchased().orElse(OffsetDateTime.MIN)
                + ")";
    }


    @Override
    public String getCommand() {
        return "list-owned";
    }

    @Override
    public String getHelp() {
        return formatHelp("Lists the products you purchased at DriveThruRPG.");
    }
}
