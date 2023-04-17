class UtilController {
    showToastMessage(message) {
        Toastify({
            text: message,
            duration: 3000,
            close: true,
            position: "center",
            stopOnFocus: true,
            style: {
                background: "linear-gradient(to right, #00b09b, #96c93d)",
            },
            onClick: function(){window.location.href="/fastfood/login";}
        }).showToast();
    }

    sendAuthorize() {
        return new Promise((resolve, reject) => {
            const authorizeXhr = new XMLHttpRequest();
            authorizeXhr.open("POST", "/api/auth/authorize", true);
            authorizeXhr.setRequestHeader("Authorization", localStorage.getItem("Authorization"));
            authorizeXhr.addEventListener("loadend", event => {
                let status = event.target.status;
                console.log(" sendAuthorize 가져온 상태 "+status+" 메시지: "+event.target.responseText);
                if ((status >= 400 && status <= 500) || (status > 500)) {
                    let responseJson= JSON.parse(event.target.responseText);
                    if(status==401 && this.checkExpired(responseJson.code)){
                        this.sendReissue();
                        resolve(true);
                    }
                    if(status==401 || status==403) {
                        this.showToastMessage('인증에 실패하였습니다.');
                        resolve(false);
                    }
                }
                else
                    resolve(true);
            });

            authorizeXhr.addEventListener("error", event => {
                this.sendDelete();
            });

            authorizeXhr.send();
        });
    }
    checkExpired(codes){
        console.log("checkExpired: "+codes);
        if(codes==="ACCESS_TOKEN_EXPIRED") {
            console.log("checkExpired: 참");
            return true;
        }
        return false;
    }
    sendReissue() {
        console.log("sendReissue");
        const reissueXhr = new XMLHttpRequest();
        reissueXhr.open("POST", "/api/auth/reissue", false);
        reissueXhr.setRequestHeader("Authorization", localStorage.getItem("Authorization"));

        reissueXhr.addEventListener("loadend", event => {
            let status = event.target.status;
            console.log(" sendReissue 가져온 상태 "+status+" 메시지: "+event.target.responseText);
            // let responseS = JSON.stringify(event.target.responseText);
            // let responseValue=JSON.parse(responseS);
            let responseValue=JSON.parse(event.target.responseText);
            //새로 받아온 accessToken을 다시 로컬스토리지에 저장
            // if (((status >= 400 && status <= 500) || (status > 500)) === false)
            if (status == 200) {
                console.log("access토큰: "+responseValue.grantType+" "+responseValue.accessToken);
                localStorage.setItem("Authorization", responseValue.grantType+responseValue.accessToken);
            }
            else{
                this.sendDelete();
            }
        });
        reissueXhr.addEventListener("error", event => {
            this.sendDelete();
        });

        reissueXhr.send();
    }
    sendDelete(){
        console.log("sendDelete");
        let deleteXhr = new XMLHttpRequest();
        deleteXhr.open("DELETE", "/api/auth/delete", false);
        //deleteXhr.setRequestHeader("Authorization", localStorage.getItem("Authorization"));

        deleteXhr.addEventListener("loadend", event => {
            let status = event.target.status;
            // let responseS = JSON.stringify(event.target.responseText);
            // let responseValue=JSON.parse(responseS);
            //새로 받아온 accessToken을 다시 로컬스토리지에 저장
            // if (((status >= 400 && status <= 500) || (status > 500)) === false)
            if (status == 200) {
                console.log("쿠키와 로컬 스토리지 삭제");
                this.deleteCookie("refreshToken");A
                window.localStorage.removeItem("Authorization");
                alert("인증 실패로 로그인으로 돌아갑니다.....")
                window.location.href="/fastfood/login"
            }
        });
        deleteXhr.addEventListener("error", event => {
            this.showToastMessage('초기화에 실패하였습니다.');
        });

        deleteXhr.send();
    }
    deleteCookie(name){
        console.log("deleteCookie 작동");
        document.cookie = name + '=; expires=Thu, 01 Jan 1999 00:00:10 GMT;domain=C.kr; path=/;';
    }
}
