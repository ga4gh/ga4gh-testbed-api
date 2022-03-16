package org.ga4gh.testbed.api.config;

import org.apache.commons.cli.Options;
import org.ga4gh.starterkit.common.config.ContainsServerProps;
import org.ga4gh.starterkit.common.config.ServerProps;
import org.ga4gh.starterkit.common.util.CliYamlConfigLoader;
import org.ga4gh.starterkit.common.util.DeepObjectMerger;
import org.ga4gh.starterkit.common.util.webserver.ServerPropertySetter;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.DefaultApplicationArguments;

import java.util.Properties;

public class TestbedApiServerPropertySetter extends ServerPropertySetter {
    private DeepObjectMerger merger;

    public TestbedApiServerPropertySetter() {
        super();
        merger = new DeepObjectMerger();

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
            String publicApiPort = serverProps.getPublicApiPort();
            String adminApiPort = serverProps.getAdminApiPort();

            // obtain system properties
            Properties systemProperties = System.getProperties();

            // set system properties for admin and public ports
            systemProperties.setProperty("server.port", publicApiPort);
            systemProperties.setProperty("server.admin.port", adminApiPort);
            final String githubClientId = System.getenv("GITHUB_OAUTH_CLIENT_ID");
            final String githubClientSecret = System.getenv("GITHUB_OAUTH_CLIENT_SECRET");

            // Github OAuth2.0
            systemProperties.setProperty("spring.security.oauth2.client.registration.github.clientId",githubClientId );
            systemProperties.setProperty("spring.security.oauth2.client.registration.github.clientSecret", githubClientSecret);

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