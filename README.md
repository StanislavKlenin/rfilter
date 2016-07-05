# rfilter
sorting/filtering programming assignment

## Build and run

For any method, `-o` argument (output file) and at least one non-option argument is required, but any number of reports may be specified in any order.
Service GUID summary (`GUID=count` format, sorted by count in descending order) is printed to standard output.

### Example with Gradle and shadowJar plugin
in `rfilter` directory execute:
```
gradle shadowJar
java -jar ./build/libs/rfilter-0.0.1-all.jar -o out.csv -- reports.xml reports.json reports.csv
```

### Example with Gradle and execArgs
in `rfilter` directory execute:
```
gradle build
gradle run -Dexec.args="-o out.csv -- reports.xml reports.json reports.csv"
```

### Alternatively, import Gradle project into IntelliJ IDEA
See [importing gradle project](https://www.jetbrains.com/help/idea/2016.1/importing-project-from-gradle-model.html)
and [run/debug configuration](https://www.jetbrains.com/help/idea/2016.1/run-debug-configuration-application.html)

