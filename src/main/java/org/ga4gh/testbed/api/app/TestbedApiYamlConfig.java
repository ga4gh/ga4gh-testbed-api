package org.ga4gh.testbed.api.app;

import org.ga4gh.starterkit.common.config.ServerProps;
import org.ga4gh.testbed.api.config.TestbedApiDatabaseProps;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TestbedApiYamlConfig {

    private ServerProps serverProps;
    private TestbedApiDatabaseProps databaseProps;

    public TestbedApiYamlConfig() {
        serverProps = new ServerProps();
        // set log file as "ga4gh-testbed-infrastructure.log". It will be added to ./logs directory
        serverProps.setLogFile("ga4gh-testbed-infrastructure.log");
        databaseProps = new TestbedApiDatabaseProps();
    }
}
