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
        databaseProps = new TestbedApiDatabaseProps();
    }
}
