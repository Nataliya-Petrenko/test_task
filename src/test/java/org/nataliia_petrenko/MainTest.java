package org.nataliia_petrenko;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class MainTest {

    @Test
    public void maxValue_test() {
        final int first = 5;
        final int second = 7;
        final int expected = 7;
        Assert.assertEquals(Main.maxValue(first, second), expected);
    }

    @Test
    public void minValue() {
        final int first = 5;
        final int second = 7;
        final int expected = 5;
        Assert.assertEquals(Main.minValue(first, second), expected);
    }

    @Test
    public void median_oneElement() {
        final int[] array = {0};
        final int expected = 0;
        Assert.assertEquals(Main.median(array), expected);
    }

    @Test
    public void median_even() {
        final int[] array = {0, 5, 3, 4};
        final int expected = 4;
        Assert.assertEquals(Main.median(array), expected);
    }

    @Test
    public void median_odd() {
        final int[] array = {0, 5, 3};
        final int expected = 5;
        Assert.assertEquals(Main.median(array), expected);
    }
}