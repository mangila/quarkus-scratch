package com.github.mangila.web1.person.data;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
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
  public PersonEntityPage findPage(Page page) {
    final Sort sort = Sort.by("name").and("email");
    final PanacheQuery<PersonEntity> query = personPostgresRepository.findAll(sort);
    query.page(page);
    final List<PersonEntity> content = query.list();
    final long count = query.count();
    final int pageCount = query.pageCount();
    final boolean hasNextPage = query.hasNextPage();
    final boolean hasPreviousPage = query.hasPreviousPage();
    return new PersonEntityPage(content, count, pageCount, hasNextPage, hasPreviousPage);
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
    final PersonEntity managed =
        personPostgresRepository
            .findByIdOptional(entityToUpdate.getId())
            .orElseThrow(EntityNotFoundException::new);
    managed.setName(entityToUpdate.getName());
    managed.setBirthDate(entityToUpdate.getBirthDate());
    managed.setEmail(entityToUpdate.getEmail());
    managed.setPhones(entityToUpdate.getPhones());
    managed.setProperties(entityToUpdate.getProperties());
  }

  @Transactional
  public void delete(UUID uuid) throws EntityNotFoundException {
    final boolean deleted = personPostgresRepository.deleteById(uuid);
    if (!deleted) {
      throw new EntityNotFoundException();
    }
  }

  @Transactional
  public long count() {
    return personPostgresRepository.count();
  }
}
