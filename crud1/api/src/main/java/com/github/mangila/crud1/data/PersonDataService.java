package com.github.mangila.crud1.data;

import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class PersonDataService {

    private final PersonPostgresRepository personPostgresRepository;

    public PersonDataService(PersonPostgresRepository personPostgresRepository) {
        this.personPostgresRepository = personPostgresRepository;
    }

    public List<PersonEntity> findPage(Page page) {
        Sort sort = Sort.by("name");
        return personPostgresRepository.findAll(sort)
                .page(page)
                .list();
    }

    public Optional<PersonEntity> findById(UUID id) {
        return personPostgresRepository.findByIdOptional(id);
    }
}
