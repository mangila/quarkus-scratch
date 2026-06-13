package com.github.mangila.web1.person.data;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.mangila.ensure4j.Ensure;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity(name = "Person")
@Table(name = "person")
@Audited
public class PersonEntity {

  @Id
  @Column(nullable = false, updatable = false)
  private UUID id;

  @Column(nullable = false, name = "person_name", columnDefinition = "TEXT")
  private String name;

  @Column(name = "birth_date", columnDefinition = "DATE", nullable = false)
  private LocalDate birthDate;

  @Column(nullable = false, unique = true, columnDefinition = "TEXT")
  private String email;

  @Type(JsonType.class)
  @Column(nullable = false, columnDefinition = "JSONB")
  private JsonNode phones;

  @Type(JsonType.class)
  @Column(nullable = false, name = "person_properties", columnDefinition = "JSONB")
  private JsonNode properties;

  @NotAudited
  @CreationTimestamp
  @Column(name = "created_at")
  private Instant createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private Instant updatedAt;

  @Column(name = "rev_version")
  @Version
  private Long version;

  public PersonEntity() {
    // do nothing, for ORM
  }

  public PersonEntity(
      UUID id,
      String name,
      LocalDate birthDate,
      String email,
      JsonNode phones,
      JsonNode properties) {
    this.id = Ensure.notNull(id, "id cannot be null");
    this.name = Ensure.notBlank(name, "name cannot be null");
    this.birthDate = Ensure.notNull(birthDate, "birthDate cannot be null");
    this.email = Ensure.notBlank(email, "email cannot be null");
    this.phones = Ensure.notNull(phones, "phones cannot be null");
    this.properties = Ensure.notNull(properties, "properties cannot be null");
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public LocalDate getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(LocalDate birthDate) {
    this.birthDate = birthDate;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public JsonNode getPhones() {
    return phones;
  }

  public void setPhones(JsonNode phone) {
    this.phones = phone;
  }

  public JsonNode getProperties() {
    return properties;
  }

  public void setProperties(JsonNode properties) {
    this.properties = properties;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Instant updatedAt) {
    this.updatedAt = updatedAt;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }
}
