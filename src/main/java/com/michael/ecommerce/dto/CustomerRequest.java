package com.michael.ecommerce.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(description = "Request payload for creating or updating a customer")
public class CustomerRequest {

    @NotBlank(message = "The name is required")
    @Size(max = 50, message = "The name has a max of 50 characters")
    @Schema(description = "Customer name", example = "Jane", required = true)
    public String name;

    @NotBlank(message = "The last name is required")
    @Size(max = 50, message = "The last name has a max of 50 characters")
    @Schema(description = "Customer last name", example = "Doe", required = true)
    public String lastName;

    @NotBlank(message = "The email is required")
    @Email(message = "The email format is invalid")
    @Size(max = 250, message = "The email has a max of 50 characters")
    @Schema(description = "Customer email", example = "Jane.Doe@mail.com", required = true)
    public String email;

    @Size(max = 20, message = "The phone has a max of 20 characters")
    @Schema(description = "Customer phone", example = "8415674171", required = false)
    public String phone;

    @Size(max = 100, message = "The address has a max of 100 characters")
    @Schema(description = "Customer address", example = "25st", required = false)
    public String address;
}
