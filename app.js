var app = require('express')();
var server = require('http').createServer(app);
var io = require('socket.io')(server);

var words = new Array('송진우', '돌맹이', '나뭇가지', '충전기', '노트북')
var queue = [];
var room;

function randomitem(a) {
  var sibre = a[Math.floor(Math.random()*a.length)];
  console.log(sibre);
  return sibre;
}

io.on('connection', function(socket) {
  console.log("user connect : " + socket.id);

  socket.on('ready', function() {
    findPeer(socket);
  });

  socket.on('color', function(color, weight){ // App1
    socket.emit('color', color, weight); // App2
  });

  socket.on('correct', function() { // App2
    console.log('Correct');
    socket.emit('correct'); // App1, App2
  });

  socket.on('EVENT_SENDER', function(x, y, event){ // App1
    socket.emit('EVENT_Receiver', x, y, event) //App2
  });

  socket.on('disconnect', function() {
    console.log('user disconnected : ' + socket.id)
    count = -1;
  });
});


var findPeer = function(socket) {
  console.log("queue.length : "+ queue.length);
  if (queue.length > 0) {
      var peer = queue.pop();
      randomwords = randomitem(words)
      if(peer == socket) {
          findPeerForLoneSocket(socket);
      }
      console.log("success");

      room = socket.id+'#'+peer.id;
      peer.join(room);
      socket.join(room);

      peer.emit('all ready', 0);
      socket.emit('all ready', 1);

      peer.emit('connect', randomwords);
      socket.emit('connect', randomwords);
      console.log("값 전달 완료")
  } else {
      console.log('push queue');
      queue.push(socket);
      console.log("queue" + queue);
  }
}

//서버를 시작한다. 
server.listen(7000, function(){
  console.log("server on 7000");
});