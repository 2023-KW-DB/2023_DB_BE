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
import java.util.Iterator;
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
        List<UserlogDto> userlogs = userlogRepository.getUserlog(userId);
        if(userlogs == null)
            return null;
        Iterator<UserlogDto> iterator = userlogs.iterator();
        while(iterator.hasNext()){
            UserlogDto userlogDto = iterator.next();
            if(userlogDto.getDeparture_station() != null){
                userlogDto.setDepature_station_statn_addr1(userlogRepository.getDepature_station_statn_addr1(userlogDto.getDeparture_station()));
                userlogDto.setDepature_station_statn_addr2(userlogRepository.getDepature_station_statn_addr2(userlogDto.getDeparture_station()));
            }
            if(userlogDto.getArrival_station() != null){
                userlogDto.setArrival_station_statn_addr1(userlogRepository.getArrival_station_statn_addr1(userlogDto.getArrival_station()));
                userlogDto.setArrival_station_statn_addr2(userlogRepository.getArrival_station_statn_addr2(userlogDto.getArrival_station()));
            }
        }

        return userlogs;
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

    public List<VisualizationUserlogDto.userUseDistanceInfo> getTopUseDistance() {
        try {
            return userlogRepository.getTopUseDistance();
        } catch (Exception e) {
            throw new GlobalException(ResponseStatus.DATABASE_ERROR);
        }
    }

    public List<VisualizationUserlogDto.userLogDto> getBetweenUserlog(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        try {
            return userlogRepository.getBetweenUserlog(startDateTime, endDateTime);
        } catch (Exception e) {
            throw new GlobalException(ResponseStatus.DATABASE_ERROR);
        }
    }

    public List<UserlogDto> getAllUserlog() {
        return userlogRepository.getAllUserlog();
    }
}
