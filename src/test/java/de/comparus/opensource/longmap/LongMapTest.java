package de.comparus.opensource.longmap;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class LongMapTest {

    private static List<Long> keys;
    private static List<String> values;
    LongMap<String> testData;



    @BeforeClass
    public static void init() {
        keys = new ArrayList<>();
        keys.add(1L);
        keys.add(2L);
        keys.add(3L);
        keys.add(4L);

        values = new ArrayList<>();
        values.add("test1");
        values.add("test2");
        values.add("test3");
        values.add("test4");
    }

    @Before
    public void createTestData() {

        testData = new LongMapImpl<>(1);

        for(int i = 0; i < keys.size(); i++) {
            testData.put(keys.get(i), values.get(i));
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

}
