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

package de.kaiserpfalzedv.rpg.core.text;

import com.github.rjeschke.txtmark.Processor;

import javax.enterprise.context.ApplicationScoped;

/**
 * MarkDownConverter -- A converter for Markdown to HTML.
 *
 * This is an anti-corruption layer for converting <a href="https://daringfireball.net/projects/markdown/">Markdown</a>
 * to HTML.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.0.0 2021-01-11
 */
@ApplicationScoped
public class MarkdownConverter {
    /**
     * Converts the markdown input to HTML output.
     *
     * @param input markdown input string.
     * @return html text as output.
     */
    public String convert(final String input) {
        return Processor.process(input);
    }
}
