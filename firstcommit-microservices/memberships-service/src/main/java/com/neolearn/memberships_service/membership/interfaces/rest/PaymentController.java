package com.neolearn.memberships_service.membership.interfaces.rest;


import com.neolearn.memberships_service.membership.domain.model.commands.CreatePaymentCommand;
import com.neolearn.memberships_service.membership.domain.model.queries.GetPaymentByIdQuery;
import com.neolearn.memberships_service.membership.domain.model.queries.GetPaymentsBySubscriptionIdQuery;
import com.neolearn.memberships_service.membership.domain.services.PaymentCommandService;
import com.neolearn.memberships_service.membership.domain.services.PaymentQueryService;
import com.neolearn.memberships_service.membership.interfaces.rest.resources.CreatePaymentResource;
import com.neolearn.memberships_service.membership.interfaces.rest.resources.PaymentResource;
import com.neolearn.memberships_service.membership.interfaces.rest.transform.PaymentResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(value= "/api/v1/payments", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name="Payments", description = "Payment Management Endpoints")
public class PaymentController {

    private final PaymentQueryService paymentQueryService;
    private final PaymentCommandService paymentCommandService;

    public PaymentController(PaymentQueryService paymentQueryService, PaymentCommandService paymentCommandService) {
        this.paymentQueryService = paymentQueryService;
        this.paymentCommandService = paymentCommandService;
    }

    @PostMapping
    public ResponseEntity<PaymentResource> createPayment(@RequestBody CreatePaymentResource resource){
        var createPaymentCommand = new CreatePaymentCommand(
            resource.getSubscriptionId(),
            resource.getAmount(),
            resource.getCurrency(),
            LocalDateTime.now(),
            resource.getPaymentStatus(),
            resource.getPaymentMethod()
        );
        var payment = paymentCommandService.handle(createPaymentCommand);
        if(payment.isEmpty()) return ResponseEntity.badRequest().build();
        var paymentResource = PaymentResourceFromEntityAssembler.toResourceFromEntity(payment.get());
        return new ResponseEntity<>(paymentResource, HttpStatus.CREATED);
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentResource> getPaymentById(@PathVariable Long paymentId){
        var getPaymentByIdQuery = new GetPaymentByIdQuery(paymentId);
        var payment = paymentQueryService.handle(getPaymentByIdQuery);
        if(payment.isEmpty()) return ResponseEntity.notFound().build();
        var paymentResource = PaymentResourceFromEntityAssembler.toResourceFromEntity(payment.get());
        return ResponseEntity.ok(paymentResource);
    }

    @GetMapping("/subscription/{subscriptionId}")
    public ResponseEntity<List<PaymentResource>> getPaymentsBySubscriptionId(@PathVariable Long subscriptionId) {
        var getPaymentsBySubscriptionIdQuery = new GetPaymentsBySubscriptionIdQuery(subscriptionId);
        var payments = paymentQueryService.handle(getPaymentsBySubscriptionIdQuery);
        if(payments.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        var paymentResources = payments.stream().map(PaymentResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(paymentResources);
    }
} 