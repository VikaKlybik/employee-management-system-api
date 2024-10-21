package com.klybik.management.exception;

import static com.klybik.management.constant.EmployeeManagementSystemConstant.Error.Logic;

public class UpdateLeadException extends RuntimeException {
    public UpdateLeadException() {
        super(Logic.UPDATED_LEAD);
    }
}
