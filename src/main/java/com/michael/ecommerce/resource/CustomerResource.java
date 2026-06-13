package com.michael.ecommerce.resource;

import com.michael.ecommerce.dto.CustomerRequest;
import com.michael.ecommerce.dto.CustomerResponse;
import com.michael.ecommerce.service.CustomerService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@Path("/api/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Customers", description = "Operations for managing customers")
public class CustomerResource {

    @Inject
    CustomerService customerService;

    @GET
    @Operation(summary = "List all customers", description = "Returns all customers in the system")
    public List<CustomerResponse> list() {
        return  customerService.findAll();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Get customer by ID", description = "Returns single customer by its ID")
    public CustomerResponse find(@PathParam("id") Long id) {
        return  customerService.findById(id);
    }

    @POST
    @Operation(summary = "Create customer", description = "Creates a new customer")
    public Response create(@Valid CustomerRequest request) {
        CustomerResponse response = customerService.create(request);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Update customer", description = "Updates an existing customer by its ID")
    public CustomerResponse update(@PathParam("id") Long id, @Valid CustomerRequest request){
        return customerService.update(id, request);
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete customer", description = "Soft deletes a customer (active = false)")
    public Response delete(@PathParam("id") Long id) {
        customerService.delete(id);
        return  Response.noContent().build();
    }
}
