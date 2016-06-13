package com.arcquim.system.android.utils;

public class Validator {

    private static final Validator INSTANCE = new Validator();

    private static final String EMAIL_REGEX = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";

    private Validator() {

    }

    public static Validator getInstance() {
        return INSTANCE;
    }

    public boolean isEmailValid(String email) {
        return email.trim().matches(EMAIL_REGEX);
    }

}
