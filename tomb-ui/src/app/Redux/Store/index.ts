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

import {combineReducers} from "redux";
import {combineEpics} from "redux-observable";
import {diceEpics, diceSlice} from "@app/Redux/Dice";
import {errorEpics, errorSlice} from "@app/Redux/Errors";


export const rootReducer = combineReducers({
  dice: diceSlice.reducer,
  errors: errorSlice.reducer,
});


export const rootEpics = combineEpics(
  diceEpics,
  errorEpics
)
