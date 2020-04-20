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

        return putEntry(entry);
    }



    @Override
    public V get(long key) {

        Entry<V> searchedEntry = new Entry(key, null);

        LinkedList<Entry> list = getBucket(searchedEntry, false);

        int index = list.indexOf(searchedEntry);

        if(index < 0) {
            return null;
        }

        searchedEntry = list.get(index);

        return searchedEntry.getValue();
    }

    public V remove(long key) {

        Entry<V> searchedEntry = new Entry(key, null);

        LinkedList<Entry> list = getBucket(searchedEntry, false);

        int index = list.indexOf(searchedEntry);

        if(index < 0) {
            return null;
        }

        searchedEntry = list.get(index);

        list.remove(index);

        size--;

        return searchedEntry.getValue();
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(long key) {

        Entry<V> searchedEntry = new Entry(key, null);

        LinkedList<Entry> list = getBucket(searchedEntry, false);

        int index = list.indexOf(searchedEntry);

        return index >= 0;
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

    @Override
    public long size() {
        return size;
    }

    public void clear() {

        size = 0;

    }

    private V putEntry(Entry entry) {

        LinkedList<Entry> bucket = getBucket(entry, true);

        int index = bucket.indexOf(entry);

        if(index >= 0) {

            return (V) bucket.get(index).setValue(entry.getValue());

        }

        bucket.add(entry);

        size++;

        return null;

    }

    private LinkedList<Entry> getBucket(Entry entry, boolean createBucket) {

        int hash = entry.hashCode();

        LinkedList<Entry> list = buckets[hash];

        if(list == null) {
            list = new LinkedList<>();

            if(createBucket) {
                buckets[hash] = list;
            }
        }

        return list;
    }

    private static int hashCode(long key, int capacity) {
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

            return false;
        }

        private boolean equals(Entry entry) {

            return entry.key == this.key;

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
