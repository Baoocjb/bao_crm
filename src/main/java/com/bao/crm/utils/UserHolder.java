package com.bao.crm.utils;

import com.bao.crm.model.UserModel;

public class UserHolder {
    private static final ThreadLocal<UserModel> tl = new ThreadLocal<>();

    public static void saveUser(UserModel user){
        tl.set(user);
    }

    public static UserModel getUser(){
        return tl.get();
    }

    public static void removeUser(){
        tl.remove();
    }
}
