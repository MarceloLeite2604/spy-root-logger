package com.figtreelake.spyrootlogger;

import ch.qos.logback.classic.Level;
import ch.qos.logback.core.AppenderBase;
import com.figtreelake.spyrootlogger.mapper.ILoggingEventToLogEventMapper;
import com.figtreelake.spyrootlogger.test.fixture.ILoggingEventForTestsFixture;
import com.figtreelake.spyrootlogger.test.fixture.LogEventFixture;
import com.figtreelake.spyrootlogger.test.fixture.ThrowableProxyFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpyRootLoggerTest {

  @InjectMocks
  private SpyRootLogger spyRootLogger;

  @Mock
  private ILoggingEventToLogEventMapper iLoggingEventToLogEventMapper;

  @Spy
  private Map<Level, List<LogEvent>> logEventsByLevelMap = new HashMap<>();

  @Test
  void shouldReturnInstanceOfAppenderBase() {
    assertThat(new SpyRootLogger()).isInstanceOf(AppenderBase.class);
  }

  @MethodSource("provideParametersForShouldAppendLogEventOnSpecifiedLogLevelTest")
  @ParameterizedTest
  void shouldAppendLogEventOnSpecifiedLogLevel(Level logLevel, Method countEventMethod) throws Exception {

    final var dummyLoggingEvent = ILoggingEventForTestsFixture.create()
        .toBuilder()
        .level(logLevel)
        .build();

    int totalEvents = retrieveCountEventMethodResult(countEventMethod);
    assertThat(totalEvents).isZero();

    when(iLoggingEventToLogEventMapper.mapTo(any())).thenCallRealMethod();

    spyRootLogger.append(dummyLoggingEvent);

    totalEvents = retrieveCountEventMethodResult(countEventMethod);
    assertThat(totalEvents).isEqualTo(1);
  }

  private int retrieveCountEventMethodResult(Method countEventMethod)
      throws IllegalAccessException, InvocationTargetException {
    final var totalEvents = countEventMethod.invoke(spyRootLogger);
    assertThat(totalEvents).isInstanceOf(Integer.class);
    return (Integer) totalEvents;
  }

  @Test
  void shouldReturnEvents() {
    final var dummyLoggingEvent = ILoggingEventForTestsFixture.create();

    when(iLoggingEventToLogEventMapper.mapTo(any())).thenCallRealMethod();

    spyRootLogger.append(dummyLoggingEvent);

    final var events = spyRootLogger.findEventsByMessage(ILoggingEventForTestsFixture.FORMATTED_MESSAGE);

    assertThat(events).hasSize(1);
  }

  @Test
  void shouldClearLogEvents() {
    spyRootLogger.clearEvents();

    verify(logEventsByLevelMap, times(1)).clear();
  }

  @Test
  void shouldFindEventByInstanceOfThrowableAttached() {

    final Throwable arithmeticException = new ArithmeticException("dummy exception");
    final var throwableProxyWithArithmeticException = ThrowableProxyFixture.create(arithmeticException);
    final var iLoggingEventWithArithmeticException = ILoggingEventForTestsFixture.create(
        throwableProxyWithArithmeticException);
    final var logEventWithArithmeticException = LogEventFixture.create(arithmeticException);

    final var logEvent = LogEventFixture.create();
    final var iLoggingEvent = ILoggingEventForTestsFixture.create();

    when(iLoggingEventToLogEventMapper.mapTo(any())).thenReturn(logEventWithArithmeticException)
        .thenReturn(logEvent);

    spyRootLogger.append(iLoggingEventWithArithmeticException);
    spyRootLogger.append(iLoggingEvent);

    final var logEvents = spyRootLogger.findEventsByInstanceOfThrowableAttached(
        ArithmeticException.class);

    assertThat(logEvents).containsExactly(logEventWithArithmeticException);
  }

  @Test
  void shouldFindEventByThrowableAttached() {

    final Throwable targetThrowable = new ArithmeticException("dummy target exception");
    final var throwableProxyWithTargetThrowable = ThrowableProxyFixture.create(targetThrowable);
    final var iLoggingEventWithTargetThrowable = ILoggingEventForTestsFixture.create(throwableProxyWithTargetThrowable);
    final var logEventWithTargetThrowable = LogEventFixture.create(targetThrowable);

    final Throwable throwable = new ArithmeticException("dummy non-target exception");
    final var throwableProxy = ThrowableProxyFixture.create(throwable);
    final var iLoggingEvent = ILoggingEventForTestsFixture.create(throwableProxy);
    final var logEvent = LogEventFixture.create(throwable);

    when(iLoggingEventToLogEventMapper.mapTo(any())).thenReturn(logEventWithTargetThrowable)
        .thenReturn(logEvent);

    spyRootLogger.append(iLoggingEventWithTargetThrowable);
    spyRootLogger.append(iLoggingEvent);

    final var logEvents = spyRootLogger.findEventsByThrowableAttached(targetThrowable);

    assertThat(logEvents).containsExactly(logEventWithTargetThrowable);
  }

  private static Stream<Arguments> provideParametersForShouldAppendLogEventOnSpecifiedLogLevelTest() throws Exception {

    return Stream.of(
        Arguments.of(Level.INFO, SpyRootLogger.class.getMethod("countInfoEvents")),
        Arguments.of(Level.WARN, SpyRootLogger.class.getMethod("countWarningEvents")),
        Arguments.of(Level.ERROR, SpyRootLogger.class.getMethod("countErrorEvents"))
    );
  }

  @Test
  void shouldReturnLogEventWithMessageMatchingPattern() {
    final var message = "Out of cheese exception";
    final var pattern = Pattern.compile(".*cheese.*");

    final var iLoggingEventWithTargetMessage = ILoggingEventForTestsFixture.create(message);
    final var logEventWithTargetMessage = LogEventFixture.create(message);

    final var iLoggingEvent = ILoggingEventForTestsFixture.create();
    final var logEvent = LogEventFixture.create();

    when(iLoggingEventToLogEventMapper.mapTo(any())).thenReturn(logEventWithTargetMessage)
        .thenReturn(logEvent);

    spyRootLogger.append(iLoggingEventWithTargetMessage);
    spyRootLogger.append(iLoggingEvent);

    final var logEvents = spyRootLogger.findEventsByMessagePattern(pattern);

    assertThat(logEvents).containsExactly(logEventWithTargetMessage);
  }
}