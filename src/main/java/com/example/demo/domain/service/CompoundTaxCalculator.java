package com.example.demo.domain.service;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Component
public class CompoundTaxCalculator implements TaxPolicy {

    private static final BigDecimal FIXED_PERCENTAGE_TAX = new BigDecimal("0.02");

    private static final BigDecimal VARIABLE_PERCENTAGE_TAX = new BigDecimal("0.01");

    @Override
    public BigDecimal calculateTax(BigDecimal value, LocalDate dueDate) {
        int monthsDifference = (int) calculateDifferenceFromNow(dueDate);

        BigDecimal valueWithFixedValue = value.multiply(FIXED_PERCENTAGE_TAX);

        BigDecimal multiplierTaxWithTime = (VARIABLE_PERCENTAGE_TAX.add(BigDecimal.ONE, MathContext.DECIMAL32)).pow(monthsDifference);
        BigDecimal compoundInterest = value.multiply(multiplierTaxWithTime).subtract(value);

        return value.add(compoundInterest).add(valueWithFixedValue).setScale(2, RoundingMode.HALF_UP);
    }

    private static long calculateDifferenceFromNow (LocalDate dueDate) {
        var dateStamp = LocalDate.now();
        return ChronoUnit.MONTHS.between(dueDate, dateStamp);

    }

}
