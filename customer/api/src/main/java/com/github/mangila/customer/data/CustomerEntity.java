package com.github.mangila.customer.data;

import com.fasterxml.jackson.databind.JsonNode;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;

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

    @Type(JsonType.class)
    @Column(
            name = "orders",
            nullable = false,
            columnDefinition = "JSONB"
    )
    private List<UUID> orders = new ArrayList<>();

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
}
