REM starting simulation...

cd "I4Simulation"
java -Dlog4j.configuration=file:classes/log4j.properties -jar DataAggregator.jar -d 20000 -o "../data
PAUSE