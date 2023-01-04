package com.figtreelake.spyrootlogger;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

public class SpyRootLogger extends AppenderBase<ILoggingEvent> {

  private final Map<Level, List<LogEvent>> logEventsByLevel;

  @Override
  protected void append(ILoggingEvent iLoggingEvent) {

  }
}
