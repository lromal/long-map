package de.comparus.opensource.longmap;

import java.util.LinkedList;

public class LongMapImpl<V> implements LongMap<V> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final double DEFAULT_DENSITY = 0.75; // 16

    private LinkedList<Entry>[] buckets;
    private int size = 0;
    private int capacity;
    private double density;

    public LongMapImpl(int capacity) {

        this.capacity = capacity;

        density = DEFAULT_DENSITY;

        buckets = new LinkedList[this.capacity];
    }

    public V put(long key, V value) {

        Entry entry = new Entry<>(key, value);

        putEntry(entry);

        return null;
    }

    private void putEntry(Entry entry) {

        LinkedList<Entry> bucket = getBucket(entry);

        int index = bucket.indexOf(entry);

        if(index >= 0) {
            bucket.get(index).setValue(entry.getValue());
            return;
        }

        bucket.add(entry);

        size++;

    }

    private LinkedList<Entry> getBucket(Entry entry) {

        int hash = entry.hashCode();

        LinkedList<Entry> list = buckets[hash];

        if(list == null) {
            list = new LinkedList<>();
            buckets[hash] = list;
        }

        return list;
    }

    public V get(long key) {

        int hash = hashCode(key, capacity);

        LinkedList<Entry> list = buckets[hash];

        if(list == null) {
            return null;
        }

        int index = list.indexOf(new Entry(key, null));

        if(index < 0) {
            return null;
        }

        Entry<V> entry = list.get(index);

        return entry.getValue();
    }

    public V remove(long key) {
        return null;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean containsKey(long key) {
        return false;
    }

    public boolean containsValue(V value) {
        return false;
    }

    public long[] keys() {
        return null;
    }

    public V[] values() {
        return null;
    }

    public long size() {
        return size;
    }

    public void clear() {

        size = 0;

    }

    public static int hashCode(long key, int capacity) {
        return Long.hashCode(key) % capacity;
    }

    class Entry<V> {

        private long key;
        private V value;

        public Entry(long key, V value) {
            this.key = key;
            this.value = value;
        }

        public boolean equals(Object object) {

            if (object == this) {
                return true;
            }

            if (object instanceof Entry) {
                return equals((Entry) object);
            }

            if (object instanceof Long) {
                return equals((Long) object);
            }

            return false;
        }

        private boolean equals(Entry entry) {

            return entry.key == this.key;

        }

        private boolean equals(Long entry) {

            return entry == this.key;

        }

        public long getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public int hashCode() {
            return LongMapImpl.hashCode(key, capacity);
        }

        public V setValue(V value) {

            V oldValue = this.value;

            this.value = value;

            return  oldValue;
        }
    }
}
