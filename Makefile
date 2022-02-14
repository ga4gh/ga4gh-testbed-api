DOCKER_ORG := ga4gh
DOCKER_REPO := ga4gh-testbed-infrastructure
DOCKER_TAG := 0.1.0
DOCKER_IMG := ${DOCKER_ORG}/${DOCKER_REPO}:${DOCKER_TAG}
DEVDB := ga4gh-testbed-infrastructure.dev.db

Nothing:
	@echo "No target provided. Stop"

# remove local postgresql database
.PHONY: clean-psql
clean-psql:
	@psql -c "drop database ga4gh_testbed_infrastructure_db;" -U postgres

# create local postgresql database with tables using create-tables.sql
.PHONY: psql-db-build
psql-db-build:
	@psql -c "create database ga4gh_testbed_infrastructure_db;" -U postgres
	@psql ga4gh_testbed_infrastructure_db < database/postgresql/create-tables.sql -U postgres

# populate the postgresql tables with test data
.PHONY: psql-db-populate
psql-db-populate:
	@psql ga4gh_testbed_infrastructure_db < database/postgresql/add-dev-dataset.sql -U postgres

.PHONY: psql-db-build-populate
psql-db-build-populate: psql-db-build psql-db-populate

.PHONY: psql-db-refresh
psql-db-refresh: clean-psql psql-db-build-populate

# remove local sqlite database
.PHONY: clean-sqlite
clean-sqlite:
	@rm -f ${DEVDB}

# create the sqlite database with tables using create-tables.sql script
.PHONY: sqlite-db-build
sqlite-db-build: clean-sqlite
	@sqlite3 ${DEVDB} < database/sqlite/create-tables.sql

# populate the sqlite database with test data
.PHONY: sqlite-db-populate-dev-dataset
sqlite-db-populate-dev-dataset:
	@sqlite3 ${DEVDB} < database/sqlite/add-dev-dataset.sql

.PHONY: sqlite-db-refresh
sqlite-db-refresh: clean-sqlite sqlite-db-build sqlite-db-populate-dev-dataset


