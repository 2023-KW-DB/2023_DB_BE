package com.dbdb.dbdb.domain.bike.service;

import com.dbdb.dbdb.domain.bike.dto.BikeDto;
import com.dbdb.dbdb.domain.bike.repository.BikeRepository;
import com.dbdb.dbdb.domain.bike.table.Bike;
import com.dbdb.dbdb.global.exception.GlobalException;
import com.dbdb.dbdb.global.exception.ResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class BikeService {

    private final BikeRepository bikeRepository;

    public void createBike(BikeDto.BikeCreateDto bikeCreateDto) {
        try {
            Bike bike = new Bike(
                0,  //auto increment id
                bikeCreateDto.getLendplace_id(),
                bikeCreateDto.getUse_status(),
                bikeCreateDto.getBike_status()
            );

            bikeRepository.insertBike(bike);

        } catch (DataAccessException e) {
            Throwable rootCause = e.getRootCause();
            if (rootCause instanceof SQLException) {
                SQLException sqlEx = (SQLException) rootCause;
                if (sqlEx.getErrorCode() == 1452) {
                    throw new GlobalException(ResponseStatus.INVALID_FK_VIOLATION);
                }
            }
            throw new GlobalException(ResponseStatus.DATABASE_ERROR);
        } catch (GlobalException e) {
            throw e;
        } catch (NullPointerException e) {
            throw new GlobalException(ResponseStatus.INVALID_REQUEST);
        } catch (Exception e) {
            throw new GlobalException(ResponseStatus.DATABASE_ERROR);
        }
    }
}
