package com.dbdb.dbdb.domain.coupon.service;

import com.dbdb.dbdb.domain.coupon.dto.TicketDto;
import com.dbdb.dbdb.domain.coupon.reposiroty.TicketRepository;
import com.dbdb.dbdb.domain.user.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    public void createTicket(int ticketPrice) {
        ticketRepository.createTicket(ticketPrice);
    }

    public void modifyTicket(int id, int newTicketPrice) {
        ticketRepository.modifyTicket(id, newTicketPrice);
    }

    public void deleteTicket(int id) {
        ticketRepository.deleteTicket(id);
    }

    public List<TicketDto> getAllTicket() {
        return ticketRepository.returnAllTickets();
    }

    public Boolean purchaseTicket(int userId, int ticketId){
        return ticketRepository.purchaseTicket(userId, ticketId);
    }
}
