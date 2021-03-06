# Table of contents
* [Analysis results](#tocAnalysisResults)
* [Java project](#tocJavaProject)
* [Finite state machine](#tocFSM)
* [Database](#tocDB)
* [Visualization](#tocVisualization)
* [Directories](#tocDirectories)


# <a name="tocAnalysisResults">Analysis results</a>
Data was saved in a [SQLite database](#tocDB). It was then analyzed and visualized with R. We analyzed 
three different variables containing information about a product: The customer (`CustomerNo`),
the material type (`MaterialNo`) and the result of the spectral analysis (`AnalysisResult`) 
carried out at the end of the production line.

* There are 8 customers
* There are 12 types of materials
* The result of the spectral analysis can be `OK` or `Not OK`

### Customer
First, we analyzed information about products aggregating by customers. The following graphs
shows that each customer orders a similar number of products and that the ratio of `OK` to `NOK`
products is alike. 

As such the spectral analysis result is __not__ dependant on the customer.
![NAnalysisResultByCustomerNo](pictures/compareNAnalysisResultByCustomerNo.png)

### Material type
Next, we analyzed product data grouping by the type of material (`MaterialNo`) used. There are 
12 different types. Here we start to see several differences in the data depending on the type of
material used. 

#### <a name="analysisResultHeatDistribution">Value distributions of Drilling and Milling Heat</a>
First, we aggregated data from the `Milling` and `Drilling` processes by calculating the average
Milling and Drilling Heat per product. After this we grouped the data by material type. The following 
graphs show the value distributions of the averages per material type:

These graphs show that it __might__ be possible to split the 12 material types into 2 material groups, 
each consisting of 6 types.

##### Milling Heat
![MillingHeatByMatNo](pictures/compareMillingHeatByMatNo.png)

##### Drilling Heat
![DrillingHeatByMatNo](pictures/compareDrillingHeatByMatNo.png)


#### Cluster analysis to further show 2 groups of material types
The scatter plot shows two clusters of average Milling and Drilling Heat, giving
further evidence of 2 groups of material types.
![ClusterMillingDrillingHeatAvg](pictures/clusterDrillingMillingHeat.png)



#### Spectral analysis result by material type
Next, we aggregated the result of the spectral analysis by the material type. The number
of products produced per material types seems to be insignificant. Assuming two different 
groups of material types, however, leads to evidence of __worse__ analysis results for the second 
group of material types.
![NAnalysisResultByMaterialNo](pictures/compareNAnalysisResultByMaterialNo.png)



#### Milling and Drilling processes
To confirm our assumption of two different material groups we looked further into the `Milling` 
and `Drilling` processes. These show the following (per product):

* 6 values measured for `Heat`
* 3 values measured for `Speed`
* Each material group shows equal `Speed` values depending on the process
* The 2nd material group shows higher `Speed` and `Heat` values, for `Milling` as
well as `Drilling`
* The 2nd material group shows __longer__ processes

The following graphs show two exemplary products, each in a different material group:

##### Milling process
![MillingByDiffMatGrp](pictures/compareProductMillingByDiffMatGrp.png)
##### Drilling process
![DrillingByDiffMatGrp](pictures/compareProductDrillingByDiffMatGrp.png)


#### Spectral analysis result
Lastly, we tried to predict the result of the spectral analysis at the end of the production line by analyzing `Milling` and `Drilling` processes. Because the `Speed` values are equal depending on material group and process only the `Heat` values can be significant. As such we compare two products with the same material type, one of which is `OK` while the other is `Not OK`. 

The following graphs show that the spectral analysis result is __not__ dependant on the values measured during the processes, because both products show very similar values. Another argument to support this is given by the [distribution of `Heat` values](analysisResultHeatDistribution), which shows that the distribution of `OK` and `Not OK` is very similar. 

##### Milling process
![MillingBySameMatGrp](pictures/compareProductMillingBySameMatGrp.png)
##### Drilling process
![DrillingBySameMatGrp](pictures/compareProductDrillingBySameMatGrp.png)

#### Discriminant analysis
To confirm the result above we tried using a discriminant analysis. We generated one function
by taking into account the standardized average of Milling and Drilling Heat values per product.
The following graph shows the result of filtering by a single material type.

As such we could __not__ predict the spectral analysis result by using a discriminant analysis,
because the distribution for `OK` and `Not OK` products is very similar for the function.
![DiscriminantMillingDrillingHeatAvg](pictures/discriminantDrillingMillingHeat.png)


### Conclusion
Taking into account the analysis above, we were able to make the following assumptions:

* The type of material used and the result of the spectral analysis is __not__ dependant on 
the customer 
* The 12 material types are split into 2 material groups
* Each material group shows different `Milling` and `Drilling` processes
* Each material group shows different ratios of the spectral analysis
* Each material group and processes show equal `Speed` values
* The spectral analysis result is not dependant on `Heat` values from the two processes



# <a name="tocJavaProject">Java project</a>
The Java project is divided into 6 parts:
* [Collect data](#collect)
* [Create objects] (#create) 
* [Product state] (#product)
* [WebSocket server] (#ws)
* [Database] (#db)
* [HTTP server] (#http)

The `Main` class starts the simulation and a `ConnectionHandler` to collect the data from the event stream as well as the HTTP and the WebSocket server.

![UML Diagramm mit Gruppierung](pictures/Kronos_Overall_UML_Groups.png)

### <a name="collect">Collect data</a>
To collect data from the simulation, the `ConnectionHandler` starts 3 listeners that run in different threads. Two of them are `MessageListeners` which use a `MessageConsumer` to read ERP and OPC data from the event stream. The third one (`SAReader`) uses a FileWatcher that gets notified when a new file is created with the spectral analysis data. All 3 listeners are Observable and give the resulting XML/JSON String to the Observer.
The Observer is a `MessageHandler`, which writes the events into a queue, to be processed further.

### <a name="create">Create objects</a>
The queue is processed by a `MessageWorker`, which is also a Thread and constantly looks in the queue for new messages. After getting a message, the worker reads the type of the message and calls a factory which converts the data into a Java object depending on the type (ERP, OPC, SA).

### <a name="product">Product state</a>
After the objects are created, the `MessageWorker` passes them to the `ProductHandler`. If the object is a ERP data a new `Product` object is created. Each `Product` contains a [Finite State Machine](#tocFSM) which observes the current state of the product. If the object given to the `ProductHandler` is an `OPCDataItem` or a `SAData` the `ProductHandler` loops over every active `Product` to check if the event can be assigned to the product. This is evaluated with the current state of the `Product` and the trigger which is connected to the object.

After the event is assigned to a `Product` it is given to the WebSocket server and the database.

### <a name="ws">WebSocket server</a>
The WebSocket server is an observer of the `ProductHandler`. As such it receives all new events generated by the simulation. It creates a `MessageObject` which is then converted to JSON. The JSON is converted to a string and sent to each client connected to the server.

### <a name="db">Database</a>
After a product is finished (after the spectral analysis) the data contained in each product is stored in an [SQLite database](#tocDB).

### <a name="http">HTTP server</a>
The HTTP server takes aggregated historical data out of the database and exposes this data in a REST API. The following calls are available:

* `/data/getLastProducts`: Gets aggregated data about the last 25 products
* `/data/getDataByAnalysisResult`: Gets data grouped by spectral analysis result
* `/data/getDataByMat`: Gets data grouped by material number
* `/data/getDataByMatGrp`: Gets data grouped by material group (see [Analysis results](#tocAnalysisResults))




# <a name="tocFSM">Finite state machine</a>
To track the current position of a product a Finite State machine is used. In the simulation products are manufactured on a production line. First, ERP data is received from the simulation. Next the production starts. `Lightbarrier 1` and `Lightbarrier 2` are used to track the position of the product at the beginning of the production line. `Lightbarrier 3` is combined with the `Milling Station`. If `Lightbarrier 3` is interrupted the product arrived at the milling station. The signifies that the first process, __Milling__, has started.
 
After the __Milling__ process has finished the product is sent to the `Drilling station`, where `Lightbarrier 4` is used to track the position. Here the second process, __Drilling__, has started. After this process has finished the product continues to `Lightbarrier 5`, which means the product is finished and removed from the production line. Lastly, the `Spectral Analysis` checks the status of the product. The status of the product can be `OK` or `Not OK`.

To represent a product on the production line a finite state machine is used. Each product takes the linear path in the following UML diagram: 
![UML Model](pictures/FiniteStateMachineUML.png)

The finite state machine consists of the states: 
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

It uses these triggers to transfer between states:
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


# <a name="tocDB">Database</a>
The sqlite database stores the historical data processed by Kronos.
The database is structured as follows:
![Alt text](/doc/Ind4ERDSpacing.png?raw=true "ER-Diagramm SQLite-Datenbank")
The table Product stores an entry for each product which production has been finished. It stores the data sent by the ERP and the spectral analysis data.
The measure table consists of an entry for each measure (OPCItem): milling speed, heat, time or drilling speed, heat and time.
In order to enable data analysis several views have been added calculating minima, maxima and averages grouping by different criteria such as the result of the spectral analysis or the material number.
![Alt text](/doc/Ind4ERD-Views.png?raw=true "ER-Diagramm SQLite-Datenbank")


#<a name="tocVisualization">Visualization</a>
Visualization using web technologies

To visualise our data and the running production process, a UI was built based on industry standard web technologies. The stack consists of:
- Bootstrap v3
- compass
- AngularJS
- flot
- d3 and c3
- jQuery
- fontAwesome
- modernizr
- raphael
- ng-websockets
- underscore 
- and several other libraries.

Further node, bower and grunt were used extensively for development and a simplified and optimized workflow.
For realtime data exchange a WebSocket connection was used to track the production process and view - e.g. - the temperatures, rpms and current location of the product on the production line.

For historical data analysis and visualisation, several RESTful-Endpoints were created and are presented with the help of morris.js.

Finally, for production use, the web frontend is heavily processed and optimized by Grunt and copied to the http directory.
For developmental use, grunt, node and liveReload were relied upon.

# <a name="tocDirectories">Directories</a>
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
