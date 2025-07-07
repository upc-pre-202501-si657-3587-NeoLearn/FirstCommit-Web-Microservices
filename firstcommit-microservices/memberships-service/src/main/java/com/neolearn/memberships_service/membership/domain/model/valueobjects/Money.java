package com.neolearn.memberships_service.membership.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Currency;

@Embeddable
@Getter
public class Money {
    private BigDecimal amount;
    private String currencyCode;

    public Money() {
        this.amount = BigDecimal.ZERO;
        this.currencyCode = "USD";
    }

    public Money(BigDecimal amount, String currencyCode) {
        this.amount = amount;
        this.currencyCode = currencyCode;
    }

    public static Money dollars(BigDecimal amount) {
        return new Money(amount, "USD");
    }

    public static Money dollars(double amount) {
        return new Money(BigDecimal.valueOf(amount), "USD");
    }

    public Money add(Money money) {
        if (!this.currencyCode.equals(money.currencyCode)) {
            throw new IllegalArgumentException("Cannot add money with different currencies");
        }
        return new Money(this.amount.add(money.amount), this.currencyCode);
    }

    public Money subtract(Money money) {
        if (!this.currencyCode.equals(money.currencyCode)) {
            throw new IllegalArgumentException("Cannot subtract money with different currencies");
        }
        return new Money(this.amount.subtract(money.amount), this.currencyCode);
    }

    public Currency getCurrency() {
        return Currency.getInstance(currencyCode);
    }
} 