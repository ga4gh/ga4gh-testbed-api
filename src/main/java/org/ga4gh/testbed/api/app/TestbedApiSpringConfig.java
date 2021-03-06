package org.ga4gh.testbed.api.app;

import ch.qos.logback.classic.Logger;
import org.apache.catalina.connector.Connector;
import org.apache.commons.cli.Options;
import org.ga4gh.starterkit.common.config.DatabaseProps;
import org.ga4gh.starterkit.common.config.ServerProps;
import org.ga4gh.starterkit.common.hibernate.HibernateEntity;
import org.ga4gh.starterkit.common.requesthandler.BasicShowRequestHandler;
import org.ga4gh.starterkit.common.util.CliYamlConfigLoader;
import org.ga4gh.starterkit.common.util.DeepObjectMerger;
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
import org.ga4gh.testbed.api.utils.logging.TestbedLoggingUtil;
import org.ga4gh.testbed.api.utils.requesthandler.report.CreateReportHandler;
import org.ga4gh.testbed.api.utils.requesthandler.report.DeleteReportHandler;
import org.ga4gh.testbed.api.utils.requesthandler.report.ShowReportHandler;
import org.ga4gh.testbed.api.utils.secretmanager.AwsSecretManagerUtil;
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
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.filter.CorsFilter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
    public TestbedLoggingUtil testbedloggingUtil() {
        return new TestbedLoggingUtil();
    }

    @Bean
    public Logger getLogger(@Autowired TestbedLoggingUtil testbedLoggingUtil) {
        return testbedLoggingUtil.getLogger();
    }

    /* ******************************
     * SECRETS MANAGER
     * ****************************** */
    @Bean
    public AwsSecretManagerUtil awsSecretManagerUtil() {
        return new AwsSecretManagerUtil();
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

    /* ******************************
     * REQUEST HANDLER
     * ****************************** */

    @Bean
    @RequestScope
    public BasicShowRequestHandler<String, Organization> showOrganizationHandler(
        @Autowired TestbedApiHibernateUtil hibernateUtil
    ) {
        BasicShowRequestHandler<String, Organization> showOrganizationHandler = new BasicShowRequestHandler<>(Organization.class);
        showOrganizationHandler.setHibernateUtil(hibernateUtil);
        return showOrganizationHandler;
    }

    @Bean
    @RequestScope
    public BasicShowRequestHandler<String, Platform> showPlatformHandler(
        @Autowired TestbedApiHibernateUtil hibernateUtil
    ) {
        BasicShowRequestHandler<String, Platform> showPlatformHandler = new BasicShowRequestHandler<>(Platform.class);
        showPlatformHandler.setHibernateUtil(hibernateUtil);
        return showPlatformHandler;
    }

    @Bean
    @RequestScope
    public ShowReportHandler showReportHandler() {
        return new ShowReportHandler();
    }

    @Bean
    @RequestScope
    public CreateReportHandler createReportHandler() {
        return new CreateReportHandler();
    }

    @Bean
    @RequestScope
    public DeleteReportHandler deleteReportHandler() {
        return new DeleteReportHandler();
    }

    @Bean
    @RequestScope
    public BasicShowRequestHandler<String, ReportSeries> showReportSeriesHandler(
        @Autowired TestbedApiHibernateUtil hibernateUtil
    ) {
        BasicShowRequestHandler<String, ReportSeries> showReportSeriesHandler = new BasicShowRequestHandler<>(ReportSeries.class);
        showReportSeriesHandler.setHibernateUtil(hibernateUtil);
        return showReportSeriesHandler;
    }

    @Bean
    @RequestScope
    public BasicShowRequestHandler<String, Specification> showSpecificationHandler(
        @Autowired TestbedApiHibernateUtil hibernateUtil
    ) {
        BasicShowRequestHandler<String, Specification> showSpecificationHandler = new BasicShowRequestHandler<>(Specification.class);
        showSpecificationHandler.setHibernateUtil(hibernateUtil);
        return showSpecificationHandler;
    }

    @Bean
    @RequestScope
    public BasicShowRequestHandler<String, Testbed> showTestbedHandler(
        @Autowired TestbedApiHibernateUtil hibernateUtil
    ) {
        BasicShowRequestHandler<String, Testbed> showTestbedHandler = new BasicShowRequestHandler<>(Testbed.class);
        showTestbedHandler.setHibernateUtil(hibernateUtil);
        return showTestbedHandler;
    }
}
