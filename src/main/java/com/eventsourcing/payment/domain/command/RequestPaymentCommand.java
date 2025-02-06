package com.eventsourcing.payment.domain.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public class RequestPaymentCommand implements PaymentCommand {

    @TargetAggregateIdentifier
    private final String paymentId;
    private final String memberId;
    private final String itemId;
    private final double amount;

    public RequestPaymentCommand(String paymentId, String memberId, String itemId, double amount) {
        this.paymentId = paymentId;
        this.memberId = memberId;
        this.itemId = itemId;
        this.amount = amount;
    }

    public String getPaymentId() { return paymentId; }
    public String getMemberId() { return memberId; }
    public String getItemId() { return itemId; }
    public double getAmount() { return amount; }
}
