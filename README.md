# Fluff Core

[![](https://jitpack.io/v/muscaa/fluff-core.svg)](https://jitpack.io/#muscaa/fluff-core) [![License: Apache 2.0](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

## Description

Fluff Core serves as the essential API for all Fluff libraries.

## Usage

To integrate Fluff Core into your project, add the following dependency:

**Gradle**
```gradle
repositories {
	maven { url "https://jitpack.io" }
}

dependencies {
	implementation "com.github.muscaa:fluff-core:VERSION"
}
```
**Maven**
```maven
<repositories>
	<repository>
		<id>jitpack.io</id>
		<url>https://jitpack.io</url>
	</repository>
</repositories>

<dependencies>
	<dependency>
		<groupId>com.github.muscaa</groupId>
		<artifactId>fluff-core</artifactId>
		<version>VERSION</version>
	</dependency>
</dependencies>
```
Replace `VERSION` with the latest release available on [JitPack](https://jitpack.io/#muscaa/fluff-core).

### Developing Programs

Before using any Fluff library, simply initialize them with one line of code, typically in the `main` method:

```java
FluffCore.init();
```

### Developing Libraries

Copy the following text to `src/main/resources/fluff_lib.info` and edit it as you wish:
```info
v1
# First line must always be the Lib Supplier ID you want
# to use, without any comments after. If you dont know
# what this is, just leave it as default.


# (required) The author
author muscaa


# (required) Library ID
id fluff-core


# (optional) Library dependencies, that need to be
# initialized before this one, in "{author}/{id}" format,
# separated by commas
# depends muscaa/fluff-core # muscaa/fluff-core is always
							# initialized first so its not
							# needed, its just an example


# (optional) The url where to find this library
# url https://github.com/muscaa/fluff-core


# (optional) Main library class that will be initialized
# class fluff.core.FluffCore
```
