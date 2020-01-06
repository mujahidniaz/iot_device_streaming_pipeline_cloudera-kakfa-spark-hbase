
import SparkStructuredStream.SparkStructuredStreamingHBase
object SparkApp {
  var sparkApp: SparkStructuredStreamingHBase = new SparkStructuredStreamingHBase()
  def main(args: Array[String]): Unit = {
    sparkApp.StartSparkStructuredStreamingJob() // Starts the Spark App
  }
}