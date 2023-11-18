package com.dbdb.dbdb.domain.coupon.controller;

import com.dbdb.dbdb.domain.coupon.dto.TicketDto;
import com.dbdb.dbdb.domain.coupon.service.TicketService;
import com.dbdb.dbdb.domain.user.dto.UserDto;
import com.dbdb.dbdb.global.dto.JsonResponse;
import com.dbdb.dbdb.global.exception.ResponseStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    // �̿�� ���� �߰�
    @PostMapping("/create-ticket")
    public ResponseEntity<?> createTicket(@RequestBody TicketDto ticketDto) {

        ticketService.createTicket(ticketDto.getTicket_price());

        return ResponseEntity.ok(new JsonResponse<>(ResponseStatus.SUCCESS_CREATE_TICKET, null));
    }

    // �̿�� ���� �߰�
    @PatchMapping("/modify-ticket")
    public ResponseEntity<?> modifyTicket(@RequestBody TicketDto ticketDto) {

        ticketService.modifyTicket(ticketDto.getId(), ticketDto.getTicket_price());

        return ResponseEntity.ok(new JsonResponse<>(ResponseStatus.SUCCESS_MODIFY_TICKET, null));
    }
//
//    // �̿�� ���� �߰�
//    @DeleteMapping("/delete-ticket")
//    public ResponseEntity<?> deleteTicket(@RequestBody TicketDto ticketDto) {
//
//
//    }
}
