import {insertUser, deletePriorList, insertOpponentUser, deletePriorChat, moveOpponentUser, deleteUserBox} from '../ForElement/InsertFindUserList.js';
import {connectStomp, disconnect, updateView} from '../ForElement/UseChat.js';
import { UtilController } from '../Util.js';

//해당 js파일은 api 통신과 버튼에 이벤트 리스너를 추가하는 로직을 담고 있다.

const util=new UtilController();
//현재 대화중인 상대방 정보를 담고있는 div를 저장할 변수

let current_userInfo;

window.onload = function () {
    //토큰 인증 검사
    //정상적인 액세스 토큰일 시
    util.sendAuthorize().then(result => {

        // '찾기' 버튼 요소
        const findEle=document.querySelector('#find-api-btn');
        // '찾기' 버튼 요소 클릭 이벤트 설정
        if(findEle!=null || findEle!=undefined) {
            findEle.addEventListener('click', function () {
                //find 함수 호출
                find();
            })
        }
        // '새로고침' 버튼 요소
        const refreshEle=document.querySelector('#refresh-btn');
        // '새로고침' 버튼 요소 클릭 이벤트 설정
        if(refreshEle!=null || refreshEle!=undefined) {
            refreshEle.addEventListener('click', function (){
                //getOpponentList() 호출
                //getOpponentList()는 async 함수이므로 프로미스를 반환하므로 then 필요
                getOpponentList().then(r => console.log(r));
            })
        }
        //첫 페이지 접속 시 상대방 목록을 불러와야한다.
        //해당 기능을 구현하는 getOpponentList 함수 호출
        getOpponentList().then(r => console.log(r));
    }).catch(result => {
            console.log(result);
    });
}

  // 상대방 목록( 상대방 이름, 최근 메시지 등)을 불러오는 async 함수
  // 기본적인 흐름
  /*
  api를 호출하여 상대방 목록 정보를 얻기 -> 기존 상대방 목록 제거 ->
  다시 새로운 상대방 목록 추가 -> 상대방 목록에 추가한 버튼에 클릭 이벤트 설정
   */
  async function getOpponentList(){
    try{
        //현재 사용자 정보 얻어오기
        const userId=document.querySelector('.hidden-id').value;
        const myUserId = document.querySelector('.hidden-userId').value;

        //fetch로 api을 호출하며 결과값은 서버에서 받아온 값을 JSON 형태로 변경해서 받는다.
        const opponentList=await fetch('/api/chat/chatted/'+userId+'/'+myUserId,{
            method : 'GET'
        }).then(response =>{
            return response.json();
        })
        //다시 새롭게 불러음로 기존의 상대방 목록을 제거
        //상대방 목록을 제거 하는 함수
        deleteUserBox();
        //받아온 결과값을 insertOpponentUser 함수를 통해 상대방 목록을 요소로 추가
        insertOpponentUser(opponentList);
        //상대방 목록에서 class(past_room) 통해 전체 div(요소)를 가져온다.
        const findList = document.querySelectorAll('.past_room');
        //상대방 목록에 있는 버튼에 클릭 이벤트 설정하는 setClickAddListener 함수 호출
        setClickAddListener(findList);

    }
    catch (error){
        console.log(error);
    }

  }

//검색칸에 키워드를 입력하면 api를 호출하여 검색 결과 값을 받아와 화면에 출력하는 함수
function find(){
      // 액세스 토큰 검사
    util.sendAuthorize().then(result=> {
        //정상적인 토큰일 시
        if (result === true) {
            //입력 필드를 가져온다.
            const inputUserId = document.querySelector(".write-userId").value.trim();
            //비어있으면 alert 창 생성
            if (!inputUserId) {
                alert("검색어가 비어있습니다.")
                //비어있지 않을 시
            } else {
                //사용자의 id(기본키)를 얻어온다.
                const myId=document.querySelector('.hidden-id').value;
                //이전에 검색을 통해 화면에 출력한 결과값(요소들)을 삭제
                deletePriorList();
                //ajax 통신
                $.ajax({
                    type: 'GET',
                    url: '/api/chat/find/' + inputUserId+"/"+myId,
                    contentType: 'application/json; charset=utf-8',
                    dataType: 'json',

                }).done(function (result) {
                    //성공 시
                    //받아온 데이터를 화면에 출력
                    insertUser(result);
                    //검색 결과 목록에서 class(create_room) 통해 전체 div(요소)를 가져온다.
                    const findList = document.querySelectorAll('.create_room');
                    //검색 결과에서도 버튼이 있으므로 클릭 이벤트를 추가
                    setClickAddListener(findList);
                    //검색한 결과를 받아왔으니 검색 필드에 존재하는 값을 지운다.
                    document.querySelector('.write-userId').value=null;

                }).fail(function (error) {
                    alert(JSON.stringify(error));
                });
            }
        }
    });
}

