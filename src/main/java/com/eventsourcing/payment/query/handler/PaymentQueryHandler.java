package com.eventsourcing.payment.query.handler;

import com.eventsourcing.payment.domain.event.PaymentApprovedEvent;
import com.eventsourcing.payment.domain.event.PaymentRequestedEvent;
import com.eventsourcing.payment.domain.event.PaymentVerifiedEvent;
import com.eventsourcing.payment.query.model.PaymentHistory;
import com.eventsourcing.payment.repository.PaymentHistoryRepository;
import org.axonframework.eventhandling.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.eventsourcing.payment.query.model.PaymentHistory.PaymentStatus.*;

@Component
@Transactional
public class PaymentQueryHandler {
    private static final Logger logger = LoggerFactory.getLogger(PaymentQueryHandler.class);
    private final PaymentHistoryRepository repository;

    public PaymentQueryHandler(PaymentHistoryRepository repository) {
        this.repository = repository;
    }

    @EventHandler
    public void on(PaymentRequestedEvent event) {
        logger.info("[EventHandler] Handling PaymentRequestedEvent: {}", event);
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
        logger.info("[EventHandler] Handling PaymentVerifiedEvent: {}", event);
        repository.findById(event.getPaymentId())
                .map(history -> history.toUpdatedStatus(VERIFIED))
                .ifPresent(repository::save);
    }

    @EventHandler
    public void on(PaymentApprovedEvent event) {
        logger.info("[EventHandler] Handling PaymentApprovedEvent: {}", event);
        repository.findById(event.getPaymentId())
                .map(history -> history.toUpdatedStatus(APPROVED))
                .ifPresent(repository::save);
    }
}
