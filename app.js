// socket 서버 열기 : 7000포트
var io = require('socket.io').listen(7000);

// 단어 배열 선언
var words = new Array('송진우', '돌맹이', '나뭇가지', '충전기', '노트북')

// 레디가 된 소켓의 수
var count = 0;

// 그리는 사람 지정
var peer;

// 단어 랜덤 선택
function randomitem(a) {
  var ritem = a[Math.floor(Math.random()*a.length)];
  console.log(ritem);
  return ritem;
}

// 소켓과 연결이 됐을 때
io.on('connection', function(socket) {
  console.log("user connect : " + socket.id);
  socket.emit('socketConnect', 'socketConnect : '+ socket.id);

  socket.on('ready', function() {
    count++;
    console.log(count);
  
    // 먼저 들어온 사람이 그리는 사람이 됨
    if(count === 1) {
        peer = socket;
    } 

    // 2번째로 온사람이 맞추는 사람이 됨과 동시에
    else if(count === 2){
        // 랜덤 단어 선택
        var randomwords = randomitem(words); 
        
        // 방 생성 및 입장
        peer.join('room');
        socket.join('room');

        // 각각의 소켓에게 0:(그림)과 1:(맞춤)로 전송 
        peer.emit('all ready', '0');
        socket.emit('all ready', '1');

        // 랜덤 단어 전송
        io.sockets.in('room').emit('onConnect', randomwords);  // room 안의 모든 소켓에 emit
        io.to(socket.id).emit('onConnect', randomwords);
        io.to(peer.id).emit('onConnect', randomwords);
      }
  });

  // 색상과 두께를 받고, 보내는 소켓을 제외한 방안의 모든 소켓에게 전송
  socket.on('color', function(color, weight){
    io.sockets.in('room').emit('color', color, weight); // room안의 '본인'을 제외한 모든 소켓에 emit
  });

  // 상대가 맞췄는지 확인
  socket.on('correct', function() {
    console.log('Correct');
    io.sockets.in('room').emit('correct');
  });

  // x, y, 이벤트 전송
  socket.on('EVENT_SENDER', function(x, y, event){
    console.log('event sender!');
    socket.broadcast.to('room').emit('EVENT_Receiver', x, y, event);
  });

  // 소켓의 연결이 해제 됨
  socket.on('disconnect', function(socket) {
    console.log('user disconnected : ' + socket.id);
    count = 0; // 하나라도 연결을 해제하면 0으로 초기화
    console.log(count);
  });
});