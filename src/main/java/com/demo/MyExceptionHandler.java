package com.demo;

import com.demo.bean.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MyExceptionHandler implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
                                         Exception ex) {
        if (ex instanceof Exception) {
            ModelAndView mv = new ModelAndView();
            /* 使用response返回 */
            response.setStatus(HttpStatus.OK.value()); // 设置状态码
            response.setContentType(MediaType.APPLICATION_JSON_VALUE); // 设置ContentType
            response.setCharacterEncoding("UTF-8"); // 避免乱码
            try {
                response.getWriter().write("你想返回的JSON字符串");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return mv;
        }
        return null;
    }
}