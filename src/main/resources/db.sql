CREATE TABLE records (
  uid       INT(11)      NOT NULL AUTO_INCREMENT,
  device_id CHAR(10)  NOT NULL,
  cmd    VARCHAR(10)  NOT NULL,
  `time`     CHAR(6) NOT NULL,
  valid CHAR(1)  NOT NULL,
  latitude VARCHAR(10)  NOT NULL,
  latitude_direction CHAR(1) NOT NULL,
  longitude VARCHAR(10)  NOT NULL,
  longitude_direction CHAR(1) NOT NULL,
  speed VARCHAR(6) NOT NULL,
  direction VARCHAR(3),
  `date` CHAR(6),
  tracker_status CHAR(4),
  PRIMARY KEY (uid)
);

CREATE TABLE nbr_records (
  uid       INT(11)      NOT NULL AUTO_INCREMENT,
  device_id CHAR(10)  NOT NULL,
  `time`     CHAR(6) NOT NULL,
  mcc VARCHAR(3)  NOT NULL,
  mnc VARCHAR(3)  NOT NULL,
  ta VARCHAR(10) NOT NULL,
  num CHAR(1)  NOT NULL,
  lac VARCHAR(5) NOT NULL,
  cid VARCHAR(5) NOT NULL,
  rxlev VARCHAR(5) NOT NULL,
  `date` CHAR(6),
  tracker_status CHAR(10),
  PRIMARY KEY (uid)
);