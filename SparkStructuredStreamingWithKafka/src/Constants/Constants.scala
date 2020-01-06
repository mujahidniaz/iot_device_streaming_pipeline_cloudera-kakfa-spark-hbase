package Constants

/*
 * A class containing all the constants used in this project
 */
class Constants extends java.io.Serializable {
final val APP_NAME:String="Spark Structured Streaming With Kafka & HBase"
final val HOST_NAME:String="quickstart.cloudera" // for spark-submit
//final val HOST_NAME:String="192.168.0.105" // for IDE
final val KAFKA_TOPIC_NAME:String="IOT_DATA_TOPIC"
final val KAFKA_PORT:String="9092"
final val ZOOKEEPER_PORT:String="2181"
final val HBASE_TABLE_NAME:String="IOT_Data_Points";
//final val SPARK_MASTER:String="local[*]" // for Local / IDE
final val SPARK_MASTER:String="yarn-client" // for Spark-Submit on yarn

}