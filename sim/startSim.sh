#!/bin/sh
echo "starting simulation"

cd "sim"
cd "I4Simulation"
java -Dlog4j.configuration=file:classes/log4j.properties -jar DataAggregator.jar -d 5000 -o "../data" -q
read -n1 -r -p "Press any key to continue..." key