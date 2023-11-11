LOAD DATA INFILE '.\\src\\main\\resources\\lendplacse.csv'
INTO TABLE bikestationinformation
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
LINES TERMINATED BY '\r\n'
IGNORE 1 LINES;