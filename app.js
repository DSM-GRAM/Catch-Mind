var app = require('express')();
var server = require('http').createServer(app);
var io = require('socket.io')(server);

var words = new array('병신', '시발', '흑흑', '흑우', '마더리스')
var count = 0

io.on('connection', function(socket) {
  console.log("user connect : " + socket.id);

  socket.on('ready', function() {
    count++;
    socket.main = count;
    socket.join(socket.main);
  });

  if(count === 2) {
    io.socket.in(1).emit('all ready', 1);
    io.socket.in(0).emit('all ready', 0);
    socket.emit('connect', randomitem(words)); // App1
  }

  socket.on('color', function(color, weight){ // App1
    sicket.emit('color', color, weight); // App2
  });

  socket.on('correct', function() { // App2
    console.log('Correct');
    socket.emit('correct', 'correct'); // App1
  });

  socket.on('EVENT_SENDER', function(x, y, event){ // App1
    socket.emit('EVENT_Receiver', x, y, event) //App2
  });

  socket.on('disconnect', function() {
    console.log('user disconnected : ' + socket.id)
    count = 0;
  })
});

//서버를 시작한다. 
server.listen(7000, function(){
  console.log("server on 7000");
});