package net.bramp.unsafe;

import org.junit.Test;

public class UnrolledUnsafeCopierBuilderTest {

  @Test(expected = NullPointerException.class) public void testNullClass() throws Exception {
    new UnrolledUnsafeCopierBuilder()
      .of(null)
      .build(null);
  }

  @Test(expected = IllegalArgumentException.class) public void testInvalidOffset() throws Exception {
    new UnrolledUnsafeCopierBuilder()
        .offset(-1)
        .build(null);
  }

  @Test(expected = IllegalArgumentException.class) public void testInvalidLength() throws Exception {
    new UnrolledUnsafeCopierBuilder()
        .length(-1)
        .build(null);
  }

  @Test(expected = IllegalArgumentException.class) public void testNoSetup() throws Exception {
    new UnrolledUnsafeCopierBuilder().build(null);
  }
}
