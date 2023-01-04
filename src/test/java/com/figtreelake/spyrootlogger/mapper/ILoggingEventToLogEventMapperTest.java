package com.figtreelake.spyrootlogger.mapper;

import ch.qos.logback.classic.spi.ThrowableProxy;
import com.figtreelake.spyrootlogger.test.fixture.ILoggingEventForTestsFixture;
import com.figtreelake.spyrootlogger.test.fixture.LogEventFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ILoggingEventToLogEventMapperTest {

  private ILoggingEventToLogEventMapper iLoggingEventToLogEventMapper;

  @BeforeEach
  void setUp() {
    iLoggingEventToLogEventMapper = new ILoggingEventToLogEventMapper();
  }

  @Test
  void shouldReturnLogEvent() {
    final var iLoggingEventForTests = ILoggingEventForTestsFixture.create();
    final var expectedLogEvent = LogEventFixture.create();
    final var actualLogEvent = iLoggingEventToLogEventMapper.mapTo(iLoggingEventForTests);

    assertThat(actualLogEvent).usingRecursiveComparison()
        .isEqualTo(expectedLogEvent);
  }

  @Test
  void shouldReturnLogEventILoggingEventDoesNotHaveAThrowableProxy() {
    final var iLoggingEventForTests = ILoggingEventForTestsFixture.create((ThrowableProxy)null);
    final var expectedLogEvent = LogEventFixture.create((Throwable)null);
    final var actualLogEvent = iLoggingEventToLogEventMapper.mapTo(iLoggingEventForTests);

    assertThat(actualLogEvent).usingRecursiveComparison()
        .isEqualTo(expectedLogEvent);
  }
}