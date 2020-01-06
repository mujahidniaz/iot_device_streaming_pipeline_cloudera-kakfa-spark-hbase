const express = require('express');
var kafka = require('kafka-node')
const server = express();
const body_parser = require('body-parser');
const port = 4000;
var TOPICS=["IOT_DATA_TOPIC"];
var KAFKA_HOST="192.168.1.162";
var KAFKA_PORT="9092";
// parse JSON (application/json content-type)
server.use(body_parser.json());

server.post("/api/v1/kafka_post_endpoint", (req, res) => {
   let item = req.body;
   
   let item2=item;
   item2.data.jsonstring=JSON.stringify(item);
   console.log(JSON.stringify(item2)+"\n");

   //let a ="{'device'}"
 
   try
   {   //  console.log(JSON.stringify(item)+"\n");
       produceMessage(JSON.stringify(item2),TOPICS[0]);
   }
   catch(exr)
   {
      console.log(exr);
   }

   // add new item to array
   

   let response = {
      status: "success"
    }
    res.json(response);
});



var Producer = kafka.Producer
// instantiate client with as connectstring host:port for  the ZooKeeper for the Kafka cluster
const client = new kafka.KafkaClient({kafkaHost: KAFKA_HOST+":"+KAFKA_PORT});
 
// name of the topic to produce to

 
    KeyedMessage = kafka.KeyedMessage,
    producer = new Producer(client),
    km = new KeyedMessage('key', 'message'),
    ProducerReady = false ;
 
producer.on('ready', function () {
    console.log("Kafka Post API endpoint is ready");
    ProducerReady = true;
});
  
producer.on('error', function (err) {
  console.error("Problem with producing Kafka message "+err);
})
 
 
function produceMessage(msg,c_topic) {
    KeyedMessage = kafka.KeyedMessage,
    payloads = [
        { topic: c_topic, messages:msg, partition: 0 }
    ];
    
      
    if (ProducerReady) {
      producer.send(payloads, function (err, data) {
      console.log(data);
      });
    } else {
        // the exception handling can be improved, for example schedule this message to be tried again later on
        console.error("sorry, Kafka post API endpoint is not ready yet, failed to produce message to Kafka.");
    }
   
 
}//produceMessage


server.listen(port, () => {
   console.log(`Server listening at ${port}`);
});