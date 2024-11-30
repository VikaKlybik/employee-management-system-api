package com.klybik.management.constant;

public interface EmployeeManagementSystemConstant {
    interface Error {
        interface NotFound {
            String USER = "User with email = '%s' not found";
            String ROLE = "Role '%s' not found";
            String JOB_TITLE = "Job title '%s' not found";
            String DEPARTMENT = "Department '%s' not found";
            String KPI = "KPI '%s' not found";
            String COMPETENCY = "Competency '%s' not found";
            String SURVEY = "Survey '%s' not found";
            String QUESTION = "Question '%s' not found";
            String EMPLOYEE = "Employee '%s' not found";
            String KPI_PERIOD = "KPI period '%s' not found";
            String DEVELOPMENT_PLAN = "Development plan '%s' not found";
        }
        interface Logic {
            String INVALID_PASSWORD = "Invalid password";
            String UPDATED_LEAD = "A leader has subordinates. Cannot update the 'is_lead' field.";
            String DELETED_LEAD = "A leader has subordinates. Cannot delete.";
            String NOT_LEADER = "Job title with id '%s' is not leader.";
            String KPI_NOT_BELONGS_TO_EMPLOYEE = "Employee with id '%s' is not belong to the employee list.";
            String SURVEY_CLOSED = "Survey has already been closed.";
            String SURVEY_PUBLISHED = "Survey has already been published.";
            String INVALID_SURVEY_STATUS = "Invalid survey status '%s'";
            String INVALID_DEVELOPMENT = "Invalid development plan data";
        }
        interface Duplicate {
            String USER = "User with email = '%s' already exists";
            String DEPARTMENT = "Department '%s' already exists";
            String JOB_TITLE = "Job title '%s' already exists in department with id '%s'";
            String COMPETENCY = "Competency '%s' already exists";
            String KPI = "KPI '%s' already exists";
        }
    }

}
