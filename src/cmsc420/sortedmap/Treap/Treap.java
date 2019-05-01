package cmsc420.sortedmap.Treap;

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


    public V get(Object key){
        Entry<K,V> p = getEntry(key);
        return (p ==null ? null: p.value);
    }

    final Entry<K,V> getEntry(Object key){
        if(comparator != null){
            return getEntryUsingComparator(key);
        }if(key == null){
            return null;
        }
        @SuppressWarnings("unchecked")
        Comparable k = (Comparable) key;
        Entry<K,V > curr = root;
        while(curr != null){
           int comp = k.compareTo(curr.key);
           if(comp < 0){
               curr =curr.left;
           }if(comp > 0){
               curr = curr.right;
            }else
                return curr;
        }
        return null;
    }

    final Entry<K,V> getEntryUsingComparator(Object key){
        K k = (K) key;
        Comparator comp = comparator;
        if(comp != null){
            Entry<K,V> p = root;
            while(p != null){
                int cmp = comp.compare(k, p.key);
                if(cmp<0)
                    p = p.left;
                if(cmp>0)
                    p = p.right;
                else
                    return p;
            }
        }
        return null;
    }

    //TODO
    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        EntrySet es = entryset;
        //todo line 850ish
    }

    @Override
    //TODO
    public boolean remove(Object key, Object value) {
        return false;
    }

    class EntrySet extends AbstractSet<Map.Entry<K,V>>{
        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            //todo
            return new EntryIterator(getFirstEntry());
        }

        public boolean contains(Object o){
            if (!(o instanceof Map.Entry)){
                return false
            }
            Map.Entry<?,?> entry = (Map.Entry<?,?>) o;
            Object value = entry.getValue();
            Entry<K,V> p = getEntry(entry.getKey());
            return p != null && valEquals(p.getValue(), value);
        }

        public boolean remove(Object o){
            //todo line 1070
            return false;
        }


        @Override
        public int size() {
            return Treap.this.size();
        }

        public void clear(){ Treap.this.clear();}

        public Spliterator<Map.Entry<K,V>> spliterator(){
            //I dont think we need to implement this
            return null;
        }
    }
    abstract class PrivateEntryIterator<T> implements Iterator<T> {
        Entry<K,V> next;
        Entry<K,V> lastReturned;
        int expectedModCount;

        PrivateEntryIterator(Entry<K,V> first){
            expectedModCount = modcount;
            lastReturned = null;
            next = first;
        }

        public final boolean hasNext(){return next != null;}

        final Entry<K,V> nextEntry(){
            Entry<K,V> e = next;
            if(e == null){
                throw new NoSuchElementException();
            }if(modcount != expectedModCount){
                throw new ConcurrentModificationException();
            }
            next = successor(e);
            lastReturned = e;
            return e;
        }

        final Entry<K,V> prevEntry() {
            Entry<K, V> e = next;
            if (e == null) {
                throw new NoSuchElementException();
            }
            if (modcount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
            next = predecessor(e);
            lastReturned = e;
            return e;
        }

        public void Remove(){
            //todo
        }

    }

    final class EntryIterator extends PrivateEntryIterator<Map.Entry<K,V>>{
        EntryIterator(Entry<K, V> first) {
            super(first);
        }

        @Override
        public Map.Entry<K, V> next() {

        }
        //todo
    }


    static <K,V> Treap.Entry<K,V> successor(Entry<K,V> t){
        if(t == null)
            return null;
        else if (t.right != null){
            Entry<K,V> p = t.right;
            while (p.left != null){
                p = p.left;
            }
            return p;
        }else{
            Entry<K,V> p = t.parent;
            Entry<K,V> ch = t;
            while (p != null && ch == p.right){
                ch = p;
                p = p.parent;
            }
            return p;
        }
    }

    static <K, V> Entry<K,V> predecessor(Entry<K,V> t){
        if(t == null){
            return null;
        }
        else if(t.left != null){
            Entry<K,V> p = t.left;
            while (p.right != null){
                p = p.right;
            }
            return p;
        }else {
            Entry<K,V> p = t.parent;
            Entry<K,V> ch = t;
            while (p != null && ch == p.left){
                ch = p;
                p = p.parent;
            }
            return p;
        }
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
