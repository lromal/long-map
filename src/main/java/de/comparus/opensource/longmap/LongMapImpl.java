package de.comparus.opensource.longmap;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LongMapImpl<V> implements LongMap<V> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final double DEFAULT_DENSITY = 0.75; // 16

    private LinkedList<Entry>[] buckets;
    private int size = 0;
    private int maxSize;
    private int capacity;
    private double density;

    public LongMapImpl(int capacity) {

        this.capacity = capacity;

        density = DEFAULT_DENSITY;

        calculateMaxSize();

        buckets = new LinkedList[this.capacity];
    }

    public LongMapImpl(int capacity, double density) {

        this.capacity = capacity;

        this.density = density;

        calculateMaxSize();

        buckets = new LinkedList[this.capacity];
    }

    public LongMapImpl() {

        capacity = DEFAULT_CAPACITY;

        density = DEFAULT_DENSITY;

        calculateMaxSize();

        buckets = new LinkedList[this.capacity];
    }

    public V put(long key, V value) {

        Entry entry = new Entry<>(key, value);

        resize();

        return putEntry(entry);
    }

    private void resize() {

        if(size < maxSize) {
            return;
        }

        capacity *= 2;

        calculateMaxSize();

        List<Entry> list = convertToStream().collect(Collectors.toList());

        clear();

        buckets = new LinkedList[this.capacity];

        for (Entry entry : list) {
            put(entry.key, (V) entry.value);
        }

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

    @Override
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

        Optional<Entry> searchedEntry = convertToStream().parallel().filter(a -> a.value == value).findAny();

        return searchedEntry.isPresent();
    }

    @Override
    public long[] keys() {

        if(size == 0) {
            return null;
        }

        return convertToStream().mapToLong((a) -> a.key).toArray();
    }

    @Override
    public V[] values() {

        if(size == 0) {
            return null;
        }

        List<Object> objects = convertToStream().map((a) -> a.value).collect(Collectors.toList());

        return objects.toArray((V[]) Array.newInstance(objects.get(0).getClass(), objects.size()));
    }

    @Override
    public long size() {
        return size;
    }

    @Override
    public void clear() {

        for(int i = 0; i < buckets.length; i++) {

            if(buckets[i] == null) {
                continue;
            }

            buckets[i].clear();
            buckets[i] = null;
        }

        size = 0;

    }

    private void calculateMaxSize() {

        maxSize = Math.toIntExact(Math.round(capacity * density));

    }

    private Stream<Entry> convertToStream() {
        return Arrays.stream(buckets).filter(Objects::nonNull).flatMap(Collection::stream);
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
