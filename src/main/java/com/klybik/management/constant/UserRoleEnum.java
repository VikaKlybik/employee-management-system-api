package com.klybik.management.constant;

import lombok.Getter;

@Getter
public enum UserRoleEnum {
    ADMIN("admin"), MANAGER("manager"), EMPLOYEE("employee");
    private final String value;

    UserRoleEnum(String value) {
        this.value = value;
    }
}
