package com.eventsourcing.payment.domain.event;

public record PaymentRequestedEvent(String paymentId,
                                    String memberId,
                                    String itemId,
                                    double amount) implements PaymentEvent {
}