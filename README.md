# kronos
Repository for the Summer School in Canada project (6th semester)

# Table of contents

# Directories
* __/data/__ Stores partly aggregated event data for a finished 
  product in a SQLite database
* __/http/__ Directory used to statically serve files via an http server
* __/lib/__ Stores .jar files for libraries not in Maven
* __/log/__ Stores .log files that are logged via Log4J
* __/R/__ Data analysis and graph generation
* __/res/__ Stores resource files
* __/sim/__ Directory for the simulation .jar, documentation and batch file to start
the simulation
* __/src/__ Java files
* __/target/__ Compiled Java files

# Java Project
The Java project is divided into 6 parts:
* [Collect data](#collect)
* [Create objects] (#create) 
* [Product state] (#product)
* [WebSocket server] (#ws)
* [Database] (#db)
* [HTTP server] (#http)

The `Main` class starts the simulation and a `ConnectionHandler` to collect the data from the event stream as well as the HTTP and the WebSocket server.

## <a name="collect">Collect data</a>
To collect data from the simulation, the `ConnectionHandler` starts 3 listeners that run in different threads. Two of them are `MessageListeners` which use a `MessageConsumer` to read ERP and OPC data from the event stream. The third one (`SAReader`) uses a FileWatcher that gets notified when a new file is created with the spectral analysis data. All 3 listeners are Observable and give the resulting XML/JSON String to the Observer.
The Observer is a `MessageHandler`, which writes the events into a queue, to be processed further.

## <a name="create">Create objects</a>
The queue is processed by a `MessageWorker`, which is also a Thread and constantly looks in the queue for new messages. After getting a message, the worker reads the type of the message and calls a factory which converts the data into a Java object depending on the type (ERP, OPC, SA).

## <a name="product">Product state</a>
After the objects are created, the `MessageWorker` passes them to the `ProductHandler`. If the object is a ERP data a new `Product` object is created. Each `Product` contains a [Finite State Machine](#fsm) which observes the current state of the product. If the object given to the `ProductHandler` is an `OPCDataItem` or a `SAData` the `ProductHandler` loops over every active `Product` to check if the event can be assigned to the product. This is evaluated with the current state of the `Product` and the trigger which is connected to the object.

After the event is assigned to a `Product` it is given to the WebSocket server and the database.

## <a name="ws">WebSocket server</a>
The WebSocket server recieves each new event, creates a `MessageObject`, which is then converted to JSON. This string is sent to each client connected to the server.

## <a name="db">Database</a>
After a product is finished (after the spectral analysis), the data contained in each product is stored in an SQLite database.

## <a name="http">HTTP server</a>
The HTTP server takes aggregated historical data out of the database and exposes this data in a REST API. The following calls are available:

* `/data/getLastProducts`: Gets aggregated data about the last 25 products
* `/data/getDataByAnalysisResult`: Gets data grouped by spectral analysis result
* `/data/getDataByMat`: Gets data grouped by material number
* `/data/getDataByMatGrp`: Gets data grouped by material group (see [Analysis results](#analysisResults))



# <a name="analysisResults">Analysis results</a>
Data was saved in a SQLite database. It was then analyzed and visualized with R. We analyzed 
three different variables containing information about a product: The customer (`CustomerNo`),
the material type (`MaterialNo`) and the result of the spectral analysis (`AnalysisResult`) 
carried out at the end of the production line.

* There are 8 customers
* There are 12 types of materials
* The result of the spectral analysis can be `OK` or `Not OK`

## Customer
First, we analyzed information about products aggregating by customers. The following graphs
shows that each customer orders a similar number of products and that the ratio of `OK` to `NOK`
products is alike. 

As such the spectral analysis result is __not__ dependant on the customer.
![NAnalysisResultByCustomerNo](pictures/compareNAnalysisResultByCustomerNo.png)

## Material type
Next, we analyzed product data grouping by the type of material (`MaterialNo`) used. There are 
12 different types. Here we start to see several differences in the data depending on the type of
material used. 

### Value distributions of Drilling and Milling Heat
First, we aggregated data from the `Milling` and `Drilling` processes by calculating the average
Milling and Drilling Heat per product. After this we grouped the data by material type. The following 
graphs show the value distributions of the averages per material type:

These graphs show that it __might__ be possible to split the 12 material types into 2 material groups, 
each consisting of 6 types.

##### Milling Heat
![MillingHeatByMatNo](pictures/compareMillingHeatByMatNo.png)

##### Drilling Heat
![DrillingHeatByMatNo](pictures/compareDrillingHeatByMatNo.png)


### Cluster analysis to further show 2 groups of material types
The scatter plot shows two clusters of average Milling and Drilling Heat, giving
further evidence of 2 groups of material types.
![ClusterMillingDrillingHeatAvg](pictures/clusterDrillingMillingHeat.png)



### Spectral analysis result by material type
Next, we aggregated the result of the spectral analysis by the material type. The number
of products produced per material types seems to be insignificant. Assuming two different 
groups of material types, however, leads to evidence of __worse__ analysis results for the second 
group of material types.
![NAnalysisResultByMaterialNo](pictures/compareNAnalysisResultByMaterialNo.png)



### Milling and Drilling processes
To confirm our assumption of two different material groups we looked further into the `Milling` 
and `Drilling` processes. These show the following (per product):

* 6 values measured for `Heat`
* 3 values measured for `Drilling`
* The 2nd group of material types show higher `Speed` and `Heat` values, for `Milling` as
well as `Drilling`
* The 2nd group of material types show __longer__ processes

The following graphs show two exemplary products, each in a different material group:

##### Milling process
![MillingByDiffMatGrp](pictures/compareProductMillingByDiffMatGrp.png)
##### Drilling process
![DrillingByDiffMatGrp](pictures/compareProductDrillingByDiffMatGrp.png)


## Spectral analysis result
Lastly, we tried to predict the result of the spectral analysis at the end of the production
line by analyzing `Milling` and `Drilling` processes. First, we compare two products with the
same material type, one of which is `OK` while the other is `Not OK`. 

The following graphs show that the spectral analysis result is __not__ dependant on the values
measured during the processes, because both products show very similar values.

##### Milling process
![MillingBySameMatGrp](pictures/compareProductMillingBySameMatGrp.png)
##### Drilling process
![DrillingBySameMatGrp](pictures/compareProductDrillingBySameMatGrp.png)

### Discriminant analysis
To confirm the result above we tried using a discriminant analysis. We generated one function
by taking into account the standardized average of Milling and Drilling Heat values per product.
The following graph shows the result of filtering by a single material type.

As such we could __not__ predict the spectral analysis result by using a discriminant analysis,
because the distribution for `OK` and `Not OK` products is very similar for the function.
![DiscriminantMillingDrillingHeatAvg](pictures/discriminantDrillingMillingHeat.png)


## Conclusion
Taking into account the analysis above, we were able to make the following assumptions:

* The type of material used and the result of the spectral analysis is __not__ dependant on 
the customer 
* The 12 material types are split into 2 material groups
* Each material group shows different `Milling` and `Drilling` processes
* Each material group shows different ratios of the spectral analysis
* The spectral analysis result is not dependant on heat values from the two processes



# Sample sim data
m_orders
```
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<erpData>
    <customerNumber>4716</customerNumber>
    <materialNumber>9823</materialNumber>
    <orderNumber>f747ec21-1928-436e-b2f3-504ca33c551f</orderNumber>
    <timeStamp>2015-07-15T13:33:56.665-04:00</timeStamp>
</erpData>
```

m_opcitems
```
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<OpcDataItem>
    <itemName>Lichtschranke 1</itemName>
    <status>GOOD</status>
    <timestamp>1436981636743</timestamp>
    <value xsi:type="xs:boolean" xmlns:xs="http://www.w3.org/2001/XMLSchema" 
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">false</value>
</OpcDataItem>
```

JSON file
```
{
	"em1":82.84782409018561,
	"em2":92.55025258144875,
	"a1":90.68883374006916,
	"a2":38.089310170829044,
	"b2":3525.1752769633727,
	"b1":7513.372544641339,
	"overallStatus":"NOK",
	"ts_start":1436978781258,
	"ts_stop":1436978792262
}
```

# <a name="fsm">Finite State Machine</a>

To track the current position of a product a Finite State machine is used. 

The finite State machine has following states:
- INIT
- LIGHTBARRIER_1,
- BETWEEN_L1_L2,
- LIGHTBARRIER_2,
- BETWEEN_L2_L3,
- MILLING_STATION,
- BETWEEN_L3_L4,
- DRILLING_STATION,
- BETWEEN_L4_L5,
- LIGHTBARRIER_5,
- END_OF_PRODUCTION,
- SPECTRAL_ANALYSIS,
- FINISH

with following triggers:
- LIGHTBARRIER_1_INTERRUPT
- LIGHTBARRIER_1_CONNECT
- LIGHTBARRIER_2_INTERRUPT
- LIGHTBARRIER_2_CONNECT
- LIGHTBARRIER_3_INTERRUPT
- MILLING_STATION
- LIGHTBARRIER_3_CONNECT
- LIGHTBARRIER_4_INTERRUPT
- DRILLING_STATION
- LIGHTBARRIER_4_CONNECT
- LIGHTBARRIER_5_INTERRUPT
- LIGHTBARRIER_5_CONNECT
- SPECTRAL_ANALYSIS

The product takes following path:
![UML Model](pictures/FiniteStateMachineUML.png)
