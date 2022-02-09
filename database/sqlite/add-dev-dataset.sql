INSERT INTO specification VALUES (
    'refget',
    'refget',
    'Reference sequence retrieval API',
    'https://github.com/samtools/hts-specs',
    'https://samtools.github.io/hts-specs/refget.html'
), (
    'rnaget',
    'RNAget',
    'Expression matrix retrieval API',
    'https://github.com/ga4gh-rnaseq/schema',
    'https://ga4gh-rnaseq.github.io/schema/docs/index.html'
);

INSERT INTO organization VALUES (
    0,
    'GA4GH',
    'https://ga4gh.org'
);

INSERT INTO platform VALUES (
    0,
    'refget starter kit',
    'ga4gh starter kit for refget spec',
    0
), (
    1,
    'RNAget starter kit',
    'ga4gh starter kit for RNAget spec',
    0
);

INSERT INTO testbed VALUES (
    0,
    'refget-compliance-suite',
    'test compliance of refget services to specification',
    'https://github.com/ga4gh/refget-compliance-suite',
    'https://hub.docker.com/repository/docker/ga4gh/refget-compliance-suite',
    'https://dockstore.org/containers/registry.hub.docker.com/ga4gh/refget-compliance-suite'
), (
    1,
    'rnaget-compliance-suite',
    'test compliance of RNAget services to specification',
    'https://github.com/ga4gh-rnaseq/rnaget-compliance-suite',
    'https://hub.docker.com',
    'https://dockstore.org'
);

INSERT INTO testbed_version VALUES (0, '1.2.6', 0), (1, '1.0.0', 1);

INSERT INTO specification_testbed VALUES (0, 0, 0), (1, 1, 1);
INSERT INTO specification_platform VALUES (0, 0, 0), (1, 1, 1);

Insert into ga4gh_testbed_report_series values (
    0,
    'abc',
    'def',
    0,
    0
),(
    1,
    'ghi',
    'jkl',
    1,
    1
);

Insert into ga4gh_testbed_summary (
	unknown,
    passed,
    warned,
    failed,
    skipped
) values 
	( 	'0',
		'10',
		'2',
		'0',
		'0'
	), 
	(
		'0',
		'12',
		'0',
		'0',
		'0'
	), 
	(
		'0',
		'8',
		'2',
		'2',
		'0'
	), 
	(
		'0',
		'10',
		'2',
		'0',
		'0'
	),
	(
		'0',
		'7',
		'2',
		'3',
		'0'
	), 
	(
		'0',
		'9',
		'2',
		'0',
		'1'
	);

Insert into ga4gh_testbed_report (
	schema_name,
    schema_version,
    input_parameters,
    start_time,
    end_time,
    status ,
    fk_summary_id,
    fk_report_series_id
) values (
	'ga4gh-testbed-report',
	'0.1.0',
	'{ "url": "https://testsite.ga4gh.org/api"}',
	'2021-10-20T12:00:00Z',
	'2021-10-20T12:00:20Z',
	'UNKNOWN',
	1,
	1
	);

Insert into ga4gh_testbed_phase (
	phase_name,
    phase_description,
    start_time,
    end_time,
    status,
    fk_summary_id,
    fk_report_id
) values (
	'sequence',
	'refget sequence endpoint',
	'2021-10-20T12:00:00Z',
	'2021-10-20T12:00:05Z',
	'PASSED',
	'2',
	'1'
	),(
	'metadata',
	'refget metadata endpoint',
	'2021-10-20T12:00:05Z',
	'2021-10-20T12:00:10Z',
	'PASSED',
	'3',
	'1'
	),(
	'service-info',
	'refget service-info endpoint',
	'2021-10-20T12:00:10Z',
	'2021-10-20T12:00:15Z',
	'PASSED',
	'4',
	'1'
	);

Insert into ga4gh_testbed_test (
    test_name ,
    test_description ,
    start_time,
    end_time,
    status,
    fk_summary_id,
    fk_phase_id
) values (
	'test_sequence_circular',
	'Test to check if server passes all the edge cases related to circular queries',
	'2021-10-20T12:00:00Z',
	'2021-10-20T12:00:03Z',
	'PASSED',
	'5',
	'1'
	),(
	'test_sequence_query_by_trunc512',
	'Test to check if server returns 200 using I test sequence trunc512 and appropriate headers if the server supports trunc512',
	'2021-10-20T12:00:03Z',
	'2021-10-20T12:00:06Z',
	'FAILED',
	'6',
	'1'
	);

Insert into ga4gh_testbed_case (
	case_name,
    case_description,
    start_time,
    end_time,
    status,
    message,
    fk_test_id 
) values (
	'case 1',
	'test for seq 1',
	'2021-10-20T12:00:00Z',
	'2021-10-20T12:00:01Z',
	'PASSED',
	'server reponds as expected',
	1
	),(
	'case 2',
	'test for seq 2',
	'2021-10-20T12:00:01Z',
	'2021-10-20T12:00:02Z',
	'FAILED',
	'test case failed',
	1
	);

Insert into ga4gh_testbed_case (
	case_name,
    case_description,
    start_time,
    end_time,
    status,
    message,
    fk_test_id 
) values (
	'case 1',
	'test with incorrect trunc512',
	'2021-10-20T12:00:03Z',
	'2021-10-20T12:00:04Z',
	'UNKNOWN',
	'unknown server response',
	2
	),(
	'case 2',
	'test with correct trunc512',
	'2021-10-20T12:00:04Z',
	'2021-10-20T12:00:05Z',
	'WARNING',
	'server responds with a warning',
	2
	);

Insert into ga4gh_testbed_log_message(
	message,
    fk_case_id
) values (
	'log message 1',
	1
    ),(
    'log message 2',
    2
    ),(
    'log message 3',
    3
    ),(
    'log message 4',
    4
    );
