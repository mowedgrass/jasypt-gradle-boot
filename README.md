# jasypt-gradle-boot

**[Jasypt](http://jasypt.org)** gradle plugin for Spring Boot. Based on [jasypt-spring-boot](https://github.com/ulisesbocchio/jasypt-spring-boot).

Plugin helps to encrypt sensitive properties and prevents from reading them directly from the source code files.
Jasypt decrypts them during runtime.

## How to start

Add a buildscript dependency:
```groovy
buildscript {
 repositories {
     mavenCentral()
 }
 dependencies {
     classpath 'org.springframework.boot:spring-boot-gradle-plugin:1.3.0.RELEASE'
 }
}
```

Apply the plugin (_after java plugin!_):
```groovy
apply plugin: 'encrypt'
```


## Tasks

For all plugin's tasks password can be provided as a command line parameter or as a system environment variable - look below for details.

### encryptText

```bash
./gradlew encryptText --text <text> [--password <password>]
```

### decryptText

```bash
./gradlew decryptText --text <text> [--password <password>]
```

### encryptProperties

```bash
./gradlew encryptProperties [--password <password>] [--confirm]
```

Parses property files (*.properties and *.yml) and encrypts properties marked with **ENCRYPT** tag.
For example property `app.secret` with value `to-be-encrypted` marked as:
```properties
app.secret=ENCRYPT(to-be-encrypted)
```
will be encrypted to:
```properties
app.secret=ENC(9ERJIetzDhJqIAdS6q7y9N5BtAJlq0KG)
```

You will be warned if any other properties is encrypted with different password.

Without **--confirm** parameter task works in a dry-run mode.

During encryption the original file will be backed up to \<file\>.bak. You will probably want to add this extension to your .gitignore file.

### decryptProperties

```bash
./gradlew decryptProperties [--password <password>] [--tag] [--confirm]
```

Parses property files (*.properties and *.yml) and decrypts properties marked with **ENC** tag.
For example property `app.secret` with encrypted value marked as:
```properties
app.secret=ENC(9ERJIetzDhJqIAdS6q7y9N5BtAJlq0KG)
```
will be decrypted to:
```properties
app.secret=to-be-encrypted
```
or (if **--tag** parameter is provided) to:
```properties
app.secret=ENCRYPT(to-be-encrypted)
```

You will be warned if any other properties is encrypted with different password.

Without **--confirm** parameter task works in a dry-run mode.

During decryption the original file will be backed up to \<file\>.bak. You will probably want to add this extension to your .gitignore file.

## Password

Plugin and runtime decryptor uses the first available password source:

* standard jasypt password property: `jasypt.encryptor.password`
* abbreviation of the above: `jasypt.password`
* system environment variable: `JASYPT_ENCRYPTOR_PASSWORD`
* abbreviation of the above: `JASYPT_PASSWORD`

There are several ways to pass the password:
* to jar
    * as an argument:
    ```bash
        java -jar <app.jar> --jasypt.password=<password>
    ```

    * as a property:
    ```bash
        java -Djasypt.password=<password> -jar <app.jar>
    ```

    * as a system environment property:
    ```bash
        JASYPT_PASSWORD=<password>
        java -jar <app.jar>
    ```

* to gradle task
    * as a property:
    ```
        ./gradlew bootRun -Djasypt.password=<password>
    ```

    * as a system environment property:
    ```bash
        JASYPT_PASSWORD=<password>
        ./gradlew bootRun
    ```
    ```bash
        JASYPT_PASSWORD=<password>
        ./gradlew encryptText --text <text>
    ```
    ```bash
        JASYPT_PASSWORD=<password>
        ./gradlew decryptProperties
    ```


## Configuration

Application and gradle plugin uses the same configuration file `jasypt.properties` (if provided). 
Look [here](https://github.com/ulisesbocchio/jasypt-spring-boot#encryption-configuration) for details.

## Tips

* keep your default spring profile unencrypted - don't make your life too complicated
* don't try to use different passwords in related profiles

## Demo

Run the default profile:
```bash
./gradlew bootRun
```

Change spring profile and try to run application without password:
```bash
SPRING_PROFILES_ACTIVE=production
./gradlew bootRun
```

Oops, what an ugly exception. Try this:
```bash
SPRING_PROFILES_ACTIVE=staging
JASYPT_PASSWORD=simple
./gradlew bootRun
```

Or this:
```bash
SPRING_PROFILES_ACTIVE=production
JASYPT_PASSWORD=qwerty
./gradlew bootRun
```

Play with decryptProperties and encryptProperties tasks. Notice the warning as a result of different passwords in different profiles/property files.
