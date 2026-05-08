package com.javaApp.finance.service;

public class CurrentUserService {
    private static int id = 1;

    public static int authUserId() {
        return id;
    }

    public static void setAuthUserId(int id) {
        CurrentUserService.id = id;
    }
}
