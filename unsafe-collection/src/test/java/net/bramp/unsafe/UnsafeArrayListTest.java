package net.bramp.unsafe;

import net.bramp.unsafe.examples.LongPoint;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UnsafeArrayListTest {

    final static int TEST_SIZE = 25; // Recommended larger than UnsafeArrayList.DEFAULT_CAPACITY

    UnsafeArrayList<LongPoint> list;

    @Before
    public void setup() {
        list = new UnsafeArrayList<LongPoint>(LongPoint.class);

        int i = 0;
        for (int j = 0; j < TEST_SIZE; j++) {
            list.add(new LongPoint(i++, i++));
        }

        assertEquals(TEST_SIZE, list.size());
    }

    @Test
    public void testGet() throws Exception {
        int i = 0;
        for (int j = 0; j < TEST_SIZE; j++) {
            assertEquals(new LongPoint(i++, i++), list.get(j));
        }
    }

    @Test
    public void testGetInPlace() throws Exception {
        LongPoint tmp = new LongPoint(-1, -1);
        int i = 0;
        for (int j = 0; j < TEST_SIZE; j++) {
            list.get(tmp, j);
            assertEquals(new LongPoint(i++, i++), tmp);
        }
    }
}