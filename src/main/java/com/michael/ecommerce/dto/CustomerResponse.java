package com.michael.ecommerce.dto;

import com.michael.ecommerce.entity.Customer;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Customer data returned by the API")
public class CustomerResponse {

    @Schema(description = "Customer ID", example = "1")
    public Long id;

    @Schema(description = "Customer name", example = "Jane")
    public String name;

    @Schema(description = "Customer lastName", example = "Doe")
    public String lastName;

    @Schema(description = "Customer email", example = "Jane.Doe@mail.com")
    public String email;

    @Schema(description = "Customer phone", example = "8415674171")
    public String phone;

    @Schema(description = "Customer address", example = "25st")
    public String address;

    @Schema(description = "Creation timestamp")
    public LocalDateTime createdAt;

    @Schema(description = "Last update timestamp")
    public LocalDateTime updatedAt;

    public static CustomerResponse from (Customer customer) {
        CustomerResponse response = new CustomerResponse();
        response.id = customer.id;
        response.name = customer.name;
        response.lastName = customer.lastName;
        response.email = customer.email;
        response.phone = customer.phone;
        response.address = customer.address;
        response.createdAt = customer.createdAt;
        response.updatedAt = customer.updatedAt;
        return response;
    }
}
