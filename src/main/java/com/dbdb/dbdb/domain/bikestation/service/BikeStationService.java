package com.dbdb.dbdb.domain.bikestation.service;

import com.dbdb.dbdb.domain.bikestation.dto.BikeStationDto;
import com.dbdb.dbdb.domain.bikestation.repository.BikeStationRepository;
import com.dbdb.dbdb.domain.user.dto.UserDto;
import com.dbdb.dbdb.domain.user.repository.UserRepository;
import com.dbdb.dbdb.global.exception.GlobalException;
import com.dbdb.dbdb.global.exception.ResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@RequiredArgsConstructor
@Service
public class BikeStationService {

    private final BikeStationRepository bikeStationRepository;

    @PostConstruct
    public void initialize() {
        bikeStationRepository.createBikeCountsView();
    }

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

    public void modifyBikeStation(BikeStationDto.BikeStationDetailDto bikeStation) {
        try {
            bikeStationRepository.update(bikeStation);
        } catch (NullPointerException e) {
            throw new GlobalException(ResponseStatus.INVALID_REQUEST);
        } catch (Exception e) {
            throw new GlobalException(ResponseStatus.DATABASE_ERROR);
        }
    }

    public void deleteBikeStation(BikeStationDto.BikeStationDeleteDto bikeStationDeleteDto) {
        try {
            bikeStationRepository.delete(bikeStationDeleteDto.getLendplace_id());
        } catch (Exception e) {
            throw new GlobalException(ResponseStatus.DATABASE_ERROR);
        }
    }

    public BikeStationDto.BikeStationWithBikeDto getBikeStation(String lendplaceId) {
        try {
            return bikeStationRepository.findDetailByLendplaceId(lendplaceId);
        } catch (EmptyResultDataAccessException e) {
            throw new GlobalException(ResponseStatus.RESULT_NOT_EXIST);
        } catch (Exception e) {
            throw new GlobalException(ResponseStatus.DATABASE_ERROR);
        }
    }

    public List<BikeStationDto.BikeStationDetailDto> searchStation(String name) {
        try {
            return bikeStationRepository.findDetailByName(name);
        } catch (Exception e) {
            throw new GlobalException(ResponseStatus.DATABASE_ERROR);
        }
    }

    public BikeStationDto.BikeStationStatus getBikeStationStatus(String lendplaceId, int userId) {
        try {
            BikeStationDto.BikeStationStatus bikeStationStatusWithUsername = bikeStationRepository.findStatusById(lendplaceId, userId);
            return bikeStationStatusWithUsername;
        } catch (EmptyResultDataAccessException e) {
            throw new GlobalException(ResponseStatus.RESULT_NOT_EXIST);
        } catch (Exception e) {
            throw new GlobalException(ResponseStatus.DATABASE_ERROR);
        }
    }

    public List<BikeStationDto.BikeStationWithCurrentBike> getAllStation(int user_id) {
        try {
            return bikeStationRepository.findAll(user_id);
        } catch (Exception e) {
            throw new GlobalException(ResponseStatus.DATABASE_ERROR);
        }
    }

    public List<BikeStationDto.BikeStationSimpleWithState> getRecentStation(int userId) {
        try {
            return bikeStationRepository.findRecentByUserId(userId);
        } catch (Exception e) {
            throw new GlobalException(ResponseStatus.DATABASE_ERROR);
        }
    }

    public List<BikeStationDto.BikeStationSimpleState> getPopularStation() {
        try {
            return bikeStationRepository.findPopular();
        } catch (Exception e) {
            throw new GlobalException(ResponseStatus.DATABASE_ERROR);
        }
    }

    public List<BikeStationDto.RentalBikeStation> getRentalStatusByUserId(int userId) {
        try {
            return bikeStationRepository.findRentalStatusByUserId(userId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (Exception e) {
            throw e; // new GlobalException(ResponseStatus.DATABASE_ERROR);
        }
    }
}
