package com.github.mangila.customer.data;

import com.fasterxml.jackson.databind.JsonNode;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.Type;

import java.util.UUID;

@Entity
@Table(name = "customer")
public class CustomerEntity {

    @Id
    private UUID id;

    @Column(nullable = false,
            columnDefinition = "TEXT"
    )
    private String name;

    @Column(nullable = false,
            columnDefinition = "TEXT"
    )
    private String address;

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
            nullable = false,
            columnDefinition = "JSONB"
    )
    private JsonNode favoritePokemon;

    public CustomerEntity() {
    }

    public CustomerEntity(UUID id, String name, String address, String email, String phone, JsonNode favoritePokemon) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.favoritePokemon = favoritePokemon;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
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

    public JsonNode getFavoritePokemon() {
        return favoritePokemon;
    }

    public void setFavoritePokemon(JsonNode favoritePokemon) {
        this.favoritePokemon = favoritePokemon;
    }
}
