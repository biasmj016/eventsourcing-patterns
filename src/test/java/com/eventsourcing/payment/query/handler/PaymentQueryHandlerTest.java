package com.eventsourcing.payment.query.handler;

import com.eventsourcing.payment.domain.event.PaymentApprovedEvent;
import com.eventsourcing.payment.domain.event.PaymentRequestedEvent;
import com.eventsourcing.payment.domain.event.PaymentVerifiedEvent;
import com.eventsourcing.payment.query.model.PaymentHistory;
import com.eventsourcing.payment.repository.PaymentHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static com.eventsourcing.payment.query.model.PaymentHistory.PaymentStatus.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class PaymentQueryHandlerTest {

    private PaymentHistoryRepository repository;
    private PaymentQueryHandler handler;

    @BeforeEach
    void setUp() {
        repository = mock(PaymentHistoryRepository.class);
        handler = new PaymentQueryHandler(repository);
    }

    @Test
    void testRequestPayment() {
        PaymentRequestedEvent event = new PaymentRequestedEvent("payment_req_202502031547", "01000000000", "Ticket_BUS", 10000);
        handler.on(event);

        ArgumentCaptor<PaymentHistory> captor = ArgumentCaptor.forClass(PaymentHistory.class);
        verify(repository, times(1)).save(captor.capture());

        PaymentHistory savedHistory = captor.getValue();
        assertThat(savedHistory.getPaymentId()).isEqualTo("payment_req_202502031547");
        assertThat(savedHistory.getStatus()).isEqualTo(REQUESTED);
    }

    @Test
    void testVerifiedPayment() {
        PaymentHistory history = new PaymentHistory("payment_req_202502031547", "01000000000", "Ticket_BUS", 10000, REQUESTED);
        when(repository.findById("payment_req_202502031547")).thenReturn(Optional.of(history));

        PaymentVerifiedEvent event = new PaymentVerifiedEvent("payment_req_202502031547");
        handler.on(event);

        ArgumentCaptor<PaymentHistory> captor = ArgumentCaptor.forClass(PaymentHistory.class);
        verify(repository, times(1)).save(captor.capture());

        PaymentHistory updatedHistory = captor.getValue();
        assertThat(updatedHistory.getStatus()).isEqualTo(VERIFIED);
    }

    @Test
    void testApprovedPayment() {
        PaymentHistory history = new PaymentHistory("payment_req_202502031547", "01000000000", "Ticket_BUS", 10000, VERIFIED);
        when(repository.findById("payment_req_202502031547")).thenReturn(Optional.of(history));

        PaymentApprovedEvent event = new PaymentApprovedEvent("payment_req_202502031547");
        handler.on(event);

        ArgumentCaptor<PaymentHistory> captor = ArgumentCaptor.forClass(PaymentHistory.class);
        verify(repository, times(1)).save(captor.capture());

        PaymentHistory updatedHistory = captor.getValue();
        assertThat(updatedHistory.getStatus()).isEqualTo(APPROVED);
    }
}