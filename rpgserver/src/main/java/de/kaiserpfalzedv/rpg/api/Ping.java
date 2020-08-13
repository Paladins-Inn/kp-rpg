package de.kaiserpfalzedv.rpg.api;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/api/ping")
public class Ping {

    @GET
    @RolesAllowed({"api"})
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "pong";
    }
}