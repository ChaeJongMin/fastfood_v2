package com.example.demo.Controller.Error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@Controller // This means that this class is a Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping(path = "/error")
    public String handleError(HttpServletRequest request) {
        // Log here !!!
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        log.info("CustomErrorController 작동");
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                return "error/error404";
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return "error/error500";
            } else if (statusCode == HttpStatus.UNAUTHORIZED.value()) {
                return "error/error401";
            }
        }
        return "error/error404";
    }

}
