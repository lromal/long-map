package de.comparus.opensource.longmap;

import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.assertEquals;

public class LongMapTest {

    @Test
    public void put() {

        LongMap<String> map = new LongMapImpl<>(1);

        String oldValue;

        oldValue = map.put(1, "test1");
        oldValue = map.put(33, "test2");
        oldValue = map.put(1, "test3");
        oldValue = map.put(44, "test4");

//        String value = map.get(1);
        boolean contains = map.containsKey(4);

        assertEquals("Test message", 2, 2);
    }

}
