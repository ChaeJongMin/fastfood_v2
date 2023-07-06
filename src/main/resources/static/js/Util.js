import { logout } from '../js/ForAjax/logout.js';
export class UtilController {
    async sendAuthorize() {
        try {
            console.log("sendAuthorize");
            const response = await fetch("/api/auth/authorize", {
                method: "POST",
            });
            if (response.status >= 400 && response.status <= 500) {
                console.log("sendAuthorize: "+response);
                console.log("sendAuthorize: "+response.body);
                const responseJson = JSON.parse(await response.text());
                console.log("responseJson: "+ responseJson);
                if (response.status === 401 && this.checkExpired(responseJson.code)) {
                    await this.sendReissue();
                } else if (response.status === 401 || response.status === 403) {

                    throw new Error("액세스 토큰 에러 발생");
                }
            }
            return true;
        } catch (error){
            console.log("error: "+error);
            alert("현재 접속중인 사용자님에 문제가 생겼습니다. 다시 로그인 해주십시오(액세스)" )
            logout()
        }
    }

    checkExpired(codes){
        console.log("checkExpired: "+codes);
        if(codes==="ACCESS_TOKEN_EXPIRED") {
            console.log("checkExpired: 참");
            return true;
        }
        return false;
    }
    async sendReissue() {
        console.log("sendReissue");
        try {
            const response = await fetch("/api/auth/reissue", {
                method: "POST",
            });
            console.log("가져온 응닶값 리프레쉬: "+response);
            console.log("가져온 응닶값(바디): "+response.body);
            if (response.status >= 400 && response.status <= 500) {
                throw new Error("리프레쉬 토큰 에러 발생");
            }

        } catch (error) {
            alert("현재 접속중인 사용자님에 문제가 생겼습니다. 다시 로그인 해주십시오(리프레쉬)")
            logout()
        }
    }

}
