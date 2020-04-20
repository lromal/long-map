package de.comparus.opensource.longmap;

import org.junit.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class LongMapTest {

    private static long[] keys;
    private static List<String> values;
    LongMap<String> testData;



    @BeforeClass
    public static void init() {
        keys = new long[4];
        keys[0] = 1;
        keys[1] = 2;
        keys[2] = 3;
        keys[3] = 4;

        values = new ArrayList<>();
        values.add("test1");
        values.add("test2");
        values.add("test3");
        values.add("test4");
    }

    @Before
    public void createTestData() {

        testData = new LongMapImpl<>(3);

        for(int i = 0; i < keys.length; i++) {
            testData.put(keys[i], values.get(i));
        }
    }


    @Test
    public void testPut() {

        assertEquals("Test message", 4, testData.size());

        testData.put(5L, "test5");

        assertEquals("Test message", 5, testData.size());

        testData.put(5L, "test5");

        assertEquals("Test message", 5, testData.size());

    }

    @Test
    public void testGet() {

        String value = testData.get(4L);

        assertEquals("Test message", "test4", value);

        value = testData.get(5L);

        assertEquals("Test message", null, value);

    }

    @Test
    public void testContainsKey() {

        assertEquals("Test message", true, testData.containsKey(4L));

        assertEquals("Test message", false, testData.containsKey(5L));

    }

    @Test
    public void testIsEmpty() {

        assertEquals("Test message", false, testData.isEmpty());

        LongMap emptyMap = new LongMapImpl(1);

        assertEquals("Test message", true, emptyMap.isEmpty());

    }

    @Test
    public void testRemove() {

        String value = testData.remove(1L);

        assertEquals("Test message", 3, testData.size());

        assertEquals("Test message", "test1", value);

        value = testData.remove(1L);

        assertEquals("Test message", 3, testData.size());

        assertEquals("Test message", null, value);

    }

    @Test
    public void testClear() {

        testData.clear();

        assertEquals("Test message", 0, testData.size());

    }

    @Test
    public void testKeys() {

        long[] testDataKeys = testData.keys();

        Arrays.sort(testDataKeys);

        assertArrayEquals("Test message", keys, testDataKeys);

        LongMap<String> newMap = new LongMapImpl<>(1);

        testDataKeys = newMap.keys();

        assertEquals("Test message", 0, testDataKeys.length);

    }

}
