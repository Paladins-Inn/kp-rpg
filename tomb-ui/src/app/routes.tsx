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
import {Route, RouteComponentProps, Switch} from 'react-router-dom';
import {Alert, PageSection} from '@patternfly/react-core';
import {DynamicImport} from '@app/DynamicImport';
import {accessibleRouteChangeHandler} from '@app/utils/utils';
import {Dashboard} from '@app/Components/Dashboard/Dashboard';
import {GeneralSettings} from '@app/Components/Settings/General/GeneralSettings';
import {ProfileSettings} from '@app/Components/Settings/Profile/ProfileSettings';
import {NotFound} from '@app/Components/NotFound/NotFound';
import {useDocumentTitle} from '@app/utils/useDocumentTitle';
import {LastLocationProvider, useLastLocation} from 'react-router-last-location';
import {Callback} from "react-oidc";
import {AppWithAuth, userManager} from "@app/index";

let routeFocusTimer: number;

const getSupportModuleAsync = () => () => import(/* webpackChunkName: 'support' */ '@app/Components/Support/Support');

const Support = (routeProps: RouteComponentProps): React.ReactElement => {
  const lastNavigation = useLastLocation();
  return (
    /* eslint-disable @typescript-eslint/no-explicit-any */
    <DynamicImport load={getSupportModuleAsync()} focusContentAfterMount={lastNavigation !== null}>
      {(Component: any) => {
        let loadedComponent: any;
        /* eslint-enable @typescript-eslint/no-explicit-any */
        if (Component === null) {
          loadedComponent = (
            <PageSection aria-label="Loading Content Container">
              <div className="pf-l-bullseye">
                <Alert title="Loading" variant="info" className="pf-l-bullseye__item" />
              </div>
            </PageSection>
          );
        } else {
          loadedComponent = <Component.Support {...routeProps} />;
        }
        return loadedComponent;
      }}
    </DynamicImport>
  );
};

export interface IAppRoute {
  label?: string; // Excluding the label will exclude the route from the nav sidebar in AppLayout
  /* eslint-disable @typescript-eslint/no-explicit-any */
  component: React.ComponentType<RouteComponentProps<any>> | React.ComponentType<any>;
  /* eslint-enable @typescript-eslint/no-explicit-any */
  exact?: boolean;
  path: string;
  title: string;
  isAsync?: boolean;
  routes?: undefined;
}

export interface IAppRouteGroup {
  label: string;
  routes: IAppRoute[];
}

export type AppRouteConfig = IAppRoute | IAppRouteGroup;

const OidcCallback = (routeProps, user) =>
  <Callback
    onSuccess={user => {
      // `user.state` will reflect the state that was passed in via signinArgs.
      routeProps.history.push('/')
    }}
    userManager={userManager}/>


const routes: AppRouteConfig[] = [
  {
    component: OidcCallback,
    exact: true,
    path: '/callback',
    title: 'OIDC Callback',
  },
  {
    component: Dashboard,
    exact: true,
    label: 'Dashboard',
    path: '/',
    title: 'Dashboard',
  },
  {
    component: Support,
    exact: true,
    isAsync: true,
    label: 'Support',
    path: '/support',
    title: 'Support',
  },
  {
    label: 'Settings',
    routes: [
      {
        component: GeneralSettings,
        exact: true,
        label: 'General',
        path: '/settings/general',
        title: 'General Settings',
      },
      {
        component: ProfileSettings,
        exact: true,
        label: 'Profile',
        path: '/settings/profile',
        title: 'Profile Settings',
      },
    ],
  },
];

// a custom hook for sending focus to the primary content container
// after a view has loaded so that subsequent press of tab key
// sends focus directly to relevant content
const useA11yRouteChange = (isAsync: boolean) => {
  const lastNavigation = useLastLocation();
  React.useEffect(() => {
    if (!isAsync && lastNavigation !== null) {
      routeFocusTimer = accessibleRouteChangeHandler();
    }
    return () => {
      window.clearTimeout(routeFocusTimer);
    };
  }, [isAsync, lastNavigation]);
};

const RouteWithTitleUpdates = ({ component: Component, isAsync = false, title, ...rest }: IAppRoute) => {
  useA11yRouteChange(isAsync);
  useDocumentTitle(title);

  function routeWithTitle(routeProps: RouteComponentProps) {
    return <Component {...rest} {...routeProps} />;
  }

  return <Route render={routeWithTitle} />;
};

const PageNotFound = ({ title }: { title: string }) => {
  useDocumentTitle(title);
  return <Route component={NotFound} />;
};

const flattenedRoutes: IAppRoute[] = routes.reduce(
  (flattened, route) => [...flattened, ...(route.routes ? route.routes : [route])],
  [] as IAppRoute[]
);

const AppRoutes = (): React.ReactElement => (
  <LastLocationProvider>
    <Switch>
      {flattenedRoutes.map(({ path, exact, component, title, isAsync }, idx) => (
        <RouteWithTitleUpdates
          path={path}
          exact={exact}
          component={component}
          key={idx}
          title={title}
          isAsync={isAsync}
        />
      ))}
      <PageNotFound title="404 Page Not Found" />
    </Switch>
  </LastLocationProvider>
);

export { AppRoutes, routes };
