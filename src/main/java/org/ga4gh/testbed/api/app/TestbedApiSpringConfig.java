package org.ga4gh.testbed.api.app;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.apache.catalina.connector.Connector;
import org.apache.commons.cli.Options;
import org.ga4gh.starterkit.common.config.DatabaseProps;
import org.ga4gh.starterkit.common.config.ServerProps;
import org.ga4gh.starterkit.common.hibernate.HibernateEntity;
import org.ga4gh.starterkit.common.util.CliYamlConfigLoader;
import org.ga4gh.starterkit.common.util.DeepObjectMerger;
import org.ga4gh.starterkit.common.util.logging.LoggingUtil;
import org.ga4gh.starterkit.common.util.webserver.AdminEndpointsConnector;
import org.ga4gh.starterkit.common.util.webserver.AdminEndpointsFilter;
import org.ga4gh.starterkit.common.util.webserver.CorsFilterBuilder;
import org.ga4gh.starterkit.common.util.webserver.TomcatMultiConnectorServletWebServerFactoryCustomizer;
import org.ga4gh.testbed.api.model.GithubUser;
import org.ga4gh.testbed.api.model.GithubUserOrganization;
import org.ga4gh.testbed.api.model.LogMessage;
import org.ga4gh.testbed.api.model.Organization;
import org.ga4gh.testbed.api.model.Phase;
import org.ga4gh.testbed.api.model.Platform;
import org.ga4gh.testbed.api.model.Report;
import org.ga4gh.testbed.api.model.ReportSeries;
import org.ga4gh.testbed.api.model.Specification;
import org.ga4gh.testbed.api.model.Summary;
import org.ga4gh.testbed.api.model.Testbed;
import org.ga4gh.testbed.api.model.TestbedCase;
import org.ga4gh.testbed.api.model.TestbedTest;
import org.ga4gh.testbed.api.model.TestbedVersion;
import org.ga4gh.testbed.api.utils.hibernate.TestbedApiHibernateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.filter.CorsFilter;

@Configuration
@ConfigurationProperties
public class TestbedApiSpringConfig {

    /* ******************************
     * TOMCAT SERVER
     * ****************************** */

    @Value("${server.admin.port:4501}")
    private String serverAdminPort;

    @Bean
    public WebServerFactoryCustomizer servletContainer() {
        Connector[] additionalConnectors = AdminEndpointsConnector.additionalConnector(serverAdminPort);
        ServerProperties serverProperties = new ServerProperties();
        return new TomcatMultiConnectorServletWebServerFactoryCustomizer(serverProperties, additionalConnectors);
    }

    @Bean
    public FilterRegistrationBean<AdminEndpointsFilter> adminEndpointsFilter() {
        return new FilterRegistrationBean<AdminEndpointsFilter>(new AdminEndpointsFilter(Integer.valueOf(serverAdminPort)));
    }
    
    /*
    @Bean
    public DrsCustomExceptionHandling customExceptionHandling() {
        return new DrsCustomExceptionHandling();
    }
    */

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter(
        @Autowired ServerProps serverProps
    ) {
        return new CorsFilterBuilder(serverProps).buildFilter();
    }

    /* ******************************
     * YAML CONFIG
     * ****************************** */

    @Bean
    public Options getCommandLineOptions() {
        final Options options = new Options();
        options.addOption("c", "config", true, "Path to Testbed API YAML config file");
        return options;
    }

    @Bean
    @Scope(TestbedApiConstants.PROTOTYPE)
    @Qualifier(TestbedApiConstants.EMPTY_CONFIG_CONTAINER)
    public TestbedApiYamlConfigContainer emptyConfigContainer() {
        return new TestbedApiYamlConfigContainer();
    }

    @Bean
    @Qualifier(TestbedApiConstants.DEFAULT_CONFIG_CONTAINER)
    public TestbedApiYamlConfigContainer defaultConfigContainer() {
        return new TestbedApiYamlConfigContainer();
    }

    @Bean
    @Qualifier(TestbedApiConstants.USER_CONFIG_CONTAINER)
    public TestbedApiYamlConfigContainer userConfigContainer(
        @Autowired ApplicationArguments args,
        @Autowired() Options options,
        @Qualifier(TestbedApiConstants.EMPTY_CONFIG_CONTAINER) TestbedApiYamlConfigContainer configContainer
    ) {
        TestbedApiYamlConfigContainer userConfigContainer = CliYamlConfigLoader.load(TestbedApiYamlConfigContainer.class, args, options, "config");
        if (userConfigContainer != null) {
            return userConfigContainer;
        }
        return configContainer;
    }

    @Bean
    @Qualifier(TestbedApiConstants.FINAL_CONFIG_CONTAINER)
    public TestbedApiYamlConfigContainer finalConfigContainer(
        @Qualifier(TestbedApiConstants.DEFAULT_CONFIG_CONTAINER) TestbedApiYamlConfigContainer defaultContainer,
        @Qualifier(TestbedApiConstants.USER_CONFIG_CONTAINER) TestbedApiYamlConfigContainer userContainer
    ) {
        DeepObjectMerger merger = new DeepObjectMerger();
        merger.merge(userContainer, defaultContainer);
        return defaultContainer;
    }

    @Bean
    public ServerProps getServerProps(
        @Qualifier(TestbedApiConstants.FINAL_CONFIG_CONTAINER) TestbedApiYamlConfigContainer configContainer
    ) {
        return configContainer.getTestbedApiConfig().getServerProps();
    }

    @Bean
    public DatabaseProps getDatabaseProps(
        @Qualifier(TestbedApiConstants.FINAL_CONFIG_CONTAINER) TestbedApiYamlConfigContainer configContainer
    ) {
        return configContainer.getTestbedApiConfig().getDatabaseProps();
    }

    /* ******************************
     * LOGGING
     * ****************************** */

    @Bean
    public LoggingUtil loggingUtil() {
        return new LoggingUtil();
    }

    /* ******************************
     * HIBERNATE CONFIG
     * ****************************** */

    @Bean
    public TestbedApiHibernateUtil getHibernateUtil(
        @Autowired DatabaseProps databaseProps
    ) {
        List<Class<? extends HibernateEntity<? extends Serializable>>> annotatedClasses = new ArrayList<>() {{
            add(GithubUser.class);
            add(LogMessage.class);
            add(Organization.class);
            add(GithubUserOrganization.class);
            add(Phase.class);
            add(Platform.class);
            add(Report.class);
            add(ReportSeries.class);
            add(Specification.class);
            add(Summary.class);
            add(Testbed.class);
            add(TestbedCase.class);
            add(TestbedTest.class);
            add(TestbedVersion.class);
        }};
        TestbedApiHibernateUtil hibernateUtil = new TestbedApiHibernateUtil();
        hibernateUtil.setAnnotatedClasses(annotatedClasses);
        hibernateUtil.setDatabaseProps(databaseProps);
        return hibernateUtil;
    }
}
