package com.figtreelake.spyrootlogger.test.fixture;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.LoggerContextVO;
import ch.qos.logback.classic.spi.ThrowableProxy;
import com.figtreelake.spyrootlogger.test.implementation.ILoggingEventForTests;
import lombok.experimental.UtilityClass;
import org.slf4j.Marker;

import java.util.Collections;
import java.util.Map;

@UtilityClass
public class ILoggingEventForTestsFixture {

  public static final String THREAD_NAME = "threadNameValue";
  public static final Level LEVEL = LogEventFixture.LEVEL;
  public static final String MESSAGE = LogEventFixture.MESSAGE;
  public static final Object[] ARGUMENT_ARRAY = {};
  public static final String FORMATTED_MESSAGE = LogEventFixture.MESSAGE;
  public static final String LOGGER_NAME = "loggerNameValue";
  public static final LoggerContextVO LOGGER_CONTEXT_VO = null;
  public static final StackTraceElement[] CALLER_DATA = {};
  public static final Marker MARKER = null;
  public static final Map<String, String> MDC = Collections.emptyMap();
  public static final long TIME_STAMP = 1663865676L;
  public static final ThrowableProxy THROWABLE_PROXY = ThrowableProxyFixture.create();

  public ILoggingEventForTests create() {
    return create(MESSAGE, THROWABLE_PROXY);
  }

  public ILoggingEventForTests create(String message) {
    return create(message, THROWABLE_PROXY);
  }

  public ILoggingEventForTests create(ThrowableProxy throwableProxy) {
    return create(MESSAGE, throwableProxy);
  }

  public ILoggingEventForTests create(String message, ThrowableProxy throwableProxy) {

    return ILoggingEventForTests.builder()
        .threadName(THREAD_NAME)
        .level(LEVEL)
        .message(message)
        .argumentArray(ARGUMENT_ARRAY)
        .formattedMessage(FORMATTED_MESSAGE)
        .loggerName(LOGGER_NAME)
        .loggerContextVO(LOGGER_CONTEXT_VO)
        .throwableProxy(throwableProxy)
        .callerData(CALLER_DATA)
        .marker(MARKER)
        .mdc(MDC)
        .timeStamp(TIME_STAMP)
        .build();
  }
}
