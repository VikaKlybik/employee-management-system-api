package com.klybik.management.constant;

public interface EmployeeManagementSystemConstant {
    interface Error {
        interface NotFound {
            String USER = "User with email = '%s' not found";
            String ROLE = "Role '%s' not found";
            String JOB_TITLE = "Job title '%s' not found";
            String DEPARTMENT = "Department '%s' not found";
            String KPI = "KPI '%s' not found";
        }
        interface Logic {
            String INVALID_PASSWORD = "Invalid password";
            String UPDATED_LEAD = "A leader has subordinates. Cannot update the 'is_lead' field.";
            String DELETED_LEAD = "A leader has subordinates. Cannot delete.";
            String NOT_LEADER = "Job title with id '%s' is not leader.";
            String KPI_NOT_BELONGS_TO_EMPLOYEE = "Employee with id '%s' is not belong to the employee list.";
        }
        interface Duplicate {
            String USER = "User with email = '%s' already exists";
            String DEPARTMENT = "Department '%s' already exists";
            String JOB_TITLE = "Job title '%s' already exists in department with id '%s'";
        }
    }

}
