package com.figtreelake.spyrootlogger.test.implementation;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.LoggerContextVO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.slf4j.Marker;
import org.slf4j.event.KeyValuePair;

import java.util.List;
import java.util.Map;

@SuppressWarnings("java:S2187")
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ILoggingEventForTests implements ILoggingEvent {

  private final String threadName;

  private final Level level;

  private final String message;

  private final Object[] argumentArray;

  private final String formattedMessage;

  private final String loggerName;

  private final LoggerContextVO loggerContextVO;

  private final IThrowableProxy throwableProxy;

  private final StackTraceElement[] callerData;

  private final Marker marker;

  private final Map<String, String> mdc;

  private final long timeStamp;

  private final List<Marker> markerList;

  private int nanoseconds;

  private long sequenceNumber;

  private List<KeyValuePair> keyValuePairs;

  @Override
  public boolean hasCallerData() {
    return callerData != null && callerData.length > 0;
  }

  @Override
  public Map<String, String> getMDCPropertyMap() {
    return mdc;
  }

  @Override
  public void prepareForDeferredProcessing() {
  }
}
