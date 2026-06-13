package com.michael.ecommerce.resource;

import com.michael.ecommerce.dto.ProductRequest;
import com.michael.ecommerce.dto.ProductResponse;
import com.michael.ecommerce.service.ProductService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import java.util.List;

@Path("/api/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Products", description = "Operations for managing products")
public class ProductResource {

    @Inject
    ProductService productService;

    @GET
    @Operation(
            summary = "List all products",
            description = "Returns all active products. Optionally filter by category using the categoryId query parameter."
    )
    public List<ProductResponse> list(
            @Parameter(description = "Filter products by category ID", example = "1")
            @QueryParam("categoryId") Long categoryId
    ) {
        return productService.findAll(categoryId);
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Get product by ID", description = "Returns a single product by its ID")
    public ProductResponse find(@PathParam("id") Long id) {
        return productService.findById(id);
    }

    @POST
    @Operation(summary = "Create product", description = "Creates a new product linked to an existing category")
    public Response create(@Valid ProductRequest request) {
        ProductResponse response = productService.create(request);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Update product", description = "Updates an existing product by its ID")
    public ProductResponse update(@PathParam("id") Long id, @Valid ProductRequest request) {
        return productService.update(id, request);
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete product", description = "Soft deletes a product (active = false)")
    public Response delete(@PathParam("id") Long id) {
        productService.delete(id);
        return Response.noContent().build();
    }
}
