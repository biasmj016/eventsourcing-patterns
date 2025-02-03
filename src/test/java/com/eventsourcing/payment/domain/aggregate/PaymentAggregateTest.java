package com.eventsourcing.payment.domain.aggregate;

import com.eventsourcing.payment.domain.command.ApprovePaymentCommand;
import com.eventsourcing.payment.domain.command.RequestPaymentCommand;
import com.eventsourcing.payment.domain.command.VerifyPaymentCommand;
import com.eventsourcing.payment.domain.event.PaymentApprovedEvent;
import com.eventsourcing.payment.domain.event.PaymentRequestedEvent;
import com.eventsourcing.payment.domain.event.PaymentVerifiedEvent;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PaymentAggregateTest {

    private AggregateTestFixture<PaymentAggregate> fixture;

    @BeforeEach
    void setUp() {
        fixture = new AggregateTestFixture<>(PaymentAggregate.class);
    }

    @Test
    void testRequestPaymentCommand() {
        String paymentId = "payment_req_202502031547";
        String memberId = "01000000000";
        String itemId = "Ticket_BUS";
        double amount = 10000;
        RequestPaymentCommand command = new RequestPaymentCommand(paymentId, memberId, itemId, amount);

        fixture.givenNoPriorActivity()
                .when(command)
                .expectEvents(new PaymentRequestedEvent(paymentId, memberId, itemId, amount));
    }

    @Test
    void testVerifyPaymentCommand() {
        String paymentId = "payment_req_202502031547";
        String memberId = "01000000000";
        String itemId = "Ticket_BUS";
        double amount = 10000;

        fixture.given(new PaymentRequestedEvent(paymentId, memberId, itemId, amount))
                .when(new VerifyPaymentCommand(paymentId))
                .expectEvents(new PaymentVerifiedEvent(paymentId));
    }

    @Test
    void testApprovePaymentCommandSuccess() {
        String paymentId = "payment_req_202502031547";
        String memberId = "01000000000";
        String itemId = "Ticket_BUS";
        double amount = 10000;

        fixture.given(
                        new PaymentRequestedEvent(paymentId, memberId, itemId, amount),
                        new PaymentVerifiedEvent(paymentId)
                )
                .when(new ApprovePaymentCommand(paymentId))
                .expectEvents(new PaymentApprovedEvent(paymentId));
    }

    @Test
    void testApprovePaymentCommandFailsIfNotVerified() {
        String paymentId = "payment_req_202502031547";
        String memberId = "01000000000";
        String itemId = "Ticket_BUS";
        double amount = 10000;

        fixture.given(new PaymentRequestedEvent(paymentId, memberId, itemId, amount))
                .when(new ApprovePaymentCommand(paymentId))
                .expectException(IllegalStateException.class);
    }
}
