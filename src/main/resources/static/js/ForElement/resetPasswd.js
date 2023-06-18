import {sendNewPasswd} from '../ForAjax/mailInit.js'
export function checkAuthCode(authCode){
    console.log("인증코드 : "+authCode);
    const inputCode=document.querySelector(".sendEmail").value;
    if(authCode==inputCode){
        inserNewPasswd();
        document.querySelector('#reset-passwd-btn').addEventListener('click',function () {
            sendNewPasswd();
        })

    } else{
        //에러메시지 출력
        setErrorMsg("인증코드가 틀렸습니다.");
    }
}

export function insertCode(email){
    //div 생성
    const parentEle=document.querySelector('.set-box-size');
    deleteChildEle(parentEle);
    const childP = document.createElement('p');
    const contentP = document.createTextNode("로 비밀번호 초기화 코드를 보냈습니다, 코드를 입력해주세요");
    const emailElement = document.createElement('strong');
    emailElement.textContent = email;
    childP.appendChild(emailElement);
    childP.appendChild(contentP);

    const input = document.createElement("input");
    input.type = "text";
    input.classList.add ("insert-field","sendEmail");
    input.placeholder = "초기화 코드를 입력해주세요";
    input.onfocus = () => {
        input.placeholder = "";
    };
    input.onblur = () => {
        input.placeholder = "초기화 코드를 입력해주세요";
    };

    const button = document.createElement("button");
    button.classList.add("btn");
    button.id = "check-code-ptn";
    button.textContent = "확인";

    parentEle.appendChild(childP);
    parentEle.appendChild(input);
    parentEle.appendChild(button);

}

export function inserNewPasswd(){
    const parentEle=document.querySelector('.set-box-size');
    deleteChildEle(parentEle);

    const p = document.createElement("p");
    p.innerText = "입력한 코드가 인증되었습니다, 변경할 비밀번호를 입력해주세요";
    parentEle.appendChild(p);

    const input1 = document.createElement("input");
    input1.type = "password";
    input1.classList.add("insert-field", "passwd");
    input1.placeholder = "새로운 비밀번호를 입력해주세요";
    input1.onfocus = function() {
        this.placeholder = "";
    };
    input1.onblur = function() {
        this.placeholder = "새로운 비밀번호를 입력해주세요";
    };
    parentEle.appendChild(input1);

    const input2 = document.createElement("input");
    input2.type = "password";
    input2.classList.add("insert-field","repeatPasswd");
    input2.placeholder = "위에 입력한 비밀번호를 다시 입력해주세요";
    input2.onfocus = function() {
        this.placeholder = "";
    };
    input2.onblur = function() {
        this.placeholder = "위에 입력한 비밀번호를 다시 입력해주세요";
    };
    parentEle.appendChild(input2);

    const button = document.createElement("button");
    button.classList.add ("btn");
    button.id = "reset-passwd-btn";
    button.innerText = "변경하기";
    parentEle.appendChild(button);


}

export function deleteChildEle(parentEle){
    const deleteChild=parentEle.children;
    console.log(deleteChild.length);
    if(deleteChild){
        while (parentEle.firstChild){
            parentEle.firstChild.remove();
        }
    }
}

export function setErrorMsg(msg){
    //에러 메시지가 존재할 시 기존 메시지 삭제
    const existingErrorMsg = document.querySelector(".error-text");
    if (existingErrorMsg) {
        existingErrorMsg.remove();
    }

    const button = document.querySelector(".btn");
    const errorText = document.createElement("p");
    errorText.classList.add("error-text");
    errorText.textContent = msg;

    button.parentNode.insertBefore(errorText, button);
}

export function validateIdAndEmail(checkArr){
    return checkArr.every(item => item === "");
}

export function validateNaverEmail(email){
    return email.endsWith('@naver.com') || email.endsWith('@gmail.com');
}

export function equalsCheckPasswd(pw1, pw2){
    return (pw1===pw2);
}