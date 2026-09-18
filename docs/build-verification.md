# Build Verification Record

## Local verification

The project was built and tested locally on 16 September 2026 using Maven and a newer installed JDK with the project compiler target configured to Java 17.

### Maven test

Command:

```text
mvn clean test
```

Observed result:

```text
Compiling 41 source files with javac [debug release 17]
Tests run: 2, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

### Maven package

Command:

```text
mvn clean package
```

Observed result:

```text
Building jar: target/campusflow-1.0.0.jar
BUILD SUCCESS
```

The compiler configuration uses `<maven.compiler.release>17</maven.compiler.release>` so the application is compiled against the Java 17 platform level even when a newer JDK is installed.
