package com.github.mangila.order.data;

import com.fasterxml.jackson.databind.JsonNode;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Type;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity(name = "Order")
@Table(name = "order_entry")
@Immutable
public class OrderEntity {

    @Id
    private UUID id;

    @Column(name = "customer_id", columnDefinition = "UUID")
    private UUID customerId;

    @Type(JsonType.class)
    @Column(columnDefinition = "JSONB")
    private JsonNode products;

    @Column(name = "price", columnDefinition = "NUMERIC(10,2)")
    private BigDecimal price;

    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt;

    public OrderEntity(UUID id, UUID customerId, JsonNode products, BigDecimal price) {
        this.id = id;
        this.customerId = customerId;
        this.products = products;
        this.price = price;
    }

    protected OrderEntity() {
        // do nothing, for ORM
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public JsonNode getProducts() {
        return products;
    }

    public void setProducts(JsonNode products) {
        this.products = products;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

}