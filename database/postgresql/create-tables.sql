CREATE TABLE IF NOT EXISTS ga4gh_testbed_summary
(
    id serial PRIMARY KEY,
    unknown integer NOT NULL,
    passed integer NOT NULL,
    warned integer NOT NULL,
    failed integer NOT NULL,
    skipped integer NOT NULL
);

CREATE TABLE IF NOT EXISTS ga4gh_testbed_report
(
    id serial PRIMARY KEY,
    schema_name text NOT NULL,
    schema_version text NOT NULL,
    testbed_name text NOT NULL,
    testbed_version text NOT NULL,
    testbed_description text NOT NULL,
    platform_name text NOT NULL,
    platform_description text NOT NULL,
    input_parameters text NOT NULL,
    start_time text NOT NULL,
    end_time text NOT NULL,
    status text NOT NULL,
    fk_summary_id integer NOT NULL,
    foreign key (fk_summary_id) references ga4gh_testbed_summary(id)
);

CREATE TABLE IF NOT EXISTS ga4gh_testbed_phase
(
    id serial PRIMARY KEY,
    phase_name text NOT NULL,
    phase_description text NOT NULL,
    start_time text NOT NULL,
    end_time text NOT NULL,
    status text NOT NULL,
    fk_summary_id integer NOT NULL,
    fk_report_id integer NOT NULL,
    foreign key (fk_summary_id) references ga4gh_testbed_summary(id),
    foreign key (fk_report_id) references ga4gh_testbed_report(id)
);

CREATE TABLE IF NOT EXISTS ga4gh_testbed_test
(
    id serial PRIMARY KEY,
    test_name text NOT NULL,
    test_description text NOT NULL,
    start_time text NOT NULL,
    end_time text NOT NULL,
    status text NOT NULL,
    fk_summary_id bigint NOT NULL,
    fk_phase_id bigint NOT NULL,
    foreign key (fk_summary_id) references ga4gh_testbed_summary(id),
    foreign key (fk_phase_id) references ga4gh_testbed_phase(id)  
);

CREATE TABLE IF NOT EXISTS ga4gh_testbed_case
(
    id serial PRIMARY KEY,
    case_name text NOT NULL,
    case_description text NOT NULL,
    start_time text NOT NULL,
    end_time text NOT NULL,
    status text NOT NULL,
    message text NOT NULL,
    fk_test_id integer,
    foreign key (fk_test_id) references ga4gh_testbed_test(id)
);

CREATE TABLE IF NOT EXISTS ga4gh_testbed_log_message
(
    id serial PRIMARY KEY,
    message text NOT NULL,
    fk_case_id integer NOT NULL,
    foreign key (fk_case_id) references ga4gh_testbed_case(id)
);