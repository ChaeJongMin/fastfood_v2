
export function insertUser(users){
    const parentEle=document.querySelector('.find-result');
    console.log("insertUser "+users.length);
    users.forEach(ele=>{
        console.log(JSON.stringify(ele))
        // console.log("가져온 상대방 정보: "+ ele.isCheck);
        let div=document.createElement('div');
        div.classList.add('find-box');

        let childSpan=document.createElement('span');
        childSpan.classList.add('user-name');

        let contentSpan=document.createTextNode(ele.userName);
        if(ele.userName !== document.querySelector('.hidden-userId').value){
            childSpan.appendChild(contentSpan);
            div.appendChild(childSpan);

            let childBtn=document.createElement('button');
            childBtn.classList.add('start-btn','create_room');
            // childBtn.id='search';
            let contentBtn;

            if(ele.check){
                contentBtn=document.createTextNode('채팅방 존재');
                childBtn.disabled=true;
            } else{
                contentBtn=document.createTextNode('대화하기');
            }

            childBtn.appendChild(contentBtn);
            div.appendChild(childBtn);

            parentEle.appendChild(div);
        }


    });

}
//새로 검색 시 검색 내용 다 삭제
export function deletePriorList(){
    const deleteEle=document.querySelectorAll('.find-box').forEach(ele=>{
        ele.remove();
    });

}
//이전 대화 내용 다 삭제
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
export function deleteUserBox(){
    const deleteEle=document.querySelectorAll('.user-box').forEach(ele=>{
        ele.remove();
    });

}
export function insertOpponentUser(data){
    const parentEle=document.querySelector('#recent-user');
    data.forEach(ele=>{
        let div=document.createElement('div');
        div.classList.add('user-box');

        let childSpan=document.createElement('span');
        childSpan.classList.add('user-name');
        let contentSpan=document.createTextNode(ele.opponentName);
        childSpan.appendChild(contentSpan);

        let childDiv=document.createElement('div');
        childDiv.classList.add('user-last-comment');
        contentSpan=document.createTextNode(ele.lastMessage);
        childDiv.appendChild(contentSpan);

        let childBtn=document.createElement('button');
        childBtn.classList.add('start-btn','past_room');
        let contentBtn=document.createTextNode('대화하기');
        childBtn.appendChild(contentBtn);

        div.appendChild(childSpan);
        div.appendChild(childDiv);
        div.appendChild(childBtn);

        parentEle.appendChild(div);

    })
}

export function moveOpponentUser(name){
    const parentEle=document.querySelector('#recent-user');
    let individualDiv=document.createElement('div');
    individualDiv.classList.add('user-box');

    let childSpan=document.createElement('span');
    childSpan.classList.add('user-name');
    let contentSpan=document.createTextNode(name);
    childSpan.appendChild(contentSpan);

    let childDiv=document.createElement('div');
    childDiv.classList.add('user-last-comment');
    contentSpan=document.createTextNode('새로운 상대!!');
    childDiv.appendChild(contentSpan);

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