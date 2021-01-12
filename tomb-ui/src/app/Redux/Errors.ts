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

import {Action, createSlice, nanoid, PayloadAction} from "@reduxjs/toolkit";
import {of} from "rxjs";
import {combineEpics, ofType} from "redux-observable";
import {mergeMap} from "rxjs/operators";

export type ErrorState = Readonly<{
  "code": string,
  "message": string,
  "id": string | undefined,
  "timestamp": Date,
  "history": ErrorState[], // history entries themselves should have an empty history ...
}>;

export const initialErrorState : ErrorState = {
  code: "200",
  message: "Ok",
  id: nanoid().toString(),
  timestamp: new Date(),
  history: []
}


export enum ErrorActionTypes {
  PUBLISH_ERROR = '@@Namespace/Errors/PUBLISH_ERROR',
  THROW_ERROR = '@@NAMESPACE/Errors/THROW_ERROR',
}

interface PublishError extends Action {
  type: ErrorActionTypes.PUBLISH_ERROR,
}

interface ThrowError extends Action {
  type: ErrorActionTypes.THROW_ERROR,
  error: ErrorState,
}


// -----------------------------------------------------------------------------

export const errorSlice = createSlice({
  name: 'errors',
  initialState: initialErrorState,
  reducers: {
    publishError(state, action: PayloadAction<ThrowError>) {
      const n = action.payload.error
      state.history ? state.history.push(n) : true ;

      state.id = n.id
      state.code = n.code
      state.message = n.message
      state.timestamp = n.timestamp
    },

  }
})


const publishError = error => of({type: ErrorActionTypes.PUBLISH_ERROR, error: error})

const receiveError = action$ => action$.pipe(
  ofType(ErrorActionTypes.PUBLISH_ERROR),
  mergeMap(action => {
    const r = action as PublishError
    return publishError(r)
  })
)
export const errorEpics = combineEpics(
  receiveError,
)
