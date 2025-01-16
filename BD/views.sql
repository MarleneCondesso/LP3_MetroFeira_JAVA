CREATE VIEW view_TripUser
AS
    SELECT 
    [TripUsers].id as 'TripUsersID',
    [TripUsers].userID as 'userID',
    [TripUsers].tripDate,
    [TripUsers].tripHour,
    stationdep.name as 'TripDEP',
    stationARR.name as 'TripARR',
    [Path].id as 'pathID',
    [view_Trips].*
    from
        [TripUsers]
        INNER JOIN [Station] as stationdep ON
        stationdep.id = TripUsers.departureID
        INNER JOIN [Station] as stationARR ON
        stationARR.id = TripUsers.arriveID
        INNER join [Path] on
    [Path].tripUserID = TripUsers.id
        INNER join [view_Trips] ON
    [Path].tripsID = [view_Trips].idTrip
GO

CREATE VIEW view_TripHistory
AS
    Select 
        TripUsersID, 
        TripARR, 
        TripDEP,
        userID,
        tripDate,
        tripHour,
        CAST (DATEADD(second, SUM(DATEDIFF(second, '00:00:00', Duration)), '00:00:00')as time) as 'Time', 
        SUM(CASE
            WHEN TripDep = StationDepName THEN stationDepPrice + stationArrPrice
            ELSE stationArrPrice
        END) as Price
    FROM view_TripUser
    GROUP BY userID, TripUsersID, TripARR,TripDEP,tripDate,tripHour
GO

CREATE VIEW view_Trips
AS
    SELECT
        Trip.id as 'idTrip',
		Trip.duration as 'Duration',
        stationLineArrival.id as 'idArrival',
        stationLineDeparture.id as 'idDeparture',
        stationArr.id as 'stationArrId',
        stationArr.name as 'stationArrName',
		stationArr.price as 'stationArrPrice',
        stationDep.id as 'stationDepId',
        stationDep.name as 'stationDepName',
		stationDep.price as 'stationDepPrice',
        [Line].letter,
        [Line].color
    FROM
        Trip
        INNER JOIN StationLine as stationLineArrival ON
        Trip.arrivalID = stationLineArrival.id
        INNER JOIN Station as stationArr ON
        stationLineArrival.stationID = stationArr.id

        INNER JOIN StationLine as stationLineDeparture ON
        Trip.departureID = stationLineDeparture.id
        INNER JOIN Station as stationDep ON
        stationLineDeparture.stationID = stationDep.id

        INNER JOIN [Line] ON
        Trip.letter = [Line].letter
GO

