package com.dbdb.dbdb.domain.paymenthistory.controller;

import com.dbdb.dbdb.domain.paymenthistory.dto.PaymentHistoryDto;
import com.dbdb.dbdb.domain.paymenthistory.service.PaymentHistoryService;
import com.dbdb.dbdb.global.dto.JsonResponse;
import com.dbdb.dbdb.global.exception.ResponseStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PaymentHistoryController {

    @Autowired
    private PaymentHistoryService paymentHistoryService;

    // 이용권 구매 이력 조회
    @GetMapping("/users/get-ticket/payment-history")
    public ResponseEntity<?> purchaseTicket(@RequestParam int userId){

        List<PaymentHistoryDto> paymentHistorys = paymentHistoryService.getAllPaymentHistory(userId);

        if(paymentHistorys.isEmpty())
            return ResponseEntity.ok(new JsonResponse<>(ResponseStatus.SUCCESS_GET_ALL_PAYMENT_HISTORY_INFO_ISEMPTY, null));

        return ResponseEntity.ok(new JsonResponse<>(ResponseStatus.SUCCESS_GET_ALL_PAYMENT_HISTORY_INFO, paymentHistorys));
    }
}
