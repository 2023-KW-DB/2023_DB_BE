LOAD DATA LOCAL INFILE '.\\src\\main\\resources\\lendplace.csv'
INTO TABLE bikestationinformation
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
LINES TERMINATED BY '\r\n'
IGNORE 1 LINES;