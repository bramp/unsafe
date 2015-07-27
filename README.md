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
java -jar target/benchmarks.jar | tee logs
...
# JMH 1.10 (released 5 days ago)
# VM invoker: /usr/lib/jvm/java-8-openjdk-amd64/jre/bin/java

Benchmark                                        Mode  Cnt          Score         Error  Units
UnrolledCopierBenchmark.LoopState.test          thrpt   25  198289612.771 ± 1067151.047  ops/s
UnrolledCopierBenchmark.HandUnrolledState.test  thrpt   25  455263443.453 ± 2379784.462  ops/s
UnrolledCopierBenchmark.UnrolledState.test      thrpt   25  451185321.134 ± 5573610.904  ops/s

Benchmark                                                              (size)  Mode  Cnt   Score    Error  Units
UnsafeListLongPointBenchmark.ArrayListState.testListIterate          40000000  avgt    5   1.198 ±  0.021   s/op
UnsafeListLongPointBenchmark.ArrayListState.testListSort             40000000  avgt    5  31.979 ±  1.502   s/op
UnsafeListLongPointBenchmark.UnsafeListState.testListIterate         40000000  avgt    5   0.894 ±  0.006   s/op
UnsafeListLongPointBenchmark.UnsafeListState.testListIterateInPlace  40000000  avgt    5   0.217 ±  0.001   s/op
UnsafeListLongPointBenchmark.UnsafeListState.testListSortInPlace     40000000  avgt    5  15.323 ±  0.446   s/op

UnsafeListFourLongsBenchmark.ArrayListState.testListIterate          40000000  avgt    5   1.197 ±  0.145   s/op
UnsafeListFourLongsBenchmark.ArrayListState.testListSort             40000000  avgt    5  35.108 ±  2.046   s/op
UnsafeListFourLongsBenchmark.UnsafeListState.testListIterate         40000000  avgt    5   1.064 ±  0.005   s/op
UnsafeListFourLongsBenchmark.UnsafeListState.testListIterateInPlace  40000000  avgt    5   0.294 ±  0.001   s/op
UnsafeListFourLongsBenchmark.UnsafeListState.testListSortInPlace     40000000  avgt    5  18.722 ±  1.442   s/op

