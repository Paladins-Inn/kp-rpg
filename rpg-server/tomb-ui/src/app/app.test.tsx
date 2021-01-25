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
import {App} from '@app/index';
import {mount, shallow} from 'enzyme';
import {Button} from '@patternfly/react-core';

describe('App tests', () => {
  test('should render default App component', () => {
    const view = shallow(<App />);
    expect(view).toMatchSnapshot();
  });

  it('should render a nav-toggle button', () => {
    const wrapper = mount(<App />);
    const button = wrapper.find(Button);
    expect(button.exists()).toBe(true);
  });

  it('should hide the sidebar on smaller viewports', () => {
    Object.defineProperty(window, 'innerWidth', {writable: true, configurable: true, value: 600});
    const wrapper = mount(<App />);
    window.dispatchEvent(new Event('resize'));
    expect(wrapper.find('#page-sidebar').hasClass('pf-m-collapsed')).toBeTruthy();
  });

  it('should expand the sidebar on larger viewports', () => {
    Object.defineProperty(window, 'innerWidth', {writable: true, configurable: true, value: 1200});
    const wrapper = mount(<App />);
    window.dispatchEvent(new Event('resize'));
    expect(wrapper.find('#page-sidebar').hasClass('pf-m-expanded')).toBeTruthy();
  });

  it('should hide the sidebar when clicking the nav-toggle button', () => {
    Object.defineProperty(window, 'innerWidth', {writable: true, configurable: true, value: 1200});
    const wrapper = mount(<App />);
    window.dispatchEvent(new Event('resize'));
    const button = wrapper.find('#nav-toggle').hostNodes();
    expect(wrapper.find('#page-sidebar').hasClass('pf-m-expanded')).toBeTruthy();
    button.simulate('click');
    expect(wrapper.find('#page-sidebar').hasClass('pf-m-collapsed')).toBeTruthy();
    expect(wrapper.find('#page-sidebar').hasClass('pf-m-expanded')).toBeFalsy();
  });
});
