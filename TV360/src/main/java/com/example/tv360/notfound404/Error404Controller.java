package com.example.tv360.notfound404;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Error404Controller implements ErrorController {

    private static final String ERROR_PATH = "/error";

    @RequestMapping(ERROR_PATH)
    public String handleError() {
        return "error404"; // Trả về tên của trang HTML báo lỗi tùy chỉnh
    }

    public String getErrorPath() {
        return ERROR_PATH;
    }
}