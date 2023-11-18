package com.dbdb.dbdb.domain.paymenthistory.service;

import com.dbdb.dbdb.domain.paymenthistory.dto.PaymentHistoryDto;
import com.dbdb.dbdb.domain.paymenthistory.repository.PaymentHistoryRepository;
import com.dbdb.dbdb.domain.ticket.dto.TicketDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentHistoryService {

    @Autowired
    private PaymentHistoryRepository paymentHistoryRepository;

    public List<PaymentHistoryDto> getAllPaymentHistory(int userId) {
        return paymentHistoryRepository.returnAllPaymentHistorys(userId);
    }
}
