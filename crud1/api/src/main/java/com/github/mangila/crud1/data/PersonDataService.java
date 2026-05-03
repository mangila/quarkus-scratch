package com.github.mangila.crud1.data;

import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class PersonDataService {

    private final PersonPostgresRepository personPostgresRepository;

    public PersonDataService(PersonPostgresRepository personPostgresRepository) {
        this.personPostgresRepository = personPostgresRepository;
    }

    @Transactional
    public List<PersonEntity> findPage(Page page) {
        Sort sort = Sort.by("name");
        return personPostgresRepository.findAll(sort)
                .page(page)
                .list();
    }

    @Transactional
    public Optional<PersonEntity> findById(UUID id) {
        return personPostgresRepository.findByIdOptional(id);
    }

    @Transactional
    public UUID persist(PersonEntity entity) {
        personPostgresRepository.persist(entity);
        return entity.getId();
    }

    @Transactional
    public void update(PersonEntity entityToUpdate) throws EntityNotFoundException {
        final PersonEntity managed = personPostgresRepository.findByIdOptional(entityToUpdate.getId())
                .orElseThrow(() -> new EntityNotFoundException("Person not found with id: %s".formatted(entityToUpdate.getId())));
        managed.setName(entityToUpdate.getName());
        managed.setBirthDate(entityToUpdate.getBirthDate());
        managed.setEmail(entityToUpdate.getEmail());
        managed.setPhone(entityToUpdate.getPhone());
        managed.setProperties(entityToUpdate.getProperties());
    }
}
