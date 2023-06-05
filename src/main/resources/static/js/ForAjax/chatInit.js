import { UtilController } from '../Util.js';
import {insertUser, deletePriorList, insertOpponentUser, deletePriorChat, moveOpponentUser, deleteUserBox} from '../ForElement/InsertFindUserList.js';
import {connectStomp, disconnect, updateView} from '../ForElement/UseChat.js';

const util=new UtilController();
let current_userInfo;

window.onload = function () {
    util.sendAuthorize().then(result => {
        console.log(result);
        const findEle=document.querySelector('#find-api-btn');
        if(findEle!=null || findEle!=undefined) {
            findEle.addEventListener('click', function () {
                //검색한 필드 값 초기화
                find();
            })
        }
        const refreshEle=document.querySelector('#refresh-btn');
        if(refreshEle!=null || refreshEle!=undefined) {
            refreshEle.addEventListener('click', function (){
                getOpponentList().then(r => console.log(r));
            })
        }
        getOpponentList().then(r => console.log(r));
    }).catch(result => {
            console.log(result);
    });
}

  async function getOpponentList(){
    try{
        const userId=document.querySelector('.hidden-id').value;
        const myUserId = document.querySelector('.hidden-userId').value;

        const opponentList=await fetch('/api/chat/chatted/'+userId+'/'+myUserId,{
            method : 'GET'
        }).then(response =>{
            return response.json();
        })
        console.log("getOpponentList 가져온 리스트: "+opponentList);
        deleteUserBox();
        insertOpponentUser(opponentList);
        const findList = document.querySelectorAll('.past_room');
        setClickAddListener(findList);

    }
    catch (error){
        console.log(error);
    }

  }


function find(){
    util.sendAuthorize().then(result=> {
        if (result === true) {
            const inputUserId = document.querySelector(".write-userId").value.trim();
            console.log("찾고자 하는 id= " + inputUserId);
            if (!inputUserId) {
                alert("검색어가 비어있습니다.")
            } else {
                const myId=document.querySelector('.hidden-id').value;
                deletePriorList();
                $.ajax({
                    type: 'GET',
                    url: '/api/chat/find/' + inputUserId+"/"+myId,
                    contentType: 'application/json; charset=utf-8',
                    dataType: 'json',

                }).done(function (result) {
                    insertUser(result);
                    const findList = document.querySelectorAll('.create_room');
                    console.log(findList);
                    setClickAddListener(findList);
                    document.querySelector('.write-userId').value=null;

                }).fail(function (error) {
                    alert(JSON.stringify(error));
                });
            }
        }
    });
}

function createRoom(nickname) {
    util.sendAuthorize().then(result => {
        if (result === true) {
            const myUserId=document.querySelector('.hidden-userId').value;
            $.ajax({
                type: 'POST',
                url: '/api/chat/' + nickname + '/' +myUserId,
                dataType: 'json',
                contentType: 'application/json; charset=utf-8',
            }).done(function (result) {
                console.log("가져온 방번호: " + result);

                const markOpponent = document.querySelector('.opponent');
                const markOpponentSpan = markOpponent.querySelector('.opponent-name');

                if (markOpponentSpan === null || markOpponentSpan === undefined) {
                    let childSpan = document.createElement('span');
                    childSpan.classList.add('opponent-name');
                    let contentSpan = document.createTextNode(nickname + "님과 대화중....");
                    childSpan.appendChild(contentSpan);
                    markOpponent.appendChild(childSpan);
                } else {
                    markOpponentSpan.textContent = (nickname + "님과 대화중....");
                }
                let obj = {
                    roomId: result,
                    opponent: nickname
                }
                disconnect();
                connectStomp(obj);
                //기존 대화 불러오기
                //그러나 이미 대화했던 유저만 불러와야된다.
                loadMessage(obj);
            }).fail(function (error) {
                alert(JSON.stringify(error));
            });

        }
    });
}

function setClickAddListener(findList){
    if (findList !== null && findList.length !== 0 && findList !== undefined) {
        findList.forEach(ele => {
            ele.addEventListener('click', function (event) {

                //user-box인지 find-box인지 확인 필요
                const parent=event.currentTarget.parentElement;
                //새로운 대화하기전 전에 대화한 유저 버튼 disable 해체 및 버튼 텍스트 변경
                console.log("current_userInfo "+current_userInfo);
                if(current_userInfo!==undefined){
                    const priorBtn=current_userInfo.querySelector('button');
                    priorBtn.disabled=false;
                    priorBtn.textContent='대화하기';
                }

                //공통 기존 내용 다 삭제

                //find-box 일 시
                deletePriorChat();
                //클릭한 상대방 이름 얻기
                const nameValue = parent.querySelector('.user-name').textContent;
                if(parent.className=='find-box'){
                    //해당 내용 user-box로 옮김
                    const eleUserBoxArr= moveOpponentUser(nameValue);
                    current_userInfo=eleUserBoxArr[0];
                    eleUserBoxArr[1].addEventListener('click',()=>{
                         createRoom(nameValue);
                    })
                    //find-box에 내용
                    parent.lastChild.textContent='대화친구'
                    parent.lastChild.disabled=true;
                } else if(parent.className=='user-box'){
                    //클릭한 상대방 이름 얻기
                    current_userInfo=parent;
                    // 버튼 상태 변경
                    const nowBtn=parent.querySelector('button');
                    nowBtn.disabled=true;
                    nowBtn.textContent='대화 중'
                }
                 createRoom(nameValue);
            });
        })
    }
}

 function loadMessage(obj){
    console.log("loadMessage 입장");
    util.sendAuthorize().then(async result => {
        if (result === true) {
            const loadMessageList = await fetch('/api/chat/load/' + obj.roomId, {
                method: 'GET'
            }).then(response => {
                return response.json();
            })
            loadMessageList.forEach(ele=>{
                console.log(ele.userId+" "+obj.opponent);
                let ballonClassName;
                if(ele.userId == obj.opponent)
                    ballonClassName='opponent-ballon';
                else
                    ballonClassName='me-ballon';
                updateView(ele.message,ballonClassName, ele.userId, ele.sendTime);
            })
        }
    });
     console.log("loadMessage 해체");
}