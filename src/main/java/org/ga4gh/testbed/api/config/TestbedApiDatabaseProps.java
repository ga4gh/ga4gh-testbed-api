package org.ga4gh.testbed.api.config;

import org.ga4gh.starterkit.common.config.DatabaseProps;

import java.util.Properties;

public class TestbedApiDatabaseProps extends DatabaseProps {

    public TestbedApiDatabaseProps() {
        super();
        System.out.println("In database config class");
        String databaseUrl = System.getenv("DBHost");
        String databasePort = System.getenv("DBPort");
        String databaseName = System.getenv("DBName");
        String databaseUser = System.getenv("DBUser");
        String databasePassword = System.getenv("DBPass");
        String jdbcUrl = null;

        if(databaseUrl == null || databaseUser == null || databasePassword == null) {
            setUrl("jdbc:sqlite:./ga4gh-testbed-api.dev.db");
        } else {
            jdbcUrl = "jdbc:postgresql://" + databaseUrl + ":" + databasePort + "/" + databaseName;
            setUrl(jdbcUrl);
            setUsername(databaseUser);
            setPassword(databasePassword);
        }
        setPoolSize("8");
    }
}
