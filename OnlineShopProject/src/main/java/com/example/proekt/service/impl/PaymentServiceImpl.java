package com.example.proekt.service.impl;

import com.example.proekt.dto.ChargeRequest;
import com.example.proekt.service.PaymentService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Value("${STRIPE_S_KEY}")
    public String secretKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = this.secretKey;
    }

    @Override
    public Charge pay(ChargeRequest chargeRequest) throws StripeException {
        Map<String, Object> chargeMap = new HashMap<>();
        chargeMap.put("amount", chargeRequest.getAmount());
        chargeMap.put("currency", chargeRequest.getCurrency());
        chargeMap.put("source", chargeRequest.getStripeToken());

        return Charge.create(chargeMap);
    }
}
