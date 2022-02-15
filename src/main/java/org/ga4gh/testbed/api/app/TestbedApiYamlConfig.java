package org.ga4gh.testbed.api.app;

import org.ga4gh.starterkit.common.config.DatabaseProps;
import org.ga4gh.starterkit.common.config.ServerProps;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TestbedApiYamlConfig {

    private ServerProps serverProps;
    private DatabaseProps databaseProps;

    public TestbedApiYamlConfig() {
        serverProps = new ServerProps();
        databaseProps = new DatabaseProps();
    }
}
