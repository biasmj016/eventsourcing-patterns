package com.eventsourcing.payment.domain.event;

public class PaymentVerifiedEvent implements PaymentEvent {
    private final String paymentId;

    public PaymentVerifiedEvent(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaymentId() {
        return paymentId;
    }

    @Override
    public String toString() {
        return String.format(
                "PaymentVerifiedEvent {\n    paymentId: '%s'\n}", paymentId
        );
    }
}
