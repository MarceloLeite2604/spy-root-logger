package com.figtreelake.spyrootlogger;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import com.figtreelake.spyrootlogger.mapper.ILoggingEventToLogEventMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Pattern;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class SpyRootLogger extends AppenderBase<ILoggingEvent> {

  private final Map<Level, List<LogEvent>> logEventsByLevel;
  private final ILoggingEventToLogEventMapper iLoggingEventToLogEventMapper;

  public SpyRootLogger() {
    logEventsByLevel = new HashMap<>();
    iLoggingEventToLogEventMapper = new ILoggingEventToLogEventMapper();
  }

  @Override
  protected void append(ILoggingEvent iLoggingEvent) {

    final var logEvent = iLoggingEventToLogEventMapper.mapTo(iLoggingEvent);

    Optional.ofNullable(logEventsByLevel.get(logEvent.getLevel()))
        .ifPresentOrElse(
            logEvents -> logEvents.add(logEvent),
            () -> {
              final var logEvents = new ArrayList<LogEvent>();
              logEvents.add(logEvent);
              logEventsByLevel.put(logEvent.getLevel(), logEvents);
            });
  }

  public int countErrorEvents() {
    return countMessagesOfLevel(Level.ERROR);
  }

  public int countWarningEvents() {
    return countMessagesOfLevel(Level.WARN);
  }

  public int countInfoEvents() {
    return countMessagesOfLevel(Level.INFO);
  }

  private int countMessagesOfLevel(Level level) {
    return Optional.ofNullable(logEventsByLevel.get(level))
        .map(List::size)
        .orElse(0);
  }

  public List<LogEvent> findEventsByMessage(String message) {
    return findEventsByMatchFunction(logEvent -> message.equals(logEvent.getMessage()));
  }

  public List<LogEvent> findEventsByInstanceOfThrowableAttached(Class<? extends Throwable> throwableClass) {
    return findEventsByMatchFunction(logEvent -> logEvent.isThrowableAttachedInstanceOf(throwableClass));
  }

  public List<LogEvent> findEventsByThrowableAttached(Throwable throwable) {
    return findEventsByMatchFunction(logEvent -> logEvent.isThrowableAttached(throwable));
  }

  public List<LogEvent> findEventsByMessagePattern(Pattern pattern) {
    return findEventsByMatchFunction(logEvent -> pattern.matcher(logEvent.getMessage())
        .matches());
  }

  private List<LogEvent> findEventsByMatchFunction(Predicate<LogEvent> matchFunction) {
    final var result = new ArrayList<LogEvent>();
    for (List<LogEvent> logEvents : logEventsByLevel.values()) {
      for (LogEvent logEvent : logEvents) {
        if (matchFunction.test(logEvent)) {
          result.add(logEvent);
        }
      }
    }

    return result;
  }

  public void clearMessages() {
    logEventsByLevel.clear();
  }
}
