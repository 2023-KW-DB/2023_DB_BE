package com.dbdb.dbdb.domain.userlog.service;

import com.dbdb.dbdb.domain.userlog.dto.UserlogDto;
import com.dbdb.dbdb.domain.userlog.dto.VisualizationUserlogDto;
import com.dbdb.dbdb.domain.userlog.repository.UserlogRepository;
import com.dbdb.dbdb.global.exception.GlobalException;
import com.dbdb.dbdb.global.exception.ResponseStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

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

    public List<UserlogDto> getUserlog(int userId) {
        return userlogRepository.getUserlog(userId);
    }

    public List<VisualizationUserlogDto.userUseTimeInfo> getTopUseTime() {
        try {
            return userlogRepository.getTopUseTime();
        } catch (Exception e) {
            throw new GlobalException(ResponseStatus.DATABASE_ERROR);
        }
    }

    public List<VisualizationUserlogDto.userUseCountInfo> getTopUseCount() {
        try {
            return userlogRepository.getTopUseCount();
        } catch (Exception e) {
            throw new GlobalException(ResponseStatus.DATABASE_ERROR);
        }
    }
}
