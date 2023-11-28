package com.dbdb.dbdb.domain.coupon.dto;

import lombok.Data;

@Data
public class CouponFullOuterJoinTicketDto {
    private String value;
    private int is_used;
    private int ticket_id;
    private int id;
    private int ticket_price;
}
