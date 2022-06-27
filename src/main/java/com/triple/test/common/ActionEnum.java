package com.triple.test.common;

public enum ActionEnum {
    ADD("ADD"),
    MOD("MOD"),
    DELETE("DELETE");

    private String name;

    ActionEnum(String name){
        this.name = name;
    }
}
