# Universal Logger ![Build Status](https://github.com/fabbroniko/simple-dependency-injector/actions/workflows/build.yml/badge.svg?branch=master&event=push)

This lightweight and flexible dependency injection library, inspired by Spring, simplifies the management of object dependencies in Java applications. Key features include:

### Key Features:

- **Class Path Scanning**: Automatically discover and load annotated classes within your application, streamlining configuration and setup.
- **Automatic Dependency Resolution**: Seamlessly inject dependencies into classes without manual wiring, allowing for more maintainable and cleaner code.
- **Logger Injection**: Loggers can be natively injected into your classes, eliminating the need for boilerplate code to create or manage logging instances.

## Setup

### Gradle configuration

The package is currently hosted on GitHub Packages, therefore requiring some extra configuration in your `build.gradle` or `pom.xml`

Firstly define where gradle can find the package.

```groovy
repositories {
    mavenCentral()
    maven {
        name = "SimpleDependencyInjectorGitHubPackage"
        url = uri("https://maven.pkg.github.com/fabbroniko/simple-dependency-injector")
        credentials {
            username = project.findProperty("github.username") ?: System.getenv("GITHUB_USERNAME")
            password = project.findProperty("github.token.read") ?: System.getenv("GITHUB_READ_TOKEN")
        }
    }
}
```

GitHub doesn't allow unauthenticated pulls, so you'll need to create `gradle.properties` next to your `build.gradle` (if not
already present) with the following content:

```properties
github.username=# Your github username
github.token.read=# An access token with read:packages scope
```

Finally, import the library as usual.

```groovy
implementation 'com.fabbroniko:simple-dependency-injector:1.0.1'
```

### Quick Start

Annotate the classes that should be automatically injected with the `@Component` annotation.

```java
@Component
public class Sample {
    
    private final Logger logger;
    private final Dependency dependency;
    
    public Sample(final Logger logger, final Dependency dependency) {
        this.logger = logger;
        this.dependency = dependency;
    }
    
    public void print() {
        logger.info("Hello World");
    }
}
```

```java
@Component
public class Dependency {
    // ...
}
```

```java
@Configuration(componentScan = "com.fabbroniko", logger = Log4jLogManager.class)
public class Application {

    public static void main(final String[] args) {
        DependencyInjector.run(Application.class).<Sample>getInstance(Sample.class).print();
    }
}
```

In the sample above the creation of singleton instances of `Sample`, `Logger` and `Dependency` are managed by the library and happen behind the scene.

There are 2 interfaces you can declare as dependency that are automatically managed by the library:
- `Logger`: It uses the facade `com.fabbroniko:universal-logger` to create an instance of a logger of your choice (`Logj4`, `Java Util`, etc).
- `ApplicationContext`: It can be used to programmatically get instances of other annotated classes.

All other dependencies of your annotated class must be annotated classes as well.

You can (and should) use Interfaces in your constructor and a valid class will be resolved automatically. If your interface is implemented
by multiple classes, those classes may have to specify a qualifier that can be used as follows:

```java
public interface Animal {}

@Component
public class Dog implements Animal {}

@Component("anotherName")
public class Cat implements Animal {}

@Component
public class Client {
    
    public Client(@Qualifier("cat") final Animal cat, @Qualifier("anotherName") final Animal dog) {
        // ...
    }
}
```

## License

The source code for the site is licensed under the MIT license, which you can find in
the LICENSE.md file.