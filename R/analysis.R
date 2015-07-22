# Connect to DB
#library("RSQLite")
#connection = dbConnect(drv="SQLite", dbname="C:/Users/D059373/git/kronos/data/historical.db")

library(sqldf)
db <- dbConnect(SQLite(), dbname="../data/historical.db")

sqlOnlyProducts <- "SELECT * FROM Product"
dataOnlyProducts <- dbGetQuery(conn = db, sqlOnlyProducts)
dataOnlyProducts$materialGroup <- ifelse(dataOnlyProducts$MaterialNo==4248 |
                                           dataOnlyProducts$MaterialNo==5653 |
                                           dataOnlyProducts$MaterialNo==6443 |
                                           dataOnlyProducts$MaterialNo==7134 |
                                           dataOnlyProducts$MaterialNo==7423 |
                                           dataOnlyProducts$MaterialNo==7432, 1, 2)

sqlProducts <- "SELECT * FROM Product NATURAL JOIN Measure"
dataProducts <- dbGetQuery(conn = db, sqlProducts)
print(paste0("Number of products: ", nrow(dataOnlyProducts)))

# Group by material group clusters
dataProducts$materialGroup <- ifelse(dataProducts$MaterialNo==4248 |
                                       dataProducts$MaterialNo==5653 |
                                       dataProducts$MaterialNo==6443 |
                                       dataProducts$MaterialNo==7134 |
                                       dataProducts$MaterialNo==7423 |
                                       dataProducts$MaterialNo==7432, 1, 2)


sqlProduct <- paste0("SELECT * FROM Measure WHERE ID=", dataProducts[1, ]$ID)
dataProduct <- dbGetQuery(conn = db, sqlProduct)


dbDisconnect(db);


# Time to vorbereiten some data for ze Diskriminanzanalyse
productID <- unique(dataProducts$ID)
analysisResult <- aggregate(AnalysisResult ~ ID, dataProducts, unique)[, 2]
  
dataProductsMillingHeat <- subset(dataProducts, Station=="Milling Heat")
millingHeatAvg <- aggregate(Value ~ ID, dataProductsMillingHeat, mean)[,2]

dataProductsMillingSpeed <- subset(dataProducts, Station=="Milling Speed")
millingSpeedAvg <- aggregate(Value ~ ID, dataProductsMillingSpeed, mean)[,2]

millingTime <- subset(dataProducts, Station=="Milling Station")$Value

dataProductsDrillingHeat <- subset(dataProducts, Station=="Drilling Heat")
drillingHeatAvg <- aggregate(Value ~ ID, dataProductsDrillingHeat, mean)[,2]

dataProductsDrillingSpeed<- subset(dataProducts, Station=="Drilling Speed")
drillingSpeedAvg <- aggregate(Value ~ ID, dataProductsDrillingSpeed, mean)[,2]

drillingTime <- subset(dataProducts, Station=="Drilling Station")$Value

analysisTime <- aggregate(AnalysisTime ~ ID, dataProducts, mean)[, 2]

productionTime <- aggregate((ProductionEnd - ProductionStart) ~ ID, dataProducts, mean)[, 2]

aData <- data.frame(productID, analysisResult,
                    millingHeatAvg, millingSpeedAvg, millingTime, 
                    drillingHeatAvg, drillingSpeedAvg, drillingTime
                    , analysisTime
                    , productionTime
)

# ANALYSE: Diskriminanzanalyse
library("MASS")
fit <- lda(analysisResult ~ 
             scale(millingHeatAvg)# + millingSpeedAvg + millingTime 
             + scale(drillingHeatAvg)# + drillingSpeedAvg + drillingTime
             #+ analysisTime + productionTime 
           , data=aData)
           #na.action="na.omit", CV=TRUE)
#print(fit) # show results
#plot(fit)


getResultRatio <- function(productAnalysisResult) {
  ratio <- length(productAnalysisResult[productAnalysisResult=="OK"])/length(productAnalysisResult)
  return (ratio)
}


