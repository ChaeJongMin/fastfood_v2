import { UtilController } from './Util.js';
import {logout} from "./ForAjax/logout.js";

window.onload = function () {
    console.log("메뉴페이지 실행");
    const util = new UtilController();
    util.sendAuthorize().then(result => {
        console.log(result);
    }).catch(result => {
            console.log(result);
    });

    fetch("/api/customer", {
        method: "GET",
    })
        .then((response) => {
            return response.text(); // 스트림을 텍스트로 변환하여 Promise 반환
        })
        .then((data) => {
            const showIds = document.querySelector(".showId");
            showIds.textContent = data;
        })
        .catch((err) => {
            console.log(err);
            alert("사용자의 인증 과정에서 알 수 없는 오류가 발생했습니다.");
            logout();
        });
}
