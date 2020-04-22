package de.comparus.opensource.longmap;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LongMapImpl<V> implements LongMap<V> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_DENSITY = 0.75f;

    private LinkedList<Entry<V>>[] buckets;
    private int size = 0;
    private int maxSize;
    private int capacity;
    private double density;


    /**
     * @param capacity the initial size of a hash table array. It should be >= 0.
     * @param density the load factor of a hash table. It should be >= 0.
     */
    public LongMapImpl(int capacity, float density) {

        if(capacity <= 0) {
            throw new IllegalArgumentException("Illegal capacity: " + capacity);
        }

        if(density <= 0) {
            throw new IllegalArgumentException("Illegal density: " + density);
        }

        this.capacity = capacity;

        this.density = density;

        calculateMaxSize();

        buckets = new LinkedList[this.capacity];
    }

    /**
     * @param capacity the initial size of a hash table array. It should be >= 0.
     */
    public LongMapImpl(int capacity) {

        this(capacity, DEFAULT_DENSITY);
    }

    public LongMapImpl() {

        this(DEFAULT_CAPACITY, DEFAULT_DENSITY);
    }

    public V put(long key, V value) {

        Entry<V> entry = new Entry<>(key, value);

        resize();

        return putEntry(entry);
    }


    @Override
    public V get(long key) {

        Entry<V> searchedEntry = new Entry<>(key, null);

        LinkedList<Entry<V>> list = getBucket(searchedEntry, false);

        int index = list.indexOf(searchedEntry);

        if(index < 0) {
            return null;
        }

        return list.get(index).getValue();
    }

    @Override
    public V remove(long key) {

        Entry<V> searchedEntry = new Entry<>(key, null);

        LinkedList<Entry<V>> list = getBucket(searchedEntry, false);

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

        Entry<V> searchedEntry = new Entry<>(key, null);

        LinkedList<Entry<V>> list = getBucket(searchedEntry, false);

        int index = list.indexOf(searchedEntry);

        return index >= 0;
    }

    public boolean containsValue(V value) {

        Optional<Entry<V>> searchedEntry = convertToStream().parallel().filter(a -> a.value == value).findAny();

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


    private void resize() {

        if(size < maxSize) {
            return;
        }

        capacity *= 2;

        calculateMaxSize();

        List<Entry<V>> list = convertToStream().collect(Collectors.toList());

        clear();

        buckets = new LinkedList[this.capacity];

        for (Entry<V> entry : list) {
            put(entry.key, entry.getValue());
        }

    }

    private void calculateMaxSize() {

        maxSize = Math.toIntExact(Math.round(capacity * density));

    }

    private Stream<Entry<V>> convertToStream() {
        return Arrays.stream(buckets).filter(Objects::nonNull).flatMap(Collection::stream);
    }

    private V putEntry(Entry<V> entry) {

        LinkedList<Entry<V>> bucket = getBucket(entry, true);

        int index = bucket.indexOf(entry);

        if(index >= 0) {

            return bucket.get(index).setValue(entry.getValue());

        }

        bucket.add(entry);

        size++;

        return null;

    }

    private LinkedList<Entry<V>> getBucket(Entry entry, boolean createBucket) {

        int hash = entry.hashCode();

        LinkedList<Entry<V>> list = buckets[hash];

        if(list == null) {
            list = new LinkedList<>();

            if(createBucket) {
                buckets[hash] = list;
            }
        }

        return list;
    }


    class Entry<T> {

        private long key;
        private T value;

        Entry(long key, T value) {
            this.key = key;
            this.value = value;
        }

        public boolean equals(Object object) {

            if (object == this) {
                return true;
            }

            return object instanceof Entry && equalsEntry(object);

        }

        private boolean equalsEntry(Object object) {

            Entry entry = (Entry) object;

            return entry.key == this.key;

        }

        T getValue() {
            return value;
        }

        public int hashCode() {
            return Long.hashCode(key) % capacity;
        }

        T setValue(T value) {

            T oldValue = this.value;

            this.value = value;

            return  oldValue;
        }
    }
}
