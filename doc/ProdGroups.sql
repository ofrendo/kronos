CREATE VIEW ProdMatGroups AS
	SELECT 
		*, 
		'A' as MatGroup 
	FROM Product 
	WHERE 	MaterialNo = 4248 OR 
			MaterialNo = 5653 OR 
			MaterialNo = 6443 OR 
			MaterialNo = 7134 OR 
			MaterialNo = 7423 OR 
			MaterialNo = 7432
	UNION
	SELECT
		*,
		'B' as MatGroup
	FROM Product 
	WHERE 	MaterialNo = 7742 OR 
			MaterialNo = 8235 OR 
			MaterialNo = 8354 OR 
			MaterialNo = 8414 OR 
			MaterialNo = 8932 OR 
			MaterialNo = 9823