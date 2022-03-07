package org.ga4gh.testbed.api.utils.logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import net.logstash.logback.encoder.LogstashEncoder;
import net.logstash.logback.fieldnames.LogstashFieldNames;
import org.ga4gh.starterkit.common.config.ServerProps;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.PostConstruct;

public class TestbedLoggingUtil{

    @Autowired
    private ServerProps serverProps;

    private boolean configured;

    private Logger logger;

    public TestbedLoggingUtil() {
        configured = false;
    }

    @PostConstruct
    public void buildLogger() {
        setLogger((Logger) LoggerFactory.getLogger(getClass()));

        LoggerContext loggerContext = logger.getLoggerContext();
        loggerContext.reset();

        // logback logstash encoder
        LogstashEncoder logstashEncoder = new LogstashEncoder();
        logstashEncoder.setIncludeCallerData(true);
        LogstashFieldNames fieldnames = logstashEncoder.getFieldNames();

        // To ignore the default fields - level value, version and thread fields,
        // set them as null
        fieldnames.setLevelValue(null);
        fieldnames.setVersion(null);
        fieldnames.setThread(null);

        // rename existing field names
        fieldnames.setLevel("level");
        fieldnames.setTimestamp("utc_timestamp");
        fieldnames.setLogger("logger");
        fieldnames.setCallerClass("class_name");
        fieldnames.setCallerMethod("method_name");
        fieldnames.setCallerFile("file_name");
        fieldnames.setCallerLine("line_number");

        // set the new fieldnames
        logstashEncoder.setFieldNames(fieldnames);

        // set the timezone as UTC
        logstashEncoder.setTimeZone("UTC");

        if (getServerProps().getLogFile() == null) {

            // write logs to console
            ConsoleAppender<ILoggingEvent> appender = new ConsoleAppender<>();
            appender.setContext(loggerContext);
            appender.setName("consoleAppender");
            appender.setEncoder(logstashEncoder);
            logstashEncoder.start();
            appender.start();
            getLogger().addAppender(appender);
        } else {

            // write logs to file

            // zip rotate at the start of every day - UTC timezone
            TimeBasedRollingPolicy<ILoggingEvent> policy = new TimeBasedRollingPolicy<>();
            policy.setFileNamePattern(("./logs/" + getServerProps().getLogFile()).replace(".log",".%d{yyyy-MM-dd, UTC}.log.gz"));

            // keep history of log files from the past 30 days.
            policy.setMaxHistory(30);
            policy.setContext(loggerContext);

            // Add to the appender
            RollingFileAppender<ILoggingEvent> appender = new RollingFileAppender<>();
            appender.setContext(loggerContext);
            appender.setName("rollingFileAppender");

            // add the log files to ./logs directory
            appender.setFile("./logs/"+getServerProps().getLogFile());
            appender.setAppend(true);
            appender.setPrudent(false);
            appender.setRollingPolicy(policy);
            appender.setEncoder(logstashEncoder);
            policy.setParent(appender);
            policy.start();
            logstashEncoder.start();
            appender.start();
            getLogger().addAppender(appender);
        }
        setLogLevel();
        setConfigured(true);
    }

    private void setLogLevel() {
        Level level;

        switch(serverProps.getLogLevel()) {
            case TRACE:
                level = Level.TRACE;
                break;
            case DEBUG:
                level = Level.DEBUG;
                break;
            case INFO:
                level = Level.INFO;
                break;
            case WARN:
                level = Level.WARN;
                break;
            case ERROR:
                level = Level.ERROR;
                break;
            default:
                level = Level.DEBUG;
                break;
        }
        getLogger().setLevel(level);
    }

    public void setServerProps(ServerProps serverProps) {
        this.serverProps = serverProps;
    }

    public ServerProps getServerProps() {
        return serverProps;
    }

    public void setConfigured(boolean configured) {
        this.configured = configured;
    }

    public boolean getConfigured() {
        return configured;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public Logger getLogger() {
        return logger;
    }

}