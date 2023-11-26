package com.dbdb.dbdb.domain.coupon.dto;

import lombok.Data;

@Data
public class CouponDto {
    private String value;
    private int is_used;
    private int ticket_id;
}
