package com.github.mangila.customer.shared;

import com.github.mangila.customer.model.Customer;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.commons.csv.CSVRecord;

import java.util.UUID;

@ApplicationScoped
public class CustomerMapper {

    public Customer toDomain(CSVRecord record) {
        final var id = UUID.fromString(record.get("id"));
        final var name = record.get("name");
        final var address = record.get("address");
        final var email = record.get("email");
        final var phone = record.get("phone");
        return new Customer(id, name, address, email, phone);
    }

}
