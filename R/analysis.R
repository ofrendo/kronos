# Connect to DB
#library("RSQLite")
#connection = dbConnect(drv="SQLite", dbname="C:/Users/D059373/git/kronos/data/historical.db")


library(sqldf)
db <- dbConnect(SQLite(), dbname="../data/historical.db")

sql <- "SELECT * FROM Product NATURAL JOIN Measure"

data <- dbGetQuery(conn = db, sql)