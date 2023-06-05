import { UtilController } from './Util.js';
const util=new UtilController();

async function request() {
    await fetch('http://localhost:8080/api/chart', {
        method: 'GET',
    })
        .then(response => {
            return response.json();
        })
        .then(data =>{
            drawChart(data);
        })
}
function drawChart(dataSet){
    // console.log(dataSet.visitorSummaryList);
    // console.log(dataSet.orderSummaryList);
    // console.log(dataSet.cateList);

    const ctxLabels=dataSet.orderSummaryList.map((result)=>result.yearMonth);
    const ctxdatas=dataSet.orderSummaryList.map((result)=>result.totalPrice);
    // console.log(ctxLabels);
    // console.log(ctxdatas);

    const ctx2Labels=dataSet.cateList.map((result)=>result.name);
    const ctx2datas=dataSet.cateList.map((result)=>result.cnt);
    // console.log(ctx2Labels);
    // console.log(ctx2datas);

    const ctx3Labels=dataSet.visitorSummaryList.map((result)=>result.yearMonth);
    const ctx3datas=dataSet.visitorSummaryList.map((result)=>result.totalNum);
    // console.log(ctx3Labels);
    // console.log(ctx3datas);

    let ctx = document.getElementById('line-chart').getContext('2d');
    new Chart(ctx, {
        type: 'line',
        data: {
            labels: ctxLabels,
            datasets: [{
                data: ctxdatas,
                borderColor: "#ff0000",
                backgroundColor: "#eb5667"
            }]
        },
        options: {
            title: {
                display: true,
                text: 'Total Sales per Month (KRW)'
            },
            legend: {
                display: true,
                labels: {
                    boxWidth: 0,
                    fontColor: 'rgba(0,0,0,0)', //제목 레이브 안보이게
                    fontSize: 20
                }
            },

        },

    });

    let ctx2 = document.getElementById('pie-chart').getContext('2d');
    new Chart(ctx2, {
        type: 'pie',
        data: {
            labels: ctx2Labels,
            datasets: [{
                backgroundColor: ["#3e95cd", "#8e5ea2", "#3cba9f", "#e8c3b9", "#c45850", "#f81605"],
                data: ctx2datas,
            }]
        },
        options: {
            title: {
                display: true,
                text: 'Sales order by category(Month)'
            }
        }

    });

    let ctx3 = document.getElementById('user-line-chart').getContext('2d');
    new Chart(ctx3, {
        type: 'line',
        data: {
            labels: ctx3Labels,
            datasets: [{
                data: ctx3datas,
                LineTension: 0,
                fill: false,
                borderColor: "white",
                pointBorderColor : 'white',
                pointBackgroundColor : 'white',
            }]
        },
        options: {
            legend: {
                display: true,
                labels: {
                    boxWidth: 0,
                    fontColor: 'rgba(0,0,0,0)', //제목 레이브 안보이게
                    fontSize: 20
                }
            },
            scales: {
                xAxes: [{
                    ticks: {
                        fontSize: 11, //x축 텍스트 폰트 사이즈
                        fontColor: 'rgba(0,0,0,0)', //x축 레이브 안보이게
                    },
                    gridLines: {
                        display: false,
                        lineWidth: 0
                    }
                }],
                yAxes: [{
                    ticks: {
                        display: false, //y축 텍스트 삭제
                        beginAtZero: false, //y축값이 0부터 시작
                    },
                    gridLines: {
                        display: false,
                        lineWidth: 0
                    }
                }]
            }
        }
    });
}
window.onload = function () {
    util.sendAuthorize().then(result=> {
       if(result==true){
          //const data=request();
           request();
       }
    });

}
