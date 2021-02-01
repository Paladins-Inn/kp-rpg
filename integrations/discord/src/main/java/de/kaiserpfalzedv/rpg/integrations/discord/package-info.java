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

/**
 * Discord -- A not so complete anti-corruption layer.
 *
 * Normally this package should complete encapsulate the {@link net.dv8tion.jda.api.JDA} library. But I like the data
 * design and see no value in doubling the classes with wrapper classes.
 *
 * So this library uses the data structures of JDA directly. If JDA is to be replaced, then it is time to do the
 * double work.
 */
package de.kaiserpfalzedv.rpg.integrations.discord;