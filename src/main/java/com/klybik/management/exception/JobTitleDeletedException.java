package com.klybik.management.exception;

import com.klybik.management.constant.EmployeeManagementSystemConstant.Error.Logic;

public class JobTitleDeletedException extends RuntimeException {
    public JobTitleDeletedException() {
        super(Logic.DELETED_LEAD);
    }
}
