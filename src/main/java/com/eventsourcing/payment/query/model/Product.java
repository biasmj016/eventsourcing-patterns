package com.eventsourcing.payment.query.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public record Product(
        @Id
        String itemId,
        double price,
        String description) {
}

