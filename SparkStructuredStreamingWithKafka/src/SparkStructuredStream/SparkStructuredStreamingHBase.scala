/*
 * This Class Implements Spark Structured Streaming with Kafka and calls HBase Foreach Writer to Write into HBase.
 */
package SparkStructuredStream
import scala.math.random
import org.apache.spark._
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._
import org.apache.spark.sql.SparkSession
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.kafka010.{ ConsumerStrategies, KafkaUtils, LocationStrategies }
import Constants.Constants
import HBase.IOTHBaseForeachWriter
class SparkStructuredStreamingHBase {
  
  
val Constants=new Constants()

  def StartSparkStructuredStreamingJob(): Unit = {
    val spark = SparkSession.builder()
      .master(Constants.SPARK_MASTER)
      .appName(Constants.APP_NAME)
      .getOrCreate()
    SparkSession.builder()
    import spark.implicits._

    val df = spark
      .readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", Constants.HOST_NAME+":"+Constants.KAFKA_PORT)
      .option("subscribe",Constants.KAFKA_TOPIC_NAME)
      .option("startingOffsets", "latest")
      .load()
    df.selectExpr("CAST(key AS STRING)", "CAST(value AS STRING)")
      .as[(String, String)]
    val kDf = df.selectExpr("CAST(value AS STRING)");
    val schema = new StructType()
      .add("data", new StructType()
        .add("deviceId", StringType)
        .add("time", StringType)
        .add("temperature", IntegerType)
        .add("location", (new StructType)
          .add("latitude", StringType)
          .add("longitude", StringType))
        .add("jsonstring", StringType))

    val IOT_DF = kDf.select(from_json(col("value"), schema).as("data"))
      .select("data.*")
    IOT_DF.withColumn("MyColumn", lit(34))
    IOT_DF.printSchema();
    val writer = new IOTHBaseForeachWriter();
    IOT_DF.createOrReplaceTempView("IOT")

    IOT_DF.sqlContext.sql("select data.deviceId, from_unixtime(data.time,'yyyy-MM-dd HH:mm:ssXXX') as time,data.temperature,data.location.latitude,data.location.longitude,data.jsonstring from IOT")
      .writeStream
     // .format("console")
      .foreach(writer)
      .outputMode(org.apache.spark.sql.streaming.OutputMode.Update())
      .start()

      .awaitTermination()
  }

}