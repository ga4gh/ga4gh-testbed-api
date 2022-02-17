package org.ga4gh.testbed.api.config;

import org.apache.commons.cli.*;
import org.ga4gh.starterkit.common.config.*;
import org.ga4gh.starterkit.common.util.*;
import org.ga4gh.starterkit.common.util.webserver.ServerPropertySetter;
import org.springframework.boot.*;

import java.util.*;

public class TestbedApiServerPropertySetter extends ServerPropertySetter {
    private DeepObjectMerger merger;

    public TestbedApiServerPropertySetter() {
        super();

    }

    @Override
    public <T extends ContainsServerProps> boolean setServerProperties(Class<T> configClass, String[] args, Options options, String optionName) {
        try {
            // obtain the final merged configuration object
            ApplicationArguments applicationArgs = new DefaultApplicationArguments(args);
            T defaultConfig = configClass.getConstructor().newInstance();
            T userConfig = CliYamlConfigLoader.load(configClass, applicationArgs, options, optionName);
            if (userConfig != null) {
                merger.merge(userConfig, defaultConfig);
            }
            T mergedConfig = defaultConfig;
            ServerProps serverProps = mergedConfig.getServerProps();
            String publicApiPort = "8080";
            String adminApiPort = serverProps.getAdminApiPort();

            // obtain system properties
            Properties systemProperties = System.getProperties();

            // set system properties for admin and public ports
            systemProperties.setProperty("server.port", publicApiPort);
            systemProperties.setProperty("server.admin.port", adminApiPort);

            // Github OAuth2.0
            systemProperties.setProperty("spring.security.oauth2.client.registration.github.clientId", "3611453ee0c99394a938"); //env var
            systemProperties.setProperty("spring.security.oauth2.client.registration.github.client-secret", "d53ef8daf0b7ce2828394500cc2d5b4fc69fb0df"); //env var

            // set system properties for Spring logging
            if (serverProps.getDisableSpringLogging()) {
                systemProperties.setProperty("logging.level.org.springframework", "OFF");
                systemProperties.setProperty("logging.level.root", "OFF");
                systemProperties.setProperty("spring.main.banner-mode", "off");
            }
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
