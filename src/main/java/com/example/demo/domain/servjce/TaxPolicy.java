package com.example.demo.domain.servjce;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface TaxPolicy {

    BigDecimal calculateTax (BigDecimal value, LocalDate dueDate);

}
