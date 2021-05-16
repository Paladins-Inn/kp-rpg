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

package de.kaiserpfalzedv.rpg.core.rest;

import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * LoggingFilter --
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 0.3.0  2021-05-13
 */
@Slf4j
public class LoggingFilter implements ContainerRequestFilter, ClientResponseFilter, WriterInterceptor {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    @Override
    public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) throws IOException {
        try {
            log.trace(
                    "uri='{} {}', headers={}, body:\n-----8<-----8<-----8<-----\n{}\n----->8----->8----->8-----",
                    requestContext.getMethod(),
                    requestContext.getUri(),
                    requestContext.getStringHeaders(),
                    requestContext.getEntity()
            );

            String responseBody = "(empty)";
            if (responseContext.hasEntity()) {
                StringBuilder sb = new StringBuilder();

                responseContext.setEntityStream(logInboundEntity(sb, responseContext.getEntityStream()
                ));

                responseBody = sb.toString();
            }

            log.trace(
                    "status={}, headers={}, body:\n-----8<-----8<-----8<-----\n{}\n----->8----->8----->8-----",
                    responseContext.getStatus(),
                    responseContext.getHeaders(),
                    responseBody
            );
        } catch (NullPointerException e) {
            log.error("Catching Exception during request logging. requestContext={}, exception='{}'", requestContext, e.getMessage());
        }
    }

    private InputStream logInboundEntity(final StringBuilder b, InputStream stream) throws IOException {
        if (!stream.markSupported()) {
            stream = new BufferedInputStream(stream);
        }
        int maxEntitySize = 1024 * 10;
        stream.mark(maxEntitySize + 1);
        final byte[] entity = new byte[maxEntitySize + 1];
        final int entitySize = stream.read(entity);
        b.append(new String(entity, 0, Math.min(entitySize, maxEntitySize), LoggingFilter.DEFAULT_CHARSET));
        if (entitySize > maxEntitySize) {
            b.append("...more...");
        }
        b.append('\n');
        stream.reset();
        return stream;
    }

    @Override
    public void filter(ContainerRequestContext context) {
        try {
            log.trace(
                    "3. method='{} {}', headers={}, body:\n-----8<-----8<-----8<-----\n{}\n----->8----->8----->8-----",
                    context.getMethod(),
                    context.getUriInfo(),
                    context.getHeaders(),
                    context.getEntityStream()
            );
        } catch (NullPointerException e) {
            log.error("Catching NPE during request logging. context={}", context);
        }
    }

    @Override
    public void aroundWriteTo(WriterInterceptorContext context) throws WebApplicationException {
        try {
            log.trace(
                    "headers={}, body:\n-----8<-----8<-----8<-----\n{}\n----->8----->8----->8-----",
                    context.getHeaders(),
                    context.getEntity()
            );
        } catch (NullPointerException e) {
            log.error("Catching NPE during request logging. context={}", context);
        }
    }
}
