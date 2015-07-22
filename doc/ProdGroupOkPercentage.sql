CREATE VIEW IF NOT EXISTS ProdGroupOKPercentage AS 
	SELECT MatGroup, (NoOK * 1.0 / NoTotal) as OKPercentage FROM
	(
		SELECT 	MatGroup,
				COUNT(*) AS NoOk 
		FROM ProdMatGroups 
		WHERE AnalysisResult = 'OK' 
		GROUP BY MatGroup
	) OKTable
	NATURAL JOIN
	(
		SELECT 	MatGroup, 
				COUNT(*) as NoTotal 
		FROM ProdMatGroups
		GROUP BY MatGroup
	) TotalTable
	ORDER BY OKPercentage