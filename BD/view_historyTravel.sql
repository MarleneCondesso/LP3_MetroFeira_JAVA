CREATE VIEW [dbo].[view_historyTravel]
AS
SELECT userID, TripUsersID, TripDEP,TripARR,TripDate, CAST (DATEADD(second, SUM(DATEDIFF(second, '00:00:00', Duration)), '00:00:00')as time) AS "Tempo", 
SUM(stationArrPrice) AS "Preco"
FROM view_TripUserID
GROUP BY  userID, TripUsersID, TripDEP, TripARR,tripDate