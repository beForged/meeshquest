package cmsc420.meeshquest.part2;

import java.util.*;
import java.util.Map;

public class Treap<K,V> extends AbstractMap<K,V> implements SortedMap<K,V> {

    private Comparator comparator;

    private Entry<K, V> root;

    private int size;
    static private Random rand = new Random();

    public Treap() {
        comparator = null;
    }

    public Treap(Comparator a) {
        this.comparator = a;
    }

    @Override
    public Comparator comparator() {
        return comparator;
    }

    @Override
    public SortedMap subMap(Object fromKey, Object toKey) {
        return null;
    }

    @Override
    //NOT IMPLEMENT
    public SortedMap headMap(Object toKey) {
        return null;
    }

    @Override
    //NOT IMPLEMENT
    public SortedMap tailMap(Object fromKey) {
        return null;
    }

    @Override
    public Object firstKey() {
        return null;
    }

    @Override
    public Object lastKey() {
        return null;
    }

    @Override
    public V put(K key, V value) {
        Entry<K,V> t = root;
        //first do regular BST insert and then rotate to maintain heap
        //property
        if(t == null){
            //if root is null then we make new entry the root
            root = new Entry<>(key, value, null);
            //todo change size, modcount?
            return value;
        }
        //else root is not null, we do regular BST insert

        return null;
    }

    @Override
    public void putAll(Map m) {

    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return null;
    }

    //we make an inner class to hold each node in the map, and extend map.entry like in treemap
    static final class Entry<K,V> implements Map.Entry<K,V>{

        K key;
        V value;
        Entry<K,V> left;
        Entry<K,V> right;
        Entry<K,V> parent;
        int priority;

        Entry(K key, V value, Entry<K,V> parent){
            this.key = key;
            this.value = value;
            this.parent = parent;
            //todo needs to be unique and random
            priority = rand.nextInt(10000);
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            this.value = value;
            return value;
        }

        public String toString() {
            return key + " = " + value;
        }
    }

}
