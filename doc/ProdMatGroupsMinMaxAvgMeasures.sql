CREATE VIEW IF NOT EXISTS MatGroupMinMaxAvgMeasures AS
SELECT *
FROM (
	SELECT 
		MatGroup,
		MIN(AnalysisTime) as MinAnalysisTime,
		AVG(AnalysisTime) as AvgAnalysisTime,
		MAX(AnalysisTime) as MaxAnalysisTime,
		MIN(Value) as MinMillingHeat, 
		AVG(Value) AS AvgMillingHeat, 
		MAX(Value) as MaxMillingHeat 
	FROM ProdMatGroups 
	Natural Join Measure 
	WHERE Station = "Milling Heat" GROUP BY MatGroup
) NATURAL JOIN (
	SELECT 
		MatGroup,
		MIN(Value) as MinDrillingHeat, 
		AVG(Value) AS AvgDrillingHeat, 
		MAX(Value) as MaxDrillingHeat 
	FROM ProdMatGroups 
	Natural Join Measure 
	WHERE Station = "Drilling Heat" GROUP BY MatGroup
) NATURAL JOIN (
	SELECT 
		MatGroup,
		MIN(Value) as MinMillingSpeed, 
		AVG(Value) AS AvgMillingSpeed, 
		MAX(Value) as MaxMillingSpeed 
	FROM ProdMatGroups 
	Natural Join Measure 
	WHERE Station = "Milling Speed" GROUP BY MatGroup
) NATURAL JOIN (
	SELECT 
		MatGroup, 
		MIN(Value) as MinDrillingSpeed, 
		AVG(Value) AS AvgDrillingSpeed, 
		MAX(Value) as MaxDrillingSpeed 
	FROM ProdMatGroups 
	Natural Join Measure 
	WHERE Station = "Drilling Speed" GROUP BY MatGroup
) NATURAL JOIN (
	SELECT 
		MatGroup, 
		MIN(Value) as MinMillingTime, 
		AVG(Value) AS AvgMillingTime, 
		MAX(Value) as MaxMillingTime 
	FROM ProdMatGroups 
	Natural Join Measure 
	WHERE Station = "Milling Station" GROUP BY MatGroup
) NATURAL JOIN (
	SELECT 
		MatGroup, 
		MIN(Value) as MinDrillingTime, 
		AVG(Value) AS AvgDrillingTime, 
		MAX(Value) as MaxDrillingTime 
	FROM ProdMatGroups 
	Natural Join Measure 
	WHERE Station = "Drilling Station" GROUP BY MatGroup
);