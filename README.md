# Cloud Provider Dataset

Dataset for the Cloud Providers Microsoft Azure and Google Cloud Platform (GCP).

## Table of Content

* [Raw Datasets](dataset)
* [Azure Web Scraper](azure-web-scraper/src/main/java/cpu/spec/scraper)
* [Google Cloud Platform Web Scraper](gcp-web-scraper/src/main/java/cpu/spec/scraper)

## Installation

Tested on __OpenJDK 17.0.2__ & __Maven 3.6.3__.

```shell
mvn clean install -DskipTests
```

## Dataset Update

### [Azure Dataset]()

Extracted from
[source tbd]()
with the responsible __Web Scraper__.

```shell
cd azure-web-scraper
java -jar ./target/exectuable.jar
```

### [Google Cloud Platform Dataset]()

Extracted from
[source tbd]()
with the responsible __Web Scraper__.

```shell
cd gcp-web-scraper
java -jar ./target/exectuable.jar
```
