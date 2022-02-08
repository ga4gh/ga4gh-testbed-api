/* ################################################## */
/* # USER / ORGANIZATION                            # */
/* ################################################## */

CREATE TABLE IF NOT EXISTS user
(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    github_id text NOT NULL
);

CREATE TABLE IF NOT EXISTS organization
(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    organization_name text NOT NULL,
    organization_url text NOT NULL
);

CREATE TABLE IF NOT EXISTS user_organization
(
    role text NOT NULL,
    fk_user_id integer NOT NULL,
    fk_organization_id integer NOT NULL,
    foreign key (fk_user_id) references user(id),
    foreign key (fk_organization_id) references organization(id)
);

/* ################################################## */
/* # PLATFORM                                       # */
/* ################################################## */

CREATE TABLE IF NOT EXISTS platform
(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    platform_name text NOT NULL,
    platform_description text NOT NULL,
    fk_organization_id integer NOT NULL,
    foreign key (fk_organization_id) references organization(id)
);

/* ################################################## */
/* # SPECIFICATION                                  # */
/* ################################################## */

CREATE TABLE IF NOT EXISTS specification
(
    id text PRIMARY KEY,
    spec_name text NOT NULL,
    spec_description text NOT NULL,
    github_url text NOT NULL,
    documentation_url text NOT NULL
);

CREATE TABLE IF NOT EXISTS specification_platform
(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    fk_specification_id integer NOT NULL,
    fk_platform_id integer NOT NULL,
    foreign key (fk_specification_id) references specification(id),
    foreign key (fk_platform_id) references platform(id)
);

/* ################################################## */
/* # TESTBED                                        # */
/* ################################################## */

CREATE TABLE IF NOT EXISTS testbed
(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    testbed_name text NOT NULL,
    testbed_description text NOT NULL,
    github_url text NOT NULL,
    dockerhub_url text NOT NULL,
    dockstore_url text NOT NULL
);

CREATE TABLE IF NOT EXISTS testbed_version
(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    testbed_version text NOT NULL,
    fk_testbed_id integer NOT NULL,
    foreign key (fk_testbed_id) references testbed(id)
);

CREATE TABLE IF NOT EXISTS specification_testbed
(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    fk_specification_id integer NOT NULL,
    fk_testbed_id integer NOT NULL,
    foreign key (fk_specification_id) references specification(id),
    foreign key (fk_testbed_id) references testbed(id)
);

/* ################################################## */
/* # REPORT                                         # */
/* ################################################## */

CREATE TABLE IF NOT EXISTS ga4gh_testbed_summary
(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    unknown integer NOT NULL,
    passed integer NOT NULL,
    warned integer NOT NULL,
    failed integer NOT NULL,
    skipped integer NOT NULL
);

CREATE TABLE IF NOT EXISTS ga4gh_testbed_report_series
(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    token_salt text NOT NULL,
    token_hash text NOT NULL,
    fk_testbed_id integer NOT NULL,
    fk_platform_id integer NOT NULL,
    foreign key (fk_testbed_id) references testbed(id),
    foreign key (fk_platform_id) references platform(id)
);

CREATE TABLE IF NOT EXISTS ga4gh_testbed_report
(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    schema_name text NOT NULL,
    schema_version text NOT NULL,
    input_parameters json NOT NULL,
    start_time timestamp without time zone NOT NULL,
    end_time timestamp without time zone NOT NULL,
    status text NOT NULL,
    fk_summary_id integer NOT NULL,
    fk_report_series_id integer NOT NULL,
    foreign key (fk_summary_id) references ga4gh_testbed_summary(id),
    foreign key (fk_report_series_id) references ga4gh_testbed_report_series(id)
);

CREATE TABLE IF NOT EXISTS ga4gh_testbed_phase
(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    phase_name text NOT NULL,
    phase_description text NOT NULL,
    start_time timestamp without time zone NOT NULL,
    end_time timestamp without time zone NOT NULL,
    status text NOT NULL,
    fk_summary_id integer NOT NULL,
    fk_report_id integer NOT NULL,
    foreign key (fk_summary_id) references ga4gh_testbed_summary(id),
    foreign key (fk_report_id) references ga4gh_testbed_report(id)
);

CREATE TABLE IF NOT EXISTS ga4gh_testbed_test
(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    test_name text NOT NULL,
    test_description text NOT NULL,
    start_time timestamp without time zone NOT NULL,
    end_time timestamp without time zone NOT NULL,
    status text NOT NULL,
    fk_summary_id bigint NOT NULL,
    fk_phase_id bigint NOT NULL,
    foreign key (fk_summary_id) references ga4gh_testbed_summary(id),
    foreign key (fk_phase_id) references ga4gh_testbed_phase(id)  
);

CREATE TABLE IF NOT EXISTS ga4gh_testbed_case
(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    case_name text NOT NULL,
    case_description text NOT NULL,
    start_time timestamp without time zone NOT NULL,
    end_time timestamp without time zone NOT NULL,
    status text NOT NULL,
    message text NOT NULL,
    fk_test_id integer,
    foreign key (fk_test_id) references ga4gh_testbed_test(id)
);

CREATE TABLE IF NOT EXISTS ga4gh_testbed_log_message
(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    message text NOT NULL,
    fk_case_id integer NOT NULL,
    foreign key (fk_case_id) references ga4gh_testbed_case(id)
);