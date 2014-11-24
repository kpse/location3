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

INSERT INTO records (device_id, cmd, time, valid, latitude, latitude_direction, longitude, longitude_direction, speed, direction, date, tracker_status)
values ('3333355555','V1','131122','A','120.123123','D','120.123123','G','3.121','N','DDMMYY','0011');
