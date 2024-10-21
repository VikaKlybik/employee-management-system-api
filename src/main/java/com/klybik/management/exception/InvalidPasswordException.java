package com.klybik.management.exception;

import com.klybik.management.constant.EmployeeManagementSystemConstant.Error.Logic;

public class InvalidPasswordException extends RuntimeException {

    public InvalidPasswordException() {
        super(Logic.INVALID_PASSWORD);
    }
}
