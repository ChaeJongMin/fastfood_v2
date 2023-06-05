package com.example.demo.Controller.Api;

import com.example.demo.Service.ManageService;
import com.example.demo.persistence.QueryFor.CategoryNameAndCnt;
import com.example.demo.persistence.QueryFor.OrderSummary;
import com.example.demo.persistence.QueryFor.VisitorSummary;
import com.nimbusds.jose.shaded.json.JSONObject;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ChartApiController {
    private final ManageService manageService;

    @GetMapping("/api/chart")
    public ResponseEntity<?> getData(){
        JSONObject obj = new JSONObject();

        //1. 첫번쨰 chart (12개월간의 판매액)
        List<OrderSummary> orderSummaryList=manageService.getOrderSummary();
        for(OrderSummary o : orderSummaryList ){
            System.out.println(o.getYearMonth()+" "+o.getTotalPrice());
        }
        obj.put("orderSummaryList",orderSummaryList);
        //2. 두번쨰 chart (각 카테고리별 판매 수)
        List<CategoryNameAndCnt> cateList=manageService.getCategoryNameAndCnt();
        obj.put("cateList",cateList);
        //3. 세번쨰 chart (9개월 간 접속한 유저 수)
        List<VisitorSummary> visitorSummaryList=manageService.getVisitorSummary();
        obj.put("visitorSummaryList",visitorSummaryList);
        System.out.println(obj.toJSONString());
        return ResponseEntity.status(HttpStatus.OK).body(obj);
    }
}
