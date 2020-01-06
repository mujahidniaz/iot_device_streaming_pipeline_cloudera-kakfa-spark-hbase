var express = require('express') //npm install express
  , bodyParser = require('body-parser') // npm install body-parser
  , fs = require('fs')
  , http = require('http');
  const port = 9898;


var client = require('node-impala').createClient({"host": '192.168.1.162'});
const server = express()
server.use(bodyParser.json());
server.listen(port, () => {
  console.log(`Server listening at ${port}`);
});



 
client.connect({
  host: '192.168.1.162',
  port: 21000,
  resultType: 'json-array'
});

server.get("/", (req, res) => {
  res.sendFile(__dirname + '/index.html');
});
server.post("/query", (req, res) => {
  console.log(req.body.query);
  client.query(req.body.query)
  .then(result => res.send(result))
  .catch(err => res.send(err))
   
  //res.send("hello");
});
 