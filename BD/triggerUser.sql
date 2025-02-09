USE [Subway2Feira]
GO
/****** Object:  Trigger [dbo].[tg_email_nif_check]    Script Date: 18/12/2021 19:10:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO


ALTER TRIGGER [dbo].[tg_email_nif_check]
ON [dbo].[User]
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
  RAISERROR('Insira um email valido!',16,1)
  ROLLBACK TRANSACTION;
  RETURN
END

    if  LEN(@nif) <> 9 OR
  @nif LIKE '%[^0-9]%' AND
  @nif IS NOT NULL 
        BEGIN
  RAISERROR ('Nif não é valido!', 16, 1);
  ROLLBACK TRANSACTION;
  RETURN
end