UnsafeListEightLongsBenchmark.ArrayListState.testListIterate          4000000  avgt    5   0.114 ±  0.014   s/op
UnsafeListEightLongsBenchmark.ArrayListState.testListSort             4000000  avgt    5   3.751 ±  0.262   s/op
UnsafeListEightLongsBenchmark.UnsafeListState.testListIterate         4000000  avgt    5   0.132 ±  0.002   s/op
UnsafeListEightLongsBenchmark.UnsafeListState.testListIterateInPlace  4000000  avgt    5   0.046 ±  0.001   s/op
UnsafeListEightLongsBenchmark.UnsafeListState.testListSortInPlace     4000000  avgt    5   1.380 ±  0.033   s/op
```

```
UnsafeListLongPointBenchmark.ArrayListState.testListIterate          40000000  avgt    5   1.198 ±  0.021   s/op
UnsafeListLongPointBenchmark.ArrayListState.testListSort                 4000  avgt    5   0.001 ±  0.001   s/op
UnsafeListLongPointBenchmark.ArrayListState.testListSort               400000  avgt    5   0.159 ±  0.002   s/op
UnsafeListLongPointBenchmark.ArrayListState.testListSort             40000000  avgt    5  31.979 ±  1.502   s/op
UnsafeListLongPointBenchmark.UnsafeListState.testListIterate             4000  avgt    5  ≈ 10⁻⁴            s/op
UnsafeListLongPointBenchmark.UnsafeListState.testListIterate           400000  avgt    5   0.010 ±  0.001   s/op
UnsafeListLongPointBenchmark.UnsafeListState.testListIterate         40000000  avgt    5   0.894 ±  0.006   s/op
UnsafeListLongPointBenchmark.UnsafeListState.testListIterateInPlace      4000  avgt    5  ≈ 10⁻⁵            s/op
UnsafeListLongPointBenchmark.UnsafeListState.testListIterateInPlace    400000  avgt    5   0.002 ±  0.001   s/op
UnsafeListLongPointBenchmark.UnsafeListState.testListIterateInPlace  40000000  avgt    5   0.217 ±  0.001   s/op
UnsafeListLongPointBenchmark.UnsafeListState.testListSortInPlace         4000  avgt    5  ≈ 10⁻³            s/op
UnsafeListLongPointBenchmark.UnsafeListState.testListSortInPlace       400000  avgt    5   0.051 ±  0.001   s/op
UnsafeListLongPointBenchmark.UnsafeListState.testListSortInPlace     40000000  avgt    5  15.323 ±  0.446   s/op
UnsafeListFourLongsBenchmark.ArrayListState.testListIterate              4000  avgt    5  ≈ 10⁻⁵            s/op
UnsafeListFourLongsBenchmark.ArrayListState.testListIterate            400000  avgt    5   0.010 ±  0.001   s/op
UnsafeListFourLongsBenchmark.ArrayListState.testListIterate          40000000  avgt    5   1.197 ±  0.145   s/op
UnsafeListFourLongsBenchmark.ArrayListState.testListSort                 4000  avgt    5   0.001 ±  0.001   s/op
UnsafeListFourLongsBenchmark.ArrayListState.testListSort               400000  avgt    5   0.186 ±  0.002   s/op
UnsafeListFourLongsBenchmark.ArrayListState.testListSort             40000000  avgt    5  35.108 ±  2.046   s/op
UnsafeListFourLongsBenchmark.UnsafeListState.testListIterate             4000  avgt    5  ≈ 10⁻⁴            s/op
UnsafeListFourLongsBenchmark.UnsafeListState.testListIterate           400000  avgt    5   0.011 ±  0.001   s/op
UnsafeListFourLongsBenchmark.UnsafeListState.testListIterate         40000000  avgt    5   1.064 ±  0.005   s/op
UnsafeListFourLongsBenchmark.UnsafeListState.testListIterateInPlace      4000  avgt    5  ≈ 10⁻⁵            s/op
UnsafeListFourLongsBenchmark.UnsafeListState.testListIterateInPlace    400000  avgt    5   0.003 ±  0.001   s/op
UnsafeListFourLongsBenchmark.UnsafeListState.testListIterateInPlace  40000000  avgt    5   0.294 ±  0.001   s/op
UnsafeListFourLongsBenchmark.UnsafeListState.testListSortInPlace         4000  avgt    5   0.001 ±  0.001   s/op
UnsafeListFourLongsBenchmark.UnsafeListState.testListSortInPlace       400000  avgt    5   0.071 ±  0.001   s/op
UnsafeListFourLongsBenchmark.UnsafeListState.testListSortInPlace     40000000  avgt    5  18.722 ±  1.442   s/op
UnsafeListEightLongsBenchmark.ArrayListState.testListIterate              400  avgt    5  ≈ 10⁻⁶            s/op
UnsafeListEightLongsBenchmark.ArrayListState.testListIterate            40000  avgt    5   0.001 ±  0.001   s/op
UnsafeListEightLongsBenchmark.ArrayListState.testListIterate          4000000  avgt    5   0.114 ±  0.014   s/op
UnsafeListEightLongsBenchmark.ArrayListState.testListSort                 400  avgt    5  ≈ 10⁻⁴            s/op
UnsafeListEightLongsBenchmark.ArrayListState.testListSort               40000  avgt    5   0.016 ±  0.001   s/op
UnsafeListEightLongsBenchmark.ArrayListState.testListSort             4000000  avgt    5   3.751 ±  0.262   s/op
UnsafeListEightLongsBenchmark.UnsafeListState.testListIterate             400  avgt    5  ≈ 10⁻⁵            s/op
UnsafeListEightLongsBenchmark.UnsafeListState.testListIterate           40000  avgt    5   0.001 ±  0.001   s/op
UnsafeListEightLongsBenchmark.UnsafeListState.testListIterate         4000000  avgt    5   0.132 ±  0.002   s/op
UnsafeListEightLongsBenchmark.UnsafeListState.testListIterateInPlace      400  avgt    5  ≈ 10⁻⁵            s/op
UnsafeListEightLongsBenchmark.UnsafeListState.testListIterateInPlace    40000  avgt    5  ≈ 10⁻³            s/op
UnsafeListEightLongsBenchmark.UnsafeListState.testListIterateInPlace  4000000  avgt    5   0.046 ±  0.001   s/op
UnsafeListEightLongsBenchmark.UnsafeListState.testListSortInPlace         400  avgt    5  ≈ 10⁻⁴            s/op
UnsafeListEightLongsBenchmark.UnsafeListState.testListSortInPlace       40000  avgt    5   0.010 ±  0.001   s/op
UnsafeListEightLongsBenchmark.UnsafeListState.testListSortInPlace     4000000  avgt    5   1.380 ±  0.033   s/op

