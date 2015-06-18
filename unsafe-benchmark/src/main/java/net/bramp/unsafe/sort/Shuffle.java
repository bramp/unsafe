package net.bramp.unsafe.sort;

import net.bramp.unsafe.InplaceList;

import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.RandomAccess;

/**
 * Inspired by Collections.shuffle(...)
 */
public abstract class Shuffle {

    // Simple Fisher–Yates shuffle
    public static void shuffle(InplaceList<?> list, Random rnd) {
        for (int i=list.size(); i>1; i--) {
            list.swap(i-1, rnd.nextInt(i));
        }
    }

    public static<T> void swap(List<T> list, int i, int j) {
        list.set(i, list.set(j, list.get(i)));
    }

    public static<T> void shuffle(List<T> list, Random rnd) {
        for (int i=list.size(); i>1; i--) {
            swap(list, i-1, rnd.nextInt(i));
        }
    }
}
