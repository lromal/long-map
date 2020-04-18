package de.comparus.opensource.longmap;

import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.assertEquals;

public class LongMapTest {

    @Test
    public void put() {

        LongMap<String> map = new LongMapImpl<>(1);

        map.put(1, "test1");
        map.put(33, "test2");
        map.put(1, "test3");
        map.put(44, "test4");

        String value = map.get(1);

        assertEquals("Test message", 2, 2);
    }

}
