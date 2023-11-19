package com.dbdb.dbdb.domain.userlog.service;

import com.dbdb.dbdb.domain.userlog.dto.UserlogDto;
import com.dbdb.dbdb.domain.userlog.repository.UserlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserlogService {

    @Autowired
    private UserlogRepository userlogRepository;

    public String bikeRental(UserlogDto userlogDto) {
        return userlogRepository.bikeRental(userlogDto.getUser_id(), userlogDto.getDeparture_station());
    }
}
