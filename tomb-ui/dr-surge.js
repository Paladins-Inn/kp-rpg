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

const fs = require('fs');
const path = require('path');
const indexPath = path.resolve(__dirname, 'dist/index.html');
const targetFilePath = path.resolve(__dirname, 'dist/200.html');
// ensure we have bookmarkable url's when publishing to surge
// https://surge.sh/help/adding-a-200-page-for-client-side-routing
fs.createReadStream(indexPath).pipe(fs.createWriteStream(targetFilePath));
