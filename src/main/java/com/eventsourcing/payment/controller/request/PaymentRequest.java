package com.eventsourcing.payment.controller.request;

public record PaymentRequest(String paymentId,
                             String memberId,
                             String itemId,
                             int count) {
}
