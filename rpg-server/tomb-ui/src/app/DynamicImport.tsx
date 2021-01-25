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
import {accessibleRouteChangeHandler} from '@app/utils/utils';

interface IDynamicImport {
  /* eslint-disable @typescript-eslint/no-explicit-any */
  load: () => Promise<any>;
  children: any;
  /* eslint-enable @typescript-eslint/no-explicit-any */
  focusContentAfterMount: boolean;
}

class DynamicImport extends React.Component<IDynamicImport> {
  public state = {
    component: null,
  };
  private routeFocusTimer: number;
  constructor(props: IDynamicImport) {
    super(props);
    this.routeFocusTimer = 0;
  }
  public componentWillUnmount(): void {
    window.clearTimeout(this.routeFocusTimer);
  }
  public componentDidMount(): void {
    this.props
      .load()
      .then((component) => {
        if (component) {
          this.setState({
            component: component.default ? component.default : component,
          });
        }
      })
      .then(() => {
        if (this.props.focusContentAfterMount) {
          this.routeFocusTimer = accessibleRouteChangeHandler();
        }
      });
  }
  public render(): React.ReactNode {
    return this.props.children(this.state.component);
  }
}

export { DynamicImport };
