# IOT Device Streaming Data Pipelines (Kafka + Hadoop(Cloudera) + Spark Structured Streaming + HBase + Impala)

## Technologies:

•	Kafka 								•	HBase

•	ZooKeeper							•	Impala

•	Hadoop (Cloudera)						•	Java EE

•	Spark Structred streaming 					•	RestAPI

•	Nodejs                                      •   Scala



## Introduction:

This application deals with streaming data from multiple IOTs. Each IOT is sending data constantly with the frequency of 1 second. In this application I have build a streaming pipeline that robustly moves that streaming data into our Haodoop stystem after transformation done in real time
This has 4 different parts

- Data Generation
- Data Ingestion
- Transformation
- Storage / Loading in Hadoop Cluster
- Querying the data

Goal of this application is to develop a robust and efficient data pipeline that can be used to move data from our IOTs to the end system and then easy way to query it.


## Working:

### Data Generation:

Fist step of this pipelines is to generate data which basically is simulating the IOTs which will be sending data to the pipeline. For simulation I have created a java application. Wich takes Data from weather APIs (Due to limited number of API Calls allowed by the **Weather APIs** I have rather parsed the google search page for weather and coordinate info ). This applicaton takes name of the city, then takes coordinates data from **Google Search** and parses the coordiates and then every **30 minutes** it updates the weather info from Google. And keeps sending data every second the **POST API** in **JSON** format. You can run **Multiple Instances** of this application to simulate more IOTs, every instance of this application works as an Indipendant IOT. You can add up to 1000s of these instances.


### Data Ingestion:
Second step of this pipeline is to ingest IOTs data into the data pipeline as fast as we can so API end point is receiving multiple concurrent POST requests every second processing data before pushing it to pipelines at this stage would be a bad design decission so I pushed it to **Apache KAFKA**. Which can handle Millions of reads and writes per second so all the parallel API requests are basically doing nothing but pushing posted data to Apache Kafka. Apache Kafka can be used in a cluster with Zookeeper to utilize distributed evironment.

### Data Transformation:
Third and most important step is to transfrom the data while its coming before storing it to the the destination storage. This part is tricky because data is coming in the form of stream, Traditional approach will not be any of the use here because of the streaming. So **SPARK** is the best choice here because **SPARK Streaming** allows us to apply transformations and computations on streaming data very fast and efficient. There were two choices here either i could go for **SPARK Streaming** or **SPARK Structured Streaming**. Whats the difference ? Spark Streaming works on something we call a **micro batch**. The stream pipeline is registered with some operations and Spark polls the source after every batch duration (defined in the application) and then a batch is created of the received data, i.e. each incoming record belongs to a batch of DStream. Each batch represents an RDD. But in case of Structured Streaming, there is no batch concept. The received data in a trigger is appended to the continuously flowing data stream. Each row of the data stream is processed and the result is updated into the unbounded result table. How you want your result (updated, new result only, or all the results) depends on the mode of your operations (Complete, Update, Append)

### Data Loading:
Fourth step is to load the transformed data into some structured storage so that it can be queried, But writing a streaming data is tricky because due to contineous streams there will be a lot of transactions a lots of writes. In case of SQL Databases which acquire locks while writing. multiple writes will become really slow and is not practical. We need something which can handle that large amount of transactional rate without slowing down the whole process and can allow concurrent reads and writes. Apache **HBASE** is a **NO SQL** database which can handle these kind of situation very efficiently and the good thing is its compatible with SPARK. We can directly write streaming data from Spark to HBASE as soon as it comes and gets transformed. You have to write your own EACH ROW Writer for the HBASE to enable SPARK to communicate with HBASE. I used HBASE to store data in column oriented NO SQL database.

### Data Querying:

Fifth and last step of the pipeplines to acess the data from the storage to use it for decission making and other purposes. HBASE is great for storage but being no SQL Database. it does not allow you to have a SQL Based querying of data. But **HIVE + IMPALA** can be used to make that data available in SQL like syntax. To do that, you have to create HIVE table on top the HBASE table and update the metadata of IMPALA. And then you can query this HBASE table using both HIVE and IMPALA.

## Pre-Requisites:
- Cloudera Quickstart VM
- Cloudera CDH 5+ installed
- Kafka must be Installed on Cloudera Quickstart VM
- Spark2 must be installed on Cloudera Quickstart VM
- Java 1.8 + must be installed and configured on each node of your cluster

## Installation Steps:

- 	Start Quickstart VM check all the services running properly. Obtain IP Address of your VM.
-  	Replace the 'KAFKA_HOST' value with IP Address in Kafka_EndPoint_API->server.js & Kafka_EndPoint_API->Create_Topics.js
- 	Run the 'Run_Kafka_End_Point.bat' file. which will create the topic and will start the Rest API End point for Kafka.
- 	Run 3 instances of IOT_Simulator which will ask for location and you can provide any city name it will pick up the City Coordinates and Temprature on its own by crawling and parsing google search.
- 	create HBase table using HBase shell (type HBase Shell in cmd) to access Hbase shell.

`create 'IOT_Data_Points','deviceId','temperature','location','time','jsonstring'`

- 	Now create hive table using hive shell (Type Hive in cmd) to access Hive shell and create Hive table using following command.

`CREATE EXTERNAL TABLE hbase_iot_data_points (rowID STRING, deviceId STRING, latitude INT,longitude INT, temperature INT, time TIMESTAMP,jsonstring STRING) 
STORED BY 'org.apache.hadoop.hive.hbase.HBaseStorageHandler' 
WITH SERDEPROPERTIES ("hbase.columns.mapping" = ":key,deviceId:deviceId,location:latitude,location:longitude,temperature:temperature,time:time,jsonstring:jsonstring") 
TBLPROPERTIES("hbase.table.name" = "IOT_Data_Points");`

- 	Now update impala metadata so that you can query HBase table from impala using following command
		invalidate metadata hbase_iot_data_points;

- 	Create a jar file form the project 'SparkStructuredStreamingWithKafka' and copy it to some locatio on your VM using WinSCP.
- 	Run Spark2-Submit job using following command by providing necessary dependencies and packages.

`spark2-submit --packages org.apache.spark:spark-sql-kafka-0-10_2.11:2.4.4,org.apache.spark:spark-streaming-kafka-0-10_2.11:2.4.4,org.apache.kafka:kafka-clients:2.4.0,org.apache.hbase :hbase-client:1.2.0,org.apache.hbase:hbase-common:1.2.0,org.apache.hbase:hbase-protocol:1.2.0  --class SparkApp --master local IOT_SPARK_STREAMING_HBASE_JOB.jar`

-  Finally Run the Impala-Shell to query the HBase table.
- 	Alternatively you can run the 'Run_FrontEnd.bat' and the go to `http://localhost:9898` to use the front end web Interface for Habase and query data.


## Demo Video 
   Please go to watch a live working Demo https://www.youtube.com/watch?v=VZ84HRKnq4I on Youtube (Must)
