package com.michael.ecommerce.resource;

import com.michael.ecommerce.dto.OrderRequest;
import com.michael.ecommerce.dto.OrderResponse;
import com.michael.ecommerce.entity.OrderStatus;
import com.michael.ecommerce.service.OrderService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@Path("/api/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Orders", description = "Operations for managing orders")
public class OrderResource {

    @Inject
    OrderService orderService;

    @GET
    @Operation(summary = "List all orders", description = "Returns all order in the system")
    public List<OrderResponse> list() {
        return orderService.findAll();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Get order by ID", description = "Returns a single order by its ID")
    public OrderResponse find(@PathParam("id") Long id) {
        return orderService.findById(id);
    }

    @POST
    @Operation(summary = "Create order", description = "Creates a new order")
    public Response create(@Valid OrderRequest request) {
        OrderResponse response = orderService.create(request);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @PATCH
    @Path("/{id}/status")
    @Operation(summary = "Update order status", description = "Updates the status or an existing order")
    public Response updateStatus(@PathParam("id") Long id, @QueryParam("status") OrderStatus status ) {
        OrderResponse response = orderService.updateStatus(id, status);
        return Response.status(Response.Status.NO_CONTENT).entity(response).build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete order", description = "Soft deletes an order (active = false)")
    public Response delete(@PathParam("id") Long id) {
        orderService.delete(id);
        return Response.noContent().build();
    }
}
