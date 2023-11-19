package com.dbdb.dbdb.domain.userlog.service;

import com.dbdb.dbdb.domain.userlog.dto.UserlogDto;
import com.dbdb.dbdb.domain.userlog.repository.UserlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class UserlogService {

    @Autowired
    private UserlogRepository userlogRepository;

    public String bikeRental(UserlogDto userlogDto) {
        return userlogRepository.bikeRental(userlogDto.getUser_id(), userlogDto.getDeparture_station());
    }

    public String bikeReturn(UserlogDto userlogDto) {
        userlogDto.setArrival_time(LocalDateTime.now());

        return userlogRepository.bikeReturn(userlogDto.getUser_id(), userlogDto.getArrival_station(), userlogDto.getArrival_time(), userlogDto.getUse_distance());
    }
}
