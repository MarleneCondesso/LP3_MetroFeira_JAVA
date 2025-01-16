CREATE PROCEDURE pd_AddOrUpdateLine
    (@letter CHAR,
    @color NVARCHAR(10))
AS
BEGIN
    IF NOT EXISTS (SELECT *
    FROM [Line]
    WHERE letter = @letter)
        BEGIN
        INSERT INTO [Line]
        VALUES
            (@letter, @color);
    END

    IF EXISTS (SELECT *
    FROM [Line]
    WHERE letter = @letter AND [color] != @color)
        BEGIN
        UPDATE [Line] SET [color] = @color WHERE letter = @letter
    END
END
GO

CREATE PROCEDURE pd_AddOrUpdateStation
    (@name NVARCHAR(50),
	@price Money,
    @outputId INT OUTPUT)
AS
BEGIN

    IF NOT EXISTS (SELECT *
    FROM [Station]
    WHERE [name] = @name)
        BEGIN
        INSERT INTO [Station]
            ([name], [price])
        VALUES
            (@name,@price);
    END

    SELECT @outputId = id
    FROM Station
    WHERE [name] = @name

END
GO

CREATE PROCEDURE pd_AddOrUpdateStationLine
    (@letter char,
    @color NVARCHAR(50) ,
    @name NVARCHAR(50),
	@price Money,
    @StationLineid INT OUTPUT)
AS
BEGIN

    DECLARE @stationId int

    EXEC pd_AddOrUpdateStation @name = @name, @price = @price, @outputId = @stationId OUTPUT;

    EXEC pd_AddOrUpdateLine @letter = @letter, @color = @color

    IF NOT EXISTS (SELECT *
    FROM [StationLine]
    WHERE letterID = @letter AND stationID = @stationId) 
        BEGIN
        INSERT INTO [StationLine]
            (letterID,stationID)
        VALUES
            (@letter, @stationId)
    END

    SELECT @StationLineid = id
    FROM [StationLine]
    WHERE letterID = @letter AND stationID = @stationId

END
GO

CREATE PROCEDURE pd_AddOrUpdateTrip
    (@letter CHAR,
    @color NVARCHAR(50),
    @arrival NVARCHAR(50),
    @departure NVARCHAR(50),
	@duration TIME
	,@pricearrival Money,
	@pricedeparture Money)
AS
BEGIN

    DECLARE @arrivalID INT,
            @departureID INT

    EXEC pd_AddOrUpdateStationLine @letter = @letter, 
                                   @color = @color, 
                                   @name = @arrival,
								   @price = @pricearrival,
                                   @StationLineid = @arrivalID OUTPUT;


    EXEC pd_AddOrUpdateStationLine @letter = @letter, 
                                   @color = @color, 
                                   @name = @departure,
								   @price = @pricedeparture,
                                   @StationLineid = @departureID OUTPUT;

    IF NOT EXISTS (
        SELECT *
    FROM Trip
    WHERE letter = @letter AND
        departureID = @departureID AND
        arrivalID = @arrivalID AND
		duration = @duration)
        BEGIN
        INSERT INTO [Trip]
            (letter,arrivalID,departureID,duration)
        VALUES
            (@letter, @arrivalID, @departureID,@duration)
    END


END
GO
