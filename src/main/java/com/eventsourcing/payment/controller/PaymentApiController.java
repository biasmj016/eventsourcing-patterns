package com.eventsourcing.payment.controller;

import com.eventsourcing.payment.config.response.ApiResponse;
import com.eventsourcing.payment.controller.request.PaymentRequest;
import com.eventsourcing.payment.controller.request.PaymentVerifyRequest;
import com.eventsourcing.payment.service.command.PaymentCommandService;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

import static com.eventsourcing.payment.config.response.ApiResponse.success;

@RestController
@RequestMapping("/payments")
public class PaymentApiController {
    private final PaymentCommandService commandService;

    public PaymentApiController(PaymentCommandService commandService) {
        this.commandService = commandService;
    }

    @PostMapping("/request")
    public CompletableFuture<ApiResponse<Object>> requestPayment(@RequestBody PaymentRequest request) {
        return commandService.requestPayment(request.paymentId(), request.memberId(), request.itemId(), request.count())
                .thenApply(result -> success());
    }

    @PostMapping("/verify")
    public CompletableFuture<ApiResponse<Object>> verify(@RequestBody PaymentVerifyRequest request) {
        return commandService.verifyPayment(request.paymentId(), request.memberId(), request.itemId(), request.amount())
                .thenApply(result -> success());
    }

    @PostMapping("/approve/{paymentId}")
    public CompletableFuture<ApiResponse<Object>> approvePayment(@PathVariable String paymentId) {
        return commandService.approvePayment(paymentId)
                .thenApply(result -> success());
    }
}