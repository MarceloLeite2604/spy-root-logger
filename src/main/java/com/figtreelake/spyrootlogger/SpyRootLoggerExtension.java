package com.figtreelake.spyrootlogger;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Optional;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class SpyRootLoggerExtension implements BeforeEachCallback, AfterEachCallback {

  private static final Class<? extends Annotation> TARGET_ANNOTATION = SpyRootLoggerInject.class;

  private static final Class<?> TARGET_FIELD_TYPE = SpyRootLogger.class;

  private final LogbackRootLogAppenderManager logbackRootLogAppenderManager;

  @Setter(AccessLevel.PACKAGE)
  @Getter(AccessLevel.PACKAGE)
  private SpyRootLogger spyRootLogger;

  public SpyRootLoggerExtension() {
    logbackRootLogAppenderManager = new LogbackRootLogAppenderManager();
  }

  @Override
  public void beforeEach(ExtensionContext extensionContext) {
    spyRootLogger = new SpyRootLogger();
    spyRootLogger.start();
    logbackRootLogAppenderManager.add(spyRootLogger);

    Optional.ofNullable(extensionContext.getRequiredTestInstance())
        .ifPresent(this::injectSpyRootLogger);
  }

  @Override
  public void afterEach(ExtensionContext extensionContext) {
    spyRootLogger.clearEvents();
    spyRootLogger.stop();
    logbackRootLogAppenderManager.detach(spyRootLogger);
  }

  private void injectSpyRootLogger(Object testInstance) {
    for (Field field : testInstance.getClass()
        .getDeclaredFields()) {
      if (field.isAnnotationPresent(TARGET_ANNOTATION)) {
        throwIllegalStateExceptionIfFieldIsNotOfTargetType(field);
        injectSpyRootLoggerOnField(testInstance, field);
      }
    }
  }

  // Since the object accessibility is immediately returned to its original value, we can suppress Sonar S3011 rule.
  @SuppressWarnings("java:S3011")
  private void injectSpyRootLoggerOnField(Object testInstance, Field field) {
    var fieldAccessChanged = false;
    try {
      if (!field.canAccess(testInstance)) {
        fieldAccessChanged = true;
        field.setAccessible(true);
      }
      field.set(testInstance, spyRootLogger);
      if (fieldAccessChanged) {
        field.setAccessible(false);
      }
    } catch (IllegalAccessException illegalAccessException) {
      final var message = String.format("Could not access \"%s\" field on test class to inject \"%s\" object.",
          field.getName(), SpyRootLogger.class.getSimpleName());
      throw new IllegalStateException(message, illegalAccessException);
    }
  }

  private void throwIllegalStateExceptionIfFieldIsNotOfTargetType(Field field) {
    if (!TARGET_FIELD_TYPE.equals(field.getType())) {
      final var message = String.format(
          "Field \"%s\" is annotated with \"%s\", but it is not an instance of \"%s\".", field.getName(),
          TARGET_ANNOTATION.getSimpleName(), TARGET_FIELD_TYPE.getSimpleName());
      throw new IllegalStateException(message);
    }
  }
}
