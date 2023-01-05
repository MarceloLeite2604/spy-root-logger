# Spy Root Logger

A JUnit5 extension to assist monitoring Logback log events and elaborate tests
to confirm that events were logged.

## Examples

```java
  import com.figtreelake.spyrootlogger.SpyRootLogger;
  import com.figtreelake.spyrootlogger.SpyRootLoggerExtension;
  import com.figtreelake.spyrootlogger.SpyRootLoggerInject;
  import org.junit.jupiter.api.BeforeEach;
  import org.junit.jupiter.api.extension.ExtendWith;
      
  import java.util.regex.Pattern;
         
  @ExtendWith(SpyRootLoggerExtension.class)
  class FooTest {
         
    private Foo foo;
         
    @SpyRootLoggerInject
    private SpyRootLogger spyRootLogger;
         
    @BeforeEach
    void setUp() {
      foo = new Foo();
    }
         
    @Test
    void shouldLogExceptionWhenHandlingRuntimeException() {
      final var exception = new RuntimeException("Dummy exception");
   
      final var responseEntity = foo.handleException(exception);
         
      /* Spy Root Logger can be used to check if there is a log event with 
      an exception attached. */
      assertThat(spyRootLogger.findEventsByThrowableAttached(exception))
          .isNotEmpty();
    }
         
    @Test
    void shouldLogErrorEventWhenHandlingIllegalStateException() {
      final var mockedIllegalStateException = mock(IllegalStateException.class);
   
      final var responseEntity = foo.handleException(mockedIllegalStateException);
   
      /* Spy Root Logger can be used to check how many events of such level were 
      logged */
      assertThat(spyRootLogger.countErrorEvents()).isPositive();
    }
         
    @Test
    void shouldLogMessageInformingMessageNotReadableWhenHandlingHttpMessageNotReadableException() {
      final var mockedHttpMessageNotReadableException = mock(HttpMessageNotReadableException.class);
         
      final var messagePattern = Pattern.compile("^Failed to read HTTP message");
         
      final var responseEntity = foo.handleException(mockedHttpMessageNotReadableException);
         
      /* Spy Root Logger can be used to find log events by regular expressions 
      on its message */
      assertThat(spyRootLogger.findEventsByMessagePattern(messagePattern))
        .isNotEmpty();
    }
  }
```

## Usage

1. Be sure to implement your tests using [JUnit5][junit5] framework.

2. Add Spy Root Logger on your project.

   1. For Maven projects add the following on your `pom.xml` file
      under `<dependencies>` tag.
      ```xml
      <dependency>
        <groupId>com.figtreelake</groupId>
        <artifactId>spy-root-logger</artifactId>
        <version>1.0</version>
      </dependency>
      ```
      
   2. For Gradle projects add the following on your `build.gradle` file
      under `dependencies` declaration.
      ```groovy
      implementation 'com.figtreelake:spy-root-logger:1.0'
      ```
      
   3. You can check the latest version available
      on [Maven Central repository][maven-central-repository].

3. Annotate your test class with [`@ExtendWith`][extend-with] informing [`SpyRootLoggerExtension`][spy-root-logger-extension]
   class as value.

4. Add a [`SpyRootLogger`][spy-root-logger] field and annotate it with [`@SpyRootLoggerInject`][spy-root-logger-inject].

   ```java
      import com.figtreelake.spyrootlogger.SpyRootLogger;
      import com.figtreelake.spyrootlogger.SpyRootLoggerExtension;
      import com.figtreelake.spyrootlogger.SpyRootLoggerInject;
      import org.junit.jupiter.api.extension.ExtendWith;
   
      @ExtendWith(SpyRootLoggerExtension.class)
      class FooTest {
   
        @SpyRootLoggerInject
        private SpyRootLogger spyRootLogger;
   
        /* Test declarations... */
      }
   ``` 

[extend-with]: https://junit.org/junit5/docs/current/user-guide/#extensions-registration-declarative
[junit5]: https://junit.org/junit5/
[maven-central-repository]: https://mvnrepository.com/repos/central
[spy-root-logger]: ./src/main/java/com/figtreelake/spyrootlogger/SpyRootLogger.java
[spy-root-logger-extension]: ./src/main/java/com/figtreelake/spyrootlogger/SpyRootLoggerExtension.java
[spy-root-logger-inject]: ./src/main/java/com/figtreelake/spyrootlogger/SpyRootLoggerInject.java
