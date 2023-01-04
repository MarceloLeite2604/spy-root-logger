package com.figtreelake.spyrootlogger;

import ch.qos.logback.classic.Level;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.util.Assert;

@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class LogEvent {

  private final Level level;

  private final String message;

  private final Throwable throwable;

  public boolean isThrowableAttachedInstanceOf(Class<? extends Throwable> throwableClass) {
    Assert.notNull(throwableClass, "Throwable class cannot be null.");

    if (throwable == null) {
      return false;
    }

    return throwableClass.isInstance(throwable);
  }

  public boolean isThrowableAttached(Throwable throwable) {
    Assert.notNull(throwable, "Throwable argument cannot be null.");

    if (this.throwable == null) {
      return false;
    }

    return this.throwable.equals(throwable);
  }
}
