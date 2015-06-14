Unsafe
------
by Andrew Brampton (bramp.net)

This is a collection of tools that make use of the [sun.misc.Unsafe class](http://www.docjar.com/docs/api/sun/misc/Unsafe.html).
This Unsafe class allows direct access to memory within the JVM, which is extremely dangerous, but fun :).

* unsafe-helper - Contains some simple methods that make using sun.misc.Unsafe easier.
* unsafe-collection - An example List modelled on the ArrayList, which instead of storing reference to objects within
the collection, instead copies the elements directly into the list. This has a few interesting properties
  * Less total memory is required for the list and contained objects. Reducing GC overheads.
  * The objects are guranteed to be contingous in memory, which may provide some good CPU cache benefits.
  * Objects are copied into the list, this copy overhead may not be worth it, and you lose many of the reference semantics you would be used t
* unsafe-unroller - At runtimes generates optomal bytecode to copy objects with the Unsafe class.
* unsafe-benchmark - Code to benchmark everything using the [JMH framework](http://openjdk.java.net/projects/code-tools/jmh/).
* unsafe-tests - Some simple test classes to help with tests of the other modules.


Benchmarks
==========

```bash
cd unsafe-benchmark
mvn
java -jar target/benchmarks.jar
...
TODO
```


References
==========
JMH
* [Introduction to JMH](http://java-performance.info/jmh/)
* [Hashmap Tests](https://github.com/mikvor/hashmapTest/blob/master/src/main/java/tests/MapTestRunner.java)