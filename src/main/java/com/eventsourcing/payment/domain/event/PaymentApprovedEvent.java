package com.eventsourcing.payment.domain.event;

public class PaymentApprovedEvent implements PaymentEvent {
    private final String paymentId;

    public PaymentApprovedEvent(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaymentId() {
        return paymentId;
    }

    @Override
    public String toString() {
        return String.format(
                "PaymentApprovedEvent {\n    paymentId: '%s'\n}", paymentId
        );
    }
}
