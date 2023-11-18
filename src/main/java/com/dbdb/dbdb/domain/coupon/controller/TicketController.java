package com.dbdb.dbdb.domain.coupon.controller;

import com.dbdb.dbdb.domain.coupon.dto.TicketDto;
import com.dbdb.dbdb.domain.coupon.service.TicketService;
import com.dbdb.dbdb.domain.user.dto.UserDto;
import com.dbdb.dbdb.global.dto.JsonResponse;
import com.dbdb.dbdb.global.exception.ResponseStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TicketController {

    @Autowired
    private TicketService ticketService;

    // 이용권 종류 추가
    @PostMapping("/admin/create-ticket")
    public ResponseEntity<?> createTicket(@RequestBody TicketDto ticketDto) {

        ticketService.createTicket(ticketDto.getTicket_price());

        return ResponseEntity.ok(new JsonResponse<>(ResponseStatus.SUCCESS_CREATE_TICKET, null));
    }

    // 이용권 종류 정보 수정
    @PatchMapping("/admin/modify-ticket")
    public ResponseEntity<?> modifyTicket(@RequestBody TicketDto ticketDto) {

        ticketService.modifyTicket(ticketDto.getId(), ticketDto.getTicket_price());

        return ResponseEntity.ok(new JsonResponse<>(ResponseStatus.SUCCESS_MODIFY_TICKET, null));
    }

    // 이용권 종류 정보 삭제
    @DeleteMapping("/admin/delete-ticket")
    public ResponseEntity<?> deleteTicket(@RequestBody TicketDto ticketDto) {

        ticketService.deleteTicket(ticketDto.getId());

        return ResponseEntity.ok(new JsonResponse<>(ResponseStatus.SUCCESS_DELETE_TICKET, null));
    }

    // 모든 이용권 종류 조회(유저&관리자 공통)
    @GetMapping("/get-all-ticket")
    public ResponseEntity<?> getAllTicket() {

        List<TicketDto> tickets = ticketService.getAllTicket();

        if(tickets.isEmpty())
            return ResponseEntity.ok(new JsonResponse<>(ResponseStatus.SUCCESS_GET_ALL_TICKETS_INFO_ISEMPTY, null));

        return ResponseEntity.ok(new JsonResponse<>(ResponseStatus.SUCCESS_GET_ALL_TICKETS_INFO, tickets));
    }

    @PostMapping("/users/purchase-ticket")
    public ResponseEntity<?> purchaseTicket(@RequestParam int userId, @RequestParam int ticketId){
        boolean isTotalMoneyEnough = ticketService.purchaseTicket(userId, ticketId);

        if(!isTotalMoneyEnough)
            return ResponseEntity.ok(new JsonResponse<>(ResponseStatus.FAILED_NOT_ENOUGHT_TOTAL_MONEY, null));
        else
            return ResponseEntity.ok(new JsonResponse<>(ResponseStatus.SUCCESS_PURCHASE_TICKET, null));
    }
}
