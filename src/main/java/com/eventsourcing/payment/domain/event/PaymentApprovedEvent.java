package com.eventsourcing.payment.domain.event;

public record PaymentApprovedEvent(String paymentId) implements PaymentEvent {
}