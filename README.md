<img src="https://www.ga4gh.org/wp-content/themes/ga4gh-theme/gfx/GA-logo-horizontal-tag-RGB.svg" alt="GA4GH Logo" style="width: 400px;"/>

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg?style=flat-square)](https://opensource.org/licenses/Apache-2.0)
[![Java 11+](https://img.shields.io/badge/java-11+-blue.svg?style=flat-square)](https://www.java.com)
[![Gradle 7.3.3+](https://img.shields.io/badge/gradle-7.3.2+-blue.svg?style=flat-square)](https://gradle.org/)
[![GitHub Actions](https://img.shields.io/github/workflow/status/ga4gh/ga4gh-testbed-api/Tests/main)](https://github.com/ga4gh/ga4gh-testbed-api/actions)
[![Codecov](https://img.shields.io/codecov/c/github/ga4gh/ga4gh-testbed-api?style=flat-square)](https://app.codecov.io/gh/ga4gh/ga4gh-testbed-api)

# GA4GH Testbed API

GA4GH-wide testbed API for hosting testbed reports

## Installation

Installation requires the following prerequisites:
* Java 11+
* Gradle 7.3.3+
* Sqlite 3

To install, first clone and enter the repository, then build the Java app with Gradle:

```
git clone https://github.com/ga4gh/ga4gh-testbed-api.git
cd ga4gh-testbed-api
./gradlew build
```

## Usage

To run the Spring Boot app:

```
./gradlew bootRun
```

To execute tests:

```
./gradlew test
```