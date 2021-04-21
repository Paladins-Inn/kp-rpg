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

package de.kaiserpfalzedv.core.ui.views.about;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.router.Route;
import de.kaiserpfalzedv.core.ui.MainView;
import de.kaiserpfalzedv.core.ui.components.TorgScreen;
import de.kaiserpfalzedv.core.ui.i18n.I18nPageTitle;

@Route(value = "about", layout = MainView.class)
@I18nPageTitle("about.title")
@CssImport("./views/about-view.css")
public class AboutView extends TorgScreen {

    public AboutView() {
        H1 title = new H1(getTranslation("application.title"));
        H2 description = new H2(getTranslation("application.description"));
        H2 version = new H2(getTranslation("application.version"));
        Span torgLicense = new Span(getTranslation("application.torg-license"));

        add(title, description, version, torgLicense);
    }
}
