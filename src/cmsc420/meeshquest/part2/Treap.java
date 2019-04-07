package cmsc420.meeshquest.part2;

import java.util.*;
import java.util.Map;

public class Treap<K,V> extends AbstractMap<K,V> implements SortedMap<K,V> {

    private Comparator comparator;

    private Entry<K, V> root;

    private int modcount;

    private int size;
    static private Random rand = new Random();

    public Treap() {
        comparator = null;
        modcount = 0;
    }

    public Treap(Comparator a) {
        this.comparator = a;
        modcount = 0;
    }

    @Override
    public Comparator comparator() {
        return comparator;
    }

    @Override
    //TODO
    public SortedMap subMap(Object fromKey, Object toKey) {
        return null;
    }

    @Override
    //NOT IMPLEMENT
    public SortedMap headMap(Object toKey) {
        throw new UnsupportedOperationException();
    }

    @Override
    //NOT IMPLEMENT
    public SortedMap tailMap(Object fromKey) {
        throw new UnsupportedOperationException();
    }

    @Override
    //TODO
    public K firstKey() {
        return null;
    }

    @Override
    //TODO
    public K lastKey() {
        return null;
    }

    @Override
    public V put(K key, V value) {
        if(key == null){
            //if the key is null we throw
            //TODO what about the value being null
            throw new NullPointerException();
        }
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
        //do comparator vs comparable
        int cmp;
        Entry<K,V> parent;

        Comparator<? super K> cpr = comparator;
        if(cpr != null){//we have a comparator and not a comparable
            do{
                parent = t;
                cmp = cpr.compare(key, t.key);
                if(cmp < 0){
                    t = t.left;
                }else if (cmp > 0){
                    t = t.right;
                }else{
                    //todo check what to do for duplicate keys
                    return t.setValue(value);
                }
            }while (t != null);
        }else{
            // if there isnt a comparator defined, then we use keys comparator
            Comparable k = (Comparable) key;
            do{
                parent = t;
                cmp = k.compareTo(t.key);
                if(cmp < 0){
                    t = t.left;
                }else if(cmp > 0){
                    t = t.right;
                }else{
                    //todo again check if this is correct implementation
                    return t.setValue(value);
                }
            }while (t != null);
        }
        Entry e = new Entry<>(key, value, parent);
        if (cmp < 0){
            parent.left = e;
        }else{
            parent.right = e;
        }
        rotate(e);
        return value; //todo check return type needed/ wanted
    }

    //we make a rotate class to call and then go from there
    //left is true, right is false
    //TODO check correct rotate
    private void rotate(Entry n){
        if(n.priority < n.left.priority){
            leftRotate(n);
            rotate(n.parent);
        }else if(n.priority < n.right.priority){
            rightRotate(n);
            rotate(n.parent);
        }
    }

    /* T1, T2 and T3 are subtrees of the tree rooted with y
  (on left side) or x (on right side)
                y                               x
               / \     Right Rotation          /  \
              x   T3   – – – – – – – >        T1   y
             / \       < - - - - - - -            / \
            T1  T2     Left Rotation            T2  T3 */


    private Entry rightRotate(Entry y){
        Entry x = y.left;
        Entry T2 = x.right;

        x.right = y;
        y.parent = x;
        y.left = T2;
        T2.parent = y;

        return x;
    }
    private Entry leftRotate(Entry x){
        Entry y = x.right;
        Entry T2 = y.left;

        y.left = x;
        x.parent = y;
        x.right = T2;
        T2.parent = x;

        return y;

    }

    @Override
    //TODO
    public void putAll(Map m) {

    }

    //TODO
    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return null;
    }

    @Override
    //TODO
    public boolean remove(Object key, Object value) {
        return false;
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
            //todo needs to be random not necessarily unique
            priority = rand.nextInt();
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
