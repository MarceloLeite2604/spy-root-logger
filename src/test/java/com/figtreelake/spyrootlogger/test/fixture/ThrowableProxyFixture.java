package com.figtreelake.spyrootlogger.test.fixture;

import ch.qos.logback.classic.spi.ThrowableProxy;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ThrowableProxyFixture {

  public ThrowableProxy create(Throwable throwable) {
    return new ThrowableProxy(throwable);
  }

  public ThrowableProxy create() {
    return create(LogEventFixture.THROWABLE);
  }
}
