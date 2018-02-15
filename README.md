# Collection-Strawman

[![Join the chat at https://gitter.im/scala/collection-strawman](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/scala/collection-strawman?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
[![Waffle.io board](https://badge.waffle.io/scala/collection-strawman.svg?label=ready&title=Ready+issues)](http://waffle.io/scala/collection-strawman)

Implementation of the new collections of Scala 2.13.

- [Gitter Discussion](https://gitter.im/scala/collection-strawman)
- [Dotty Issue](https://github.com/lampepfl/dotty/issues/818)
- [Scala Center Proposal](https://github.com/scalacenter/advisoryboard/blob/master/proposals/007-collections.md)

## Current Status and Roadmap

The new collections are available as a regular external library (see below usage
instructions). The collections live in the `strawman.collection` namespace
(instead of `scala.collection`).

Almost all operations and collection types of the current standard collections
are available. If you see something missing, please
[create an issue](https://github.com/scala/collection-strawman/issues/new).

The new collections will be part of the 2.13.0-M4 Scala distribution, where they will
replace the standard collections.

## Use it in your project

### Build setup

Add the following dependency to your project:

~~~ scala
libraryDependencies += "ch.epfl.scala" %% "collection-strawman" % "0.9.0"
libraryDependencies += "ch.epfl.scala" %% "collections-contrib" % "0.9.0" // optional
~~~

The 0.9.0 version is compatible with Scala 2.13 and Dotty 0.6. Scala 2.12 is also supported
but you might encounter type inference issues with it.

We also automatically publish snapshots on Sonatype:

~~~ scala
resolvers += Resolver.sonatypeRepo("snapshots")

libraryDependencies += "ch.epfl.scala" %% "collection-strawman" % "0.10.0-SNAPSHOT"
~~~

The `collections-contrib` artifact provides additional operations on the collections (see the
[Additional operations](#additional-operations) section).

### API Documentation

- [`collection-strawman`](https://static.javadoc.io/ch.epfl.scala/collection-strawman_2.12/0.9.0/index.html)
- [`collections-contrib`](https://static.javadoc.io/ch.epfl.scala/collections-contrib_2.12/0.9.0/index.html)

### Migrating from the standard collections to the strawman

There is an [entry in the FAQ](https://github.com/scala/collection-strawman/wiki/FAQ#what-are-the-breaking-changes)
that aims to list all breaking changes.

A tool is being developed to automatically migrate code that uses the standard
collection to use the strawman.

To use it, add the [scalafix](https://scalacenter.github.io/scalafix/) sbt plugin
to your build, as explained in
[its documentation](https://scalacenter.github.io/scalafix/#Installation).

Two situations are supported: (1) migrating a 2.12 code base to a 2.12 code base that
uses the collection strawman as a library (instead of the standard collections), and
(2) migrating a 2.12 code base to 2.13 code base.

The migration tool is not exhaustive and we will continue to improve
it over time. If you encounter a use case that’s not supported, please
report it as described in the
[contributing documentation](CONTRIBUTING.md#migration-tool).

#### Migrating a 2.12 code base to a 2.12 code base that uses the collection strawman as a library

Run the following sbt task on your project:

~~~
> scalafix https://github.com/scala/collection-strawman/raw/master/scalafix/2.12/rules/src/main/scala/fix/Collectionstrawman_v0.scala
~~~

In essence, the migration tool changes the imports in your source code
so that the strawman definitions are imported. It also rewrites
expressions that use an API that is different in the strawman.

#### Migrating a 2.12 code base to 2.13 code base

Run the following sbt task on your project:

~~~
> scalafix https://github.com/scala/collection-strawman/raw/master/scalafix/2.13/rules/src/main/scala/fix/Collectionstrawman_v0.scala
~~~

## Contributing

We welcome contributions!

For more information, see the [CONTRIBUTING](CONTRIBUTING.md) file.
