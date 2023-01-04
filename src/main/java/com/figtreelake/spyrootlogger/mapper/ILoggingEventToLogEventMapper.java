package com.figtreelake.spyrootlogger.mapper;

import com.figtreelake.spyrootlogger.LogEvent;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.ThrowableProxy;

public class ILoggingEventToLogEventMapper implements Mapper<ILoggingEvent, LogEvent> {

  @Override
  public LogEvent mapTo(ILoggingEvent iLoggingEvent) {
    final var level = iLoggingEvent.getLevel();
    final var message = iLoggingEvent.getFormattedMessage();
    final var throwable = retrieveThrowable(iLoggingEvent);

    return LogEvent.builder()
        .level(level)
        .message(message)
        .throwable(throwable)
        .build();
  }

  private Throwable retrieveThrowable(ILoggingEvent iLoggingEvent) {
    final var iThrowableProxy = iLoggingEvent.getThrowableProxy();
    if (iThrowableProxy instanceof ThrowableProxy throwableProxy) {
      return throwableProxy.getThrowable();
    }
    return null;
  }
}