let Set = {
    totalPrice: 0, 
   
    //재계산
    reCalc: function(pos){
        this.totalPrice =0;
         let sel1=document.querySelector('input[name=sideradio'+pos+']:checked').value;

        let sel2=document.querySelector('input[name=drinkradio'+pos+']:checked').value;
        let sel3=document.querySelector('input[name=setSizeradio'+pos+']:checked').value;


        let defprice=document.getElementById("setsprice"+pos).value;
        defprice*=1;


        let sideinfo=sel1.split(",");
        let drinkinfo=sel2.split(",");

        let sideprice=sideinfo[1];
        sideprice*=1;
        let drinkprice=drinkinfo[1];
        drinkprice*=1;
        let sizeprice=0;

        if(sel3!=3){sizeprice=1000;}

        this.totalPrice=(defprice+sideprice+drinkprice+sizeprice);
      //화면 업데이트
   this.updateUI(pos);   

   },
 updateUI: function (pos) {      
        document.querySelector('#sum_total_price'+pos).textContent =  this.totalPrice.formatNumber() + '원';
    }
}
// 숫자 3자리 콤마찍기
Number.prototype.formatNumber = function(){
	return this;
    
};