```

`` 2015-07-25:
UnsafeListLongPointBenchmark.ArrayListState.testListIterate          40000000  avgt    5   1.337 ±  0.139   s/op
UnsafeListLongPointBenchmark.ArrayListState.testListSort             40000000  avgt    5  38.614 ±  4.235   s/op
UnsafeListLongPointBenchmark.UnsafeListState.testListIterate         40000000  avgt    5   0.928 ±  0.152   s/op
UnsafeListLongPointBenchmark.UnsafeListState.testListIterateInPlace  40000000  avgt    5   0.190 ±  0.012   s/op
UnsafeListLongPointBenchmark.UnsafeListState.testListSortInPlace     40000000  avgt    5  14.651 ±  1.667   s/op

UnsafeListLongPointBenchmark.ArrayListState.testListIterate              4000  avgt    5  ≈ 10⁻⁵            s/op
UnsafeListLongPointBenchmark.ArrayListState.testListIterate            400000  avgt    5   0.005 ±  0.001   s/op
UnsafeListLongPointBenchmark.ArrayListState.testListSort                 4000  avgt    5   0.001 ±  0.001   s/op
UnsafeListLongPointBenchmark.ArrayListState.testListSort               400000  avgt    5   0.137 ±  0.007   s/op
UnsafeListLongPointBenchmark.UnsafeListState.testListIterate             4000  avgt    5  ≈ 10⁻⁴            s/op
UnsafeListLongPointBenchmark.UnsafeListState.testListIterate           400000  avgt    5   0.009 ±  0.001   s/op
UnsafeListLongPointBenchmark.UnsafeListState.testListIterateInPlace      4000  avgt    5  ≈ 10⁻⁵            s/op
UnsafeListLongPointBenchmark.UnsafeListState.testListIterateInPlace    400000  avgt    5   0.002 ±  0.001   s/op
UnsafeListLongPointBenchmark.UnsafeListState.testListSortInPlace         4000  avgt    5  ≈ 10⁻³            s/op
UnsafeListLongPointBenchmark.UnsafeListState.testListSortInPlace       400000  avgt    5   0.045 ±  0.003   s/op
```


Further Reading
==============
* [Java Objects Memory Structure](http://www.codeinstructions.com/2008/12/java-objects-memory-structure.html)
* [sun.misc.Unsafe Javadoc](http://www.docjar.com/docs/api/sun/misc/Unsafe.html)
* [OpenJDK Unsafe source](http://hg.openjdk.java.net/jdk7/jdk7/jdk/file/9b8c96f96a0f/src/share/classes/sun/misc/Unsafe.java)
* [The infamous sun.misc.Unsafe explained](http://mydailyjava.blogspot.com/2013/12/sunmiscunsafe.html)
* [Dangerous Code: How to be Unsafe with Java Classes & Objects in Memory](https://zeroturnaround.com/rebellabs/dangerous-code-how-to-be-unsafe-with-java-classes-objects-in-memory/)


JMH
* [Introduction to JMH](http://java-performance.info/jmh/)
* [Hashmap Tests](https://github.com/mikvor/hashmapTest/blob/master/src/main/java/tests/MapTestRunner.java)