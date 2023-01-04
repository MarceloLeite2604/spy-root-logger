package com.figtreelake.spyrootlogger.test.fixture;

import ch.qos.logback.classic.Level;
import com.figtreelake.spyrootlogger.LogEvent;
import lombok.experimental.UtilityClass;

@UtilityClass
public class LogEventFixture {

  public static final Level LEVEL = Level.WARN;
  public static final String MESSAGE = "messageContent";
  public static final Throwable THROWABLE = new IllegalArgumentException("dummy exception");

  public static LogEvent create(String message, Throwable throwable) {
    return LogEvent.builder()
        .level(LEVEL)
        .message(message)
        .throwable(throwable)
        .build();
  }

  public static LogEvent create() {
    return create(MESSAGE, THROWABLE);
  }

  public static LogEvent create(String message) {
    return create(message, THROWABLE);
  }

  public static LogEvent create(Throwable throwable) {
    return create(MESSAGE, throwable);
  }
}
