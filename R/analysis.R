# Connect to DB
#library("RSQLite")
#connection = dbConnect(drv="SQLite", dbname="C:/Users/D059373/git/kronos/data/historical.db")

source("saveImg.R")

library(sqldf)
library(ggplot2)
db <- dbConnect(SQLite(), dbname="../data/historical.db")

sqlOnlyProducts <- "SELECT * FROM Product ORDER BY ID"
dataOnlyProducts <- dbGetQuery(conn = db, sqlOnlyProducts)
dataOnlyProducts$materialGroup <- ifelse(dataOnlyProducts$MaterialNo==4248 |
                                           dataOnlyProducts$MaterialNo==5653 |
                                           dataOnlyProducts$MaterialNo==6443 |
                                           dataOnlyProducts$MaterialNo==7134 |
                                           dataOnlyProducts$MaterialNo==7423 |
                                           dataOnlyProducts$MaterialNo==7432, 1, 2)

sqlProducts <- "SELECT * FROM Product NATURAL JOIN Measure ORDER BY ID"
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
#metaData <- dataOnlyProducts[c("ID", "MaterialNo", "materialGroup")]
  #aggregate(MaterialNo ~ ID, dataProducts, unique)  # unique(dataProducts$ID)
#analysisResult <- aggregate(AnalysisResult ~ ID, dataProducts, unique)[,2]
  
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

aData <- data.frame(dataOnlyProducts$ID, 
                    dataOnlyProducts$MaterialNo, 
                    dataOnlyProducts$materialGroup,
                    dataOnlyProducts$AnalysisResult,
                    #analysisResult,
                    millingHeatAvg, millingSpeedAvg, millingTime, 
                    drillingHeatAvg, drillingSpeedAvg, drillingTime
                    , analysisTime
                    , productionTime
)
aData <- subset(aData, dataOnlyProducts.materialGroup == 2)


# ANALYSE: Diskriminanzanalyse
library("MASS")
fit <- lda(dataOnlyProducts.AnalysisResult ~ 
             scale(millingHeatAvg)# + millingTime # + millingSpeedAvg
             + scale(drillingHeatAvg)# + drillingTime  #+ drillingSpeedAvg 
             #+ analysisTime + productionTime 
           , data=aData)
           #na.action="na.omit", CV=TRUE)
#print(fit) # show results
# Get results from fit
c1 <- fit$scaling[1]
c2 <- fit$scaling[2]

f1 <- function(aData) {
  return (c1 * aData$millingHeatAvg + c2*aData$drillingHeatAvg)
}
aData$lda <- scale(f1(aData))

g <- ggplot(aData, aes(lda)) + geom_bar() +
  facet_grid(dataOnlyProducts.AnalysisResult ~ .) +
  xlab("F1") + 
  ggtitle(paste0("Discriminant analysis Milling/Drilling Heat Avg, MatGrp=", aData[1, 3]))

openImg("discriminantDrillingMillingHeat.png")
print(g)
closeImg()


getResultRatio <- function(productAnalysisResult) {
  ratio <- length(productAnalysisResult[productAnalysisResult=="OK"])/length(productAnalysisResult)
  return (ratio)
}


# Einfache Analysen
analysisResultNumbers <- aggregate(AnalysisResult ~ MaterialNo, dataOnlyProducts, getResultRatio)
n <- aggregate(ID ~ MaterialNo, dataOnlyProducts, length)[,2]

easyAnalysis <- data.frame(analysisResultNumbers, n)


# Boxplot daten: Verteilung von OK/NOK Daten in Abhängigkeit Material
plotAndSaveBoxplot <- function(varName, title, fName) {
  data <- subset(dataProducts, Station==varName)
  g <- ggplot(data, aes(relevel(factor(AnalysisResult), "OK"), Value)) + 
    geom_boxplot() +
    facet_grid(. ~ MaterialNo) +
    ggtitle(paste0(title, ", n=", length(unique(data$ID)))) + xlab("AnalysisResult")
  openImg(fName)
  print(g)
  closeImg()
  
  return (g)
}


# Zeigt vielleicht was? Hitze für NOK meistens etwas höher
#dataMillingHeat <- subset(dataProducts, Station=="Milling Heat")
plotAndSaveBoxplot("Milling Heat", "MillingHeat distribution by MaterialNo", "compareMillingHeatByMatNo.png")

