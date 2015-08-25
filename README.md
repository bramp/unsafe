# Unsafe
by Andrew Brampton ([bramp.net](https://bramp.net))

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


## Benchmarks

Read more about the benchmarks at bramp.net TODO add link.

```bash
cd unsafe-benchmark
mvn
java -jar target/benchmarks.jar | tee logs
...
# JMH 1.10.3 (released 9 days ago)
# VM version: JDK 1.8.0_45-internal, VM 25.45-b02

Benchmark                                       Mode     Cnt     Score          Error  Units
UnrolledCopierBenchmark.HandUnrolledState.test  thrpt    25      438819259.527  ±      14364692.101  ops/s
UnrolledCopierBenchmark.LoopState.test          thrpt    25      196408390.244  ±      2173851.339   ops/s
UnrolledCopierBenchmark.UnrolledState.test      thrpt    25      458324068.892  ±      6192069.477   ops/s

Benchmark                                  (clazz)                      (size)    Mode   Cnt    Score   Error  Units
ArrayListBenchmark.testIterate             ArrayList<LongPoint>         80000000  avgt   5      2.266   ±      0.229  s/op
ArrayListBenchmark.testIterate             ArrayList<LongPoint>         20000000  avgt   5      0.552   ±      0.019  s/op
ArrayListBenchmark.testIterate             ArrayList<LongPoint>         5000000   avgt   5      0.136   ±      0.004  s/op
ArrayListBenchmark.testSort                ArrayList<LongPoint>         80000000  avgt   5      70.310  ±      3.939  s/op
ArrayListBenchmark.testSort                ArrayList<LongPoint>         20000000  avgt   5      14.754  ±      0.541  s/op
ArrayListBenchmark.testSort                ArrayList<LongPoint>         5000000   avgt   5      3.250   ±      0.139  s/op

Benchmark                                  (clazz)                      (size)    Mode   Cnt    Score   Error  Units
ArrayListBenchmark.testIterate             UnsafeArrayList<LongPoint>   80000000  avgt   5      1.790   ±      0.030  s/op
ArrayListBenchmark.testIterate             UnsafeArrayList<LongPoint>   20000000  avgt   5      0.449   ±      0.016  s/op
ArrayListBenchmark.testIterate             UnsafeArrayList<LongPoint>   5000000   avgt   5      0.112   ±      0.001  s/op
ArrayListBenchmark.testListIterateInPlace  UnsafeArrayList<LongPoint>   80000000  avgt   5      0.442   ±      0.023  s/op
ArrayListBenchmark.testListIterateInPlace  UnsafeArrayList<LongPoint>   20000000  avgt   5      0.110   ±      0.003  s/op
ArrayListBenchmark.testListIterateInPlace  UnsafeArrayList<LongPoint>   5000000   avgt   5      0.028   ±      0.002  s/op
ArrayListBenchmark.testSort                UnsafeArrayList<LongPoint>   80000000  avgt   5      18.690  ±      3.158  s/op
ArrayListBenchmark.testSort                UnsafeArrayList<LongPoint>   20000000  avgt   5      3.414   ±      0.034  s/op
ArrayListBenchmark.testSort                UnsafeArrayList<LongPoint>   5000000   avgt   5      0.682   ±      0.014  s/op

Benchmark                                  (clazz)                      (size)    Mode   Cnt    Score   Error  Units
ArrayListBenchmark.testIterate             ArrayList<FourLongs>         80000000  avgt   5      2.277   ±      0.211  s/op
ArrayListBenchmark.testIterate             ArrayList<FourLongs>         20000000  avgt   5      0.557   ±      0.023  s/op
ArrayListBenchmark.testIterate             ArrayList<FourLongs>         5000000   avgt   5      0.140   ±      0.007  s/op
ArrayListBenchmark.testSort                ArrayList<FourLongs>         80000000  avgt   5      79.673  ±      6.119  s/op
ArrayListBenchmark.testSort                ArrayList<FourLongs>         20000000  avgt   5      16.705  ±      1.353  s/op
ArrayListBenchmark.testSort                ArrayList<FourLongs>         5000000   avgt   5      3.673   ±      0.156  s/op

Benchmark                                  (clazz)                      (size)    Mode   Cnt    Score   Error  Units
ArrayListBenchmark.testIterate             UnsafeArrayList<FourLongs>   80000000  avgt   5      2.126   ±      0.019  s/op
ArrayListBenchmark.testIterate             UnsafeArrayList<FourLongs>   20000000  avgt   5      0.533   ±      0.004  s/op
ArrayListBenchmark.testIterate             UnsafeArrayList<FourLongs>   5000000   avgt   5      0.133   ±      0.002  s/op
ArrayListBenchmark.testListIterateInPlace  UnsafeArrayList<FourLongs>   80000000  avgt   5      0.648   ±      0.019  s/op
ArrayListBenchmark.testListIterateInPlace  UnsafeArrayList<FourLongs>   20000000  avgt   5      0.163   ±      0.005  s/op
ArrayListBenchmark.testListIterateInPlace  UnsafeArrayList<FourLongs>   5000000   avgt   5      0.040   ±      0.006  s/op
ArrayListBenchmark.testSort                UnsafeArrayList<FourLongs>   80000000  avgt   5      24.822  ±      0.790  s/op
ArrayListBenchmark.testSort                UnsafeArrayList<FourLongs>   20000000  avgt   5      4.843   ±      0.075  s/op
ArrayListBenchmark.testSort                UnsafeArrayList<FourLongs>   5000000   avgt   5      1.020   ±      0.017  s/op

Benchmark                                  (clazz)                      (size)    Mode   Cnt    Score   Error  Units
ArrayListBenchmark.testIterate             ArrayList<EightLongs>        80000000  avgt   5      2.792   ±      0.072  s/op
ArrayListBenchmark.testIterate             ArrayList<EightLongs>        20000000  avgt   5      0.564   ±      0.022  s/op
ArrayListBenchmark.testIterate             ArrayList<EightLongs>        5000000   avgt   5      0.138   ±      0.007  s/op
ArrayListBenchmark.testSort                ArrayList<EightLongs>        80000000  avgt   5      97.687  ±      4.860  s/op
ArrayListBenchmark.testSort                ArrayList<EightLongs>        20000000  avgt   5      20.084  ±      1.124  s/op
ArrayListBenchmark.testSort                ArrayList<EightLongs>        5000000   avgt   5      4.474   ±      0.248  s/op

Benchmark                                  (clazz)                      (size)    Mode   Cnt    Score   Error  Units
ArrayListBenchmark.testIterate             UnsafeArrayList<EightLongs>  80000000  avgt   5      2.672   ±      0.322  s/op
ArrayListBenchmark.testIterate             UnsafeArrayList<EightLongs>  20000000  avgt   5      0.688   ±      0.014  s/op
ArrayListBenchmark.testIterate             UnsafeArrayList<EightLongs>  5000000   avgt   5      0.171   ±      0.003  s/op
ArrayListBenchmark.testListIterateInPlace  UnsafeArrayList<EightLongs>  80000000  avgt   5      0.941   ±      0.032  s/op
ArrayListBenchmark.testListIterateInPlace  UnsafeArrayList<EightLongs>  20000000  avgt   5      0.236   ±      0.008  s/op
ArrayListBenchmark.testListIterateInPlace  UnsafeArrayList<EightLongs>  5000000   avgt   5      0.058   ±      0.002  s/op
ArrayListBenchmark.testSort                UnsafeArrayList<EightLongs>  80000000  avgt   5      40.697  ±      0.743  s/op
ArrayListBenchmark.testSort                UnsafeArrayList<EightLongs>  20000000  avgt   5      7.608   ±      0.267  s/op
ArrayListBenchmark.testSort                UnsafeArrayList<EightLongs>  5000000   avgt   5      1.729   ±      0.101  s/op
```


## Further Reading
* [Java Objects Memory Structure](http://www.codeinstructions.com/2008/12/java-objects-memory-structure.html)
* [sun.misc.Unsafe Javadoc](http://www.docjar.com/docs/api/sun/misc/Unsafe.html)
* [OpenJDK Unsafe source](http://hg.openjdk.java.net/jdk7/jdk7/jdk/file/9b8c96f96a0f/src/share/classes/sun/misc/Unsafe.java)
* [The infamous sun.misc.Unsafe explained](http://mydailyjava.blogspot.com/2013/12/sunmiscunsafe.html)
* [Dangerous Code: How to be Unsafe with Java Classes & Objects in Memory](https://zeroturnaround.com/rebellabs/dangerous-code-how-to-be-unsafe-with-java-classes-objects-in-memory/)

### JMH
* [Introduction to JMH](http://java-performance.info/jmh/)
* [Hashmap Tests](https://github.com/mikvor/hashmapTest/blob/master/src/main/java/tests/MapTestRunner.java)
