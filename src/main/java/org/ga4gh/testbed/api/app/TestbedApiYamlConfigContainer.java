package org.ga4gh.testbed.api.app;

import org.ga4gh.starterkit.common.config.ContainsServerProps;
import org.ga4gh.starterkit.common.config.ServerProps;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TestbedApiYamlConfigContainer implements ContainsServerProps {

    private TestbedApiYamlConfig testbedApiConfig;

    public TestbedApiYamlConfigContainer() {
        testbedApiConfig = new TestbedApiYamlConfig();
    }

    public ServerProps getServerProps() {
        return getTestbedApiConfig().getServerProps();
    }
}
