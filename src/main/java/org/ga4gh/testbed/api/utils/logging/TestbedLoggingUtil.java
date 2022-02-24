package org.ga4gh.testbed.api.utils.logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.contrib.jackson.JacksonJsonFormatter;
import ch.qos.logback.contrib.json.classic.JsonLayout;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import org.ga4gh.starterkit.common.config.ServerProps;
import org.ga4gh.starterkit.common.util.logging.LoggingUtil;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.PostConstruct;

public class TestbedLoggingUtil extends LoggingUtil {

    private static final String MESSAGE_FORMAT = "%date{yyyy-MM-dd HH:mm:ss} [%p] %message%n";

    @Autowired
    private ServerProps serverProps;

    private boolean configured;

    private Logger logger;

    public TestbedLoggingUtil() {
        configured = false;
    }

    public void trace(String msg) {
        logger.trace(msg);
    }

    public void debug(String msg) {
        logger.debug(msg);
    }

    public void info(String msg) {
        logger.info(msg);
    }

    public void warn(String msg) {
        logger.warn(msg);
    }

    public void error(String msg) {
        logger.error(msg);
    }

    @PostConstruct
    public void buildLogger() {
        setLogger((Logger) LoggerFactory.getLogger(getClass()));

        LoggerContext loggerContext = logger.getLoggerContext();
        loggerContext.reset();

        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(loggerContext);
        encoder.setPattern(MESSAGE_FORMAT);
        encoder.start();

        if (getServerProps().getLogFile() == null) {
            ConsoleAppender<ILoggingEvent> appender = new ConsoleAppender<>();
            JsonLayout jsonLayout= new JsonLayout();
            appender.setLayout(jsonLayout);
            appender.setContext(loggerContext);
            appender.setEncoder(encoder);
            appender.start();
            getLogger().addAppender(appender);
        } else {
            // log file format = ga4gh-testbed-infrastructure.%d{yyyy-MM-dd}.log.gz

            // Add logs in json format
            JsonLayout jsonLayout= new JsonLayout();
            JacksonJsonFormatter jsonFormatter = new JacksonJsonFormatter();
            jsonFormatter.setPrettyPrint(true);
            jsonLayout.setJsonFormatter(jsonFormatter);
            jsonLayout.setTimestampFormat("yyyy-MM-dd'T'HH:mm:ssX");
            jsonLayout.setTimestampFormatTimezoneId("Etc/UTC");
            jsonLayout.setContext(loggerContext);

            // Zip Rotate the log files every day at UTC
            TimeBasedRollingPolicy<ILoggingEvent> policy = new TimeBasedRollingPolicy<>();
            // zip rotate at the start of every day - UTC timezone
            policy.setFileNamePattern(("./logs/" + getServerProps().getLogFile()).replace(".log",".%d{yyyy-MM-dd, UTC}.log.gz"));
            // keep the log files from the past 30 days.
            policy.setMaxHistory(30);
            policy.setContext(loggerContext);

            // Add to the appender
            RollingFileAppender<ILoggingEvent> appender = new RollingFileAppender<>();
            appender.setContext(loggerContext);
            appender.setName("rollingAppender");
            // add the log files to ./logs directory
            appender.setFile("./logs/"+getServerProps().getLogFile());
            appender.setAppend(true);
            appender.setPrudent(false);
            appender.setRollingPolicy(policy);
            appender.setLayout(jsonLayout);
            policy.setParent(appender);
            policy.start();
            jsonLayout.start();
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
