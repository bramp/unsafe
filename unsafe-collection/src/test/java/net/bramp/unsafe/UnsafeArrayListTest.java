/**
 * Tests the UnsafeArrayList using guava-testlib.
 * Used https://www.klittlepage.com/2014/01/08/testing-collections-with-guava-testlib-and-junit-4/
 * as a reference.
 */
package net.bramp.unsafe;

import com.google.common.collect.testing.*;
import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.ListFeature;
import junit.framework.TestSuite;
import net.bramp.unsafe.examples.LongPoint;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Your test class must be annotated with {@link RunWith} to specify that it's a
 * test suite and not a single test.
 */
@RunWith(Suite.class)
/**
 * We need to use static inner classes as JUnit only allows for empty "holder"
 * suite classes.
 */
@Suite.SuiteClasses({
    UnsafeArrayListTest.GuavaTests.class,
    UnsafeArrayListTest.AdditionalTests.class,
})
public class UnsafeArrayListTest {

  /**
   * Add your additional test cases here.
   */
  public static class AdditionalTests {

    static final int TEST_SIZE = 25; // Recommended larger than UnsafeArrayList.DEFAULT_CAPACITY

    UnsafeArrayList<LongPoint> list;

    @Before
    public void setup() {
      list = new UnsafeArrayList<>(LongPoint.class);

      for (int i = 0; i < TEST_SIZE; i++) {
        list.add(new LongPoint(i * 2, i * 2 + 1));
      }

      assertEquals(TEST_SIZE, list.size());
    }

    @Test public void testGet() throws Exception {
      for (int i = 0; i < TEST_SIZE; i++) {
        assertEquals(new LongPoint(i * 2, i * 2 + 1), list.get(i));
      }
    }

    @Test public void testGetInPlace() throws Exception {
      LongPoint tmp = new LongPoint(-1, -1);
      for (int i = 0; i < TEST_SIZE; i++) {
        list.get(tmp, i);
        assertEquals(new LongPoint(i * 2, i * 2 + 1), tmp);
      }
    }

  }

  /**
   * This class will generate the guava test suite. It needs a public static
   * magic method called {@link GuavaTests#suite()} to do so.
   */
  public static class GuavaTests {

    public static TestSuite suite() {

      return ListTestSuiteBuilder
          .using(new TestStringListGenerator() {

            @Override
            protected List<String> create(String[] elements) {
              return new UnsafeArrayList<>(String.class, Arrays.asList(elements));
            }
          })

          .named("UnsafeArrayList")

          // Guava has a host of "features" in the
          // com.google.common.collect.testing.features package. Use
          // them to specify how the collection should behave, and
          // what operations are supported.
          .withFeatures(ListFeature.GENERAL_PURPOSE, CollectionSize.ANY).createTestSuite();
    }
  }
}
