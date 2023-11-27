LOAD DATA LOCAL INFILE '.\\src\\main\\resources\\lendplace.csv'
INTO TABLE bikestationinformation
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
LINES TERMINATED BY '\r\n'
IGNORE 1 LINES
(lendplace_id, statn_addr1, statn_addr2, startn_lat, startn_lnt)
SET max_stands = 20,
    station_status = 1;


--LOAD DATA LOCAL INFILE '.\\src\\main\\resources\\selected_userlogs.csv'
--INTO TABLE userlog
--FIELDS TERMINATED BY ','
--ENCLOSED BY '"'
--LINES TERMINATED BY '\r\n'
--IGNORE 1 LINES
--(@date, @standard, @departure_time, @departure_station_id, @departure_station, @arrival_station_id, @arrival_station, @dummy, @use_time, @use_distance)
--SET departure_station = @departure_station_id,
--    arrival_station = @arrival_station_id,
--    departure_time = STR_TO_DATE(CONCAT(@date, ' ', LEFT(LPAD(@departure_time, 4, '0'), 2), ':', RIGHT(LPAD(@departure_time, 4, '0'), 2)), '%Y%m%d %H:%i'),
--    arrival_time = DATE_ADD(STR_TO_DATE(CONCAT(@date, ' ', LEFT(LPAD(@departure_time, 4, '0'), 2), ':', RIGHT(LPAD(@departure_time, 4, '0'), 2)), '%Y%m%d %H:%i'), INTERVAL @use_time MINUTE),
--    use_time = @use_time,
--    use_distance = @use_distance,
--    return_status = 1;