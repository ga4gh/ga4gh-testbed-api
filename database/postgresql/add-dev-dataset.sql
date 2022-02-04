Insert into ga4gh_testbed_report_series (
	organization_name,
	platform_name,
	platform_description,
	implementation_name
) values (
	'test_organization_1',
	'refget starter kit',
	'ga4gh starter kit for refget spec',
	'dev'),(
	'test_organization_1', 
	'refget starter kit', 
	'ga4gh starter kit for refget spec', 
	'prod');

Insert into ga4gh_testbed_report_series (
	organization_name,
	platform_name,
	platform_description,
	implementation_name
) values ;

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
	);
