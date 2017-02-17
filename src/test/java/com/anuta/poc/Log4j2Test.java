package com.anuta.poc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.io.File;
import java.util.Objects;
import java.util.ResourceBundle;

public class Log4j2Test {
    private static final Logger logger = LoggerFactory.getLogger(Log4j2Test.class);
    private static final String jsonConfig = "src/main/resources/log4j2-json.xml";
    private static final String textConfig = "src/main/resources/log4j2-text.xml";

    @Before
    public void setUpLogger() {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("logging");
        String format = resourceBundle.getString("format");

        if (Objects.equals(format, "text")) {
            configurePatternLayout();
        } else if (Objects.equals(format, "json")) {
            configureJsonLayout();
        }
    }

    private void configurePatternLayout() {
        configureLogging(textConfig);
    }

    private void configureJsonLayout() {
        configureLogging(jsonConfig);
    }

    private void configureLogging(String path) {
        LoggerContext context = (org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false);
        File file = new File(path);
        context.setConfigLocation(file.toURI());
    }

    @Test
    public void testLogging() {
        logInfo();
        logDebug();
        logError();
        logWarning();
    }

    private void logWarning() {
        long currentTimeMillis = System.currentTimeMillis();
        MDC.put("endTime", String.valueOf(currentTimeMillis));
        logger.warn("The test is about to complete execution");
        MDC.clear();
    }

    private void logError() {
        MDC.put("device.ip", "10.31.252.123");
        MDC.put("device.name", "Router_North_America_Bay_1");
        MDC.put("device.version", "32.9.8.5");
        logger.error("Device could not be restarted due to access restrictions");
        MDC.clear();
    }

    private void logDebug() {
        MDC.put("device.ip", "10.31.252.123");
        MDC.put("device.name", "Router_North_America_Bay_1");
        logger.debug("The device could not be reached due to an IP address conflict");
        MDC.clear();
    }

    private void logInfo() {
        long currentTimeMillis = System.currentTimeMillis();
        MDC.put("beginTime", String.valueOf(currentTimeMillis));
        logger.info("The test has began execution");
        MDC.clear();
    }
}
