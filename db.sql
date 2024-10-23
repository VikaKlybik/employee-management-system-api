-- Created by Vertabelo (http://vertabelo.com)
-- Last modification date: 2024-10-17 16:00:12.583

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";;

-- tables
-- Table: assessment_summary
CREATE TABLE assessment_summary (
                                    id uuid  NOT NULL DEFAULT uuid_generate_v4(),
                                    assessment_summary decimal(10,2)  NOT NULL,
                                    total_reviews  int  NOT NULL,
                                    employee_id uuid  NOT NULL,
                                    competency_id uuid  NOT NULL,
                                    survey_id uuid  NOT NULL,
                                    CONSTRAINT assessment_summary_pk PRIMARY KEY (id)
);

-- Table: competency
CREATE TABLE competency (
                            id uuid  NOT NULL,
                            name varchar(255)  NOT NULL,
                            description text  NOT NULL,
                            CONSTRAINT competency_pk PRIMARY KEY (id)
);

-- Table: department
CREATE TABLE department (
                            id uuid  NOT NULL DEFAULT uuid_generate_v4(),
                            name varchar(255)  NOT NULL,
                            CONSTRAINT department_pk PRIMARY KEY (id)
);

-- Table: development_plan
CREATE TABLE development_plan (
                                  id uuid  NOT NULL DEFAULT uuid_generate_v4(),
                                  status int  NOT NULL,
                                  comments text  NOT NULL,
                                  goal text  NOT NULL,
                                  survey_id uuid  NOT NULL,
                                  competency_id uuid  NOT NULL,
                                  employee_id uuid  NOT NULL,
                                  CONSTRAINT development_plan_pk PRIMARY KEY (id)
);

-- Table: employee
CREATE TABLE employee (
                          id uuid  NOT NULL DEFAULT uuid_generate_v4(),
                          job_title_id uuid  NOT NULL,
                          CONSTRAINT employee_pk PRIMARY KEY (id)
);

-- Table: evaluated_person
CREATE TABLE evaluated_person (
                                  id uuid  NOT NULL,
                                  employee_id uuid  NOT NULL,
                                  CONSTRAINT evaluated_person_pk PRIMARY KEY (id)
);

-- Table: evaluators
CREATE TABLE evaluators (
                            id uuid  NOT NULL DEFAULT uuid_generate_v4(),
                            employee_id uuid  NOT NULL,
                            CONSTRAINT evaluators_pk PRIMARY KEY (id)
);

-- Table: job_title
CREATE TABLE job_title (
                           id uuid  NOT NULL DEFAULT uuid_generate_v4(),
                           department_id uuid  NOT NULL,
                           name varchar(255)  NOT NULL,
                           is_lead boolean NOT NULL,
                           lead_id uuid  NULL,
                           CONSTRAINT job_title_pk PRIMARY KEY (id)
);

-- Table: kpi
CREATE TABLE kpi (
                     id uuid  NOT NULL DEFAULT uuid_generate_v4(),
                     name text  NOT NULL,
                     description text  NOT NULL,
                     target_value decimal(10,2)  NOT NULL,
                     measure_unit int  NOT NULL,
                     weight decimal(5,2)  NOT NULL,
                     employee_id uuid  NOT NULL,
                     CONSTRAINT kpi_pk PRIMARY KEY (id)
);

-- Table: kpi_assessment
CREATE TABLE kpi_assessment (
                                id uuid  NOT NULL DEFAULT uuid_generate_v4(),
                                actual_value decimal(10,2)  NOT NULL,
                                assessment_date timestamp  NOT NULL,
                                comments text  NOT NULL,
                                kpi_id uuid  NOT NULL,
                                CONSTRAINT kpi_assessment_pk PRIMARY KEY (id)
);

-- Table: passing
CREATE TABLE passing (
                         id uuid  NOT NULL DEFAULT uuid_generate_v4(),
                         is_pass boolean  NOT NULL,
                         evaluated_person_id uuid  NOT NULL,
                         evaluators_id uuid  NOT NULL,
                         survey_id uuid  NOT NULL,
                         CONSTRAINT passing_pk PRIMARY KEY (id)
);

-- Table: question
CREATE TABLE question (
                          id uuid  NOT NULL DEFAULT uuid_generate_v4(),
                          name text  NOT NULL,
                          competency_id uuid  NOT NULL,
                          survey_id uuid  NOT NULL,
                          CONSTRAINT question_pk PRIMARY KEY (id)
);

-- Table: response
CREATE TABLE response (
                          id uuid  NOT NULL DEFAULT uuid_generate_v4(),
                          question_id uuid  NOT NULL,
                          mark int  NOT NULL,
                          CONSTRAINT response_pk PRIMARY KEY (id)
);

-- Table: role
CREATE TABLE role (
                      id uuid  NOT NULL,
                      name varchar(255)  NOT NULL,
                      CONSTRAINT role_pk PRIMARY KEY (id)
);

-- Table: survey
CREATE TABLE survey (
                        id uuid  NOT NULL DEFAULT uuid_generate_v4(),
                        name text  NOT NULL DEFAULT uuid_generate_v4(),
                        description text  NOT NULL,
                        created_at timestamp  NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        status int  NOT NULL,
                        evaluation_method varchar(255)  NOT NULL,
                        CONSTRAINT survey_pk PRIMARY KEY (id)
);

-- Table: user
CREATE TABLE "user" (
                        id uuid  NOT NULL DEFAULT uuid_generate_v4(),
                        first_name varchar(255)  NOT NULL,
                        last_name varchar(255)  NOT NULL,
                        email text  NOT NULL,
                        password text  NOT NULL,
                        employee_id uuid  NULL,
                        role_id uuid  NOT NULL,
                        is_first_start boolean NOT NULL,
                        CONSTRAINT user_pk PRIMARY KEY (id)
);

-- foreign keys
-- Reference: assessment_summary_competency (table: assessment_summary)
ALTER TABLE assessment_summary ADD CONSTRAINT assessment_summary_competency
    FOREIGN KEY (competency_id)
        REFERENCES competency (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- Reference: assessment_summary_employee (table: assessment_summary)
ALTER TABLE assessment_summary ADD CONSTRAINT assessment_summary_employee
    FOREIGN KEY (employee_id)
        REFERENCES employee (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- Reference: assessment_summary_survey (table: assessment_summary)
ALTER TABLE assessment_summary ADD CONSTRAINT assessment_summary_survey
    FOREIGN KEY (survey_id)
        REFERENCES survey (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- Reference: development_plan_competency (table: development_plan)
ALTER TABLE development_plan ADD CONSTRAINT development_plan_competency
    FOREIGN KEY (competency_id)
        REFERENCES competency (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- Reference: development_plan_employee (table: development_plan)
ALTER TABLE development_plan ADD CONSTRAINT development_plan_employee
    FOREIGN KEY (employee_id)
        REFERENCES employee (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- Reference: development_plan_survey (table: development_plan)
ALTER TABLE development_plan ADD CONSTRAINT development_plan_survey
    FOREIGN KEY (survey_id)
        REFERENCES survey (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- Reference: employee_job_title (table: employee)
ALTER TABLE employee ADD CONSTRAINT employee_job_title
    FOREIGN KEY (job_title_uuid)
        REFERENCES job_title (uuid)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- Reference: evaluated_person_employee (table: evaluated_person)
ALTER TABLE evaluated_person ADD CONSTRAINT evaluated_person_employee
    FOREIGN KEY (employee_id)
        REFERENCES employee (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- Reference: evaluators_employee (table: evaluators)
ALTER TABLE evaluators ADD CONSTRAINT evaluators_employee
    FOREIGN KEY (employee_id)
        REFERENCES employee (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- Reference: job_title_department (table: job_title)
ALTER TABLE job_title ADD CONSTRAINT job_title_department
    FOREIGN KEY (department_id)
        REFERENCES department (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- Reference: job_title_job_title (table: job_title)
ALTER TABLE job_title ADD CONSTRAINT job_title_job_title
    FOREIGN KEY (lead_id)
        REFERENCES job_title (uuid)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- Reference: kpi_assessment_kpi (table: kpi_assessment)
ALTER TABLE kpi_assessment ADD CONSTRAINT kpi_assessment_kpi
    FOREIGN KEY (kpi_id)
        REFERENCES kpi (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- Reference: kpi_employee (table: kpi)
ALTER TABLE kpi ADD CONSTRAINT kpi_employee
    FOREIGN KEY (employee_id)
        REFERENCES employee (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- Reference: passing_evaluated_person (table: passing)
ALTER TABLE passing ADD CONSTRAINT passing_evaluated_person
    FOREIGN KEY (evaluated_person_id)
        REFERENCES evaluated_person (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- Reference: passing_evaluators (table: passing)
ALTER TABLE passing ADD CONSTRAINT passing_evaluators
    FOREIGN KEY (evaluators_id)
        REFERENCES evaluators (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- Reference: passing_survey (table: passing)
ALTER TABLE passing ADD CONSTRAINT passing_survey
    FOREIGN KEY (survey_id)
        REFERENCES survey (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- Reference: question_competency (table: question)
ALTER TABLE question ADD CONSTRAINT question_competency
    FOREIGN KEY (competency_id)
        REFERENCES competency (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- Reference: question_survey (table: question)
ALTER TABLE question ADD CONSTRAINT question_survey
    FOREIGN KEY (survey_id)
        REFERENCES survey (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- Reference: response_question (table: response)
ALTER TABLE response ADD CONSTRAINT response_question
    FOREIGN KEY (question_id)
        REFERENCES question (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- Reference: user_employee (table: user)
ALTER TABLE "user" ADD CONSTRAINT user_employee
    FOREIGN KEY (employee_id)
        REFERENCES employee (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- Reference: user_role (table: user)
ALTER TABLE "user" ADD CONSTRAINT user_role
    FOREIGN KEY (role_id)
        REFERENCES role (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

INSERT INTO role(name) values ('admin', 'manager', 'employee');
-- End of file.

