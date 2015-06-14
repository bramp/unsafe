package net.bramp.unsafe;

import com.google.common.base.Stopwatch;
import net.bramp.unsafe.examples.LongPoint;
import net.bramp.unsafe.examples.Person;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Main {


    public static void printMemoryLayout2() {
        Object o1 = new Object();
        Object o2 = new Object();

        Byte   b1 = new Byte((byte)0x12);
        Byte   b2 = new Byte((byte)0x34);
        Byte   b3 = new Byte((byte)0x56);

        Byte[] bArray0 = new Byte[]{};
        Byte[] bArray1 = new Byte[]{b1};
        Byte[] bArray2 = new Byte[]{b1, b2};
        Byte[] bArray3 = new Byte[]{b1, b2, b3};

        Long   l = new Long(0x0123456789ABCDEFL);
        Person p = new Person("Bob", 406425600000L, 'M');

        System.out.printf("Object len:%d header:%d\n", UnsafeHelper.sizeOf(o1), UnsafeHelper.headerSize(o1));
        UnsafeHelper.hexDump(System.out, o1);
        UnsafeHelper.hexDump(System.out, o2);

        System.out.printf("Byte len:%d header:%d\n", UnsafeHelper.sizeOf(b1), UnsafeHelper.headerSize(b1));
        UnsafeHelper.hexDump(System.out, b1);
        UnsafeHelper.hexDump(System.out, b2);
        UnsafeHelper.hexDump(System.out, b3);

        System.out.printf("Byte[0] len:%d header:%d\n", UnsafeHelper.sizeOf(bArray0), UnsafeHelper.headerSize(bArray0));
        UnsafeHelper.hexDump(System.out, bArray0);
        System.out.printf("Byte[1] len:%d header:%d\n", UnsafeHelper.sizeOf(bArray1), UnsafeHelper.headerSize(bArray1));
        UnsafeHelper.hexDump(System.out, bArray1);
        System.out.printf("Byte[2] len:%d header:%d\n", UnsafeHelper.sizeOf(bArray2), UnsafeHelper.headerSize(bArray2));
        UnsafeHelper.hexDump(System.out, bArray2);
        System.out.printf("Byte[3] len:%d header:%d\n", UnsafeHelper.sizeOf(bArray3), UnsafeHelper.headerSize(bArray3));
        UnsafeHelper.hexDump(System.out, bArray3);

        System.out.printf("Long len:%d header:%d\n", UnsafeHelper.sizeOf(l), UnsafeHelper.headerSize(l));
        UnsafeHelper.hexDump(System.out, l);

        System.out.printf("Person len:%d header:%d\n", UnsafeHelper.sizeOf(p), UnsafeHelper.headerSize(p));
        UnsafeHelper.hexDump(System.out, p);
    }

    /**
     _________________________________________________________________
     | Test                 | Trial| Time (s)| Extra                  |
     |================================================================|
     | Create               | 0    | 19.281  |                        |
     | Safelist Sum         | 0    | 0.208   | -1254641639, 1253696635|
     | Unsafelist Sum (copy)| 0    | 0.448   | -1254641639, 1253696635|
     | Safelist Sum         | 1    | 0.235   | -1254641639, 1253696635|
     | Unsafelist Sum (copy)| 1    | 0.282   | -1254641639, 1253696635|
     | Safelist Sum         | 2    | 0.259   | -1254641639, 1253696635|
     | Unsafelist Sum (copy)| 2    | 0.22    | -1254641639, 1253696635|
     | Safelist Sum         | 3    | 0.23    | -1254641639, 1253696635|
     | Unsafelist Sum (copy)| 3    | 0.271   | -1254641639, 1253696635|
     | Safelist Sum         | 4    | 0.251   | -1254641639, 1253696635|
     | Unsafelist Sum (copy)| 4    | 0.263   | -1254641639, 1253696635|
     */
    public static class UnsafeTester {

        final int SIZE = 40000000; // 40000000 uses ~3.1GiB of RAM. TODO work out what I expect
        Stopwatch stopwatch = Stopwatch.createUnstarted();

        public void go() {

            final ArrayList<LongPoint> safeList = new ArrayList<LongPoint>(SIZE);
            final UnsafeArrayList<LongPoint> unsafeList = new UnsafeArrayList<LongPoint>(LongPoint.class, SIZE);
            final Random r = new Random(0);

            LongPoint p;

            stopwatch.start();

            for (int i = 0; i < SIZE; i++) {
                // New Point for each entry
                p = new LongPoint(r.nextLong(), r.nextLong());
                safeList.add(p);
            }

            recordResult(0, "ArrayList Create", "");

            r.setSeed(0);

            stopwatch.reset().start();
            p = new LongPoint(0, 0);
            for (int i = 0; i < SIZE; i++) {
                // Reuse single point (since it gets copied into array)
                p.x = r.nextLong();
                p.y = r.nextLong();
                unsafeList.add(p);
            }

            recordResult(0, "UnsafeList Create", "");

            for (int trial = 0; trial < 5; trial++) {
                int sumX, sumY;

                sumX = 0;
                sumY = 0;
                stopwatch.reset().start();
                for (int i = 0; i < SIZE; i++) {
                    p = safeList.get(i);
                    sumX += p.x;
                    sumY += p.y;
                }

                recordResult(trial, "ArrayList Sum", sumX + ", " + sumY);

                /*
                sumX = 0; sumY = 0;
                stopwatch.reset().start();
                for (int i = 0; i < SIZE; i++) {
                    Point p = unsafeList.get(i);
                    sumX += p.x; sumY += p.y;
                }

                recordResult("Unsafelist Sum", sumX + ", " + sumY);
                */

                sumX = 0;
                sumY = 0;
                stopwatch.reset().start();
                p = new LongPoint(0, 0); // Used as a buffer to copy values into
                for (int i = 0; i < SIZE; i++) {
                    unsafeList.get(p, i);
                    sumX += p.x;
                    sumY += p.y;
                }

                recordResult(trial, "UnsafeList Sum (inplace)", sumX + ", " + sumY);
            }

            printResults();
        }

        final ArrayList<Object[]> results = new ArrayList<Object[]>();
        private void recordResult(int trial, String test, String extra) {
            stopwatch.stop();
            results.add( new Object[]{test, trial, stopwatch.elapsed(TimeUnit.MILLISECONDS) / 1000d, extra} );
            System.gc();
        }


        private void printResults() {
            /*
            Object[][] data = new Object[results.size()][];
            results.toArray(data);

            TextTable tt = new TextTable(
                new String[]{"Test", "Trial", "Time (s)", "Extra"},
                data
            );
            tt.setSort(1);
            tt.printTable();
            */
        }
    }

    public static void main(String[] args) {

        //printMemoryLayout();

        /*
        net.bramp.unsafe.UnsafeArrayList<Long> list = new net.bramp.unsafe.UnsafeArrayList<Long>(Long.class, 3);
        list.set(0, new Long(1));
        list.set(1, new Long(2));
        list.set(2, new Long(3));
        */

        /*
        net.bramp.unsafe.UnsafeArrayList<Person> list = new net.bramp.unsafe.UnsafeArrayList<Person>(Person.class, 3);
        list.set(0, new Person("Andrew", 406425600000L, 'M'));
        list.set(1, new Person("Bob", 406425600000L, 'M'));
        list.set(2, new Person("Charlie", 406425600000L, 'M'));
        */

        //Helper.hexDump(System.out, new Object[] { list } );

        /*
        System.out.printf("Address: 0x%08X\n", Helper.toAddress(list));
        System.out.printf("Base: 0x%08X\n", list.base);
        //System.out.printf("Base: 0x%08X\n", Helper.getUnsafe().getAddress( Helper.toAddress(list) ));

        //Helper.hexDumpAddress(System.out, Helper.toAddress(list), Helper.sizeOf(list));
        Helper.hexDump(System.out, list);
        Helper.hexDumpAddress(System.out, list.base, list.capacity * list.elementSize);

        //Helper.hexDumpAddress(System.out, Helper.toAddress(list), 16);
        */

        /*
        System.out.printf("1 %s\n", safeList.get(0), unsafeList.get(0));
        System.out.printf("2 %s\n", safeList.get(1), unsafeList.get(1));
        System.out.printf("3 %s\n", safeList.get(2), unsafeList.get(2));
        */

        /*
        Collections.sort(list, new Comparator<Point>() {
            @Override
            public int compare(Point o1, Point o2) {
                return o2.x - o1.x;
            }
        });
        */
        /*
        System.out.printf("1 %s\n", safeList.get(0), unsafeList.get(0));
        System.out.printf("2 %s\n", safeList.get(1), unsafeList.get(1));
        System.out.printf("3 %s\n", safeList.get(2), unsafeList.get(2));
        */

        //printMemoryLayout2();
        new UnsafeTester().go();

    }

}
