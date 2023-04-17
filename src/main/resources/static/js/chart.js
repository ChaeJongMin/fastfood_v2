
window.onload = function () {
    const ctx = document.getElementById('line-chart').getContext('2d');
    new Chart(ctx,{
        type:'line',
        data:{
            labels:['23/3','23/2','23/1','22/12','22/11','22/10','22/9','22/8','22/7','22/6','22/5','22/4'],
            datasets:{
                label:'Month Total Sales',
                data:[10, 8 , 6 , 5 , 12 , 7,10, 8 , 6 , 5 , 12 , 7,12,2]
            }
        }
    });
}