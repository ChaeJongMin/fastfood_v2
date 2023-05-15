import { UtilController } from './Util.js';
const util=new UtilController();

window.onload = function () {
    let saveEle=document.querySelector('#order-save-btn');
    if(saveEle!=null){
        saveEle.addEventListener('click',function (){
            orderSave();
        })
    }
}

function orderSave(){
    //빈칸여부
    util.sendAuthorize().then(result=> {
        if (result === true) {
            let cardNum=document.querySelector('input[name="cardNumber"]').value;
            if(cardNum==undefined || cardNum==null || cardNum===""){
                document.querySelector('.error-text').innerHTML="카드번호를 적어주세요.";
                return;
            }
            //유저아이디,카드번호, 카드회사
            let data={
                cardNum:document.querySelector('input[name="cardNumber"]').value,
                cardCompany:document.querySelector('#card-company > option:checked').value
            }
            let id=document.querySelector('#user-id').value;
            $.ajax({
                type: 'POST',
                url: '/api/order/'+id,
                dataType: 'json',
                contentType:'application/json; charset=utf-8',
                data: JSON.stringify(data)
            }).done(function(results) {
                // alert('주문완료!!! 메뉴로 돌아갑니다.');
                // window.location.reload();
                if(results!=-1){
                    alert('주문완료!!! 메뉴로 돌아갑니다.');
                    window.location.href="/fastfood/menu";
                }
                else{
                    document.querySelector('.error-text').innerHTML="정보가 틀렸습니다.";
                }
            }).fail(function (error) {
                alert(JSON.stringify(error));
            });
        }
    });

}