# Das hier zeigt nichts
dataMillingSpeed <- subset(dataProducts, Station=="Milling Speed")
ggplot(dataMillingSpeed, aes(factor(AnalysisResult), Value)) + geom_boxplot() + facet_grid(. ~ materialGroup)

# So wie milling heat
#dataDrillingHeat <- subset(dataProducts, Station=="Drilling Heat")
#ggplot(dataDrillingHeat, aes(factor(AnalysisResult), Value)) + geom_boxplot() + facet_grid(. ~ MaterialNo)
plotAndSaveBoxplot("Drilling Heat", "DrillingHeat distribution by MaterialNo", "compareDrillingHeatByMatNo.png")


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


# Anzahl Produkte in Abhängigkeit vom CustomerNo
dataByCustomer <- aggregate(AnalysisResult ~ CustomerNo, dataOnlyProducts, getResultRatio)
dataByCustomer$N <- aggregate(ID ~ CustomerNo, dataOnlyProducts, length)[, 2]
names(dataByCustomer) <- c("CustomerNo", "AnalysisResultRatio", "N")

g1 <- ggplot(dataByCustomer, aes(factor(CustomerNo), N)) + 
  geom_bar(stat="identity") +
  xlab("CustomerNo") + ggtitle("N by CustomerNo")
g2 <- ggplot(dataByCustomer, aes(factor(CustomerNo), AnalysisResultRatio)) + 
  geom_bar(stat="identity") +
  xlab("CustomerNo") + ggtitle("AnalysisResultRatio by CustomerNo")

openImg("compareNAnalysisResultByCustomerNo.png")
multiplot(g1, g2, cols=2)
closeImg()

# ANALYSE: Clusteranalyse
dataMillingHeatAvg <- aggregate(Value ~ ID, dataProductsMillingHeat, mean)[, 2]
dataDrillingHeatAvg <- aggregate(Value ~ ID, dataProductsDrillingHeat, mean)[, 2]

clusterData = data.frame(dataMillingHeatAvg, dataDrillingHeatAvg)
cl <- kmeans(clusterData, 2)
clusterData$cluster <- cl$cluster
g <- ggplot(clusterData, aes(x=dataMillingHeatAvg, y=dataDrillingHeatAvg, colour=cluster)) + 
  geom_point() +
  xlab("Milling Heat Avg") + ylab("Drilling Heat Avg") +
  ggtitle("Cluster by Drilling/Milling Heat Avg") +
  theme(legend.position="none")

openImg("clusterDrillingMillingHeat.png")
print(g)
closeImg()

#plot(clusterData, col = cl$cluster)
#text(clusterData, labels=dataMillingHeatAvg[, 1], col=dataMillingHeatAvg[, 1])

#library(cluster) 
#clusplot(clusterData, cl$cluster, color=TRUE, shade=TRUE, 
#         labels=2, lines=0)


# Analyse einzelnes Produkt (NICHT BENUTZT)
productOK <- subset(dataProducts, AnalysisResult=="OK" & ID==3)
productNOK <- subset(dataProducts, AnalysisResult=="NOK" & MaterialNo==7432 & ID==52)

ggplot(subset(dataProducts,Station=="Drilling Heat" & (ID==3 | ID==52)), 
       aes(x=-Timestamp, y=Value)) + 
  geom_line() +
  geom_point() + 
  facet_grid(. ~ AnalysisResult, scales = "free")



# Milling Speed und Heat in Abhängigkeit von Zeit
# Beide A
# 11 OK, 1 NOK, beide von MaterialNo 7423
# 247 NOK, 23 OK, beide von MaterialNo 4248
visualizeSpeedHeat <- function(ggValues, IDPar, tMin) {
  varName <- ggValues$varName
  xLimits <- ggValues$x
  yLimits <- ggValues$y
  
  data <- subset(dataProducts, 
                 (Station==varName) & ID==IDPar)[c("Timestamp", "Value", "Station")]
  data$Timestamp <- data$Timestamp - tMin #min(data$Timestamp)
  dataProduct <- subset(dataProducts, ID==IDPar)
  g <- ggplot(data, aes(x=Timestamp, y=Value)) + 
    geom_line() + 
    geom_point() + 
    geom_text(aes(label=floor(Timestamp/1000)), hjust=1.5, vjust=-0.3) + 
    #facet_grid(Station ~ ., scale = "free_y") +
    ggtitle(paste0("ID=", IDPar, 
                   ", AnalysisResult=", dataProduct$AnalysisResult[1], "\n",
                   "MNo=", dataProduct$MaterialNo[1],
                   ", MGroup=", dataProduct$materialGroup[1])) +
    getGGTheme() + 
    ylab(varName) + 
    scale_x_continuous(limits=xLimits) +
    scale_y_continuous(limits=yLimits, expand=c(0.15, 0))
  return (g)
}

