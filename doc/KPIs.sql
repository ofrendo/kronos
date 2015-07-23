DROP VIEW IF EXISTS KPIs;
CREATE VIEW IF NOT EXISTS KPIs AS
	SELECT NOKPercentage, CustomerNo, NoTotalProducts, Streak FROM
	(SELECT (NoOK * 1.0 / NoTotal) as NOKPercentage, NoOk, NoTotal FROM
	(
		SELECT COUNT(*) AS NoOk 
		FROM Product 
		WHERE AnalysisResult = 'NOK' 
	) OKTable
	NATURAL JOIN
	(
		SELECT COUNT(*) as NoTotal 
		FROM Product 
	) TotalTable)
	JOIN 
	(
		SELECT COUNT(*) AS NoProducts, CustomerNo 
		FROM Product GROUP BY CustomerNo 
		ORDER BY NoProducts DESC 
		LIMIT 1
	)
	JOIN
	(
		SELECT COUNT(*) AS NoTotalProducts FROM PRODUCT
	)
	JOIN
	(
		SELECT COUNT(*) AS Streak 
		FROM PRODUCT 
		WHERE AnalysisResult = "OK" AND ProductionEnd > (SELECT ProductionEnd FROM PRODUCT WHERE AnalysisResult = "NOK" ORDER BY ProductionEnd DESC Limit 1)
		ORDER BY ProductionEnd DESC
	)