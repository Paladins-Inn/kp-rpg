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

package de.kaiserpfalzedv.core.ui.views.dashboard;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import de.kaiserpfalzedv.core.ui.MainView;
import de.kaiserpfalzedv.core.ui.i18n.I18nPageTitle;

@Route(value = "/dashboard", layout = MainView.class)
@I18nPageTitle("dashboard.title")
@CssImport("./views/dashboard-view.css")
public class DashboardView extends Div {

    public DashboardView() {
        addClassName("dashboard-view");
        add(new Text("Content placeholder"));
    }

}
