package com.eventsourcing.payment.domain.aggregate;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

import com.eventsourcing.payment.domain.command.ApprovePaymentCommand;
import com.eventsourcing.payment.domain.command.RequestPaymentCommand;
import com.eventsourcing.payment.domain.command.VerifyPaymentCommand;
import com.eventsourcing.payment.domain.event.PaymentApprovedEvent;
import com.eventsourcing.payment.domain.event.PaymentRequestedEvent;
import com.eventsourcing.payment.domain.event.PaymentVerifiedEvent;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
public final class PaymentAggregate {
    @AggregateIdentifier
    private String paymentId;
    private String memberId;
    private String itemId;
    private double amount;
    private boolean verified;
    private boolean approved;

    public PaymentAggregate() {}

    @CommandHandler
    public PaymentAggregate(RequestPaymentCommand command) {
        apply(new PaymentRequestedEvent(
                command.paymentId(),
                command.memberId(),
                command.itemId(),
                command.amount()
        ));
    }

    @EventSourcingHandler
    public void on(PaymentRequestedEvent event) {
        this.paymentId = event.paymentId();
        this.memberId = event.memberId();
        this.itemId = event.itemId();
        this.amount = event.amount();
        this.verified = false;
        this.approved = false;
    }

    @CommandHandler
    public void handle(VerifyPaymentCommand command) {
        if (this.verified) throw new IllegalStateException("Payment is already verified.");
        apply(new PaymentVerifiedEvent(command.paymentId()));
    }

    @EventSourcingHandler
    public void on(PaymentVerifiedEvent event) {
        this.verified = true;
    }

    @CommandHandler
    public void handle(ApprovePaymentCommand command) {
        if (!verified) throw new IllegalStateException("Payment must be verified before approval.");
        if (this.approved) throw new IllegalStateException("Payment is already approved.");
        apply(new PaymentApprovedEvent(command.paymentId()));
    }

    @EventSourcingHandler
    public void on(PaymentApprovedEvent event) {
        this.approved = true;
    }
}
