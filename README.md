# Surrounding Logs 🚀

[![License](https://img.shields.io/badge/license-MIT-green)](https://choosealicense.com/licenses/mit/)
[![Maven Central Version](https://img.shields.io/maven-central/v/io.github.paoloruggirello/surrounding-logs)](https://central.sonatype.com/artifact/io.github.paoloruggirello/surrounding-logs)

**Surrounding Logs** is a lightweight and flexible Java library that allows automatic logging of method input, output, and execution time through an annotation. By applying the `@SurroundingLogs` annotation to any method, logs are generated both before and after the method execution, providing clear insight into how the method operates, including its input parameters, returned values, and any potential exceptions.

The library is highly customizable, allowing you to tailor its behavior to fit your specific needs:

- 📝 **Log Input and Output**: You can configure the annotation to log the method's input parameters, the output, or both.
- ⚙️ **Log Level Configuration** : The logging level can be controlled through properties or specified directly at the method level via the annotation. The default log level is `DEBUG`, but it can be adjusted to `INFO`, `WARN`, `ERROR`, or other levels as needed.
- ⏱️ **Execution Time Logging** : The annotation can also automatically calculate and log the method's execution time.
- 🚨 **Exception Handling**: If an exception occurs during the method execution, the log level automatically switches to `ERROR`. Additionally, the log will capture details about the exception, such as the exception class and the error message, providing useful information for debugging.
- 🎛️ **Customization at the Method Level** : Specific methods can have their own log configurations, such as log level or execution time logging, independent of the global settings.

By integrating **Surrounding Logs** into your project, you can gain detailed visibility into the behavior of critical methods without writing extensive logging code yourself. The library is easy to configure and can be enabled or disabled via properties, offering full control over logging output.

## Import 🛠️

To add **Surrounding Logs** to your project, include the following dependency in your `pom.xml`:

```xml
<dependency>
    <groupId>io.github.pruggirello</groupId>
    <artifactId>surrounding-logs</artifactId>
    <version>${latestVersion}</version>
</dependency>
```
> **Note:** The library is disabled by default. You need to enable it explicitly in your configuration.

### Enabling the Library 🔧✨

To use the library, you need to enable it by setting the following property:

```properties
surrounding-logs.enabled=true
```
By default, the library is disabled. You also need to import the configuration class into your Spring Boot project:

```java
@Import(SurroundingLogsConfiguration.class)
```
This ensures that the library’s configuration is correctly loaded and ready for use.

## Features 🌟

The `@SurroundingLogs` annotation is highly customizable and supports various options:

- **Input Logging**: Logs the input parameters of the method.
- **Output Logging**: Logs the return value of the method.
- **Both Input and Output Logging**: Logs both the input parameters and the return value.

### Log Level Configuration ⚙️

The log level can be configured in different ways:

1. Through the `surrounding-logs.level` property in the configuration file.
2. By specifying the log level directly on the method annotation using the `logLevel` parameter.
3. If no level is specified, the default log level `DEBUG` is used.

If an exception is thrown during execution, the log level will automatically be set to `ERROR`.

### Priority Order 🔝

The log level follows this priority order:

1. Log level specified in the method annotation.
2. Log level specified in the properties.
3. Default log level (`DEBUG`).

### Execution Time Logging ⏳

The **Surrounding Logs** library not only captures input and output logs but also provides the functionality to log the execution time of methods. This feature is particularly useful for performance monitoring and optimization.

When you apply the `@SurroundingLogs` annotation to a method, you must specify the `includeExecutionTime` parameter and set it to `true` to enable execution time logging, as it is not calculated by default. This parameter allows the library to record the time before the method starts executing and then captures the time again once the execution is complete. This provides you with the duration the method takes to run, which can be invaluable when identifying bottlenecks in your application.

Here’s how you can enable execution time logging:

```java
@SurroundingLogs(includeExecutionTime = true)
public void myMethod() {
    // Method implementation
}
```
This comprehensive logging approach helps developers quickly understand the performance characteristics of their methods and make informed decisions about potential optimizations. Overall, logging execution time helps maintain the efficiency and responsiveness of applications by providing insights into which parts of your code may require further attention.


## Example Usage 📚

1. **Annotation without parameters and without modified property**: This will log the input and output of the method, will not include execution time, and will use `logLevel` as `DEBUG`.

    ```java
    @SurroundingLogs
    public void myMethod() {
        // Method implementation
    }
    ```

2. **Annotation without parameters but with property set to `INFO`**: This will log the input and output of the method, will not include execution time, and will use `logLevel` as `INFO`.

    ```java
    @SurroundingLogs
    public void myMethod() {
        // Method implementation
    }
    ```

   *(Assuming the property `surrounding-logs.level=INFO` is set in the configuration)*

3. **Annotation with `logLevel` parameter set to `WARN` but with property set to `INFO`**: This will log the input and output of the method, will not include execution time, and will use `logLevel` as `WARN`.

    ```java
    @SurroundingLogs(logLevel = Level.WARN)
    public void myMethod() {
        // Method implementation
    }
    ```

4. **Annotation with `logLevel` parameter set to `INFO` and execution time set to `true`**: This will log the input and output of the method, include execution time, and use `logLevel` as `INFO`.

    ```java
    @SurroundingLogs(logLevel = Level.INFO, includeExecutionTime = true)
    public void myMethod() {
        // Method implementation
    }
    ```

5. **Annotation with `logLevel` parameter set to `INFO`, execution time set to `true`, and `surroundingType` set to `BEFORE`**: This will log only the input, include execution time, and use `logLevel` as `INFO`.

    ```java
    @SurroundingLogs(logLevel = Level.INFO, includeExecutionTime = true, surroundingType = SurroundingType.BEFORE)
    public void myMethod() {
        // Method implementation
    }
    ```

6. **Annotation with `logLevel` parameter set to `INFO`, execution time set to `true`, and `surroundingType` set to `AFTER`**: This will log only the output, include execution time, and use `logLevel` as `INFO`.

    ```java
    @SurroundingLogs(logLevel = Level.INFO, includeExecutionTime = true, surroundingType = SurroundingType.AFTER)
    public void myMethod() {
        // Method implementation
    }
    ```

These examples demonstrate how to utilize the `@SurroundingLogs` annotation with different configurations to achieve various logging behaviors.


## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
