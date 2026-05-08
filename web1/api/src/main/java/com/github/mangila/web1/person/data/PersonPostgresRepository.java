package com.github.mangila.web1.person.data;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.UUID;

@ApplicationScoped
public class PersonPostgresRepository implements PanacheRepositoryBase<PersonEntity, UUID> {}