# Einfache Analysen
analysisResultNumbers <- aggregate(AnalysisResult ~ MaterialNo, dataOnlyProducts, getResultRatio)
n <- aggregate(ID ~ MaterialNo, dataOnlyProducts, length)[,2]

easyAnalysis <- data.frame(analysisResultNumbers, n)


# Boxplot daten: Verteilung von OK/NOK Daten in Abhängigkeit Material

# Zeigt vielleicht was? Hitze für NOK meistens etwas höher
dataMillingHeat <- subset(dataProducts, Station=="Milling Heat")
ggplot(dataMillingHeat, aes(factor(AnalysisResult), Value)) + geom_boxplot() + facet_grid(. ~ materialGroup)

# Das hier zeigt nichts
dataMillingSpeed <- subset(dataProducts, Station=="Milling Speed")
ggplot(dataMillingSpeed, aes(factor(AnalysisResult), Value)) + geom_boxplot() + facet_grid(. ~ materialGroup)

# So wie milling heat
dataDrillingHeat <- subset(dataProducts, Station=="Drilling Heat")
ggplot(dataDrillingHeat, aes(factor(AnalysisResult), Value)) + geom_boxplot() + facet_grid(. ~ MaterialNo)

# Zeigt nischt
dataDrillingSpeed <- subset(dataProducts, Station=="Drilling Speed")
ggplot(dataDrillingSpeed, aes(factor(AnalysisResult), Value)) + geom_boxplot() + facet_grid(. ~ MaterialNo)

dataMillingTime <- subset(dataProducts, Station=="Milling Station")
ggplot(dataMillingTime, aes(factor(AnalysisResult), Value)) + geom_boxplot() + facet_grid(. ~ MaterialNo)


dataDrillingTime <- subset(dataProducts, Station=="Drilling Station")
ggplot(dataDrillingTime, aes(factor(AnalysisResult), Value)) + geom_boxplot() + facet_grid(. ~ MaterialNo)

# Production time: Zeigt eher was, NOK Produkte eher kürzer
ggplot(dataOnlyProducts, aes(factor(AnalysisResult), ProductionEnd-ProductionStart)) + 
  geom_boxplot() + 
  facet_grid(. ~ materialGroup)

# Analysis time (eher komplett Random, aber: auch 0 Werte?)
ggplot(dataOnlyProducts, aes(factor(AnalysisResult), AnalysisTime)) + 
  geom_boxplot() + 
  facet_grid(. ~ MaterialNo)


# Anzahl Produkte in Abhängigkeit vom MaterialNo und CustomerNo
n <- aggregate(AnalysisResult ~ MaterialNo + CustomerNo, dataOnlyProducts, getResultRatio)


# ANALYSE: Clusteranalyse
dataMillingHeatAvg <- aggregate(Value ~ ID, dataProductsMillingHeat, mean)[, 2]
dataDrillingHeatAvg <- aggregate(Value ~ ID, dataProductsDrillingHeat, mean)[, 2]

clusterData = data.frame(dataMillingHeatAvg, dataDrillingHeatAvg)
cl <- kmeans(clusterData, 2)
plot(clusterData, col = cl$cluster)
#text(clusterData, labels=dataMillingHeatAvg[, 1], col=dataMillingHeatAvg[, 1])

#library(cluster) 
#clusplot(clusterData, cl$cluster, color=TRUE, shade=TRUE, 
#         labels=2, lines=0)


# Analyse einzelnes Produkt
productOK <- subset(dataProducts, AnalysisResult=="OK" & ID==3)
productNOK <- subset(dataProducts, AnalysisResult=="NOK" & MaterialNo==7432 & ID==52)

ggplot(subset(dataProducts,Station=="Drilling Heat" & (ID==3 | ID==52)), 
       aes(x=-Timestamp, y=Value)) + 
  geom_line() +
  geom_point() + 
  facet_grid(. ~ AnalysisResult, scales = "free")

# VISUALIZE THE BIG DATA
library("ggplot2")

#productSubset <- subset(dataProduct, Station=="Milling Heat")
#print(ggplot(data=productSubset, aes(x=Timestamp, y=Value)) + geom_line())



