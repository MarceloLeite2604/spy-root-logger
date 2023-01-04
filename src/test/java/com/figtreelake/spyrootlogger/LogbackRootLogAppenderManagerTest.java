package com.figtreelake.spyrootlogger;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.core.Appender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;

class LogbackRootLogAppenderManagerTest {

  private LogbackRootLogAppenderManager logbackRootLogAppenderManager;

  final Logger logger = (Logger)LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
  
  @BeforeEach
  void setUp() {
    logbackRootLogAppenderManager = new LogbackRootLogAppenderManager();
  }

  @SuppressWarnings("unchecked")
  @Test
  void shouldAttachAppender() {
    final var mockedAppender = mock(Appender.class);
    logbackRootLogAppenderManager.add(mockedAppender);

    assertThat(logger.isAttached(mockedAppender)).isTrue();
  }

  @SuppressWarnings("unchecked")
  @Test
  void shouldDetachAppender() {
    final var mockedAppender = mock(Appender.class);
    logbackRootLogAppenderManager.add(mockedAppender);

    assertThat(logger.isAttached(mockedAppender)).isTrue();

    logbackRootLogAppenderManager.detach(mockedAppender);

    assertThat(logger.isAttached(mockedAppender)).isFalse();
  }

  @SuppressWarnings("unchecked")
  @Test
  void shouldNotThrowExceptionWhenAttemptingToDetachAppenderNotAttached() {
    final var mockedAppender = mock(Appender.class);

    assertDoesNotThrow(() -> logbackRootLogAppenderManager.detach(mockedAppender));
  }
}