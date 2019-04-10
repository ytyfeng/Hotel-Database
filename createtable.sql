CREATE TABLE Hotel (
hotelNo   VARCHAR(4) NOT NULL,
hotelName VARCHAR(32) NOT NULL,
city      VARCHAR(32) NOT NULL,
PRIMARY KEY (hotelNo));


CREATE TABLE Guest (
guestNo       VARCHAR(5) NOT NULL,
guestName 	   VARCHAR(32) NOT NULL,
guestAddress  VARCHAR(64) NOT NULL,
PRIMARY KEY (guestNo));

CREATE TABLE Room (
roomNo    VARCHAR(4) NOT NULL, 
hotelNo   VARCHAR(4) NOT NULL, 
type      VARCHAR(16) NOT NULL,
price     INTEGER NOT NULL,       
PRIMARY KEY (roomNo, hotelNo),
FOREIGN KEY (hotelNo) REFERENCES Hotel,
CONSTRAINT priceMinimum CHECK (price > 0));


CREATE TABLE Booking (
hotelNo    VARCHAR(4) NOT NULL, 
guestNo    VARCHAR(5) NOT NULL, 
dateFrom   DATE NOT NULL,
dateTo     DATE NOT NULL, 
roomNo	VARCHAR(4),      
PRIMARY KEY (hotelNo, guestNo, dateFrom),
/* referential integrity constraints */
FOREIGN KEY (hotelNo) REFERENCES Hotel,
FOREIGN KEY (guestNo) REFERENCES Guest,
CONSTRAINT dateToCheck CHECK (dateTo > dateFrom));

