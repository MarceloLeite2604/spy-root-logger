package com.figtreelake.spyrootlogger;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Test classes which uses <a href="https://junit.org/junit5/">JUnit5</a>
 * {@link SpyRootLoggerExtension} extension can use this annotation on
 * {@link SpyRootLogger} objects to inject an instance which can be used to
 * track and check log events.
 *
 * @author MarceloLeite2604
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SpyRootLoggerInject {
}
