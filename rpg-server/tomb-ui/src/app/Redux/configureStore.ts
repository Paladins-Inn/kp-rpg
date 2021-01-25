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

import {applyMiddleware, createStore} from "redux";
import {createEpicMiddleware} from "redux-observable";
import {rootEpics, rootReducer} from "@app/Redux/Store";
import {ApplicationState} from "@app/Redux/Store/ApplicationState";

const epicMiddleware = createEpicMiddleware();

export const store = createStore(rootReducer, {} as ApplicationState, applyMiddleware(epicMiddleware));

epicMiddleware.run(rootEpics)
