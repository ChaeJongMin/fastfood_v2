import { UtilController } from '../Util.js';
const util=new UtilController();

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
function save(){
    util.sendAuthorize().then(result=>{
        if(result===true) {
            const formData = new FormData();

            const file=document.forms["editForms"].imgFile.files[0];
            console.log(file.name);
            formData.append("file",file);
            const checkbox = document.querySelector('#check1').value;
            const is_checked = checkbox.checked;

            const data={
                name:document.querySelector('#name').value,
                price: document.querySelector('#price').value,
                cateName:document.querySelector('#cateName > option:checked').value,
                allSale : is_checked
            }
            formData.append("productData", new Blob([JSON.stringify(data)] , {type: "application/json"}));

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
function update(){
    util.sendAuthorize().then(result=> {
        if (result === true) {
            const formData = new FormData();
            const imgSrc=document.querySelector(".showImg").src;

            if(imgSrc===null || imgSrc===undefined){
                alert("이미지를 선택해주세요!");
                return ;
            }


            const file=document.forms["editForms"].imgFile.files[0];
            console.log(file);

            formData.append("file",file);
            const checkbox = document.querySelector('#check1').value;
            const is_checked = checkbox.checked;

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
function deletes(){
    util.sendAuthorize().then(result=> {
        if (result === true) {
            const id=document.querySelector("#hidden-product-id").value;
            $.ajax({
                type: 'DELETE',
                url: '/api/product/'+id,
                dataType: 'json',
                contentType:'application/json; charset=utf-8'
            }).done(function () {
                alert("상품 수정 완료!!!!");
                window.location.href = "/fastfood/superMainHome";
            }).fail(function (error) {
                alert(JSON.stringify(error));
            });
        }
    });
}