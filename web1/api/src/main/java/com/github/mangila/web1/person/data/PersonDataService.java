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
    final PanacheQuery<PersonEntity> pageQuery = personPostgresRepository.findAll(sort).page(page);
    final List<PersonEntity> content = pageQuery.list();
    final long count = pageQuery.count();
    final int pageCount = pageQuery.pageCount();
    final boolean hasNextPage = pageQuery.hasNextPage();
    final boolean hasPreviousPage = pageQuery.hasPreviousPage();
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
