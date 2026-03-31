package com.github.mangila.customer.shared;

import com.github.mangila.customer.data.CustomerEntity;
import com.github.mangila.customer.domain.Customer;
import com.github.mangila.customer.web.CustomerDto;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.commons.csv.CSVRecord;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class CustomerMapper {

    private final JsonService jsonService;

    public CustomerMapper(JsonService jsonService) {
        this.jsonService = jsonService;
    }

    public Customer toDomain(CSVRecord record) {
        final var id = UUID.fromString(record.get("id"));
        final var name = record.get("name");
        final var address = record.get("address");
        final var email = record.get("email");
        final var phone = record.get("phone");
        return new Customer(id, name, address, email, phone, Optional.empty());
    }

    public Customer toDomain(CustomerDto dto) {
        final var id = dto.id();
        final var name = dto.name();
        final var address = dto.address();
        final var email = dto.email();
        final var phone = dto.phone();
        final var favoritePokemon = dto.favoritePokemon();
        return new Customer(id, name, address, email, phone, Optional.ofNullable(favoritePokemon));
    }

    public CustomerEntity toEntity(Customer customer) {
        final var id = customer.id();
        final var name = customer.name();
        final var address = customer.address();
        final var email = customer.email();
        final var phone = customer.phone();
        final var favoritePokemon = customer.favoritePokemon();
        return new CustomerEntity(id, name, address, email, phone, favoritePokemon.orElse(jsonService.createObjectNode()));
    }

    public Customer toDomain(CustomerEntity entity) {
        final var id = entity.getId();
        final var name = entity.getName();
        final var address = entity.getAddress();
        final var email = entity.getEmail();
        final var phone = entity.getPhone();
        final var favoritePokemon = entity.getFavoritePokemon();
        return new Customer(id, name, address, email, phone, Optional.ofNullable(favoritePokemon));
    }

    public CustomerDto toDto(Customer customer) {
        final var id = customer.id();
        final var name = customer.name();
        final var address = customer.address();
        final var email = customer.email();
        final var phone = customer.phone();
        final var favoritePokemon = customer.favoritePokemon();
        return new CustomerDto(id, name, address, email, phone, favoritePokemon.orElse(jsonService.createObjectNode()));
    }
}
