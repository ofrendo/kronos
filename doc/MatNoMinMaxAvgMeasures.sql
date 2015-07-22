CREATE VIEW IF NOT EXISTS MatNoMinMaxAvgMeasures AS
SELECT *
FROM (
	SELECT 
		MaterialNo,
		MIN(AnalysisTime) as MinAnalysisTime,
		AVG(AnalysisTime) as AvgAnalysisTime,
		MAX(AnalysisTime) as MaxAnalysisTime,
		MIN(Value) as MinMillingHeat, 
		AVG(Value) AS AvgMillingHeat, 
		MAX(Value) as MaxMillingHeat 
	FROM Product 
	Natural Join Measure 
	WHERE Station = "Milling Heat" GROUP BY MaterialNo
) NATURAL JOIN (
	SELECT 
		MaterialNo,
		MIN(Value) as MinDrillingHeat, 
		AVG(Value) AS AvgDrillingHeat, 
		MAX(Value) as MaxDrillingHeat 
	FROM Product 
	Natural Join Measure 
	WHERE Station = "Drilling Heat" GROUP BY MaterialNo
) NATURAL JOIN (
	SELECT 
		MaterialNo,
		MIN(Value) as MinMillingSpeed, 
		AVG(Value) AS AvgMillingSpeed, 
		MAX(Value) as MaxMillingSpeed 
	FROM Product 
	Natural Join Measure 
	WHERE Station = "Milling Speed" GROUP BY MaterialNo
) NATURAL JOIN (
	SELECT 
		MaterialNo, 
		MIN(Value) as MinDrillingSpeed, 
		AVG(Value) AS AvgDrillingSpeed, 
		MAX(Value) as MaxDrillingSpeed 
	FROM Product 
	Natural Join Measure 
	WHERE Station = "Drilling Speed" GROUP BY MaterialNo
) NATURAL JOIN (
	SELECT 
		MaterialNo, 
		MIN(Value) as MinMillingTime, 
		AVG(Value) AS AvgMillingTime, 
		MAX(Value) as MaxMillingTime 
	FROM Product 
	Natural Join Measure 
	WHERE Station = "Milling Station" GROUP BY MaterialNo
) NATURAL JOIN (
	SELECT 
		MaterialNo, 
		MIN(Value) as MinDrillingTime, 
		AVG(Value) AS AvgDrillingTime, 
		MAX(Value) as MaxDrillingTime 
	FROM Product 
	Natural Join Measure 
	WHERE Station = "Drilling Station" GROUP BY MaterialNo
);