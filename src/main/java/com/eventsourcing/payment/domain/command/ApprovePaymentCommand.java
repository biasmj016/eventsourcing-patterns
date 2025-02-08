package com.eventsourcing.payment.domain.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public class ApprovePaymentCommand implements PaymentCommand {

    @TargetAggregateIdentifier
    private final String paymentId;

    public ApprovePaymentCommand(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaymentId() {
        return paymentId;
    }

    @Override
    public String toString() {
        return String.format(
                "ApprovePaymentCommand {\n    paymentId: '%s'\n}", paymentId
        );
    }
}
