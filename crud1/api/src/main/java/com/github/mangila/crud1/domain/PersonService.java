package com.github.mangila.crud1.domain;

import com.github.mangila.crud1.data.PersonDataService;
import com.github.mangila.crud1.data.PersonEntity;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class PersonService {

    private final PersonDomainMapper personDomainMapper;
    private final PersonDataService personDataService;

    public PersonService(PersonDomainMapper personDomainMapper,
                         PersonDataService personDataService) {
        this.personDomainMapper = personDomainMapper;
        this.personDataService = personDataService;
    }

    public List<Person> findPage(Page page) {
        final List<PersonEntity> entities = personDataService.findPage(page);
        return personDomainMapper.toDomains(entities);
    }

    public Optional<Person> findById(UUID id) {
        return personDataService.findById(id)
                .map(personDomainMapper::toDomain);
    }
}
