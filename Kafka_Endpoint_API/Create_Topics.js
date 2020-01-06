var kafka = require('kafka-node');
var Producer = kafka.Producer
console.log("here");
// instantiate client with as connectstring host:port for  the ZooKeeper for the Kafka cluster
const client = new kafka.KafkaClient({kafkaHost: "192.168.1.162:9092"});
 
    producer = new Producer(client);


   
 
var topicsToCreate = [
{
  topic: 'IOT_DATA_TOPIC',
  partitions: 1,
  replicationFactor: 1
}

];
 
client.createTopics(topicsToCreate, (error, result) => {
  // result is an array of any errors if a given topic could not be created
  console.log(result);
  console.log(error);
  client.close();
  
});

