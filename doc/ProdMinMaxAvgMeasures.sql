DROP VIEW IF EXISTS ProdMinMaxAvgMeasures;
CREATE VIEW IF NOT EXISTS ProdMinMaxAvgMeasures AS
SELECT *
FROM (
	SELECT 
		OrderNo,
		ProductionStart,
		ProductionEnd,
		MaterialNo,
		AnalysisResult,
		AnalysisTime,
		MIN(Value) as MinMillingHeat, 
		AVG(Value) AS AvgMillingHeat, 
		MAX(Value) as MaxMillingHeat 
	FROM Product 
	Natural Join Measure 
	WHERE Station = "Milling Heat" GROUP BY OrderNo
) NATURAL JOIN (
	SELECT 
		OrderNo, 
		MIN(Value) as MinDrillingHeat, 
		AVG(Value) AS AvgDrillingHeat, 
		MAX(Value) as MaxDrillingHeat 
	FROM Product 
	Natural Join Measure 
	WHERE Station = "Drilling Heat" GROUP BY OrderNo
) NATURAL JOIN (
	SELECT 
		OrderNo, 
		MIN(Value) as MinMillingSpeed, 
		AVG(Value) AS AvgMillingSpeed, 
		MAX(Value) as MaxMillingSpeed 
	FROM Product 
	Natural Join Measure 
	WHERE Station = "Milling Speed" GROUP BY OrderNo
) NATURAL JOIN (
	SELECT 
		OrderNo, 
		MIN(Value) as MinDrillingSpeed, 
		AVG(Value) AS AvgDrillingSpeed, 
		MAX(Value) as MaxDrillingSpeed 
	FROM Product 
	Natural Join Measure 
	WHERE Station = "Drilling Speed" GROUP BY OrderNo
) NATURAL JOIN (
	SELECT 
		OrderNo, 
		MIN(Value) as MinMillingTime, 
		AVG(Value) AS AvgMillingTime, 
		MAX(Value) as MaxMillingTime 
	FROM Product 
	Natural Join Measure 
	WHERE Station = "Milling Station" GROUP BY OrderNo
) NATURAL JOIN (
	SELECT 
		OrderNo, 
		MIN(Value) as MinDrillingTime, 
		AVG(Value) AS AvgDrillingTime, 
		MAX(Value) as MaxDrillingTime 
	FROM Product 
	Natural Join Measure 
	WHERE Station = "Drilling Station" GROUP BY OrderNo
);