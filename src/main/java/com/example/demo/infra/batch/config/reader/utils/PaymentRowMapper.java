package com.example.demo.infra.batch.config.reader.utils;

import com.example.demo.domain.enums.PaymentStatus;
import com.example.demo.domain.model.Payment;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class PaymentRowMapper implements RowMapper<Payment> {

    private static final String ID_COLUMN = "id";

    private static final String CLIENT_ID_COLUMN = "client_id";

    private static final String CLIENT_NAME_COLUMN = "client_name";

    private static final String VALUE_COLUMN = "value";

    private static final String TOTAL_VALUE_COLUMN = "total_value";

    private static final String DUE_DATE_COLUMN = "due_date";

    private static final String STATUS_COLUMN = "status";

    @Override
    public Payment mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Payment.builder()
                .id(rs.getLong(ID_COLUMN))
                .clientId(rs.getLong(CLIENT_ID_COLUMN))
                .clientName(rs.getString(CLIENT_NAME_COLUMN))
                .value(rs.getBigDecimal(VALUE_COLUMN))
                .valueWithTax(rs.getBigDecimal(TOTAL_VALUE_COLUMN))
                .dueDate(LocalDate.parse(rs.getString(DUE_DATE_COLUMN)))
                .status(PaymentStatus.valueOf(rs.getString(STATUS_COLUMN)))
                .build();
    }

}
