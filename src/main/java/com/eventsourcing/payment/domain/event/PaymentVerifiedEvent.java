package com.eventsourcing.payment.domain.event;

public record PaymentVerifiedEvent(String paymentId) implements PaymentEvent {
}