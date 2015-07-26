package net.bramp.unsafe.collection;

public interface BenchmarkInterface {
    void setup() throws Exception;
    void setSize(int size);

    // Returns the size of the datastructures
    long bytes();
}