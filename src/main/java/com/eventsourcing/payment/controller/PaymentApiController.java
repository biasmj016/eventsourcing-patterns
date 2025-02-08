package com.eventsourcing.payment.controller;

import com.eventsourcing.payment.controller.request.PaymentRequest;
import com.eventsourcing.payment.controller.request.PaymentVerifyRequest;
import com.eventsourcing.payment.service.command.PaymentCommandService;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/payments")
public class PaymentApiController {

    private final PaymentCommandService commandService;

    public PaymentApiController(PaymentCommandService commandService) {
        this.commandService = commandService;
    }

    @PostMapping("/request")
    public CompletableFuture<Object> requestPayment(@RequestBody PaymentRequest request) {
        return commandService.requestPayment(request.paymentId(), request.memberId(), request.itemId(), request.count());
    }

    @PostMapping("/verify")
    public CompletableFuture<Object> verify(@RequestBody PaymentVerifyRequest request) {
        return commandService.verifyPayment(request.paymentId(), request.memberId(), request.itemId(), request.amount());
    }

    @PostMapping("/approve/{paymentId}")
    public CompletableFuture<Object> approvePayment(@PathVariable String paymentId) {
        return commandService.approvePayment(paymentId);
    }
}