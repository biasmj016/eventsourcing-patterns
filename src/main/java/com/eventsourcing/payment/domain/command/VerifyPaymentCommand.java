package com.eventsourcing.payment.domain.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public class VerifyPaymentCommand implements PaymentCommand {

    @TargetAggregateIdentifier
    private final String paymentId;

    public VerifyPaymentCommand(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaymentId() {
        return paymentId;
    }

    @Override
    public String toString() {
        return String.format(
                "VerifyPaymentCommand {\n    paymentId: '%s'\n}", paymentId
        );
    }
}
