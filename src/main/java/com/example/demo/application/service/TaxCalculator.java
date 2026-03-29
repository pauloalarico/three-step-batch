package com.example.demo.application.service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public final class TaxCalculator {

    private static final BigDecimal FIXED_PERCENTAGE_TAX = new BigDecimal("0.02");

    private static final BigDecimal VARIABLE_PERCENTAGE_TAX = new BigDecimal("0.01");


    public static BigDecimal calculateTax (BigDecimal value, LocalDate dueDate) {

        int monthsDifference = (int) calculateDifferenceFromNow(dueDate);

        BigDecimal valueWithFixedValue = value.multiply(FIXED_PERCENTAGE_TAX);
        BigDecimal totalFixedValue = value.add(valueWithFixedValue);

        BigDecimal multiplierTaxWithTime = (VARIABLE_PERCENTAGE_TAX.add(BigDecimal.ONE, MathContext.DECIMAL32)).pow(monthsDifference);
        BigDecimal totalTaxWithTime = value.multiply(multiplierTaxWithTime);

        return totalFixedValue.add(totalTaxWithTime).setScale(2, RoundingMode.HALF_UP);
    }

    private static long calculateDifferenceFromNow (LocalDate dueDate) {
        var dateStamp = LocalDate.now();
        return ChronoUnit.MONTHS.between(dueDate, dateStamp);

    }

}
