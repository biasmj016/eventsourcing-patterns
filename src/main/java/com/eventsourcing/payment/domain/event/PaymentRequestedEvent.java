package com.eventsourcing.payment.domain.event;

public class PaymentRequestedEvent implements PaymentEvent {
    private final String paymentId;
    private final String memberId;
    private final String itemId;
    private final double amount;

    public PaymentRequestedEvent(String paymentId, String memberId, String itemId, double amount) {
        this.paymentId = paymentId;
        this.memberId = memberId;
        this.itemId = itemId;
        this.amount = amount;
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

    public double getAmount() {
        return amount;
    }
}
