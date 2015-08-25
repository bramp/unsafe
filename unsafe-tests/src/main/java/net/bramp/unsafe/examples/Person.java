package net.bramp.unsafe.examples;

import java.util.Date;

public class Person {

    final String name;
    final long birthday; // Timestamp (as returned by currentTimeMillis)
    final char gender;

    public Person(String name, long birthday, char gender) {
        this.name = name;
        this.birthday = birthday;
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public long getBirthday() {
        return birthday;
    }

    public char getGender() {
        return gender;
    }

    /**
     * Note: Not accurate, but only used as an example.
     * @return the age of the person in years.
     */
    public int getAge() {
        return (int) ((System.currentTimeMillis() - birthday) / 1000 / 60 / 60 / 24 / 365);
    }

    public String toString() {
        return "Person(" + getName() + ", " + new Date(getBirthday()) + ", " + getGender() + ")";
    }
}
