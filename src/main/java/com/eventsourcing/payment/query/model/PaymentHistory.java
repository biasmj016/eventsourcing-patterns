package com.eventsourcing.payment.query.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public record PaymentHistory(
        @Id String paymentId,
        String memberId,
        String itemId,
        double amount,
        PaymentStatus status) {

    public PaymentHistory toUpdatedStatus(PaymentStatus newStatus) {
        return new PaymentHistory(this.paymentId, this.memberId, this.itemId, this.amount, newStatus);
    }

    public enum PaymentStatus {
        REQUESTED,
        VERIFIED,
        APPROVED
    }
}
