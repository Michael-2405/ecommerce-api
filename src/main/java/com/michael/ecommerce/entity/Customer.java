package com.michael.ecommerce.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "customers")
public class Customer extends BaseEntity {

    @Column(name = "name", nullable = false, length = 50)
    public String name;

    @Column(name = "last_name", nullable = false, length = 50)
    public String lastName;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    public String email;

    @Column(name = "phone", length = 20)
    public String phone;

    @Column(name = "address", length = 100)
    public String address;

    public static Customer findByEmail(String email) {
        return find("LOWER(email) = ?1", email.toLowerCase()).firstResult();
    }
}
