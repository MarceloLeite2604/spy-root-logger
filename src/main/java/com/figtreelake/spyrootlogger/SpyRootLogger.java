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

/**
 * A {@link AppenderBase} extension to monitor logging events emitted by
 * <a href="https://logback.qos.ch/">Logback</a> root
 * {@link ch.qos.logback.classic.Logger}.
 *
 * @author MarceloLeite2604
 */
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class SpyRootLogger extends AppenderBase<ILoggingEvent> {

  private final Map<Level, List<LogEvent>> logEventsByLevel;
  private final ILoggingEventToLogEventMapper iLoggingEventToLogEventMapper;

  /**
   * The default constructor.
   *
   * @see SpyRootLogger
   */
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

  /**
   * Count all events of type {@link Level#ERROR}.
   *
   * @return The total {@link Level#ERROR} events tracked.
   */
  public int countErrorEvents() {
    return countMessagesOfLevel(Level.ERROR);
  }

  /**
   * Count all events of type {@link Level#WARN}.
   *
   * @return The total {@link Level#WARN} events tracked.
   */
  public int countWarningEvents() {
    return countMessagesOfLevel(Level.WARN);
  }

  /**
   * Count all events of type {@link Level#INFO}.
   *
   * @return The total {@link Level#INFO} events tracked.
   */
  public int countInfoEvents() {
    return countMessagesOfLevel(Level.INFO);
  }

  private int countMessagesOfLevel(Level level) {
    return Optional.ofNullable(logEventsByLevel.get(level))
        .map(List::size)
        .orElse(0);
  }

  /**
   * Find all events which contains the messaged informed.
   *
   * @param message The event message to find.
   * @return All events which the message matches the one informed on
   * {@code message} parameter.
   */
  public List<LogEvent> findEventsByMessage(String message) {
    return findEventsByMatchFunction(logEvent -> message.equals(logEvent.getMessage()));
  }

  /**
   * Find all events which has an instance of {@code throwableClass} attached.
   *
   * @param throwableClass The {@link Throwable} type to match with the
   *                       throwable attached on the events.
   * @return All events which has a {@link Throwable} of type
   * {@code throwableClass} attached.
   */
  public List<LogEvent> findEventsByInstanceOfThrowableAttached(Class<? extends Throwable> throwableClass) {
    return findEventsByMatchFunction(logEvent -> logEvent.isThrowableAttachedInstanceOf(throwableClass));
  }

  /**
   * Find all events which has {@code throwable} object attached.
   *
   * @param throwable The {@link Throwable} object to check if it is attached in
   *                  the events.
   * @return All events which has {@code throwable} attached.
   */
  public List<LogEvent> findEventsByThrowableAttached(Throwable throwable) {
    return findEventsByMatchFunction(logEvent -> logEvent.isThrowableAttached(throwable));
  }

  /**
   * Find all events which message matches the regular expression pattern
   * informed.
   *
   * @param pattern The regular expression pattern to match with the event
   *                messages.
   * @return All events which message matches the pattern informed on
   * {@code pattern} parameter.
   */
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

  /**
   * Clear all events tracked.
   */
  public void clearEvents() {
    logEventsByLevel.clear();
  }
}
