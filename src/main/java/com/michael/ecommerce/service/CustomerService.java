package com.michael.ecommerce.service;

import com.michael.ecommerce.dto.CustomerRequest;
import com.michael.ecommerce.dto.CustomerResponse;
import com.michael.ecommerce.entity.Customer;
import com.michael.ecommerce.exception.BusinessException;
import com.michael.ecommerce.exception.ResourceNotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class CustomerService {
    public List<CustomerResponse> findAll() {
        return Customer.listAll()
                .stream()
                .map(entity -> CustomerResponse.from((Customer) entity))
                .toList();
    }

    public CustomerResponse findById(Long id) {
        Customer customer = (Customer) Customer.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", id));
        return CustomerResponse.from(customer);
    }

    @Transactional
    public CustomerResponse create(CustomerRequest request) {
        if (Customer.findByEmail(request.email) != null) {
            throw  new BusinessException(
                    "A customer with this email already exists: " + request.email
            );
        }

        Customer customer = new Customer();
        customer.name = request.name;
        customer.lastName = request.lastName;
        customer.email = request.email;
        customer.phone = request.phone;
        customer.address = request.address;
        customer.persist();

        return CustomerResponse.from(customer);
    }

    @Transactional
    public CustomerResponse update (Long id, CustomerRequest request) {
        Customer customer = (Customer) Customer.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", id));

        if (!customer.email.equalsIgnoreCase(request.email)) {
            if(Customer.findByEmail(request.email) != null) {
                throw  new BusinessException(
                        "A customer with this email already exists: " + request.email
                );
            }
        }

        customer.name = request.name;
        customer.lastName = request.lastName;
        customer.email = request.email;
        customer.phone = request.phone;
        customer.address = request.address;

        return CustomerResponse.from(customer);
    }

    @Transactional
    public void delete(Long id) {
        Customer customer = (Customer) Customer.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", id));
        customer.active = false;
    }
}
