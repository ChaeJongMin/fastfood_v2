window.onload = function (){
    let saveEle=document.querySelector('#save-button');
    let updateEle=document.querySelector('#update-button');
    if(saveEle!=null){
        saveEle.addEventListener('click',function () {
            save();
        })
    }
    if(updateEle!=null){
        updateEle.addEventListener('click',function (){
            update();
        })
    }

}
function save(){
    let data={
        userId:document.querySelector('#userId').value,
        userPasswd:document.querySelector('#userPasswd').value,
        email:document.querySelector('#email').value,
        phoneNum:document.querySelector('#phoneNum').value,
        cardCompany:document.querySelector('#cardCompany').value,
        cardNum:document.querySelector('#cardNum').value
    }
    $.ajax({
        type: 'POST',
        url : '/api/customer',
        dataType: 'json',
        contentType: 'application/json; charset=utf-8',
        data: JSON.stringify(data),
    }).done(function(){
        alert("회원가입 완료!!!!");
        window.location.href="/fastfood/login";
    }).fail(function(error){
        alert(JSON.stringify(error));
    });
}
function update(){
    let data={
        userId:document.querySelector('#userId').value,
        email:document.querySelector('#email').value,
        phoneNum:document.querySelector('#phoneNum').value,
        cardCompany:document.querySelector('#cardCompany').value,
        cardNum:document.querySelector('#cardNum').value
    }
    let id=document.querySelector('#id').value;
    $.ajax({
        type: 'PUT',
        url : '/api/customer/'+id,
        dataType: 'json',
        contentType: 'application/json; charset=utf-8',
        data: JSON.stringify(data),
    }).done(function(){
        alert("수정이 완료 됐으니 다시 로그인 해주세요");
        window.location.href="/fastfood/login";
    }).fail(function(error){
        alert(JSON.stringify(error));
    });
}

