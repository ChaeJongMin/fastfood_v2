let Set = {
    totalPrice: 0, 
   
    //재계산
    reCalc: function(pos){
        this.totalPrice =0;
        alert("js 들어옴"+pos);
         var sel1=document.querySelector('input[name=sideradio'+pos+']:checked').value;        
        
        var sel2=document.querySelector('input[name=drinkradio'+pos+']:checked').value;
        var sel3=document.querySelector('input[name=setSizeradio'+pos+']:checked').value;
       
        
        var defprice=document.getElementById("setsprice"+pos).value;
        defprice*=1;
       
          
        var sideinfo=sel1.split(",");
        var drinkinfo=sel2.split(",");
        
        var sideprice=sideinfo[1];
        sideprice*=1;
        var drinkprice=drinkinfo[1];
        drinkprice*=1;
        var sizeprice=0;        
        if(sel3!=="기본"){sizeprice=1000;}      
                                                                                                                         
        this.totalPrice=(defprice+sideprice+drinkprice+sizeprice);
         alert("전체가격: "+this.totalPrice);           
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