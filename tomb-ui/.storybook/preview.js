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

// .storybook/preview.js

import React from 'react';
import {Provider} from "react-redux";
import {default as configureStore} from "@app/Redux/configureStore";
import {RestfulProvider} from "restful-react";

const store = configureStore();

export const decorators = [
  (story) => (
    <Provider store={{store}}>
      { story() }
    </Provider>
  ),
  (story) => (
    <RestfulProvider base='https://kp-discord-bot.apps.numma.kaiserpfalz-edv.de/'>
      { story() }
    </RestfulProvider>
  )
];
