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

        testData = new LongMapImpl<>();

        for(int i = 0; i < keys.length; i++) {
            testData.put(keys[i], values[i]);
        }

        testEmptyData = new LongMapImpl<>();
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructor_IllegalCapacity_ExceptionThrown() {

        testData = new LongMapImpl<>(-10);

    }

    @Test(expected = IllegalArgumentException.class)
    public void constructor_IllegalDensity_ExceptionThrown() {

        testData = new LongMapImpl<>(16, -10);

    }


    @Test
    public void size_InitialTestData_TestDataLength() {

        assertEquals("Test data has the wrong size", TEST_DATA_LENGTH, testData.size());

    }

    @Test
    public void put_AddedNewEntryWithExistingKey_SizeIsSameAndOldValueIsNotNull() {

        String oldValue = testData.put(4, "test44");

        assertEquals("Test data has the wrong size", TEST_DATA_LENGTH, testData.size());

        assertEquals("Old value is wrong", "test4", oldValue);

    }

    @Test
    public void put_AddedNewEntryWithNotExistingKey_SizeIncreasedAndOldValueIsNull() {

        String oldValue = testData.put(5, "test5");

        assertEquals("Test data has the wrong size", TEST_DATA_LENGTH + 1, testData.size());

        assertNull("Old value is wrong", oldValue);

    }

    @Test
    public void get_EntryExists_test4() {

        String value = testData.get(4L);

        assertEquals("Founded value is wrong", "test4", value);

    }

    @Test
    public void get_EntryNotExists_Null() {

        String value = testData.get(5L);

        assertNull("Some value was found by the wrong key", value);

    }

    @Test
    public void containsKey_EntryExists_True() {

        assertEquals("Key was not found", true, testData.containsKey(4L));

    }

    @Test
    public void containsKey_EntryNotExists_False() {

        assertEquals("Some value was found by the wrong key", false, testData.containsKey(5L));

    }

    @Test
    public void isEmpty_InitialTestData_False() {

        assertEquals("Test data is empty", false, testData.isEmpty());

    }

    @Test
    public void isEmpty_InitialEmptyTestData_True() {

        assertEquals("Empty data contains some values", true, testEmptyData.isEmpty());

    }

    @Test
    public void remove_OneEntryRemoved_TestDataSizeDecreasedAndOldValueIsNotNull() {

        String value = testData.remove(1L);

        assertEquals("Test data has the wrong size", TEST_DATA_LENGTH - 1, testData.size());

        assertEquals("Old value is wrong", "test1", value);

    }

    @Test
    public void remove_KeyNotExists_TestDataSizeNotDecreasedAndOldValueIsNull() {

        String value = testData.remove(5L);

        assertEquals("Test data has the wrong size", TEST_DATA_LENGTH, testData.size());

        assertNull("Old value is not null", value);

    }

    @Test
    public void clear_EmptyMap_SizeIs0() {

        testData.clear();

        assertEquals("Test data is not empty", 0, testData.size());

    }

    @Test
    public void containsValue_ValueExists_True() {

        assertEquals("Existed value was not found", true, testData.containsValue("test1"));

    }

    @Test
    public void containsValue_ValueNotExists_False() {

        assertEquals("Some entry was found by the wrong value", false, testData.containsValue("test5"));

    }

    @Test
    public void containsValue_SearchingInEmptyMap_False() {

        assertEquals("Some entry was found in the empty map", false, testEmptyData.containsValue("test1"));

    }

    @Test
    public void keys_InitialTestData_TestDataKeys() {

        long[] testDataKeys = testData.keys();

        Arrays.sort(testDataKeys);

        assertArrayEquals("Some keys are missing", keys, testDataKeys);

    }

    @Test
    public void keys_InitialTestEmptyData_Null() {

        long[] testDataKeys = testEmptyData.keys();

        assertNull("Some keys were found in the empty map", testDataKeys);

    }

    @Test
    public void values_InitialTestData_TestDataValues() {

        String[] testDataValues = testData.values();

        Arrays.sort(testDataValues);

        assertArrayEquals("Some values are missing", values, testDataValues);

    }

    @Test
    public void values_InitialTestEmptyData_Null() {

        String[] testDataValues = testEmptyData.values();

        assertNull("Some values were found in the empty map", testDataValues);

    }

}
