import { UtilController } from './Util.js';

class initLoads extends UtilController{
    constructor() {
        super();
        this.showIds=document.querySelector(".showId");
        this.logoutBtn=document.querySelector("#logout-btn");
    }
    init(){
        console.log("init 실행");
        this.sendAuthorize().then(ret => {
            if(ret==true){
                this.#sendMainData();
            }
            else {

                //this.sendDelete();
            }
        });
        //로그아웃 버튼 구현
        this.logoutBtn.addEventListener("click",evt => {
            console.log("로그아웃 작동");
            this.sendDelete();
            // let logoutXhr = new XMLHttpRequest();
            // logoutXhr.open("DELETE", "/api/customer/logout");
            // logoutXhr.setRequestHeader("Authorization", localStorage.getItem("Authorization"));
            // logoutXhr.addEventListener("loadend", event => {
            //     let status = event.target.status;
            //
            //     if (status === 200) {
            //         this.sendDelete();
            //     }
            //     else{
            //         console.log("로그아웃 실패");
            //     }
            // });
            //
            // logoutXhr.addEventListener("error", event => {
            //     this.showToastMessage('로그아웃에 실패하였습니다.');
            // });
            //
            // logoutXhr.send();
        });
    }
    #sendMainData() {
        console.log("sendMainData 실행");
        const mainXhr = new XMLHttpRequest();
        mainXhr.open("GET", "/api/customer");
        mainXhr.addEventListener("loadend", event => {
            let status = event.target.status;
            const responseValue = JSON.parse(event.target.responseText);
            if (status === 200) {
                this.showIds.textContent=responseValue["userId"];
            }
            else{
                this.sendDelete();
            }
        });

        mainXhr.addEventListener("error", event => {
            this.showToastMessage('로그인에 실패하였습니다.');
        });

        mainXhr.send();
    }

};
// function showIds(){
//     const showIds=document.querySelector(".showId");
//     const userId= document.querySelector(".hidden-id").value;
//     showIds.textContent=userId;
// }
window.onload = function () {
    // const initLoad = new initLoads();
    // initLoad.init();
    const util = new UtilController();
    util.sendAuthorize().then(result => {
        console.log(result);
    })
        .catch(result => {
            console.log(result);
        });
}