# ggValues
ggMillingHeat <- data.frame(varName="Milling Heat",
                            x=c(0, 30000), 
                            y=c(0, 250)) 
ggMillingSpeed <- data.frame(varName="Milling Speed",
                             x=ggMillingHeat$x,
                             y=c(0, 17000)) 
ggDrillingHeat <- data.frame(varName="Drilling Heat", 
                             x=c(0, 25000), 
                             y=c(100, 350))
ggDrillingSpeed <- data.frame(varName="Drilling Speed",
                              x=ggDrillingHeat$x,
                              y=c(0, 20000))

plotAndSaveSpeedHeat <- function(IDPar1, IDPar2, fName1, fName2) {
  # Products from different MatGrps, both OK
  # Milling
  tMin <- min(subset(dataProducts, Station=="Milling Speed" & ID==IDPar1)$Timestamp)
  g1 <- visualizeSpeedHeat(ggMillingHeat, IDPar1, tMin)
  g2 <- visualizeSpeedHeat(ggMillingSpeed, IDPar1, tMin)
  tMin <- min(subset(dataProducts, Station=="Milling Speed" & ID==IDPar2)$Timestamp)
  g3 <- visualizeSpeedHeat(ggMillingHeat, IDPar2, tMin)
  g4 <- visualizeSpeedHeat(ggMillingSpeed, IDPar2, tMin)
  openImg(fName1)
  multiplot(g1, g2, g3, g4, cols=2)
  closeImg()
  
  # Drilling
  tMin <- min(subset(dataProducts, Station=="Drilling Speed" & ID==IDPar1)$Timestamp)
  g1 <- visualizeSpeedHeat(ggDrillingHeat, IDPar1, tMin)
  g2 <- visualizeSpeedHeat(ggDrillingSpeed, IDPar1, tMin)
  tMin <- min(subset(dataProducts, Station=="Drilling Speed" & ID==IDPar2)$Timestamp)
  g3 <- visualizeSpeedHeat(ggDrillingHeat, IDPar2, tMin)
  g4 <- visualizeSpeedHeat(ggDrillingSpeed, IDPar2, tMin)
  openImg(fName2)
  multiplot(g1, g2, g3, g4, cols=2)
  closeImg()
}

# Different matGrps
plotAndSaveSpeedHeat(70, 4, 
                "compareProductMillingByDiffMatGrp.png", 
                "compareProductDrillingByDiffMatGrp.png")
# Products from same MatNo, different result,11 OK, 1 NOK, beide von MaterialNo 7423
plotAndSaveSpeedHeat(11, 1, 
                "compareProductMillingBySameMatGrp.png", 
                "compareProductDrillingBySameMatGrp.png")



# VISUALIZE THE BIG DATA

#productSubset <- subset(dataProduct, Station=="Milling Heat")
#print(ggplot(data=productSubset, aes(x=Timestamp, y=Value)) + geom_line())






multiplot <- function(..., plotlist=NULL, file, cols=1, layout=NULL) {
  library(grid)
  
  # Make a list from the ... arguments and plotlist
  plots <- c(list(...), plotlist)
  
  numPlots = length(plots)
  
  # If layout is NULL, then use 'cols' to determine layout
  if (is.null(layout)) {
    # Make the panel
    # ncol: Number of columns of plots
    # nrow: Number of rows needed, calculated from # of cols
    layout <- matrix(seq(1, cols * ceiling(numPlots/cols)),
                     ncol = cols, nrow = ceiling(numPlots/cols))
  }
  
  if (numPlots==1) {
    print(plots[[1]])
    
  } else {
    # Set up the page
    grid.newpage()
    pushViewport(viewport(layout = grid.layout(nrow(layout), ncol(layout))))
    
    # Make each plot, in the correct location
    for (i in 1:numPlots) {
      # Get the i,j matrix positions of the regions that contain this subplot
      matchidx <- as.data.frame(which(layout == i, arr.ind = TRUE))
      
      print(plots[[i]], vp = viewport(layout.pos.row = matchidx$row,
                                      layout.pos.col = matchidx$col))
    }
  }
}



