package com.github.mangila.customer.shared;

import com.github.mangila.customer.data.CustomerEntity;
import com.github.mangila.customer.domain.Customer;
import com.github.mangila.customer.rest.cqrs.UpdateCustomerCommand;
import com.github.mangila.customer.rest.dto.CustomerDto;
import com.github.mangila.integration.csv.CustomerCsvRecord;
import com.github.mangila.shared.JsonService;
import com.github.mangila.shared.UuidFactory;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Collections;

@ApplicationScoped
public class CustomerMapper {

    private final JsonService jsonService;
    private final UuidFactory uuidFactory;

    public CustomerMapper(JsonService jsonService, UuidFactory uuidFactory) {
        this.jsonService = jsonService;
        this.uuidFactory = uuidFactory;
    }

    public Customer toDomain(CustomerDto dto) {
        final var id = dto.id();
        final var name = dto.name();
        final var address = dto.address();
        final var email = dto.email();
        final var phone = dto.phone();
        final var orders = dto.orders();
        return new Customer(id, name, address, email, phone, orders);
    }

    public CustomerEntity toEntity(Customer domain) {
        final var id = domain.id();
        final var name = domain.name();
        final var address = domain.address();
        final var email = domain.email();
        final var phone = domain.phone();
        final var orders = domain.orders();
        return new CustomerEntity(id, name, address, email, phone, orders);
    }

    public Customer toDomain(CustomerEntity entity) {
        final var id = entity.getId();
        final var name = entity.getName();
        final var address = entity.getAddress();
        final var email = entity.getEmail();
        final var phone = entity.getPhone();
        final var orders = entity.getOrders();
        return new Customer(id, name, address, email, phone, orders);
    }

    public CustomerDto toDto(Customer customer) {
        final var id = customer.id();
        final var name = customer.name();
        final var address = customer.address();
        final var email = customer.email();
        final var phone = customer.phone();
        final var orders = customer.orders();
        return new CustomerDto(id, name, address, email, phone, orders);
    }

    public Customer toDomain(UpdateCustomerCommand command) {
        final var id = command.id();
        final var name = command.name();
        final var address = command.address();
        final var email = command.email();
        final var phone = command.phone();
        return new Customer(id, name, address, email, phone, Collections.emptyList());
    }

    public Customer toDomain(CustomerCsvRecord csvRecord) {
        final var id = uuidFactory.create(csvRecord.getId());
        final var name = csvRecord.getName();
        final var address = jsonService.createObjectNode().put("street", csvRecord.getAddress());
        final var email = csvRecord.getEmail();
        final var phone = csvRecord.getPhone();
        return new Customer(id, name, address, email, phone, Collections.emptyList());
    }
}
