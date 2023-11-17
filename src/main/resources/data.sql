LOAD DATA LOCAL INFILE '.\\src\\main\\resources\\lendplace.csv'
INTO TABLE bikestationinformation
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
LINES TERMINATED BY '\r\n'
IGNORE 1 LINES
(lendplace_id, statn_addr1, statn_addr2, startn_lat, startn_lnt)
SET max_stands = 20,
    station_status = NULL;
