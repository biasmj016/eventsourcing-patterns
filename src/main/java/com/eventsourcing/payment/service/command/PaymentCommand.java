package com.eventsourcing.payment.service.command;

import com.eventsourcing.payment.domain.command.ApprovePaymentCommand;
import com.eventsourcing.payment.domain.command.RequestPaymentCommand;
import com.eventsourcing.payment.domain.command.VerifyPaymentCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

public interface PaymentCommand {
    CompletableFuture<String> requestPayment(String paymentId, String memberId, String itemId, double amount);
    CompletableFuture<String> verifyPayment(String paymentId);
    CompletableFuture<String> approvePayment(String paymentId);

    @Service
    class PaymentCommandService implements PaymentCommand {

        private final CommandGateway commandGateway;

        public PaymentCommandService(CommandGateway commandGateway) {
            this.commandGateway = commandGateway;
        }

        @Override
        public CompletableFuture<String> requestPayment(String paymentId, String memberId, String itemId, double amount) {
            return commandGateway.send(new RequestPaymentCommand(paymentId, memberId, itemId, amount));
        }

        @Override
        public CompletableFuture<String> verifyPayment(String paymentId) {
            return commandGateway.send(new VerifyPaymentCommand(paymentId));
        }

        @Override
        public CompletableFuture<String> approvePayment(String paymentId) {
            return commandGateway.send(new ApprovePaymentCommand(paymentId));
        }
    }
}
