package com.bao.crm.interceptor;


import com.bao.crm.exceptions.NoLoginException;
import com.bao.crm.service.UserService;
import com.bao.crm.utils.LoginUserUtil;
import com.bao.crm.utils.UserHolder;
import com.bao.crm.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User user = userService.selectByPrimaryKey(LoginUserUtil.releaseUserIdFromCookie(request));
        if(user == null){
            throw new NoLoginException();
        }
        UserHolder.saveUser(LoginUserUtil.buildUserInfo(user));
        return true;
    }
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserHolder.removeUser();
    }
}
