package com.example.proekt.service;

import com.example.proekt.dto.ChargeRequest;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;

public interface PaymentService {
    Charge pay(ChargeRequest chargeRequest) throws StripeException;
}
