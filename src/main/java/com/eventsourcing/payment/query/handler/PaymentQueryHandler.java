package com.eventsourcing.payment.query.handler;

import com.eventsourcing.payment.domain.event.PaymentApprovedEvent;
import com.eventsourcing.payment.domain.event.PaymentRequestedEvent;
import com.eventsourcing.payment.domain.event.PaymentVerifiedEvent;
import com.eventsourcing.payment.query.model.PaymentHistory;
import com.eventsourcing.payment.repository.PaymentHistoryRepository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

import static com.eventsourcing.payment.query.model.PaymentHistory.PaymentStatus.*;

@Component
public class PaymentQueryHandler {
    private final PaymentHistoryRepository repository;

    public PaymentQueryHandler(PaymentHistoryRepository repository) {
        this.repository = repository;
    }

    @EventHandler
    public void on(PaymentRequestedEvent event) {
        repository.save(new PaymentHistory(
                event.getPaymentId(),
                event.getMemberId(),
                event.getItemId(),
                event.getAmount(),
                REQUESTED
        ));
    }

    @EventHandler
    public void on(PaymentVerifiedEvent event) {
        repository.findById(event.getPaymentId())
                .map(history -> history.toUpdatedStatus(VERIFIED))
                .ifPresent(repository::save);
    }

    @EventHandler
    public void on(PaymentApprovedEvent event) {
        repository.findById(event.getPaymentId())
                .map(history -> history.toUpdatedStatus(APPROVED))
                .ifPresent(repository::save);
    }
}
