package com.dbdb.dbdb.domain.ticket.service;

import com.dbdb.dbdb.domain.ticket.dto.TicketDto;
import com.dbdb.dbdb.domain.ticket.reposiroty.TicketRepository;
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
