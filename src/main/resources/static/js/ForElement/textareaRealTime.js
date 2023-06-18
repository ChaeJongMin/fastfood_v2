//채팅할 메시지를 입력하는 textarea 관한 함수

//textarea 크기를 설정하는 함수
function resizeTextArea(ele) {
    //엔터가 아닐 시
    if(ele.keyCode !== 13){
        //textarea 높이를 자동 설정
        ele.style.height='auto';
        let height = ele.scrollHeight;
        ele.style.height= (height + 8)+'px' ;
    }
};

//textarea를 클릭 시 가장 마지막 위치로 이동
function resetCursor(txtElement){
    txtElement.setSelectionRange(txtElement.value.length,txtElement.value.length);
}