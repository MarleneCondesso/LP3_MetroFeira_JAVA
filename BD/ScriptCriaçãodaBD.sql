
CREATE DATABASE Subway2Feira
GO
Use Subway2Feira
GO

CREATE TABLE Line
(
  letter char NOT NULL,
  color nvarchar(10) NOT NULL,
  CONSTRAINT PK_Line PRIMARY KEY (letter)
)
GO

CREATE TABLE Station
(
  id int IDENTITY(0,1) NOT NULL,
  name nvarchar(50) NOT NULL,
  price money NOT NULL,
  CONSTRAINT PK_Station PRIMARY KEY (id)
)
GO

CREATE TABLE StationLine
(
  id int IDENTITY(0,1) NOT NULL,
  letterID char NOT NULL,
  stationID int NOT NULL,
  CONSTRAINT PK_StationLine PRIMARY KEY (id),
  CONSTRAINT FK_Line_TO_StationLine FOREIGN KEY (letterID) REFERENCES Line (letter),
  CONSTRAINT FK_Station_TO_StationLine  FOREIGN KEY (stationID) REFERENCES Station (id)
)
GO

CREATE TABLE Trip
(
  id int IDENTITY(0,1) NOT NULL,
  letter char NOT NULL,
  departureID int NOT NULL,
  arrivalID int NOT NULL,
  duration time NOT NULL,
  CONSTRAINT PK_Trips PRIMARY KEY (id),
  CONSTRAINT FK_Line_TO_Trips FOREIGN KEY (letter) REFERENCES Line (letter),
  CONSTRAINT FK_StationLine_TO_TripsArrivalID FOREIGN KEY (ArrivalID) REFERENCES StationLine (id),
  CONSTRAINT FK_StationLine_TO_TripsdepartureID FOREIGN KEY (departureID) REFERENCES StationLine (id)
  
)
GO

CREATE TABLE [User]
(
  id int IDENTITY(0,1) CONSTRAINT PK_User PRIMARY KEY NOT NULL,
  name nvarchar(50) NOT NULL,
  email nvarchar(50) NOT NULL,
  password nvarchar(100) NOT NULL,
  nif nvarchar(9) NULL,
  userType nvarchar(50) NOT NULL,
  state nvarchar(20),
  salt nvarchar(50) NOT NULL
)
GO

CREATE TABLE TripUsers
(
  id int IDENTITY(0,1) NOT NULL,
  userID int NOT NULL,
  departureID int NOT NULL,
  arriveID int NOT NULL,
  tripDate date NOT NULL,
  tripHour Time NOT NULL,
  price money NOT NULL,
  CONSTRAINT PK_TripUsers PRIMARY KEY (id),
  CONSTRAINT FK_User_TO_TripUsers FOREIGN KEY (userID) REFERENCES [User] (id),
  CONSTRAINT FK_Station_TO_TripUsersdepartureID FOREIGN KEY (departureID) REFERENCES Station (id),
  CONSTRAINT FK_Station_TO_TripUsersarriveID FOREIGN KEY (arriveID) REFERENCES Station (id)
)
GO

CREATE TABLE Path
(
  id int IDENTITY(0,1) NOT NULL,
  tripUserID int NOT NULL,
  tripsID int NOT NULL,
  CONSTRAINT PK_Route PRIMARY KEY (id),
  CONSTRAINT FK_TripUsers_TO_Route FOREIGN KEY (tripUserID) REFERENCES TripUsers (id),
  CONSTRAINT FK_Trips_TO_Route FOREIGN KEY (tripsID) REFERENCES Trip (id)
)
GO

ALTER TABLE [User]
  ADD CONSTRAINT UQ_id UNIQUE (id)
GO


CREATE TRIGGER tg_email_nif_check
ON [User]
for INSERT
AS  

    DECLARE @email NVARCHAR(50),
            @nif NVARCHAR(9)

    SELECT @email = email, @nif = nif
FROM inserted

    if @email LIKE '%[|!"#$%&/()=?»´`~^ºª+*¨:;,\/§£€< >]%' OR
  @email LIKE '%..%' OR
  @email LIKE '%@%@%'
        BEGIN
  RAISERROR('Insira um email valido!!',16,1)
  RETURN
END

    if  LEN(@nif) <> 9 OR
  @nif LIKE '%[^0-9]%' AND
  @nif IS NOT NULL 
        BEGIN
  RAISERROR ('Nif não é valido!!', 16, 1);
  ROLLBACK TRANSACTION;
  RETURN
end
GO

