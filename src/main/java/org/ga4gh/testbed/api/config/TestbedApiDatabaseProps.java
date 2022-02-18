package org.ga4gh.testbed.api.config;

import org.ga4gh.starterkit.common.config.DatabaseProps;

public class TestbedApiDatabaseProps extends DatabaseProps {

    public TestbedApiDatabaseProps() {
        super();
        setUrl("jdbc:sqlite:./ga4gh-testbed-infrastructure.dev.db");
        setPoolSize("8");
    }
}
