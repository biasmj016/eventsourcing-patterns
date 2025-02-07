package com.eventsourcing.payment.query.model;

import jakarta.persistence.*;

@Entity
public class PaymentHistory {

    @Id
    private String paymentId;
    private String memberId;
    private String itemId;
    private double amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "varchar(255)")
    private PaymentStatus status;

    protected PaymentHistory() {}

    public PaymentHistory(String paymentId, String memberId, String itemId, double amount, PaymentStatus status) {
        this.paymentId = paymentId;
        this.memberId = memberId;
        this.itemId = itemId;
        this.amount = amount;
        this.status = status;
    }

    public PaymentHistory toUpdatedStatus(PaymentStatus newStatus) {
        return new PaymentHistory(this.paymentId, this.memberId, this.itemId, this.amount, newStatus);
    }

    public String getPaymentId() {
        return paymentId;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getItemId() {
        return itemId;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public boolean checkPayment(double compareAmount, String compareItemId) {
        return amount == compareAmount && itemId.equals(compareItemId);
    }


    public enum PaymentStatus {
        REQUESTED,
        VERIFIED,
        APPROVED
    }
}

