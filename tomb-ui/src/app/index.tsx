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

import * as React from 'react';
import '@patternfly/react-core/dist/styles/base.css';
import {BrowserRouter as Router} from 'react-router-dom';
import {AppLayout} from '@app/AppLayout/AppLayout';
import {AppRoutes} from '@app/routes';
import '@app/app.css';
import {UserManagerSettings} from "oidc-client";
import {makeAuthenticator, makeUserManager} from "react-oidc";

const App = () => (
    <Router>
      <AppLayout>
        <AppRoutes />
      </AppLayout>
    </Router>
);

const oauthBaseUrl : String = "https://discord.com/api/oauth2";

const userManagerConfig : UserManagerSettings = {
  authority: "Discord",
  client_id: "800069820812886036",
  client_secret: "09JiGO6PaFXlQq1ysACFJHDt3QIoHEPl",
  scope: "openid connections email identity guilds",
  loadUserInfo: true,
  redirect_uri: "http://0.0.0.0:9000/callback",
  metadata: {
    authorization_endpoint: oauthBaseUrl + "/authorize",
    token_endpoint: oauthBaseUrl + "/token",
  }
}

export const userManager = makeUserManager(userManagerConfig);
const AppWithAuth = makeAuthenticator({
  userManager: userManager,
  placeholderComponent: () => {return <div>Not logged in</div>},
  signinArgs: {
    state: {
      foo: 15
    }
  }
})(App)

export { AppWithAuth as App };
