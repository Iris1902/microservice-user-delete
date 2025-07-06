package org.uce.controller;

import org.uce.entity.User;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.transaction.Transactional;

@Path("/api/users-delete")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController {

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response softDeleteUser(@PathParam("id") Long id) {
        User user = User.findById(id);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Usuario no encontrado").build();
        }
        if (!user.isActive) {
            return Response.status(Response.Status.BAD_REQUEST).entity("El usuario ya está inactivo").build();
        }
        user.isActive = false;
        user.persist();
        return Response.ok("Usuario desactivado correctamente").build();
    }

    @POST
    @Path("/{id}/restore")
    @Transactional
    public Response restoreUser(@PathParam("id") Long id) {
        User user = User.findById(id);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Usuario no encontrado").build();
        }
        if (user.isActive) {
            return Response.status(Response.Status.BAD_REQUEST).entity("El usuario ya está activo").build();
        }
        user.isActive = true;
        user.persist();
        return Response.ok("Usuario restaurado correctamente").build();
    }

    @GET
    @Path("/health")
    public Response healthCheck() {
        return Response.ok("Service is up and running").build();
    }
}
