package com.dbdb.dbdb.domain.paymenthistory.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PaymentHistoryDto {
    private int history_id;
    private int user_id;
    private int ticket_id;
    private int is_used;
    private LocalDateTime registration_at;
}
