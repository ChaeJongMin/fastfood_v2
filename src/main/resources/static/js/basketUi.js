import { UtilController } from './Util.js';
const util=new UtilController();


const basket = {
    totalCount: 0, 
    totalPrice: 0,
    //체크한 장바구니 상품 비우기
    delCheckedItem: function(){
        if(document.querySelectorAll("input[name=buy]:checked").length==
            document.querySelectorAll("input[name=buy]").length){
            if(confirm("마지막 음식까지 삭제하면 메뉴페이지로 돌아갑니다.")){
                document.querySelectorAll("input[name=buy]:checked").forEach(function (item) {
                    item.parentElement.parentElement.parentElement.remove();
                });
                //전송 처리 결과가 성공이면
                this.reCalc();
                this.updateUI();
                alert("메뉴 페이지로 돌아갑니다....");
                //ajax
                callAjax();
            }
        } else{
            document.querySelectorAll("input[name=buy]:checked").forEach(function (item) {
                item.parentElement.parentElement.parentElement.remove();
            });
            //전송 처리 결과가 성공이면
            this.reCalc();
            this.updateUI();
        }

    },
    //장바구니 전체 비우기
    delAllItem: function(){
        if(confirm("장바구니를 초기화하면 메뉴페이지로 돌아갑니다.")){
            document.querySelectorAll('.row.data').forEach(function (item) {
                item.remove();
            });
            //전송 처리 결과가 성공이면
            this.totalCount = 0;
            this.totalPrice = 0;
            this.reCalc();
            this.updateUI();
            alert("메뉴 페이지로 돌아갑니다....");

            //AJAX 서버 업데이트 전송
            callAjax();
        }
    },
    //재계산
    reCalc: function(){
        this.totalCount = 0;
        this.totalPrice = 0;
        document.querySelectorAll(".p_num").forEach(function (item) {
            if(item.parentElement.parentElement.parentElement.previousElementSibling.firstElementChild.firstElementChild.checked == true){
                var count = parseInt(item.getAttribute('value'));
                this.totalCount += count;
                var price = item.parentElement.parentElement.previousElementSibling.firstElementChild.getAttribute('value');
                this.totalPrice += count * price;
            }
        }, this); // forEach 2번째 파라메터로 객체를 넘겨서 this 가 객체리터럴을 가리키도록 함. - thisArg
    },
    //화면 업데이트
    updateUI: function () {
        document.querySelector('#sum_p_num').textContent = '상품갯수: ' + this.totalCount.formatNumber() + '개';
        document.querySelector('#sum_p_price').textContent = '합계금액: ' + this.totalPrice.formatNumber() + '원';
    },
    //개별 수량 변경
    changePNum: function (pos) {
        console.log("수량 변경!!!");
        let item = document.querySelector('input[name=p_num'+pos+']');
        let p_num = parseInt(item.getAttribute('value'));
        let newval = event.target.classList.contains('up') ? p_num+1 : event.target.classList.contains('down') ? p_num-1 : event.target.value;
        
        if (parseInt(newval) < 1 || parseInt(newval) > 99) { return false; }

        item.setAttribute('value', newval);
        item.value = newval;

        let price = item.parentElement.parentElement.previousElementSibling.firstElementChild.getAttribute('value');
        item.parentElement.parentElement.nextElementSibling.textContent = (newval * price).formatNumber()+"원";

        this.reCalc();
        this.updateUI();
    },
    checkItem: function () {
        this.reCalc();
        this.updateUI();
    },
    delItem: function () {
        if(document.querySelectorAll("input[name=buy]").length==1){
            if(confirm("마지막 음식까지 삭제하면 메뉴페이지로 돌아갑니다.")){
                event.target.parentElement.parentElement.parentElement.remove();
                this.reCalc();
                this.updateUI();
                alert("메뉴 페이지로 돌아갑니다....");
                //ajax
                callAjax();
            }
        } else{
            event.target.parentElement.parentElement.parentElement.remove();
            this.reCalc();
            this.updateUI();
        }

    }
}

// 숫자 3자리 콤마찍기
Number.prototype.formatNumber = function(){
    if(this==0) return 0;
    let regex = /(^[+-]?\d+)(\d{3})/;
    let nstr = (this + '');
    while (regex.test(nstr)) nstr = nstr.replace(regex, '$1' + ',' + '$2');
    return nstr;
};
function callAjax(){
    util.sendAuthorize().then(result=> {
        if (result === true) {
            let id=document.querySelector('#userId').value;
            $.ajax({
                type: 'DELETE',
                url : '/api/basket/'+id,
                contentType: 'application/json; charset=utf-8'
            }).done(function(){
                window.location.href="/fastfood/menu";
            }).fail(function(error){
                alert(JSON.stringify(error));
            });
        }
    });

}