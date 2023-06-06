import { UtilController } from './Util.js';
const util=new UtilController();

//차트에 그릴 데이터를 받기 위해 api 통신
async function request() {
    await fetch('http://localhost:8080/api/chart', {
        method: 'GET',
    })
        .then(response => {
            return response.json();
        })
        .then(data =>{
            //받아온 데이터로 chart를 그리는 함수
            drawChart(data);
        })
}
//chart 그리는 함수
function drawChart(dataSet){

    //각 차트에 필요한 데이터를 분리
    //dataSet은 객체안에 객체로 구성되어있으며 map를 통해 배열로 변환

    //각 달에 판매 금액을 담당
    const ctxLabels=dataSet.orderSummaryList.map((result)=>result.yearMonth);
    const ctxdatas=dataSet.orderSummaryList.map((result)=>result.totalPrice);

    //카테고리별 판매된 제품 수를 담당
    const ctx2Labels=dataSet.cateList.map((result)=>result.name);
    const ctx2datas=dataSet.cateList.map((result)=>result.cnt);

    //각 월에 방분한 유저의 수를 담당
    const ctx3Labels=dataSet.visitorSummaryList.map((result)=>result.yearMonth);
    const ctx3datas=dataSet.visitorSummaryList.map((result)=>result.totalNum);

    //1번쨰 차트 (선 차트) = 각 달에 판매 금액
    let ctx = document.getElementById('line-chart').getContext('2d');
    //Chart 객체를 생성
    new Chart(ctx, {
        //차트 형태를 선 차트로 결정
        type: 'line',
        //해당 차트에 들어갈 데이터 정리
        data: {
            // 라벨(x축) = 년도.월
            labels: ctxLabels,
            //데이터
            datasets: [{
                //데이터 = 판매금액 (x축 라벨에 해당하는 데이터)
                data: ctxdatas,
                //경계선 색상
                borderColor: "#ff0000",
                //배경색
                backgroundColor: "#eb5667"
            }]
        },
        //차트의 옵션을 설정
        options: {
            //제목 설정
            title: {
                display: true,
                text: 'Total Sales per Month (KRW)'
            },
            // 범례 설정
            legend: {
                display: true,
                // 아래 부분에서 범례 라벨 설정
                labels: {
                    boxWidth: 0,
                    //제목 라벨을 안보이게
                    fontColor: 'rgba(0,0,0,0)',
                    fontSize: 20
                }
            },

        },

    });
    //2번쨰 차트 (파이 차트) = 카테고리별 판매된 제품 수
    let ctx2 = document.getElementById('pie-chart').getContext('2d');
    new Chart(ctx2, {
        type: 'pie',
        data: {
            //카테고리명
            labels: ctx2Labels,
            datasets: [{
                //카테고리 수가 적어 직접 색상을 설정
                backgroundColor: ["#3e95cd", "#8e5ea2", "#3cba9f", "#e8c3b9", "#c45850", "#f81605"],
                data: ctx2datas,
            }]
        },
        options: {
            title: {
                display: true,
                text: 'Sales order by category'
            }
        }

    });
    //3번쨰 차트 (파이 차트) = 각 월에 방분한 유저의 수
    let ctx3 = document.getElementById('user-line-chart').getContext('2d');
    new Chart(ctx3, {
        type: 'line',
        data: {
            labels: ctx3Labels,
            datasets: [{
                data: ctx3datas,
                // 베지어 곡선 장력을 0으로 설정
                //0으로 설정하면 직선
                LineTension: 0,
                //선 아래 영역을 채우지 않는다.
                fill: false,
                borderColor: "white",
                pointBorderColor : 'white',
                pointBackgroundColor : 'white',
            }]
        },
        options: {
            title: {
                display: true,
                text: 'Number of visitors(Month)'
            },
            legend: {
                display: true,
                labels: {
                    boxWidth: 0,
                    fontColor: 'rgba(0,0,0,0)', //제목 레이브 안보이게
                    fontSize: 20
                }
            },
            //측도 옵션을 설정
            scales: {
                //x축 설정
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
                //y축 설정
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
