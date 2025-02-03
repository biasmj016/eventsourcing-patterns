package com.eventsourcing.payment.domain.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public record RequestPaymentCommand(@TargetAggregateIdentifier String paymentId,
                                    String memberId,
                                    String itemId,
                                    double amount) implements PaymentCommand {}
