//logout을 유도하는 api 통신을 호출
export function logout(){
    const result=confirm("로그아웃 하겠습니까??");
    if(result){
        $.ajax({
            type: 'POST',
            Accept: "text/plain" ,
            url : '/customer/logout',
            success:function(result) {
                window.location.href="/fastfood/login";
            }
        });
    }
}