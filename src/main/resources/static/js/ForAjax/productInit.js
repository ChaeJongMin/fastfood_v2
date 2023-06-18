import { UtilController } from '../Util.js';
const util=new UtilController();

//제품에 관련된 api 통신

// 각 버튼에 이벤트 리스너(클릭)를 등록하는 함수
window.onload = function (){
    const saveEle=document.querySelector('#product-add-btn');
    const updateEle=document.querySelector('#product-upload-btn');
    const deleteEle=document.querySelector('#product-delete-btn');
    if(saveEle!=null){
        saveEle.addEventListener('click',function () {
            save();
        })
    }
    if(updateEle!=null){
        updateEle.addEventListener('click',function () {
            update();
        })
    }
    if(deleteEle!=null){
        deleteEle.addEventListener('click',function () {
            deletes();
        })
    }
}
//제품을 저장하는 api 통신을 하는 함수
function save(){
    //액세스 토큰 유효성 검사
    util.sendAuthorize().then(result=>{
        //정상일 떄
        if(result===true) {
            //이미지를 보내기 위해 FormData 객체 생성
            // HTML 폼 데이터를 캡슐화하고, 해당 데이터를 쉽게 생성하고 조작할 수 있는 인터페이스를 제공하는 객체
            // FormData 객체는 웹 애플리케이션에서 파일 업로드 및 이미지 파일과 함께 데이터를 전송해야 할 때 편리하게 사용할 수 있는 기능을 제공
            const formData = new FormData();
            const imgSrc=document.querySelector(".showImg").src;
            //선택된 이미지가 없을 시
            if(imgSrc===null || imgSrc===undefined){
                alert("이미지를 선택해주세요!");
                return ;
            }

            //불러온 이미지 파일을 저장
            const file=document.forms["editForms"].imgFile.files[0];
            //formData에 파일 넣기
            formData.append("file",file);

            //체크박스 값(품절 여부) 선택
            const checkbox = document.querySelector('#check1').value;
            const is_checked = checkbox.checked;

            //form에 추가적인 데이터를 넣기위해 객체 생성
            const data={
                name:document.querySelector('#name').value,
                price: document.querySelector('#price').value,
                cateName:document.querySelector('#cateName > option:checked').value,
                allSale : is_checked
            }

            // formData에 객체 넣기
            // JSON 데이터는 직접 FormData에 추가할 수 없어 따라서 JSON 데이터를 Blob 객체로 래핑한 후에 FormData에 추가해야 한다
            formData.append("productData", new Blob([JSON.stringify(data)] , {type: "application/json"}));
            //ajax 통신
            $.ajax({
                type: 'POST',
                url: '/api/product',
                processData: false,
                contentType: false,
                enctype : 'multipart/form-data',
                data: formData,
            }).done(function () {
                alert("상품 저장 완료!!!!");
                window.location.href = "/fastfood/superMainHome";
            }).fail(function (error) {
                alert(JSON.stringify(error));
            });
         }
    });
}
//제품 업데이트하는 함수
//위 save 함수와 로직이 비슷
function update(){
    util.sendAuthorize().then(result=> {
        if (result === true) {
            const formData = new FormData();
            const imgSrc=document.querySelector(".showImg").src;

            const file=document.forms["editForms"].imgFile.files[0];
            console.log(file);

            formData.append("file",file);
            const checkbox = document.querySelector('#check1');
            const is_checked = checkbox.checked;
            console.log("품절여부: "+is_checked);
            const data={
                price: document.querySelector('#price').value,
                allSale : is_checked
            }
            formData.append("productData", new Blob([JSON.stringify(data)] , {type: "application/json"}));

            const id=document.querySelector("#hidden-product-id").value;
            $.ajax({
                type: 'PUT',
                url: '/api/product/'+id,
                processData: false,
                contentType: false,
                enctype : 'multipart/form-data',
                data: formData,
            }).done(function () {
                alert("상품 수정 완료!!!!");
                window.location.href = "/fastfood/superMainHome";
            }).fail(function (error) {
                alert(JSON.stringify(error));
            });
        }
    });
}
//제품 삭제하는 함수
function deletes(){
    util.sendAuthorize().then(result=> {
        if (result === true) {
            const id=document.querySelector("#hidden-product-id").value;
            //ajax 통신
            $.ajax({
                type: 'DELETE',
                url: '/api/product/'+id,
                dataType: 'json',
                contentType:'application/json; charset=utf-8'
            }).done(function () {
                alert("상품 삭제 완료!!!!");
                window.location.href = "/fastfood/superMainHome";
            }).fail(function (error) {
                alert(JSON.stringify(error));
            });
        }
    });
}