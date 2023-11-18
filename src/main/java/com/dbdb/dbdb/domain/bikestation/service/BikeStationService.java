package com.dbdb.dbdb.domain.bikestation.service;

import com.dbdb.dbdb.domain.bikestation.dto.BikeStationDto;
import com.dbdb.dbdb.domain.bikestation.repository.BikeStationRepository;
import com.dbdb.dbdb.global.exception.GlobalException;
import com.dbdb.dbdb.global.exception.ResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;

@RequiredArgsConstructor
@Service
public class BikeStationService {

    private final BikeStationRepository bikeStationRepository;

    public void createBikeStation(BikeStationDto.BikeStationDetailDto bikeStation) {
        try {
            bikeStationRepository.insert(bikeStation);
        } catch (DataAccessException e) {
            Throwable rootCause = e.getRootCause();
            if (rootCause instanceof SQLIntegrityConstraintViolationException) {
                throw new GlobalException(ResponseStatus.DUPLICATE_STATION);
            }
            throw new GlobalException(ResponseStatus.DATABASE_ERROR);
        } catch (NullPointerException e) {
            throw new GlobalException(ResponseStatus.INVALID_REQUEST);
        } catch (Exception e) {
            throw new GlobalException(ResponseStatus.DATABASE_ERROR);
        }

    }
}
