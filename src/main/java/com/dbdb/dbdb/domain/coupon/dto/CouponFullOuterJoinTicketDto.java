package com.dbdb.dbdb.domain.coupon.dto;

import lombok.Data;

@Data
public class CouponFullOuterJoinTicketDto {
    private String value;
    private Integer is_used;
    private Integer ticket_id;
    private int id;
    private int ticket_price;
}
