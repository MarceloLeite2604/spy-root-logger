package com.figtreelake.spyrootlogger;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpyRootLoggerExtensionTest {

  @InjectMocks
  private SpyRootLoggerExtension spyRootLoggerExtension;

  @Mock
  private LogbackRootLogAppenderManager logbackRootLogAppenderManager;

  @Test
  void shouldReturnInstanceOfBeforeEachCallbackAndAfterEachCallback() {
    assertThat(new SpyRootLoggerExtension()).isInstanceOf(BeforeEachCallback.class)
        .isInstanceOf(AfterEachCallback.class);
  }

  @Test
  void shouldAddInstanceOfSpyRootLoggerOnRootLogger() {
    final var mockedExtensionContext = mock(ExtensionContext.class);
    spyRootLoggerExtension.beforeEach(mockedExtensionContext);

    verify(logbackRootLogAppenderManager, times(1)).add(any(SpyRootLogger.class));
  }

  @Test
  void shouldInstantiateNewSpyRootLogger() {

    final var originalSpyRootLogger = spyRootLoggerExtension.getSpyRootLogger();

    final var mockedExtensionContext = mock(ExtensionContext.class);

    spyRootLoggerExtension.beforeEach(mockedExtensionContext);

    final var actualSpyRootLogger = spyRootLoggerExtension.getSpyRootLogger();

    assertThat(actualSpyRootLogger).isNotEqualTo(originalSpyRootLogger);
  }

  @Test
  void shouldInjectSpyRootLoggerOnTestInstance() {
    final var dummyTestInstance = new DummyTestInstance();
    final var mockedExtensionContext = mock(ExtensionContext.class);
    when(mockedExtensionContext.getRequiredTestInstance()).thenReturn(dummyTestInstance);
    spyRootLoggerExtension.beforeEach(mockedExtensionContext);

    assertThat(dummyTestInstance.spyRootLogger).isNotNull();
  }

  @Test
  void shouldThrowIllegalStateExceptionWhenAnnotatedTestInstanceFieldIsNotOfTypeSpyRootLogger() {
    final var dummyTestInstanceWithWrongTypeAnnotated = new DummyTestInstanceWithWrongTypeAnnotated();
    final var mockedExtensionContext = mock(ExtensionContext.class);
    when(mockedExtensionContext.getRequiredTestInstance()).thenReturn(dummyTestInstanceWithWrongTypeAnnotated);

    assertThrows(IllegalStateException.class, () -> spyRootLoggerExtension.beforeEach(mockedExtensionContext));
  }

  @Test
  void shouldStopAndClearSpyRootLoggerMessages() {
    final var mockedExtensionContext = mock(ExtensionContext.class);

    final var mockedSpyRootLogger = mock(SpyRootLogger.class);

    spyRootLoggerExtension.setSpyRootLogger(mockedSpyRootLogger);
    spyRootLoggerExtension.afterEach(mockedExtensionContext);

    verify(mockedSpyRootLogger, times(1)).stop();
    verify(mockedSpyRootLogger, times(1)).clearEvents();
  }

  @Test
  void shouldDetachSpyRootLoggerFromRootLogger() {
    final var mockedExtensionContext = mock(ExtensionContext.class);

    final var mockedSpyRootLogger = mock(SpyRootLogger.class);

    spyRootLoggerExtension.setSpyRootLogger(mockedSpyRootLogger);
    spyRootLoggerExtension.afterEach(mockedExtensionContext);

    verify(logbackRootLogAppenderManager, times(1)).detach(mockedSpyRootLogger);
  }

  static class DummyTestInstance {
    @SpyRootLoggerInject
    private SpyRootLogger spyRootLogger;
  }

  static class DummyTestInstanceWithWrongTypeAnnotated {
    @SpyRootLoggerInject
    private Long value;
  }
}