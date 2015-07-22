CREATE VIEW IF NOT EXISTS AnalysisResultByMat AS 
	SELECT MaterialNo, (NoOK * 1.0 / NoTotal) as OKPercentage, NoOk, NoTotal FROM
	(
		SELECT 	MaterialNo, 
					COUNT(*) AS NoOk 
		FROM Product 
		WHERE AnalysisResult = 'OK' 
		GROUP BY MaterialNo
	) OKTable
	NATURAL JOIN
	(
		SELECT 	MaterialNo, 
					COUNT(*) as NoTotal 
		FROM Product 
		GROUP BY MaterialNo
	) TotalTable
	ORDER BY OKPercentage