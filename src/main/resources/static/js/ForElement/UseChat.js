import { UtilController } from '../Util.js';
const util=new UtilController();

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

let roomInfo={
    roomId : 0,
    myUserId : "default",
    opponentUserId : "default"
}
let ws;
let sock;
// const sock = new SockJS(`http://${location.host}/stomp/chat`);
export function connectStomp(info){
    console.log("connect 입장");
    roomInfo.roomId=info.roomId;
    roomInfo.opponentUserId=info.opponent;
    roomInfo.myUserId=document.querySelector('.hidden-userId').value;
    if(sock===null || sock===undefined)
        sock = new SockJS(`http://${location.host}/stomp/chat`);
    let client=Stomp.over(sock);
    ws=client;

    client.connect("","",function (){
        console.log("연결");

        client.subscribe("/receive/room/"+roomInfo.roomId+"/"+roomInfo.myUserId, function (event){
            let data = JSON.parse(event.body);
            console.log("받아온 데이터: "+data);
            let sendTime = getTime();
            updateView(data.message,'opponent-ballon',roomInfo.opponentUserId,sendTime);
            document.querySelector('.chat-box').scrollTop = document.querySelector('.chat-box').scrollHeight;
        });

        client.subscribe("/receive/set", function (event){
            let data = JSON.parse(event.body);
            console.log("set 받아온 데이터: "+data);
        });
    })
    console.log("connect 해체");
}
function send(){
    let sendMessage=document.querySelector('.chat-text').value;
    console.log(sendMessage);
    util.sendAuthorize().then(result=> {
        let sendTime = getTime();
        updateView(sendMessage,'me-ballon',roomInfo.myUserId,sendTime);
        if (result === true) {
            ws.send("/send/room/"+roomInfo.roomId+"/"+roomInfo.opponentUserId, {} ,JSON.stringify({
                senderNickname : roomInfo.myUserId,
                message :  sendMessage
            }));
            document.querySelector('.chat-box').scrollTop = document.querySelector('.chat-box').scrollHeight;
        }
    });

}
function getTime(){
    let time = new Date();
    let convertTime=time.toLocaleDateString();
    convertTime=convertTime.substring(convertTime.length-3);
    return convertTime;
}
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
export function updateView(message,className, userId, time){
    const parentEle=document.querySelector('.chat-box');
    let parentDiv=document.createElement('div');
    parentDiv.classList.add(className);

    let childDiv=document.createElement('div');

    let opponentUserIdSpan=document.createElement('span');
    opponentUserIdSpan.classList.add('opponent-userId');
    let opponentUserIdText=document.createTextNode(userId);
    opponentUserIdSpan.appendChild(opponentUserIdText);

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

