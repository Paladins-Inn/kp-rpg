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

package de.kaiserpfalzedv.google.spreadsheet;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * SpreadSheet --
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 0.3.0  2021-05-23
 */
@Slf4j
@Builder(setterPrefix = "with", toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class SpreadSheet {
    private NetHttpTransport transport;
    private JsonFactory factory;
    private Credential credential;
    private String name;

    public Sheets open() {
        return new Sheets.Builder(transport, factory, credential)
                .setApplicationName(name)
                .build();
    }

    public Sheets.Spreadsheets allSheets() {
        return open().spreadsheets();
    }

    public Sheets.Spreadsheets.Get sheet(final String sheet) throws IOException {
        return open().spreadsheets().get(sheet);
    }
}
