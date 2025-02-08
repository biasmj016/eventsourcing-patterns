package com.eventsourcing.payment.controller.request;

public record PaymentVerifyRequest(String paymentId,
                                   String memberId,
                                   String itemId,
                                   double amount) {
}
