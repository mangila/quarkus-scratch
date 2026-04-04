package com.github.mangila.customer.shared;

import com.github.mangila.customer.domain.Customer;
import com.github.mangila.customer.rest.cqrs.CreateCustomerCommand;
import com.github.mangila.shared.UuidFactory;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Collections;
import java.util.UUID;

@ApplicationScoped
public class CustomerFactory {

    private final UuidFactory uuidFactory;

    public CustomerFactory(UuidFactory uuidFactory) {
        this.uuidFactory = uuidFactory;
    }

    public Customer from(CreateCustomerCommand command) {
        final UUID id = uuidFactory.create();
        return new Customer(id, command.name(), command.address(), command.email(), command.phone(), Collections.emptyList());
    }

}
