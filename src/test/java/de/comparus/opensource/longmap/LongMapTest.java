package de.comparus.opensource.longmap;

import org.junit.*;

import java.util.*;

import static org.junit.Assert.*;

public class LongMapTest {

    private static long[] keys;
    private static String[] values;
    private LongMap<String> testData;
    private LongMap<String> testEmptyData;
    private final static int TEST_DATA_LENGTH = 4;



    @BeforeClass
    public static void init() {
        keys = new long[TEST_DATA_LENGTH];
        keys[0] = 1;
        keys[1] = 2;
        keys[2] = 3;
        keys[3] = 4;

        values = new String[TEST_DATA_LENGTH];
        values[0] = "test1";
        values[1] = "test2";
        values[2] = "test3";
        values[3] = "test4";
    }

    @Before
    public void createTestData() {

        testData = new LongMapImpl<>(3);

        for(int i = 0; i < keys.length; i++) {
            testData.put(keys[i], values[i]);
        }

        testEmptyData = new LongMapImpl<>();
    }


    @Test
    public void testPut() {

        assertEquals("Test message", TEST_DATA_LENGTH, testData.size());


        String newValue = "test5";
        long newKey = 5;


        String oldValue = testData.put(newKey, newValue);

        assertEquals("Test message", TEST_DATA_LENGTH + 1, testData.size());

        assertNull("Test message", oldValue);


        oldValue = testData.put(newKey, "test55");

        assertEquals("Test message", TEST_DATA_LENGTH + 1, testData.size());

        assertEquals("Test message", newValue, oldValue);

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

        assertEquals("Test message", true, testEmptyData.isEmpty());

    }

    @Test
    public void testRemove() {

        String value = testData.remove(1L);

        assertEquals("Test message", TEST_DATA_LENGTH - 1, testData.size());

        assertEquals("Test message", "test1", value);

        value = testData.remove(1L);

        assertEquals("Test message", TEST_DATA_LENGTH - 1, testData.size());

        assertEquals("Test message", null, value);

    }

    @Test
    public void testClear() {

        testData.clear();

        assertEquals("Test message", 0, testData.size());

    }

    @Test
    public void testContainsValue() {

        assertEquals("Test message", true, testData.containsValue("test1"));

        assertEquals("Test message", false, testData.containsValue("test5"));

        assertEquals("Test message", false, testEmptyData.containsValue("test1"));

    }

    @Test
    public void testKeys() {

        long[] testDataKeys = testData.keys();

        Arrays.sort(testDataKeys);

        assertArrayEquals("Test message", keys, testDataKeys);

        testDataKeys = testEmptyData.keys();

        assertNull("Test message", testDataKeys);

    }

    @Test
    public void testValues() {

        String[] testDataValues = testData.values();

        Arrays.sort(testDataValues);

        assertArrayEquals("Test message", values, testDataValues);

        testDataValues = testEmptyData.values();

        assertNull("Test message", testDataValues);

    }

}
