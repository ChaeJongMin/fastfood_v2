import { UtilController } from './Util.js';
const util=new UtilController();

window.onload = function (){

            let saveEle=document.querySelectorAll('#save-button');
            let saveSetEle=document.querySelectorAll('#save-set-button');
            // let buyEle=document.querySelector('#basket-update-btn');
            if(saveEle!=null){
                saveEle.forEach(function (btnItem) {
                    btnItem.addEventListener('click',function (){
                        let savaForm=this.closest('.single').querySelector('#save-product-form');
                        save(savaForm);
                    })
                })
            }
            if(saveSetEle!=null){
                saveSetEle.forEach(function (btnItem) {
                    btnItem.addEventListener('click',function (){
                        let savaForm=this.closest('.sets').querySelector('#save-set-form');
                        setSave(savaForm);
                    })
                })
            }


}
function save(saveForm) {
    util.sendAuthorize().then(result=> {
        if (result === true) {
            let data={
                pid:saveForm.querySelector('#pid').value,
                size: saveForm.querySelector('input[name="size"]:checked').value,
                temp: saveForm.querySelector('input[name="temp"]:checked').value
            }
            let productName=saveForm.querySelector('#productNames').value
            let id=document.querySelector('#user-ids').value;
            $.ajax({
                type: 'POST',
                url : '/api/basket/'+id,
                dataType: 'json',
                contentType: 'application/json; charset=utf-8',
                data: JSON.stringify(data),
            }).done(function(cnt){
                document.querySelector('#basketCounts').textContent=cnt.formatNumber();
                document.querySelector('#h-basket-size').value=cnt;
                alert(productName+"를 담았습니다!!!!");
            }).fail(function(error){
                alert(JSON.stringify(error));
            });
        }
    });

}

function setSave(saveForm) {
    util.sendAuthorize().then(result=> {
        if (result === true) {
            let OriginpriceTxt = saveForm.closest('.set-container').querySelector('.set-price').textContent;
            let prices = OriginpriceTxt.slice(0, OriginpriceTxt.length - 1);
            let id=document.querySelector('#user-ids').value;
            let data = {
                //음식명, 사이드명, 음료수, 사이즈, 가격
                pid: saveForm.querySelector('#set-pid').value,
                side: saveForm.querySelector('input[class="set-side"]:checked').value.split(",")[0],
                drink: saveForm.querySelector('input[class="set-drink"]:checked').value.split(",")[0],
                size: saveForm.querySelector('input[class="set-size"]:checked').value,
                price: prices
            }
            //세트명
            let productName = saveForm.querySelector('#set-productNames').value;
            //에이젝스
            $.ajax({
                type: 'POST',
                url: '/api/basket/'+id,
                dataType: 'json',
                contentType: 'application/json; charset=utf-8',
                data: JSON.stringify(data),
            }).done(function(cnt){
                document.querySelector('#basketCounts').textContent=cnt.formatNumber();
                alert(productName+"를 담았습니다!!!!");
            }).fail(function(error){
                alert(JSON.stringify(error));
            });
        }
    });
}
function buys(){
    util.sendAuthorize().then(result=> {
        if (result === true) {
            let data=[];
            let regex = /[^0-9]/g;
            document.querySelectorAll('#basket-info').forEach(function (infoSet) {
                if (infoSet.querySelector('input[name="buy"]').checked) {
                    let list_piece = {
                        //아이디, 수량, 가격
                        bid: infoSet.querySelector('input[name="buy"]:checked').value,
                        pcount: infoSet.querySelector('#item-cnt').value,
                        price: infoSet.querySelector('#item-sum').textContent.replace(regex, "")
                    }
                    data.push(list_piece);
                }
            });

            let id = document.querySelector('#userId').value;
            $.ajax({
                type: 'PUT',
                url : '/api/basket/'+id,
                dataType: 'JSON',
                contentType: 'application/json; charset=utf-8',
                data: JSON.stringify(data)
            }).success(function(result){
                console.log("success: "+result);
                window.location.href="/fastfood/Payment";
            }).error(function(error){
                alert(JSON.stringify(error));
                console.log("error");
            });
        }
    });

}
function emptyCheck(cnt){
    if(cnt==0){
      alert("장바구니가 비어있습니다... 음식을 담아주세요.")
    } else {
        if(confirm("장바구니로 이동하겠습니까??")){
                window.location.href="/fastfood/my_baket";
        }

    }
}
