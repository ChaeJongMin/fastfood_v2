//찾기 버튼 클릭 시 받아오는 유저 정보을 화면에 출력하는 함수
export function insertUser(users){
    //찾아온 유저 정보를 넣을 부모 div의 div를 찾아온다.
    const parentEle=document.querySelector('.find-result');
    users.forEach(ele=>{
        //유저 정보를 담을 div를 생성
        let div=document.createElement('div');
        //해당 div에 find-box 클래스를 추가
        div.classList.add('find-box');

        //찾은 유저 명 텍스트를 넣을 span 생성
        let childSpan=document.createElement('span');
        //user-name 클래스를 추가
        childSpan.classList.add('user-name');

        //span에 유저 명 텍스트 추가
        let contentSpan=document.createTextNode(ele.userName);
        //찾아온 유저명이 만약 사용자 유저명과 다를 시 추가한다. 자신의 유저명은 추가할 필요가 없다
        if(ele.userName != document.querySelector('.hidden-userId').value){
            //span을 div에 붙인다.
            childSpan.appendChild(contentSpan);
            div.appendChild(childSpan);

            //버튼을 생성
            let childBtn=document.createElement('button');
            childBtn.classList.add('start-btn','create_room');
            //버튼에 넣을 텍스트 명
            let contentBtn;

            // api 통신으로 받아온 json 데이터에 사용자와 이미 채팅방 여부를 나타내는 check를 통해
            //버튼 텍스트와 disabled를 결정한다.
            if(ele.check){
                //이미 채팅방이 존재할 시
                contentBtn=document.createTextNode('채팅방 존재');
                childBtn.disabled=true;
            //채팅방이 없는 유저일 시
            } else{
                contentBtn=document.createTextNode('대화하기');
            }
            // btn을 div에 붙인다.
            childBtn.appendChild(contentBtn);
            div.appendChild(childBtn);

            //div를 부모 div에 붙인다.
            parentEle.appendChild(div);
        }


    });

}
//유저 검색을 다시 할 경우 이미 존재한 찾기 리스트에서 존재한 div를 제거해야한다.
export function deletePriorList(){
    const deleteEle=document.querySelectorAll('.find-box').forEach(ele=>{
        ele.remove();
    });

}
//다른 유저와 채팅 시 기존 채팅 내용을 지우는 함수
export function deletePriorChat(){
    const deleteParentEle = document.querySelector('.chat-box');
    const deleteParent = deleteParentEle.children;

    if(deleteParent){
        console.log(deleteParent);
       while (deleteParentEle.firstChild){
           deleteParentEle.firstChild.remove();
       }

    }
}
//찾기 리스트에서 새로운 유저와 채팅 시 찾기 리스트에서 해당 유저를 삭제해야한다.
export function deleteUserBox(){
    const deleteEle=document.querySelectorAll('.user-box').forEach(ele=>{
        ele.remove();
    });
}

//기존의 채팅한 유저를 상대방 리스트에 추가할 함수
export function insertOpponentUser(data){
    //새로운 유저 정보를 넣을 div의 부모 div를 찾아온다.
    const parentEle=document.querySelector('#recent-user');
    data.forEach(ele=>{
        //유저 정보를 넣을 div생성
        let div=document.createElement('div');
        div.classList.add('user-box');

        //유저명 넣을 span 태그 생성
        let childSpan=document.createElement('span');
        childSpan.classList.add('user-name');
        let contentSpan=document.createTextNode(ele.opponentName);
        childSpan.appendChild(contentSpan);

        //해당 유저와의 가장 최근 메시지를 넣을 div 태그 생성
        let childDiv=document.createElement('div');
        childDiv.classList.add('user-last-comment');
        contentSpan=document.createTextNode(ele.lastMessage);
        childDiv.appendChild(contentSpan);

        //대화하기 버튼 생성
        let childBtn=document.createElement('button');
        childBtn.classList.add('start-btn','past_room');
        let contentBtn=document.createTextNode('대화하기');
        childBtn.appendChild(contentBtn);

        //div에 붙인다.
        div.appendChild(childSpan);
        div.appendChild(childDiv);
        div.appendChild(childBtn);

        //부모 div에 붙여 화면에 보여주게 한다.
        parentEle.appendChild(div);

    })
}

//찾기 리스트에서 새로운 유저와 채팅 시 유저 정보를 상대방 리스트로 옮기는 함수
export function moveOpponentUser(name){

    //유저 정보를 넣을 div의 부모 div를 찾는다.
    const parentEle=document.querySelector('#recent-user');
    //유저 정보를 넣을 div를 생성
    let individualDiv=document.createElement('div');
    individualDiv.classList.add('user-box');

    //유저명을 넣을 span 태그 생성
    let childSpan=document.createElement('span');
    childSpan.classList.add('user-name');
    let contentSpan=document.createTextNode(name);
    childSpan.appendChild(contentSpan);

    //마지막 대화를 넣을 div 태그를 생성한다.
    //그러나 새로운 유저이므로 새로운 상대라는 텍스트로 변경한다.
    let childDiv=document.createElement('div');
    childDiv.classList.add('user-last-comment');
    contentSpan=document.createTextNode('새로운 상대!!');
    childDiv.appendChild(contentSpan);

    //대화하기 버튼 생성
    let childBtn=document.createElement('button');
    childBtn.classList.add('start-btn','past_room');
    let contentBtn=document.createTextNode('대화중');
    childBtn.appendChild(contentBtn);
    childBtn.disabled=true;

    individualDiv.appendChild(childSpan);
    individualDiv.appendChild(childDiv);
    individualDiv.appendChild(childBtn);

    parentEle.appendChild(individualDiv);

    return [individualDiv,childBtn];

}