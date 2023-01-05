package com.figtreelake.spyrootlogger;

import ch.qos.logback.classic.Level;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.util.Assert;

/**
 * A simplified version of an event emitted by a
 * {@link ch.qos.logback.classic.Logger}.
 *
 * @author MarceloLeite2604
 */
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class LogEvent {

  private final Level level;

  private final String message;

  private final Throwable throwable;

  /**
   * Check if the {@link Throwable} attached to the event (if any) is an
   * instance of the type specified by {@code throwableClass} parameter.
   *
   * @param throwableClass Type to match with if the {@link Throwable} attached
   *                       with the event.
   * @return {@code true} if there is a {@link Throwable} attached and it
   * is an instance of {@code throwableClass} parameter. {@code false} otherwise
   */
  public boolean isThrowableAttachedInstanceOf(Class<? extends Throwable> throwableClass) {
    Assert.notNull(throwableClass, "Throwable class cannot be null.");

    if (throwable == null) {
      return false;
    }

    return throwableClass.isInstance(throwable);
  }

  /**
   * Check if the {@link Throwable} attached in the event matches the
   * {@code throwable} parameter informed.
   *
   * @param throwable The {@link Throwable} object to check if it is attached.
   * @return {@code true} if there is a throwable attached and it matches the
   * {@code throwable} parameter informed. {@code false} otherwise.
   */
  public boolean isThrowableAttached(Throwable throwable) {
    Assert.notNull(throwable, "Throwable argument cannot be null.");

    if (this.throwable == null) {
      return false;
    }

    return this.throwable.equals(throwable);
  }
}
