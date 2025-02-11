/*******************************************************************************
 * Copyright (c) 2023 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package jaxrspropagation.responses;

import static jakarta.ws.rs.core.Response.Status.BAD_REQUEST;

import java.net.URI;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.UriInfo;

/**
 *
 */
@ApplicationPath("/")
@Path("responseCodeEndpoints")
public class JaxRsResponseCodeTestEndpoints extends Application {

    @Path("200")
    @GET
    public Response get200() {
        return Response.ok("get200").build();
    }

    @Path("202")
    @GET
    public Response get202() {
        return Response.accepted("get202").build();
    }

    @Path("400")
    @GET
    public Response get400() {
        return Response.status(BAD_REQUEST)
                        .entity("get400")
                        .build();
    }

    @Path("404")
    @GET
    public Response get404() {
        return Response.status(Status.NOT_FOUND)
                        .entity("get404")
                        .build();
    }

    @Path("500")
    @GET
    public Response get500() {
        return Response.status(Status.INTERNAL_SERVER_ERROR)
                        .entity("get500")
                        .build();
    }

    @Path("307")
    @GET
    public Response get307(@Context UriInfo uriInfo) {
        URI targetUri = uriInfo.getBaseUriBuilder()
                        .path(JaxRsResponseCodeTestEndpoints.class)
                        .path(JaxRsResponseCodeTestEndpoints.class, "redirectTarget")
                        .build();
        return Response.temporaryRedirect(targetUri).entity("get307").build();
    }

    @Path("redirectTarget")
    @GET
    public Response redirectTarget() {
        return Response.ok("redirectTarget").build();
    }

}
