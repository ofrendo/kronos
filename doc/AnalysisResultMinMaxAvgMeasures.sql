DROP VIEW IF EXISTS AnalysisResultMinMaxAvgMeasures;
CREATE VIEW IF NOT EXISTS AnalysisResultMinMaxAvgMeasures AS
SELECT *
FROM (
	SELECT 
		AnalysisResult,
		COUNT(*) as TotalNo,
		MIN(AnalysisTime) as MinAnalysisTime,
		AVG(AnalysisTime) as AvgAnalysisTime,
		MAX(AnalysisTime) as MaxAnalysisTime
	FROM Product
	GROUP BY AnalysisResult
) NATURAL JOIN(
	SELECT
		AnalysisResult,
		MIN(Value) as MinMillingHeat, 
		AVG(Value) AS AvgMillingHeat, 
		MAX(Value) as MaxMillingHeat 
	FROM Product 
	Natural Join Measure 
	WHERE Station = "Milling Heat" GROUP BY AnalysisResult
) NATURAL JOIN (
	SELECT 
		AnalysisResult,
		MIN(Value) as MinDrillingHeat, 
		AVG(Value) AS AvgDrillingHeat, 
		MAX(Value) as MaxDrillingHeat 
	FROM Product 
	Natural Join Measure 
	WHERE Station = "Drilling Heat" GROUP BY AnalysisResult
) NATURAL JOIN (
	SELECT 
		AnalysisResult,
		MIN(Value) as MinMillingSpeed, 
		AVG(Value) AS AvgMillingSpeed, 
		MAX(Value) as MaxMillingSpeed 
	FROM Product 
	Natural Join Measure 
	WHERE Station = "Milling Speed" GROUP BY AnalysisResult
) NATURAL JOIN (
	SELECT 
		AnalysisResult, 
		MIN(Value) as MinDrillingSpeed, 
		AVG(Value) AS AvgDrillingSpeed, 
		MAX(Value) as MaxDrillingSpeed 
	FROM Product 
	Natural Join Measure 
	WHERE Station = "Drilling Speed" GROUP BY AnalysisResult
) NATURAL JOIN (
	SELECT 
		AnalysisResult, 
		MIN(Value) as MinMillingTime, 
		AVG(Value) AS AvgMillingTime, 
		MAX(Value) as MaxMillingTime 
	FROM Product 
	Natural Join Measure 
	WHERE Station = "Milling Station" GROUP BY AnalysisResult
) NATURAL JOIN (
	SELECT 
		AnalysisResult, 
		MIN(Value) as MinDrillingTime, 
		AVG(Value) AS AvgDrillingTime, 
		MAX(Value) as MaxDrillingTime 
	FROM Product 
	Natural Join Measure 
	WHERE Station = "Drilling Station" GROUP BY AnalysisResult
);