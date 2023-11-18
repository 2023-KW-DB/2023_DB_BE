package com.dbdb.dbdb.domain.coupon.service;

import com.dbdb.dbdb.domain.coupon.reposiroty.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
