package com.github.mangila.customer.data;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class CustomerPostgresRepository implements PanacheRepositoryBase<CustomerEntity, UUID> {

}
