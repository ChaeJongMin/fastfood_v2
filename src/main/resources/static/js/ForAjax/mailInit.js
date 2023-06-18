import * as funcs from '../ForElement/resetPasswd.js'
import {equalsCheckPasswd, setErrorMsg} from "../ForElement/resetPasswd.js";

let localEmail;
let authCode;
window.onload = function (){
    const checkIdAndEmail=document.querySelector('#check-info-btn');

    if(checkIdAndEmail!==null || checkIdAndEmail !== undefined){
        checkIdAndEmail.addEventListener('click', function (){
            sendIdAndEmail();
        });
    }

}

function sendIdAndEmail(){
    const name=document.querySelector('.userId').value;
    const email=document.querySelector('.userEmail').value;
    //둘중 하나라도 빈칸일 떄
    if(funcs.validateIdAndEmail([name,email])){
            funcs.setErrorMsg("빈칸이 존재합니다.")
            return ;
    }
    //이메일 naver.com일 떄
    if(!funcs.validateNaverEmail(email)){
        funcs.setErrorMsg("네이버 이메일이 아닙니다.")
        return;
    }

    console.log("sendIdAndEmail");
    $.ajax({
        type: 'GET',
        url : '/api/mail/'+name+'/'+email ,
        contentType: 'application/json; charset=utf-8',
    }).done(function(result){
        console.log("아이디, 이메일이 존재 성공");
        authCode=result;
        localEmail=email;

        funcs.insertCode(email);

        const checkCodeBtn=document.querySelector("#check-code-ptn")
        checkCodeBtn.addEventListener('click', function () {
            funcs.checkAuthCode(authCode);


        });

    }).fail(function(error){
        funcs.setErrorMsg(error.responseText);
    });

}
// reset-passwd-btn 연결
export function sendNewPasswd(){
    const email=localEmail;
    const passwd=document.querySelector(".passwd").value;
    const repeatPasswd=document.querySelector(".repeatPasswd").value;
    //빈칸 일떄
    if(funcs.validateIdAndEmail([passwd,repeatPasswd])){
        funcs.setErrorMsg("빈칸이 존재합니다.")
        return ;
    }
    //서로 패스워드가 틀릴 떄
    if(!funcs.equalsCheckPasswd(passwd,repeatPasswd)){
        funcs.setErrorMsg("비밀번호를 서로 다릅니다.")
        return ;
    }

    let sendData={
        email : email,
        passwd : passwd
    }

    $.ajax({
        type: 'PATCH',
        url : '/api/mail',
        dataType: 'json',
        contentType: 'application/json; charset=utf-8',
        data: JSON.stringify(sendData)
    }).done(function(result){
        alert("비밀번호가 변경되었습니다. 로그인 페이지로 이동합니다.")
        window.location.href="/fastfood/login";
    }).fail(function(error){
        console.log(JSON.stringify(error));
    });
}
