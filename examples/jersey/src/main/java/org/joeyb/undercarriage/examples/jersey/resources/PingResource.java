package org.joeyb.undercarriage.examples.jersey.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path(value = PingResource.PATH)
public class PingResource {

    public static final String PATH = "/ping";

    public static final String PONG = "pong";

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response get() {
        return Response.ok(PONG).build();
    }
}
