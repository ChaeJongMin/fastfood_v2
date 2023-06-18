//이미지에 관련된 js

//파일 타입이 올바른지 확인하는 함수
//이미지 파일을 넣을 떄 .png, .jpg 확장자만 가능하도록 설정한다,
function validFileType(filename) {
    const fileTypes = ["png", "jpg"];
    return fileTypes.indexOf(filename.substring(filename.lastIndexOf(".")+1, filename.length)
        .toLowerCase()) >= 0;
}

//파일 이름에 제한을 두는 (30) 함수
function validFileNameSize(filename){
    if(filename.length > 30){ //30자
        return false;
    }else{
        return true;
    }
}
//삽입한 이미지를 화면에 보여주는 함수
function showImg(input){
    // 비동기적으로 파일의 내용을 읽는 FileReader 객체 생성
    const reader = new FileReader();
    //읽기 동작이 정상적으로 작동할 시
    reader.onload = function (e) {
        //화면에 이미지를 보여줄 img 태그를 얻는다.
        const previewImage = document.querySelector('.showImg');
        // FileReader가 읽은 이미지 파일의 데이터 URL을 가져와 src에 대입
        previewImage.src = e.target.result;
    }
    // 바이너리 파일을 Base64 Encode 문자열로 반환하며 onload 핸들러가 작동
    // 비동기적으로 실행
    // 이미지를 웹 페이지에 포함하거나 이미지를 다른 애플리케이션으로 전송하는 데 사용
    reader.readAsDataURL(input);
}
//선택한 이미지에 valid, 보여주기 등 관리하는 함수
function previewImage(input){
    //받아온 파일의 이름을 얻어온다.
    const filename = input.files[0].name;
    console.log(filename);
    //파일 유효성 검사
    if (!validFileType(filename)) {
        alert(".jpg / .png 확장자 파일만 가능합니다.");
        return false;
    } else {
        if (!validFileNameSize(filename)) {
            alert("파일명이 30자를 초과합니다.");
            return false;
        } else{
            const imgNameBox=document.querySelector(".upload-name");
            imgNameBox.value=filename;
        }
    }
    //선택한 이미지를 화면에 보여주는 함수 호출
    showImg(input.files[0]);
}
