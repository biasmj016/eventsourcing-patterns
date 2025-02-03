package com.eventsourcing.payment.domain.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public record ApprovePaymentCommand(@TargetAggregateIdentifier String paymentId) implements PaymentCommand {}
