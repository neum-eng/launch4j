To allow for easy JRE management and conflict prevention following the methodology described [here](https://github.com/neum-eng/.github-private/blob/master/docs/java.md#java-runtime), this custom version of Launch4J allows for searching through Windows registry to find JREs that have JavaFX modules present, if required by the application.

The primary use is through a [Maven plugin](https://github.com/neum-eng/launch4j-maven-plugin).
