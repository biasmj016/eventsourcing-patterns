package com.eventsourcing.payment.query.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public record Member(
        @Id String memberId,
        String name
) {
}

