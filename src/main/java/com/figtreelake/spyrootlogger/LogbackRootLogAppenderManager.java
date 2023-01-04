package com.figtreelake.spyrootlogger;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import org.slf4j.LoggerFactory;

class LogbackRootLogAppenderManager {

  private Logger logbackRootLogger;

  public void add(Appender<ILoggingEvent> appender) {
    final var logger = retrieveLogbackRootLogger();

    if (!logger.isAttached(appender)) {
      logger.addAppender(appender);
    }
  }

  public void detach(Appender<ILoggingEvent> appender) {
    final var logger = retrieveLogbackRootLogger();
    if (logger.isAttached(appender)) {
      retrieveLogbackRootLogger().detachAppender(appender);
    }
  }

  private Logger retrieveLogbackRootLogger() {
    if (logbackRootLogger == null) {
      final org.slf4j.Logger slf4jRootLogger = LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);

      if (!(slf4jRootLogger instanceof Logger)) {
        throw new IllegalStateException("Slf4j implementation library used is not Logback.");
      }
      logbackRootLogger = (Logger) slf4jRootLogger;
    }

    return logbackRootLogger;
  }
}
