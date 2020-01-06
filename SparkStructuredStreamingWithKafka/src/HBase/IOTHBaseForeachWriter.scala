/*
 * This Class Contains the custom ForeachWriter for HBase.
 * Which Writes each row of Data Frame into HBase.
 */
package HBase
import org.apache.spark.sql.ForeachWriter
import org.apache.spark.sql.Row
import org.apache.hadoop.conf._
import org.apache.hadoop.hbase.util._
import org.apache.hadoop.hbase.TableName
import org.apache.hadoop.hbase.client._
import org.apache.hadoop.hbase.HBaseConfiguration
import Constants.Constants;
class IOTHBaseForeachWriter extends ForeachWriter[Row] {

  val Constants = new Constants();

  var hbaseConfig: Configuration = _
  var connection: Connection = _
  override def open(partitionId: Long, version: Long): Boolean =
    {
      hbaseConfig = HBaseConfiguration.create
      hbaseConfig.set("hbase.zookeeper.quorum", Constants.HOST_NAME)
      hbaseConfig.set("hbase.zookeeper.property.clientPort", Constants.ZOOKEEPER_PORT)
      connection = ConnectionFactory.createConnection(hbaseConfig)

      true
    }

  override def process(value: Row): Unit = {

    val table = connection.getTable(TableName.valueOf(Constants.HBASE_TABLE_NAME))

    val rowKey = Bytes.toBytes(
      value.getAs[String](fieldName = "time").toString() + ":" + value.getAs[String](fieldName = "deviceId").toString())

    val row = new Put(rowKey)
    row.addColumn(
      Bytes.toBytes("deviceId"),
      Bytes.toBytes("deviceId"),
      Bytes.toBytes(value.getAs[String](fieldName = "deviceId").toString()))
    row.addColumn(
      Bytes.toBytes("location"),
      Bytes.toBytes("latitude"),
      Bytes.toBytes(value.getAs[String](fieldName = "latitude").toString()))

    row.addColumn(
      Bytes.toBytes("location"),
      Bytes.toBytes("longitude"),
      Bytes.toBytes(value.getAs[String](fieldName = "longitude").toString()))
    row.addColumn(
      Bytes.toBytes("time"),
      Bytes.toBytes("time"),
      Bytes.toBytes(value.getAs[String](fieldName = "time").toString()))
    row.addColumn(
      Bytes.toBytes("temperature"),
      Bytes.toBytes("temperature"),
      Bytes.toBytes(value.getAs[Int](fieldName = "temperature").toString()))
    row.addColumn(
      Bytes.toBytes("jsonstring"),
      Bytes.toBytes("jsonstring"),
      Bytes.toBytes(value.getAs[String](fieldName = "jsonstring").toString()))

    table.put(row)
    println("process:" + value.mkString(", "))
  }

  override def close(errorOrNull: Throwable): Unit = {
    connection.close()
    println("close")
  }
}