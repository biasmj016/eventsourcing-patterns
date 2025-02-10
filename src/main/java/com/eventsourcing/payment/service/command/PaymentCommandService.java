package com.eventsourcing.payment.service.command;

import com.eventsourcing.payment.domain.command.ApprovePaymentCommand;
import com.eventsourcing.payment.domain.command.RequestPaymentCommand;
import com.eventsourcing.payment.domain.command.VerifyPaymentCommand;
import com.eventsourcing.payment.repository.MemberRepository;
import com.eventsourcing.payment.repository.PaymentHistoryRepository;
import com.eventsourcing.payment.repository.ProductRepository;
import org.axonframework.commandhandling.CommandExecutionException;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class PaymentCommandService {
    private static final Logger logger = LoggerFactory.getLogger(PaymentCommandService.class);
    private final CommandGateway commandGateway;
    private final PaymentHistoryRepository paymentHistoryRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    public PaymentCommandService(CommandGateway commandGateway,
                                 PaymentHistoryRepository paymentHistoryRepository,
                                 MemberRepository memberRepository,
                                 ProductRepository productRepository) {
        this.commandGateway = commandGateway;
        this.paymentHistoryRepository = paymentHistoryRepository;
        this.memberRepository = memberRepository;
        this.productRepository = productRepository;
    }

    public CompletableFuture<Object> requestPayment(String paymentId, String memberId, String itemId, int count) {
        logger.info("[PaymentCommandService] Requesting payment: paymentId={}, memberId={}, itemId={}, count={}", paymentId, memberId, itemId, count);
        double totalAmount = productRepository.findById(itemId)
                .map(p -> p.getPrice() * count)
                .orElseThrow(() -> new IllegalStateException("Product not found: " + itemId));

        return commandGateway.send(new RequestPaymentCommand(paymentId, memberId, itemId, totalAmount))
                .exceptionally(ex -> handleCommandException("Payment request failed", ex));
    }


    public CompletableFuture<Object> verifyPayment(String paymentId, String memberId, String itemId, double amount) {
        logger.info("[PaymentCommandService] Verifying payment: paymentId={}, memberId={}, itemId={}, amount={}", paymentId, memberId, itemId, amount);
        memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalStateException("Member not found: " + memberId));

        paymentHistoryRepository.findById(paymentId)
                .filter(p -> p.checkPayment(amount, itemId))
                .orElseThrow(() -> new IllegalStateException("Amount is not match: " + itemId + "(" + amount + " Won)"));

        return commandGateway.send(new VerifyPaymentCommand(paymentId))
                .exceptionally(ex -> handleCommandException("Payment verification failed", ex));
    }

    public CompletableFuture<Object> approvePayment(String paymentId) {
        logger.info("[PaymentCommandService] Approving payment: paymentId={}", paymentId);
        return commandGateway.send(new ApprovePaymentCommand(paymentId))
                .exceptionally(ex -> handleCommandException("Payment approval failed", ex));
    }

    /**
     * 공통 예외 처리 메서드
     */
    private Object handleCommandException(String action, Throwable ex) {
        Throwable cause = Optional.ofNullable(ex.getCause()).orElse(ex);

        if (cause instanceof CommandExecutionException) {
            throw new IllegalArgumentException(action + ": " + cause.getMessage(), cause);
        }
        throw new IllegalStateException("Unexpected error occurred: " + cause.getMessage(), cause);
    }
}
