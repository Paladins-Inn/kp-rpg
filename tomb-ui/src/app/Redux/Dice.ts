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

import {Action, createSlice, PayloadAction} from "@reduxjs/toolkit";
import {DieResult, ExpressionTotal, RollTotal} from "@app/generated/tomb.d";
import {of} from "rxjs";
import rollDice from "@app/Services/dice/DiceService";
import {combineEpics, ofType} from "redux-observable";
import {mergeMap} from "rxjs/operators";


export type DiceState = Readonly<{
  "roll": string,
  "result": RollTotal | undefined,
  "history": RollTotal[],
  "timestamp": Date,
}>;


export const initialDiceState : DiceState = {
  roll: "none",
  result: {
    empty: false,
    description: "1D6: 5 | 5 {5}",
    expressions: [
      {
        amountOfDice: 1,
        dieIdentifier: "D6",
        expression: "1d6",
        description: "1D6: 5 | 5 {5}",
        rolls: [
          {
            die: "D6",
            display: "D6: 5 {5}",
            shortDisplay: "5",
            total: "5",
            rolls: ["5"],
          } as DieResult
        ],
      } as ExpressionTotal
    ],
  } as RollTotal,
  history: [],
  timestamp: new Date(),
};


export enum DiceActionTypes {
  ROLL_DIE = '@@Namespace/Dice/ROLL_DICE',
  PUBLISH_DIE = '@@Namespace/Dice/PUBLISH_DICE',
  CLEAR_DIE = '@@Namespace/Dice/CLEAR_DIE',
}

interface ClearDieResult extends Action {
  type: DiceActionTypes.CLEAR_DIE,
}

interface RollDie extends Action {
  type: DiceActionTypes.ROLL_DIE,
  dice: string,
}

interface PublishDie extends Action {
  type: DiceActionTypes.PUBLISH_DIE,
  result: RollTotal,
}

type DiceActions = ClearDieResult | RollDie | PublishDie;

// -----------------------------------------------------------------------------

export const diceSlice = createSlice({
  name: 'dice',
  initialState: initialDiceState,
  reducers: {
    clearDice(state, action: PayloadAction<ClearDieResult>) {
      state.result = undefined
    },
    rollDice(state, action: PayloadAction<RollDie>) {
      state.roll = action.payload.dice
    },
    publishDice(state, action: PayloadAction<PublishDie>) {
      state.result = action.payload.result
      state.history.push(action.payload.result)
    }
  }
})


const publishDie = dice => of({type: DiceActionTypes.PUBLISH_DIE, result: rollDice(dice)})
const rollDiceEpic = action$ => action$.pipe(
  ofType(DiceActionTypes.ROLL_DIE),
  mergeMap(action => {
    const r = action as RollDie
    return publishDie(r.dice)
  })
)
export const diceEpics = combineEpics(
  rollDiceEpic,
)
