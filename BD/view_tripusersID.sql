CREATE VIEW [dbo].[view_TripUserID]
AS
    SELECT 
    [TripUsers].id as 'TripUsersID',
    [TripUsers].userID as 'userID',
    stationdep.id as 'TripDEP',
    stationARR.id as 'TripARR',
	[TripUsers].tripDate as 'TripDate',
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
