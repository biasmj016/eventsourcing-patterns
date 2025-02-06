package com.eventsourcing.payment.service.command;

import com.eventsourcing.payment.domain.command.ApprovePaymentCommand;
import com.eventsourcing.payment.domain.command.RequestPaymentCommand;
import com.eventsourcing.payment.domain.command.VerifyPaymentCommand;
import com.eventsourcing.payment.query.model.Member;
import com.eventsourcing.payment.query.model.Product;
import com.eventsourcing.payment.repository.MemberRepository;
import com.eventsourcing.payment.repository.ProductRepository;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PaymentCommandServiceTest {

    private CommandGateway commandGateway;
    private MemberRepository memberRepository;
    private ProductRepository productRepository;
    private PaymentCommandService service;

    @BeforeEach
    void setUp() {
        commandGateway = Mockito.mock(CommandGateway.class);
        memberRepository = Mockito.mock(MemberRepository.class);
        productRepository = Mockito.mock(ProductRepository.class);
        service = new PaymentCommandService(commandGateway, memberRepository, productRepository);
    }

    @Test
    void testRequestPayment() {
        String paymentId = "payment_req_202502031547";
        String memberId = "01000000000";
        String itemId = "Ticket_BUS";
        int count = 2;

        when(productRepository.findById(itemId))
                .thenReturn(Optional.of(new Product(itemId, 10000, "Bus Ticket")));

        when(commandGateway.send(any(RequestPaymentCommand.class)))
                .thenReturn(CompletableFuture.completedFuture(paymentId));

        CompletableFuture<Object> result = service.requestPayment(paymentId, memberId, itemId, count);

        ArgumentCaptor<RequestPaymentCommand> captor = ArgumentCaptor.forClass(RequestPaymentCommand.class);
        verify(commandGateway, times(1)).send(captor.capture());
        RequestPaymentCommand sentCommand = captor.getValue();
        assertThat(sentCommand.getPaymentId()).isEqualTo(paymentId);
        assertThat(result).isCompletedWithValue(paymentId);
    }

    @Test
    void testVerifyPayment() {
        String paymentId = "payment_req_202502031547";
        String memberId = "01000000000";
        String itemId = "Ticket_BUS";
        double amount = 10000;
        // Setup repository mocks: Member exists and Product exists with matching price.
        when(memberRepository.findById(memberId))
                .thenReturn(Optional.of(new Member(memberId, "Test Member")));
        when(productRepository.findById(itemId))
                .thenReturn(Optional.of(new Product(itemId, 10000, "Bus Ticket")));
        when(commandGateway.send(any(VerifyPaymentCommand.class)))
                .thenReturn(CompletableFuture.completedFuture(paymentId));

        CompletableFuture<Object> result = service.verifyPayment(paymentId, memberId, itemId, amount);

        ArgumentCaptor<VerifyPaymentCommand> captor = ArgumentCaptor.forClass(VerifyPaymentCommand.class);
        verify(commandGateway, times(1)).send(captor.capture());
        VerifyPaymentCommand sentCommand = captor.getValue();
        assertThat(sentCommand.getPaymentId()).isEqualTo(paymentId);
        assertThat(result).isCompletedWithValue(paymentId);
    }

    @Test
    void testVerifyPayment_invalidMember() {
        String paymentId = "payment_req_202502031547";
        String memberId = "01000000000";
        String itemId = "Ticket_BUS";
        double amount = 10000;

        when(memberRepository.findById(memberId))
                .thenReturn(Optional.empty());

        try {
            service.verifyPayment(paymentId, memberId, itemId, amount);
        } catch (IllegalStateException e) {
            assertThat(e.getMessage()).contains("Member not found: " + memberId);
        }
        verify(commandGateway, never()).send(any(VerifyPaymentCommand.class));
    }

    @Test
    void testVerifyPayment_invalidProduct() {
        String paymentId = "payment_req_202502031547";
        String memberId = "01000000000";
        String itemId = "Ticket_BUS";
        double amount = 10000;

        when(memberRepository.findById(memberId))
                .thenReturn(Optional.of(new Member(memberId, "Test Member")));
        when(productRepository.findById(itemId))
                .thenReturn(Optional.empty());

        try {
            service.verifyPayment(paymentId, memberId, itemId, amount);
        } catch (IllegalStateException e) {
            assertThat(e.getMessage()).contains("Product is invalid: " + itemId);
        }
        verify(commandGateway, never()).send(any(VerifyPaymentCommand.class));
    }

    @Test
    void testVerifyPayment_invalidProductPrice() {
        String paymentId = "payment_req_202502031547";
        String memberId = "01000000000";
        String itemId = "Ticket_BUS";
        double amount = 50.0;
        when(memberRepository.findById(memberId))
                .thenReturn(Optional.of(new Member(memberId, "Test Member")));
        when(productRepository.findById(itemId))
                .thenReturn(Optional.of(new Product(itemId, 10000, "Bus Ticket")));

        try {
            service.verifyPayment(paymentId, memberId, itemId, amount);
        } catch (IllegalStateException e) {
            assertThat(e.getMessage()).contains("Product is invalid");
        }
        verify(commandGateway, never()).send(any(VerifyPaymentCommand.class));
    }

    @Test
    void testApprovePayment() {
        String paymentId = "payment_req_202502031547";
        when(commandGateway.send(any(ApprovePaymentCommand.class)))
                .thenReturn(CompletableFuture.completedFuture(paymentId));

        CompletableFuture<Object> result = service.approvePayment(paymentId);

        ArgumentCaptor<ApprovePaymentCommand> captor = ArgumentCaptor.forClass(ApprovePaymentCommand.class);
        verify(commandGateway, times(1)).send(captor.capture());
        ApprovePaymentCommand sentCommand = captor.getValue();
        assertThat(sentCommand.getPaymentId()).isEqualTo(paymentId);
        assertThat(result).isCompletedWithValue(paymentId);
    }
}