// 대화하기 버튼을 클릭 시 채팅방을 만드거나 찾는 함수
function createRoom(nickname) {
      //액세스 토큰 검사
    util.sendAuthorize().then(result => {
        //액세스 토큰이 정상적일 경우
        if (result === true) {
            const myUserId=document.querySelector('.hidden-userId').value;
            //ajax 통신
            $.ajax({
                type: 'POST',
                url: '/api/chat/' + nickname + '/' +myUserId,
                dataType: 'json',
                contentType: 'application/json; charset=utf-8',
                //result로 새롭게 또는 기존에 있는 방번호를 가져온다.
            }).done(function (result) {
                //대화하기 버튼을 눌렀을 떄 해당 상대방의 정보를 얻어온다.
                const markOpponent = document.querySelector('.opponent');
                const markOpponentSpan = markOpponent.querySelector('.opponent-name');
                // 채팅방 상위에 ~~와 대화중을 출력하기 위한 로직
                // 채팅방에 들어와서 처음으로 채팅 시
                if (markOpponentSpan === null || markOpponentSpan === undefined) {
                    //span에 쓸 텍스트를 구합니다.
                    let childSpan = document.createElement('span');
                    childSpan.classList.add('opponent-name');
                    let contentSpan = document.createTextNode(nickname + "님과 대화중....");
                    //span을 설정했으면 특정 div에 자식으로 추가하여 화면에 보이게합니다.
                    childSpan.appendChild(contentSpan);
                    markOpponent.appendChild(childSpan);
                    // 이미 채팅을 했다가 다른 사람이랑 채팅할 시
                    //굳이 span 태크를 특정 div에 자식으로 붙일 필요가 없고 값만 변경
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

//대화화기 버튼에 클릭 이벤트를 추가하는 메소드
//매개변수인 findList는 이벤트를 추가할 버튼 리스트입니다.
function setClickAddListener(findList){
    if (findList !== null && findList.length !== 0 && findList !== undefined) {
        findList.forEach(ele => {
            ele.addEventListener('click', function (event) {

                //user-box인지 find-box인지 확인 필요 (버튼의 부모)
                //user-box는 상대방 목록
                //find-box는 검색으로 찾은 유저 목록
                const parent=event.currentTarget.parentElement;
                //새로운 대화하기전 전에 대화한 유저 버튼 disable 해체 및 버튼 텍스트 변경
                if(current_userInfo!==undefined){
                    const priorBtn=current_userInfo.querySelector('button');
                    priorBtn.disabled=false;
                    priorBtn.textContent='대화하기';
                }
                //채팅방에 대화했던 메시지를 삭제
                deletePriorChat();
                //새롭게 채팅할 상대방 이름 얻기
                const nameValue = parent.querySelector('.user-name').textContent;
                //find-box일 시
                if(parent.className=='find-box'){
                    //대화할 상대방을 상대방 목록으로 이동하는 로직

                    const eleUserBoxArr= moveOpponentUser(nameValue);
                    //새롭게 대화중인 상대방의 정보를 담고있는 div로 변경
                    current_userInfo=eleUserBoxArr[0];
                    //찾은 유저를 상대방 목록으로 이동 -> 새로운 대화하기 버튼이 생성 -> 클릭 이벤트 추가
                    eleUserBoxArr[1].addEventListener('click',()=>{
                         createRoom(nameValue);
                    })
                    //이제 상대방은 상대방 목록(대화했던)에 있으니
                    //검색 목록에 있는 상대방의 버튼을 disable , 대화친구로 변경
                    parent.lastChild.textContent='대화친구'
                    parent.lastChild.disabled=true;
                //user-box일 경우
                } else if(parent.className=='user-box'){
                    //새롭게 대화중인 상대방의 정보를 담고있는 div로 변경
                    current_userInfo=parent;
                    // 버튼 상태 변경
                    const nowBtn=parent.querySelector('button');
                    nowBtn.disabled=true;
                    nowBtn.textContent='대화 중'
                }
                //대화하기 버튼을 클릭했으니 기존 채팅방이나 새로운 채팅방을 생성하는 createRoom() 함수 호출
                 createRoom(nameValue);
            });
        })
    }
}

//상대방과 이전에 대화한 채팅(메시지)과 관련된 데이터를 받아와 채팅방에 출력하는 함수
 function loadMessage(obj){
    console.log("loadMessage 입장");
    util.sendAuthorize().then(async result => {
        if (result === true) {
            //fetch로 api 통신
            const loadMessageList = await fetch('/api/chat/load/' + obj.roomId, {
                method: 'GET'
            }).then(response => {
                return response.json();
            })
            loadMessageList.forEach(ele=>{

                let ballonClassName;
                //현재 받아온 데이터.id가 상대방와 id가 같을 시
                if(ele.userId == obj.opponent)
                    //채팅방에서 좌측(상대방 메시지)에 해당하는 div class 이름을 설정
                    ballonClassName='opponent-ballon';
                //현재 받아온 데이터.id가 자신과 id가 같을 시
                else
                    //채팅방에서 우측(내가 보낸 메시지)에 해당하는 div class 이름을 설정
                    ballonClassName='me-ballon';
                //채팅창에 메시지를 화면에 출력하게 해주는 updateView 함수를 호출
                updateView(ele.message,ballonClassName, ele.userId, ele.sendTime);
            })
        }
    });
     console.log("loadMessage 해체");
}