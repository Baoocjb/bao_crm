package com.bao.crm;

import com.alibaba.fastjson.JSON;
import com.bao.crm.base.ResultInfo;
import com.bao.crm.exceptions.NoLoginException;
import com.bao.crm.exceptions.ParamsException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 全局异常处理器
 */
@Component
public class GlobalExceptionResolver implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) {
        if (e instanceof NoLoginException) {
            ModelAndView mv = new ModelAndView("redirect:/index");
            return mv;
        }

        // 设置默认异常返回视图
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("code", 500);
        modelAndView.addObject("msg", "系统异常,请稍后重试...");

        // 判断全局异常是否是方法抛出
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            ResponseBody responseBody = handlerMethod.getMethod().getDeclaredAnnotation(ResponseBody.class);
            // 判断该方法返回值是对象还是视图
            if (responseBody == null) {
                // 返回视图
                // 判断异常类型
                if (e instanceof ParamsException) {
                    ParamsException p = (ParamsException) e;
                    modelAndView.addObject("code", p.getCode());
                    modelAndView.addObject("msg", p.getMsg());
                }
                return modelAndView;
            } else {
                // 返回对象
                ResultInfo resultInfo = new ResultInfo();
                resultInfo.setMsg("系统异常,请稍后重试...");

                resultInfo.setCode(500);

                if (e instanceof ParamsException) {
                    ParamsException p = (ParamsException) e;
                    resultInfo.setCode(p.getCode());
                    resultInfo.setMsg(p.getMsg());
                }
                // 设置响应类型以及编码格式(JSON格式)
                response.setContentType("application/json;charset=utf-8");
                PrintWriter writer = null;
                try {
                    writer = response.getWriter();
                    // 将resultInfo转为Json格式
                    String jsonString = JSON.toJSONString(resultInfo);
                    writer.write(jsonString);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                } finally {
                    if (writer != null) {
                        writer.close();
                    }
                }
            }
        }
        return modelAndView;
    }
}
