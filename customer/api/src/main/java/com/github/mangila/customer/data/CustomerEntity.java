package com.github.mangila.customer.data;

import com.fasterxml.jackson.databind.JsonNode;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity(name = "Customer")
@Table(name = "customer")
@Audited
public class CustomerEntity {

    @Id
    private UUID id;

    @Column(nullable = false,
            columnDefinition = "TEXT"
    )
    private String name;

    @Type(JsonType.class)
    @Column(nullable = false,
            columnDefinition = "JSONB"
    )
    private JsonNode address;

    @Column(nullable = false,
            unique = true,
            columnDefinition = "TEXT"
    )
    private String email;

    @Column(nullable = false,
            columnDefinition = "TEXT"
    )
    private String phone;

    @NotAudited
    @Type(JsonType.class)
    @Column(
            name = "orders",
            nullable = false,
            columnDefinition = "JSONB"
    )
    private List<UUID> orders = new ArrayList<>();

    @NotAudited
    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    @Version
    private Long version;

    public CustomerEntity() {
        // do nothing, for ORM 🐍 - it's swedish for "snake"
    }

    public CustomerEntity(UUID id, String name, JsonNode address, String email, String phone, List<UUID> orders) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.orders = orders;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JsonNode getAddress() {
        return address;
    }

    public void setAddress(JsonNode address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<UUID> getOrders() {
        return orders;
    }

    public void setOrders(List<UUID> orders) {
        this.orders = orders;
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
