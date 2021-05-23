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

import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

/**
 * SecurityConfiguration --
 *
 * @author paulroemer (github.com/vaadin-lerning-center/spring-secured-vaadin)
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 0.1.0  2021-03-26
 */
@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    static final String LOGIN_URL = "/login";
    private static final Logger LOG = LoggerFactory.getLogger(SecurityConfiguration.class);
    private static final String LOGIN_PROCESSING_URL = "/login";
    private static final String LOGIN_FAILURE_URL = "/login";
    private static final String LOGOUT_SUCCESS_URL = "/login";

    /**
     * Datasource for logins
     */
    private final DataSource dataSource;

    @Autowired
    public SecurityConfiguration(@NotNull DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Require login to access internal pages and configure login form.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        LOG.info("Configuring Vaadin security.");

        http
                // Not using Spring CSRF here to be able to use plain HTML for the login page
                .csrf().disable() //

                // Register our CustomRequestCache that saves unauthorized access attempts, so
                // the user is redirected after login.
                .requestCache().requestCache(new CustomRequestCache()) //

                // Restrict access to our application.
                .and().authorizeRequests()

                // Allow all flow internal requests.
                .requestMatchers(SecurityUtils::isFrameworkInternalRequest).permitAll() //

                // Allow all requests by logged in users.
                .anyRequest().authenticated() //

                // Configure the login page.
                .and().formLogin()
                .loginPage(LOGIN_URL).permitAll() //
                .loginProcessingUrl(LOGIN_PROCESSING_URL) //
                .failureUrl(LOGIN_FAILURE_URL)

                // Configure logout
                .and().logout().logoutSuccessUrl(LOGOUT_SUCCESS_URL)

                .and().headers().frameOptions().disable();
    }

    /**
     * Allows access to static resources, bypassing Spring security.
     */
    @Override
    public void configure(WebSecurity web) {
        LOG.info("Configuring web security.");

        web.ignoring().antMatchers(
                // Vaadin Flow static resources
                "/VAADIN/**",

                // the standard favicon URI
                "/favicon.ico",

                // the robots exclusion standard
                "/robots.txt",

                // Actuator and API
                "/api/**",
                "/actuator/**",

                // ThreatCard Editor
                "/ThreatCard/**",

                "/register",
                "/register/**",

                // web application manifest
                "/manifest.webmanifest",
                "/sw.js",
                "/offline-page.html",
                "/offline.html",

                // icons and images
                "/icons/**",
                "/images/**",
                "/img/**",

                // (development mode) static resources
                "/frontend/**",

                // (development mode) webjars
                "/webjars/**",

                // (production mode) static resources
                "/frontend-es5/**", "/frontend-es6/**");
    }


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        LOG.info("Configuring database based authentication.");

        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("select USERNAME,PASSWORD,ACCOUNT_ENABLED AND NOT ACCOUNT_LOCKED from PERSONS where USERNAME=?")
                .authoritiesByUsernameQuery("select p.USERNAME,r.ROLE from PERSONS p join ROLES r on p.ID=r.PERSON_ID where p.USERNAME=?");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        LOG.debug("Creating the BCrypt based password encoder.");

        return new BCryptPasswordEncoder();
    }
}
