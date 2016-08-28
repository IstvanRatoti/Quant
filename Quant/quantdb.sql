CREATE DATABASE IF NOT EXISTS QuantDB;

USE QuantDB;		        -- sql doesnt automatically switches to the new db

CREATE TABLE Activities(	-- first table with basic activity info
	id INT(6) PRIMARY KEY NOT NULL AUTO_INCREMENT,	-- id, use this to link it with time table
	actName VARCHAR(255),
	description TEXT,
	actType INT(1)			-- type of activity, for the program to interpret
);

CREATE TABLE Timetable(		-- second table with time and place data
	id INT(6) PRIMARY KEY NOT NULL AUTO_INCREMENT,
	actId INT(6),			-- use this to join with activites table
	actDate DATETIME,		-- date AND time, format: YYYY-MM-DD HH:MI:SS
	place varchar(255),
	duration TIME
);

INSERT						-- putting some data in for testing
INTO
	activities(
		actName,
		actType,
		description
	)
VALUES(
	"Doing Something",
	0,
	"Trying out database connectivity."
);

INSERT						-- again, putting in some data
INTO
	timetable(
		actId,
		actDate,
		place,
		duration
	)
VALUES(
	1,						-- actId, should have a "pair" in the activites table
	"2017-01-01 12:00",		-- datetime format, mentioned above
	"home"
);