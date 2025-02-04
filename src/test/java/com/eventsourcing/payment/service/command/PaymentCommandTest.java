package com.eventsourcing.payment.service.command;

import com.eventsourcing.payment.domain.command.ApprovePaymentCommand;
import com.eventsourcing.payment.domain.command.RequestPaymentCommand;
import com.eventsourcing.payment.domain.command.VerifyPaymentCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PaymentCommandTest {

    private CommandGateway commandGateway;
    private PaymentCommand.PaymentCommandService service;

    @BeforeEach
    void setUp() {
        commandGateway = Mockito.mock(CommandGateway.class);
        service = new PaymentCommand.PaymentCommandService(commandGateway);
    }

    @Test
    void testRequestPayment() {
        String paymentId = "payment_req_202502031547";
        String memberId = "01000000000";
        String itemId = "Ticket_BUS";
        double amount = 100.0;
        when(commandGateway.send(any(RequestPaymentCommand.class)))
                .thenReturn(CompletableFuture.completedFuture(paymentId));

        CompletableFuture<String> result = service.requestPayment(paymentId, memberId, itemId, amount);

        ArgumentCaptor<RequestPaymentCommand> captor = ArgumentCaptor.forClass(RequestPaymentCommand.class);
        verify(commandGateway, times(1)).send(captor.capture());
        RequestPaymentCommand sentCommand = captor.getValue();
        assertThat(sentCommand.paymentId()).isEqualTo(paymentId);
        assertThat(result).isCompletedWithValue(paymentId);
    }

    @Test
    void testVerifyPayment() {
        String paymentId = "payment_req_202502031547";
        when(commandGateway.send(any(VerifyPaymentCommand.class)))
                .thenReturn(CompletableFuture.completedFuture(paymentId));

        CompletableFuture<String> result = service.verifyPayment(paymentId);

        ArgumentCaptor<VerifyPaymentCommand> captor = ArgumentCaptor.forClass(VerifyPaymentCommand.class);
        verify(commandGateway, times(1)).send(captor.capture());
        assertThat(captor.getValue().paymentId()).isEqualTo(paymentId);
        assertThat(result).isCompletedWithValue(paymentId);
    }

    @Test
    void testApprovePayment() {
        String paymentId = "payment_req_202502031547";
        when(commandGateway.send(any(ApprovePaymentCommand.class)))
                .thenReturn(CompletableFuture.completedFuture(paymentId));

        CompletableFuture<String> result = service.approvePayment(paymentId);

        ArgumentCaptor<ApprovePaymentCommand> captor = ArgumentCaptor.forClass(ApprovePaymentCommand.class);
        verify(commandGateway, times(1)).send(captor.capture());
        assertThat(captor.getValue().paymentId()).isEqualTo(paymentId);
        assertThat(result).isCompletedWithValue(paymentId);
    }
}