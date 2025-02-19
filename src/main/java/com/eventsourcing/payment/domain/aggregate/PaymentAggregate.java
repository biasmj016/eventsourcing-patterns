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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aggregate
public final class PaymentAggregate {
    private static final Logger logger = LoggerFactory.getLogger(PaymentAggregate.class);

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
        logger.info("[CommandHandler] Handling RequestPaymentCommand: {}", command);
        apply(new PaymentRequestedEvent(
                command.getPaymentId(),
                command.getMemberId(),
                command.getItemId(),
                command.getAmount()
        ));
    }

    @EventSourcingHandler
    public void on(PaymentRequestedEvent event) {
        logger.info("[EventSourcingHandler] Applying PaymentRequestedEvent: {}", event);
        this.paymentId = event.getPaymentId();
        this.memberId = event.getMemberId();
        this.itemId = event.getItemId();
        this.amount = event.getAmount();
        this.verified = false;
        this.approved = false;
    }

    @CommandHandler
    public void handle(VerifyPaymentCommand command) {
        logger.info("[CommandHandler] Handling VerifyPaymentCommand: {}", command);
        if (this.verified) throw new IllegalStateException("Payment is already verified.");
        apply(new PaymentVerifiedEvent(command.getPaymentId()));
    }

    @EventSourcingHandler
    public void on(PaymentVerifiedEvent event) {
        logger.info("[EventSourcingHandler] Applying PaymentVerifiedEvent: {}", event);
        this.verified = true;
    }

    @CommandHandler
    public void handle(ApprovePaymentCommand command) {
        logger.info("[CommandHandler] Handling ApprovePaymentCommand: {}", command);
        if (!verified) throw new IllegalStateException("Payment must be verified before approval.");
        if (this.approved) throw new IllegalStateException("Payment is already approved.");
        apply(new PaymentApprovedEvent(command.getPaymentId()));
    }

    @EventSourcingHandler
    public void on(PaymentApprovedEvent event) {
        logger.info("[EventSourcingHandler] Applying PaymentApprovedEvent: {}", event);
        this.approved = true;
    }
}
