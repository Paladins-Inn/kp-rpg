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

package de.kaiserpfalzedv.rpg.integrations.discord;

/**
 * DiscordPluginNotAllowedException -- The user does not have the needed permissions to use this plugin.
 *
 * It knows two different states. If {@link #isBlame()} is {@literal true}, the user should be called out for trying to use this
 * command. If it is {@literal false}, then the command should be silently ignored.
 */
public class DiscordPluginNotAllowedException extends DiscordPluginException {
    private final boolean blame;

    public DiscordPluginNotAllowedException(final boolean blame) {
        this.blame = blame;
    }

    public DiscordPluginNotAllowedException(final boolean blame, final String message) {
        super(message);
        this.blame = blame;
    }

    public DiscordPluginNotAllowedException(final boolean blame, final String message, final Throwable cause) {
        super(message, cause);
        this.blame = blame;
    }

    public DiscordPluginNotAllowedException(final boolean blame, final Throwable cause) {
        super(cause);
        this.blame = blame;
    }

    public DiscordPluginNotAllowedException(final boolean blame, final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.blame = blame;
    }

    /**
     * @return TRUE blame the user for trying to call this plugin; FALSE silently ignore this call.
     */
    public boolean isBlame() {
        return blame;
    }
}
