package com.figtreelake.spyrootlogger;

import com.figtreelake.spyrootlogger.test.fixture.LogEventFixture;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LogEventTest {

  @Test
  void shouldReturnTrueWhenLogEventHasInstanceOfThrowableAttached() {
    final var logEvent = LogEventFixture.create();

    assertThat(logEvent.isThrowableAttachedInstanceOf(LogEventFixture.THROWABLE.getClass())).isTrue();
    assertThat(logEvent.isThrowableAttachedInstanceOf(RuntimeException.class)).isTrue();
  }

  @Test
  void shouldReturnFalseWhenLogEventDoesNotHaveInstanceOfThrowableAttached() {
    final var logEvent = LogEventFixture.create();

    assertThat(logEvent.isThrowableAttachedInstanceOf(ArithmeticException.class)).isFalse();
  }

  @Test
  void shouldReturnFalseWhenLogEventDoesAThrowableAttached() {
    final var logEvent = LogEventFixture.create((Throwable) null);

    assertThat(logEvent.isThrowableAttachedInstanceOf(ArithmeticException.class)).isFalse();
  }

  @Test
  void shouldThrowIllegalArgumentExceptionWhenInformedClassIsNull() {
    final var logEvent = LogEventFixture.create();

    assertThrows(IllegalArgumentException.class, () -> logEvent.isThrowableAttachedInstanceOf(null));
  }

  @Test
  void shouldReturnTrueWhenThrowableInstanceIsAttached() {
    final var logEvent = LogEventFixture.create();

    assertThat(logEvent.isThrowableAttached(LogEventFixture.THROWABLE)).isTrue();
  }

  @Test
  void shouldReturnFalseWhenThrowableInstanceIsNotAttached() {
    final var logEvent = LogEventFixture.create();
    final var exception = new ArithmeticException("Dummy exception");

    assertThat(logEvent.isThrowableAttached(exception)).isFalse();
  }

  @Test
  void shouldReturnFalseWhenInvokingIsThrowableAttachedAndLogEventDoesAThrowableAttached() {
    final var logEvent = LogEventFixture.create((Throwable) null);
    final var exception = new ArithmeticException("Dummy exception");

    assertThat(logEvent.isThrowableAttached(exception)).isFalse();
  }

  @Test
  void shouldThrowIllegalArgumentExceptionWhenExceptionIsNull() {
    final var logEvent = LogEventFixture.create();

    assertThrows(IllegalArgumentException.class, () -> logEvent.isThrowableAttached(null));
  }
}