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

package de.kaiserpfalzedv.core.security;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * ConfigureUIServiceInitListener --
 *
 * @author paulroemer (github.com/vaadin-lerning-center/spring-secured-vaadin)
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 0.1.0  2021-03-26
 */
@Component
@Sfl4j
public class ConfigureUIServiceInitListener implements VaadinServiceInitListener {
    private static final List<String> ALLOWED_PATHS = Arrays.asList("register", "images", "img", "icons", "api", "actuator", "static");


    @Override
    public void serviceInit(ServiceInitEvent event) {
        log.trace("Service initialization. service={}", event.getSource().getServiceName());

        event.getSource().addUIInitListener(uiEvent -> {
            final UI ui = uiEvent.getUI();
            ui.addBeforeEnterListener(this::beforeEnter);
        });
    }

    /**
     * Reroutes the user if (s)he is not authorized to access the view.
     *
     * @param event before navigation event with event details
     */
    private void beforeEnter(BeforeEnterEvent event) {
        log.trace("Check security. ui={}, location={}", event.getUI().getId(), event.getLocation().getPath());

        String path = event.getLocation().getPath();
        path = path.startsWith("/") ? path.substring(1) : path;
        String[] firstUrlPath = path.split("/");
        log.debug("Checking Request. resource={}, pathElements={}, allowedPaths={}", event.getLocation().getPath(), firstUrlPath, ALLOWED_PATHS);

        if (firstUrlPath.length >= 1 && ALLOWED_PATHS.contains(firstUrlPath[0])) {
            log.debug("Request ok. resource={}", event.getLocation().getPath());
            return;
        }

        if (!SecurityUtils.isUserLoggedIn()) {
            log.debug("User tries to login. Everything is fine with not being logged in.");

            if (!event.getLocation().getPath().equals(SecurityConfiguration.logIN_URL.substring(1))) {
                log.info("User is not logged in. Redirecting to login page. loginPage={}", SecurityConfiguration.logIN_URL);

                UI.getCurrent().getPage().setLocation(SecurityConfiguration.logIN_URL);
            }
        }
    }

    @Bean
    public Authentication getPrincipal() {
        if (SecurityUtils.isUserLoggedIn()) {
            return SecurityContextHolder.getContext().getAuthentication();
        }

        return null;
    }
}
