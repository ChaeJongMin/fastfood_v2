function validFileType(filename) {
    const fileTypes = ["png", "jpg"];
    return fileTypes.indexOf(filename.substring(filename.lastIndexOf(".")+1, filename.length)
        .toLowerCase()) >= 0;
}
function validFileNameSize(filename){
    if(filename.length > 30){ //30자
        return false;
    }else{
        return true;
    }
}
function showImg(input){

    const reader = new FileReader();
    reader.onload = function (e) {
        const previewImage = document.querySelector('.showImg');
        previewImage.src = e.target.result;
    }
    reader.readAsDataURL(input);

}
function previewImage(input){

    const filename = input.files[0].name;
    console.log(filename);
    if (!validFileType(filename)) {
        alert(".jpg / .png 확장자 파일만 가능합니다.");
        return false;
    } else {
        if (!validFileNameSize(filename)) {
            alert("파일명이 30자를 초과합니다.");
            return false;
        }
    }
    const svalue=document.querySelector('.upload-name').value=filename;

    showImg(input.files[0]);
}
