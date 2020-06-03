DROP SCHEMA FlightPub;
CREATE SCHEMA FlightPub;
USE FlightPub;

CREATE TABLE `Country` (
                           `countryCode2` char(2) NOT NULL,
                           `countryCode3` char(3) NOT NULL,
                           `countryName` varchar(80) NOT NULL DEFAULT '',
                           `alternateName1` varchar(80) NOT NULL DEFAULT '',
                           `alternateName2` varchar(80) NOT NULL DEFAULT '',
                           `motherCountryCode3` char(3) NOT NULL DEFAULT '',
                           `motherCountryComment` varchar(80) NOT NULL DEFAULT '',
                           PRIMARY KEY (`countryCode3`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `Airlines` (
                            `AirlineCode` char(2) NOT NULL,
                            `AirlineName` varchar(30) NOT NULL,
                            `CountryCode3` char(3) NOT NULL,
                            PRIMARY KEY (`AirlineCode`),
                            KEY `AirlinesCountryCode3_FK` (`CountryCode3`),
                            CONSTRAINT `AirlinesCountryCode3_FK` FOREIGN KEY (`CountryCode3`) REFERENCES `Country` (`countryCode3`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `PlaneType` (
                             `PlaneCode` varchar(20) NOT NULL,
                             `Details` varchar(50) NOT NULL,
                             `NumFirstClass` int(11) NOT NULL,
                             `NumBusiness` int(11) NOT NULL,
                             `NumPremiumEconomy` int(11) NOT NULL,
                             `Economy` int(11) NOT NULL,
                             PRIMARY KEY (`PlaneCode`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `Destinations` (
                                `DestinationCode` char(3) NOT NULL,
                                `Airport` varchar(30) NOT NULL,
                                `CountryCode3` char(3) NOT NULL,
                                PRIMARY KEY (`DestinationCode`),
                                KEY `DestinationCountryCode_FK` (`CountryCode3`),
                                CONSTRAINT `DestinationCountryCode_FK` FOREIGN KEY (`CountryCode3`) REFERENCES `Country` (`countryCode3`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `TicketClass` (
                               `ClassCode` char(3) NOT NULL,
                               `Details` varchar(20) NOT NULL,
                               PRIMARY KEY (`ClassCode`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `TicketType` (
                              `TicketCode` char(1) NOT NULL,
                              `Name` varchar(50) NOT NULL,
                              `Transferrable` bit(1) NOT NULL,
                              `Refundable` bit(1) NOT NULL,
                              `Exchangeable` bit(1) NOT NULL,
                              `FrequentFlyerPoints` bit(1) NOT NULL,
                              PRIMARY KEY (`TicketCode`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `Availability` (
                                `AirlineCode` char(2) NOT NULL,
                                `FlightNumber` varchar(6) NOT NULL,
                                `DepartureTime` datetime NOT NULL,
                                `ClassCode` char(3) NOT NULL,
                                `TicketCode` char(1) NOT NULL,
                                `NumberAvailableSeatsLeg1` int(11) NOT NULL,
                                `NumberAvailableSeatsLeg2` int(11) DEFAULT NULL,
                                PRIMARY KEY (`AirlineCode`,`FlightNumber`,`DepartureTime`,`ClassCode`,`TicketCode`),
                                KEY `AvailabilityClassCode_FK` (`ClassCode`),
                                KEY `AvailabilityTicketCode_FK` (`TicketCode`),
                                CONSTRAINT `AvailabilityTicketCode_FK` FOREIGN KEY (`TicketCode`) REFERENCES `TicketType` (`TicketCode`),
                                CONSTRAINT `AvailabilityAirlineCode_FK` FOREIGN KEY (`AirlineCode`) REFERENCES `Airlines` (`AirlineCode`),
                                CONSTRAINT `AvailabilityClassCode_FK` FOREIGN KEY (`ClassCode`) REFERENCES `TicketClass` (`ClassCode`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `Distances` (
                             `DestinationCode1` char(3) NOT NULL,
                             `DestinationCode2` char(3) NOT NULL,
                             `DistancesInKms` int(11) NOT NULL,
                             PRIMARY KEY (`DestinationCode1`,`DestinationCode2`),
                             KEY `DestinationCode2_FK` (`DestinationCode2`),
                             CONSTRAINT `DestinationCode2_FK` FOREIGN KEY (`DestinationCode2`) REFERENCES `Destinations` (`DestinationCode`),
                             CONSTRAINT `DestinationCode1_FK` FOREIGN KEY (`DestinationCode1`) REFERENCES `Destinations` (`DestinationCode`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `Price` (
                         `AirlineCode` char(2) NOT NULL,
                         `FlightNumber` varchar(6) NOT NULL,
                         `ClassCode` char(3) NOT NULL,
                         `TicketCode` char(1) NOT NULL,
                         `StartDate` datetime NOT NULL,
                         `EndDate` datetime NOT NULL,
                         `Price` decimal(10,2) NOT NULL,
                         `PriceLeg1` decimal(10,2) DEFAULT NULL,
                         `PriceLeg2` decimal(10,2) DEFAULT NULL,
                         PRIMARY KEY (`AirlineCode`,`FlightNumber`,`ClassCode`,`TicketCode`,`StartDate`),
                         KEY `PriceClassCode_FK` (`ClassCode`),
                         KEY `PriceTicketCode_FK` (`TicketCode`),
                         CONSTRAINT `PriceAirlineCode_FK` FOREIGN KEY (`AirlineCode`) REFERENCES `Airlines` (`AirlineCode`),
                         CONSTRAINT `PriceClassCode_FK` FOREIGN KEY (`ClassCode`) REFERENCES `TicketClass` (`ClassCode`),
                         CONSTRAINT `PriceTicketCode_FK` FOREIGN KEY (`TicketCode`) REFERENCES `TicketType` (`TicketCode`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `Flights` (
                           `AirlineCode` char(2) NOT NULL,
                           `FlightNumber` varchar(6) NOT NULL,
                           `DepartureCode` char(3) NOT NULL,
                           `StopOverCode` char(3) DEFAULT NULL,
                           `DestinationCode` char(3) NOT NULL,
                           `DepartureTime` datetime NOT NULL,
                           `ArrivalTimeStopOver` datetime DEFAULT NULL,
                           `DepartureTimeStopOver` datetime DEFAULT NULL,
                           `ArrivalTime` datetime NOT NULL,
                           `PlaneCode` varchar(20) NOT NULL,
                           `Duration` int(11) NOT NULL,
                           `DurationSecondLeg` int(11) DEFAULT NULL,
                           PRIMARY KEY (`AirlineCode`,`FlightNumber`,`DepartureTime`),
                           KEY `FlightsDepartureCode_FK` (`DepartureCode`),
                           KEY `FlightsStopOverCode_FK` (`StopOverCode`),
                           KEY `FlightsDestinationCode_FK` (`DestinationCode`),
                           KEY `FlightsPlaneCode_FK` (`PlaneCode`),
                           CONSTRAINT `FlightsPlaneCode_FK` FOREIGN KEY (`PlaneCode`) REFERENCES `PlaneType` (`PlaneCode`),
                           CONSTRAINT `FlightsAirlineCode_FK` FOREIGN KEY (`AirlineCode`) REFERENCES `Airlines` (`AirlineCode`),
                           CONSTRAINT `FlightsDepartureCode_FK` FOREIGN KEY (`DepartureCode`) REFERENCES `Destinations` (`DestinationCode`),
                           CONSTRAINT `FlightsDestinationCode_FK` FOREIGN KEY (`DestinationCode`) REFERENCES `Destinations` (`DestinationCode`),
                           CONSTRAINT `FlightsStopOverCode_FK` FOREIGN KEY (`StopOverCode`) REFERENCES `Destinations` (`DestinationCode`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `UserAccount`  (
                                `UserID` int(11) NOT NULL AUTO_INCREMENT,
                                `FirstName` varchar(20) NOT NULL,
                                `MiddleNames` varchar(60),
                                `LastName` varchar(20) NOT NULL,
                                `Email` varchar(60) NOT NULL,
                                `Phone` int(20),
                                `Gender` int(2) NOT NULL,
                                `Citizenship` varchar(60) NOT NULL,
                                `DateOfBirth` date NOT NULL,
                                `UserType` int(2) NOT NULL,
                                `Password` varchar(14) NOT NULL,
                                `FamilyMembers` varchar(120),
                                `EmergencyContact` varchar(60),
                                `Address` varchar(120),
                                PRIMARY KEY (`UserID`),
                                UNIQUE(`Email`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

ALTER TABLE UserAccount AUTO_INCREMENT=1;

CREATE TABLE `Booking` (
                           `BookingID` int NOT NULL AUTO_INCREMENT,

                           `UserID` int(11) NOT NULL,
                           `BookingType` int NOT NULL,
                           `FirstName` varchar(20) NOT NULL,
                           `MiddleNames` varchar(60),
                           `LastName` varchar(20) NOT NULL,
                           `DateOfBirth` date NOT NULL,
                           `BookingDate` date NOT NULL,
                           `GroupSize` int NOT NULL,


                           `AirlineCode` char(2) NOT NULL,
                           `FlightNumber` varchar(6) NOT NULL,
                           `DepartureTime` datetime NOT NULL,
                           `ClassCode` char(3) NOT NULL,
                           `TicketCode` char(1) NOT NULL,

                           `AirlineCode2` varchar(6),
                           `FlightNumber2` varchar(6),
                           `DepartureTime2` datetime,
                           `ClassCode2`char(3),
                           `TicketCode2` char(1),

                           `AirlineCode3` varchar(6),
                           `FlightNumber3` varchar(6),
                           `DepartureTime3` datetime,
                           `ClassCode3`char(3),
                           `TicketCode3` char(1),

                           `AirlineCode4` varchar(6),
                           `FlightNumber4` varchar(6),
                           `DepartureTime4` datetime,
                           `ClassCode4`char(3),
                           `TicketCode4` char(1),

                           PRIMARY KEY (`BookingID`),
                           FOREIGN KEY (`UserID`) REFERENCES `UserAccount` (`UserID`),
                           FOREIGN KEY (`AirlineCode`,`FlightNumber`,`DepartureTime`,`ClassCode`,`TicketCode`) REFERENCES `Availability` (`AirlineCode`,`FlightNumber`,`DepartureTime`,`ClassCode`,`TicketCode`),
                           FOREIGN KEY (`AirlineCode2`,`FlightNumber2`,`DepartureTime2`,`ClassCode2`,`TicketCode2`) REFERENCES `Availability` (`AirlineCode`,`FlightNumber`,`DepartureTime`,`ClassCode`,`TicketCode`),
                           FOREIGN KEY (`AirlineCode3`,`FlightNumber3`,`DepartureTime3`,`ClassCode3`,`TicketCode3`) REFERENCES `Availability` (`AirlineCode`,`FlightNumber`,`DepartureTime`,`ClassCode`,`TicketCode`),
                           FOREIGN KEY (`AirlineCode4`,`FlightNumber4`,`DepartureTime4`,`ClassCode4`,`TicketCode4`) REFERENCES `Availability` (`AirlineCode`,`FlightNumber`,`DepartureTime`,`ClassCode`,`TicketCode`)

) ENGINE=InnoDB DEFAULT CHARSET=latin1;






