//stomp 연결을 담당하는 js

import { UtilController } from '../Util.js';
const util=new UtilController();

//보내기 , 지우기에 대한 엔터, 클릭 이벤트 리스너를 설정
document.addEventListener("DOMContentLoaded", function(){
    document.querySelector('.text-box').addEventListener('keydown',function (event){
        if(event.keyCode== 13)
            send();
    })
    document.querySelector('#send-message-btn').addEventListener('click',function (){
        send();
    })
    document.querySelector('#clear-message-btn').addEventListener('click',function () {
        document.querySelector('.chat-text').value=null;
    })
});
//현재 대화중인 채팅방에 대한 객체
let roomInfo={
    roomId : 0,
    myUserId : "default",
    opponentUserId : "default"
}
// 다른 함수에 사용하기 위한 프로퍼티 ( sockJs 객체, STOMP 클라이언트)
let ws;
let sock;

//stomp를 connect, 구독을 설정하는 함수
export function connectStomp(info){
    console.log("connect 입장");
    //채팅방 정보를 roomInfo 객체에 저장
    roomInfo.roomId=info.roomId;
    roomInfo.opponentUserId=info.opponent;
    roomInfo.myUserId=document.querySelector('.hidden-userId').value;
    //이미 사용중인 sockJs 객체가 없을 경우
    if(sock===null || sock===undefined)
        // 백엔드에서 설정한 등록 엔드포인트 주소로 sockjs 객체 생성
        sock = new SockJS(`http://${location.host}/stomp/chat`);
    //가져온 sockJS 객체로 stomp 클라이언트 연결
    let client=Stomp.over(sock);
    ws=client;
    // 메시지 브로커에 연결
    client.connect("","",function (){
        console.log("연결");
        // 서버에서 보낸 메시지를 받기 위해 지정된 대상으로 구독을 등록
        client.subscribe("/receive/room/"+roomInfo.roomId+"/"+roomInfo.myUserId, function (event){
            let data = JSON.parse(event.body);
            let sendTime = getTime();
            //받아온 수신 메시지를 채팅방 화면에 보이게 updateView 함수 호출
            updateView(data.message,'opponent-ballon',roomInfo.opponentUserId,sendTime);
            //채팅방 화면에서 마지막 스크롤로 유지
            document.querySelector('.chat-box').scrollTop = document.querySelector('.chat-box').scrollHeight;
        });

    })
    console.log("connect 해체");
}
//자신이 메시지를 보내는 함수
function send(){
    //보낼 메시지를 가져온다.
    let sendMessage=document.querySelector('.chat-text').value;
    console.log(sendMessage);
    //액세스 토큰 검사
    util.sendAuthorize().then(result=> {
        //채팅방에 메시지 출력
        let sendTime = getTime();
        updateView(sendMessage,'me-ballon',roomInfo.myUserId,sendTime);
        //액세스 토큰이 유효할 시
        if (result === true) {
            //지정된 대상으로 메시지를 보낸다.
            ws.send("/send/room/"+roomInfo.roomId+"/"+roomInfo.opponentUserId, {} ,JSON.stringify({
                senderNickname : roomInfo.myUserId,
                message :  sendMessage
            }));
            //채팅방 화면에서 마지막 스크롤로 유지
            document.querySelector('.chat-box').scrollTop = document.querySelector('.chat-box').scrollHeight;
        }
    });

}
//메시지를 보낸 시간을 얻어오는 함수
function getTime() {
    let time = new Date();
    let hours = time.getHours();
    let minutes = time.getMinutes();
    let period = hours >= 12 ? '오후' : '오전';

    // 12시간 형식으로 변환
    hours = hours % 12;
    hours = hours ? hours : 12; // 0시일 경우 12로 변경

    // 시간과 분이 한 자리 수인 경우 앞에 0을 추가하여 두 자리로 만들기
    hours = String(hours).padStart(2, '0');
    minutes = String(minutes).padStart(2, '0');

    let convertedTime = period + ' ' + hours + ':' + minutes;

    return convertedTime;
}
//새로운 채팅방을 들어갈 시 기존 연결 점을 끊는다.
// stomp 클라이언트와 소켓이 너무 많아지면 다운된다.
export function disconnect(){
    console.log("disconnect 입장");
    if(ws){
        console.log("disconnect 작동");
        ws.disconnect();
        sock.close();
        sock=undefined;

    }
    console.log("disconnect 해체");
}
//메시지를 채팅방 화면에 보여주는 함수
export function updateView(message,className, userId, time){
    //채팅창 div 선택
    const parentEle=document.querySelector('.chat-box');

    // 메시지를 보여줄 div 생성
    let parentDiv=document.createElement('div');
    parentDiv.classList.add(className);

    let childDiv=document.createElement('div');

    // 유저명을 넣을 span 태그
    let opponentUserIdSpan=document.createElement('span');
    opponentUserIdSpan.classList.add('opponent-userId');
    let opponentUserIdText=document.createTextNode(userId);
    opponentUserIdSpan.appendChild(opponentUserIdText);

    // 시간을 넣을 span 태그
    let timeSpan=document.createElement('span');
    timeSpan.classList.add('time-text');
    let timeText=document.createTextNode(time);
    timeSpan.appendChild(timeText);

    let setMsg=document.createTextNode(message);

    //상대방 메시지
    if(className=='opponent-ballon') {
        parentDiv.appendChild(opponentUserIdSpan);
        childDiv.classList.add('ballon', 'opponentB');
        childDiv.appendChild(setMsg);
        parentDiv.appendChild(childDiv);
        parentDiv.appendChild(timeSpan);
    }
    //내가 보낸 메시지
    else {
        parentDiv.appendChild(timeSpan);
        childDiv.classList.add('ballon');
        childDiv.appendChild(setMsg);
        parentDiv.appendChild(childDiv);
    }
    parentEle.appendChild(parentDiv);
}

