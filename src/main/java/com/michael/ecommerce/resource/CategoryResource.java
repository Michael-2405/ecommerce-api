package com.michael.ecommerce.resource;

import com.michael.ecommerce.dto.CategoryRequest;
import com.michael.ecommerce.dto.CategoryResponse;
import com.michael.ecommerce.service.CategoryService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import java.util.List;

/**
 * REST resource for category management.
 * This layer only handles HTTP concerns:
 * receiving requests, delegating to the service, returning responses.
 */
@Path("/api/categories")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Categories", description = "Operations for managing product categories")
public class CategoryResource {

    @Inject
    CategoryService categoryService;

    @GET
    @Operation(summary = "List all categories", description = "Returns all categories in the system")
    public List<CategoryResponse> list() {
        return categoryService.findAll();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Get category by ID", description = "Returns a single category by its ID")
    public CategoryResponse find(@PathParam("id") Long id) {
        return categoryService.findById(id);
    }

    @POST
    @Operation(summary = "Create category", description = "Creates a new product category")
    public Response create(@Valid CategoryRequest request) {
        CategoryResponse response = categoryService.create(request);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Update category", description = "Updates an existing category by its ID")
    public CategoryResponse update(@PathParam("id") Long id, @Valid CategoryRequest request) {
        return categoryService.update(id, request);
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete category", description = "Soft deletes a category (active = false)")
    public Response delete(@PathParam("id") Long id) {
        categoryService.delete(id);
        return Response.noContent().build();
    }
}
