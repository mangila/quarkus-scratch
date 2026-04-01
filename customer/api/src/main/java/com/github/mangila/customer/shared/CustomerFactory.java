package com.github.mangila.customer.shared;

import com.github.mangila.customer.domain.Customer;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class CustomerFactory {

    public Customer from(String name,
                         String email,
                         String address,
                         String phone) {
        final var id = UUID.randomUUID();
        return new Customer(id, name, email, address, phone, Optional.empty());
    }
}
