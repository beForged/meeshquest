package cmsc420.sortedmap;

import cmsc420.meeshquest.part2.GenericException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.awt.geom.Point2D;
import java.util.*;

public class Treap2<K,V> extends AbstractMap<K,V> implements SortedMap<K,V> {
    private Comparator comparator;
    private Entry<K, V> root;
    private int modCount;
    private int size;
    static private Random rand = new Random();

    private transient EntrySet entrySet;

    /**
     * default constructor
     */
    public Treap2() {
        comparator = null;
        modCount = 0;
    }

    /**
     * other constructor
     */
    public Treap2(Comparator c){
        this.comparator = c;
        modCount = 0;
    }

    public void printModCount(){
        //debug method
        System.out.println(modCount);
    }
    public V put(K key, V value) {
        //Most of this is taken straight from Treemap implementation
        if(key == null || value == null){
            throw new NullPointerException();
        }
        Entry<K,V> t = root;
        //first do regular BST insert and then rotate to maintain heap
        if(t == null){
            //if root is null then we make new entry the root
            compare(key, key);
            root = new Entry<>(key, value, null);
            size = 1;
            modCount++;
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
                cmp = cpr.compare(key, t.getKey());
                if(cmp < 0){
                    t = t.left;
                }else if (cmp > 0){
                    t = t.right;
                }else{
                    return t.setValue(value);
                }
            }while (t != null);
        }else{
            // if there isnt a comparator defined, then we use keys comparator
            Comparable k = (Comparable) key;
            do{
                parent = t;
                cmp = k.compareTo(t.getKey());
                if(cmp < 0){
                    t = t.left;
                }else if(cmp > 0){
                    t = t.right;
                }else{
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
        rotate(parent);
        size++;
        modCount++;
        return value;
    }

    //rotates all taken from online source in readme
    private void rotate(Entry<K,V> n) {
        while (n != null ) {
            if (n.left != null && n.left.priority > n.priority) {
                rightRotate(n);
            } else if (n.right != null && n.right.priority > n.priority) {
                leftRotate(n);
            }
            n = n.parent;
        }
    }

    private void rightRotate(Entry<K,V> p){
        if (p != null) {
            Entry<K,V> l = p.left;
            p.left = l.right;
            if (l.right != null) l.right.parent = p;
            l.parent = p.parent;
            if (p.parent == null)
                root = l;
            else if (p.parent.right == p)
                p.parent.right = l;
            else p.parent.left = l;
            l.right = p;
            p.parent = l;
        }
    }

    private void leftRotate(Entry<K,V> p){
        if (p != null) {
            Entry<K,V> r = p.right;
            p.right = r.left;
            if (r.left != null)
                r.left.parent = p;
            r.parent = p.parent;
            if (p.parent == null)
                root = r;
            else if (p.parent.left == p)
                p.parent.left = r;
            else
                p.parent.right = r;
            r.left = p;
            p.parent = r;
        }
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        super.putAll(m);
    }

    public boolean containsKey(Object key){
        if(key == null){
            throw new NullPointerException();
        }
        return super.containsKey(key);
    }

    public void clear(){
        modCount++;
        size = 0;
        root = null;
    }

    public int size(){
        return size;
    }

    @Override
    public Comparator comparator() {
        return comparator;
    }

    public Element printTreap(Document doc) throws GenericException {
        Entry e = root;
        if(e == null){
            throw new GenericException("emptyTree");
        }else{
            return e.printTreap(doc);
        }
    }

    @Override
    public SortedMap headMap(Object toKey) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SortedMap tailMap(Object fromKey) {
        throw new UnsupportedOperationException();
    }

    @Override
    public K firstKey() {
        return getFirstEntry().getKey();
    }

    @Override
    public K lastKey() {
        return getLastEntry().getKey();
    }

    final int compare(Object k1, Object k2){
        return comparator == null ? ((Comparable<? super K>)k1).compareTo((K)k2) : comparator.compare((K) k1, (K) k2);
    }
    /**
     * Submap function and class
     */
    @Override
    public SortedMap subMap(K fromKey, K toKey) {
        return new SubMap(fromKey, toKey);
    }

    public class SubMap extends AbstractMap<K,V> implements SortedMap<K,V>{
        private boolean fromStart = false, toEnd = false;
        private K fromKey, toKey;

        /**
         * constructors
         */
        SubMap(K fromKey, K toKey){
            if(compare(fromKey, toKey) > 0){
                throw new IllegalArgumentException("fromKey > toKey");
            }
            this.fromKey = fromKey;
            this.toKey = toKey;
        }

        SubMap(boolean fromStart, K fromKey, boolean toEnd, K toKey) {
            this.fromStart = fromStart;
            this.toEnd = toEnd;
            this.fromKey = fromKey;
            this.toKey = toKey;
        }

        public boolean isEmpty(){
            return entrySet.isEmpty();
        }

        @Override
        public V put(K key, V value) {
            if(!inRange(key)){
                throw new IllegalArgumentException();
            }
            return Treap2.this.put(key, value);
        }

        @Override
        public void putAll(Map m) {
        //todo
        }

        @Override
        public Comparator comparator() {
            return Treap2.this::compare;
        }

        @Override
        public SortedMap subMap(K fromKey, K toKey) {
            if (!inRange(fromKey))
                throw new IllegalArgumentException ("fromKey out of range");
            if (!inRange(toKey))
                throw new IllegalArgumentException ("toKey out of range");
            return new SubMap(fromKey, toKey);
        }

        @Override
        public SortedMap headMap(Object toKey) { throw new UnsupportedOperationException(); }
        @Override
        public SortedMap tailMap(Object fromKey) { throw new UnsupportedOperationException(); }

        @Override
        public K firstKey() {
            Treap2.Entry <K,V> e = fromStart ? getFirstEntry() : getCeilingEntry(fromKey);
            K first = e.getKey();
            if (!toEnd && compare(first, toKey) >= 0)
                throw(new NoSuchElementException ());
            return first;
        }

        @Override
        public K lastKey() {
            Treap2.Entry <K,V> e = toEnd ? getLastEntry() : getFloorEntry(toKey);
            K last = e.getKey();
            if (!fromStart && compare(last, fromKey) < 0)
                throw(new NoSuchElementException());
            return last;
        }
        private transient Set <Map.Entry <K,V>> entrySet = new EntrySetView();

        public Set <Map.Entry <K,V>> entrySet() {
            return entrySet;
        }

        private class EntrySetView extends AbstractSet<Map.Entry<K,V>>{
            private transient int size = -1;
            private int sizeModCount;

            public int size() {
                if (size == -1 || sizeModCount != Treap2.this.modCount) {
                    size = 0; sizeModCount = Treap2.this.modCount;
                    Iterator i = iterator();
                    while (i.hasNext()) {
                        size++;
                        i.next();
                    }
                }
                return size;
            }

            public boolean isEmpty(){
                return !iterator().hasNext();
            }

            public boolean contains(Object o){
                if(!(o instanceof Map.Entry)){
                    return false;
                }
                Map.Entry<K,V> e = (Map.Entry<K,V>)o;
                K key = e.getKey();
                if(!inRange(key)){
                    return false;
                }
                Treap2.Entry node = getEntry(key);
                return node != null && valEquals(node.getValue(), e.getValue());
            }

            @Override
            public boolean remove(Object o) {
                //todo remove in part 3
                return super.remove(o);
            }

            public Iterator<Map.Entry<K,V>> iterator(){
                return new SubMapEntryIterator(
                    (fromStart ? getFirstEntry() : getCeilingEntry(fromKey)), (toEnd ? null : getCeilingEntry(toKey))
                );
            }
        }

        private boolean inRange(K key) {
            return (fromStart || compare(key, fromKey) >= 0) && (toEnd || compare(key, toKey) < 0);
        }
    }
    /**
     * get and helpers
     */
    public V get(Object key){
        if(key == null){
            throw new NullPointerException();
        }
        Entry<K,V> p = getEntry(key);
        return (p ==null ? null: p.getValue());
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
            int comp = k.compareTo(curr.getKey());
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
                int cmp = comp.compare(k, p.getKey());
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

    static final boolean valEquals(Object o1, Object o2){
        return (o1 == null ? o2 == null : o1.equals(o2));
    }

    /**
     * gets the first, last and closest above and below entry
     * @return
     */
    final Entry<K,V> getFirstEntry(){
        Entry<K,V> p = root;
        if (p != null){
            while(p.left != null){
                p = p.left;
            }
        }
        return p;
    }

    final Entry<K,V> getLastEntry(){
        Entry<K,V> p = root;
        if (p != null){
            while(p.right != null){
                p = p.right;
            }
        }
        return p;
    }

    final Entry<K,V> getCeilingEntry(K key){
        Entry<K,V> p = root;
        while(p != null){
            int cmp = compare(key, p.getKey());
            if (cmp < 0){
                if(p.left != null)
                    p = p.left;
                else
                    return p;
            }else if(cmp > 0){
                if(p.right != null){
                    p = p.right;
                }else {
                    Entry<K,V> parent = p.parent;
                    Entry<K,V> ch = p;
                    while(parent != null && ch == parent.right){
                        ch = parent;
                        parent = parent.parent;
                    }
                    return parent;
                }
            }else{
                return p;
            }
        }
        return null;
    }

    final Entry<K,V> getFloorEntry(K key) {
        Entry<K,V> p = root;
        while (p != null) {
            int cmp = compare(key, p.getKey());
            if (cmp > 0) {
                if (p.right != null)
                    p = p.right;
                else
                    return p;
            } else if (cmp < 0) {
                if (p.left != null) {
                    p = p.left;
                } else {
                    Entry<K,V> parent = p.parent;
                    Entry<K,V> ch = p;
                    while (parent != null && ch == parent.left) {
                        ch = parent;
                        parent = parent.parent;
                    }
                    return parent;
                }
            } else
                return p;
        }
        return null;
    }

    /**
     * iterators and helpers for them
     */
    private class SubMapEntryIterator extends PrivateEntryIterator<Map.Entry<K,V>>{
        private final K firstExcludedKey;

        SubMapEntryIterator(Entry<K,V> first, Entry<K,V> firstExcluded) {
            super(first);
            firstExcludedKey = (firstExcluded == null ? null : firstExcluded.getKey()); }

        public boolean hasNext() {
            return next != null && next.getKey()!= firstExcludedKey;
            }

        public Map.Entry <K,V> next() {
            if (next == null || next.getKey() == firstExcludedKey)
                throw new NoSuchElementException ();
            return nextEntry();
        }
    }

    private class EntryIterator extends PrivateEntryIterator<Map.Entry<K,V>>{
        public Map.Entry<K,V> next(){
            return nextEntry();
        }
    }

    static <K,V> Entry<K,V> successor(Entry<K,V> t){
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

    abstract class PrivateEntryIterator<T> implements Iterator<T> {
        Entry<K,V> next;
        Entry<K,V> lastReturned;
        int expectedmodCount;

        PrivateEntryIterator(){
            expectedmodCount = Treap2.this.modCount;
            next = getFirstEntry();
        }

        PrivateEntryIterator(Entry<K,V> first){
            expectedmodCount = Treap2.this.modCount;
            lastReturned = null;
            next = first;
        }

        public boolean hasNext(){return next != null;}

        final Entry<K,V> nextEntry(){
            Entry<K,V> e = next;
            if(e == null){
                throw new NoSuchElementException();
            }if(modCount != expectedmodCount){
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
            if (modCount != expectedmodCount) {
                throw new ConcurrentModificationException();
            }
            next = predecessor(e);
            lastReturned = e;
            return e;
        }

        public void remove(){
            //todo remove
        }

    }

    /**
     * Entry set function returns the entryset
     * @return
     */
    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        if(entrySet == null){
            entrySet = new EntrySet();
        }
        return entrySet;
    }


    private class EntrySet extends AbstractSet<Map.Entry<K,V>>{

        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            return new EntryIterator();
        }

        public boolean contains(Object o){
            if(!(o instanceof Map.Entry)){
                return false;
            }
            Map.Entry<K,V> entry = (Map.Entry<K,V>) o;
            V value = entry.getValue();
            Entry<K,V> p = getEntry(entry.getKey());
            return p != null && valEquals(p.getValue(), value);
        }

        public boolean remove(Object o){
            //todo later
            throw new UnsupportedOperationException();
        }

        @Override
        public int size() {
            return Treap2.this.size();
        }

        public void clear(){
            Treap2.this.clear();
        }
    }


    /**
     * Entry class that stores values
     */
    static final class Entry<K,V> extends AbstractMap.SimpleEntry<K,V> implements SortedMap.Entry<K,V>{

        Entry<K,V> left;
        Entry<K,V> right;
        Entry<K,V> parent;
        int priority;

        Entry(K key, V value, Entry<K,V> parent){
            super(key, value);
            this.parent = parent;
            priority = rand.nextInt();
        }

        public Element printTreap(Document doc){
            //System.err.println(this);
            Element e = doc.createElement("node");
            //uncaught exception
            Point2D a = (Point2D.Float) super.getValue();
            e.setAttribute("key", (String) super.getKey());
            e.setAttribute("priority", Integer.toString(priority));
            String val = "(" + (int)a.getX() + "," + (int)a.getY() + ")";
            e.setAttribute("value", val);
            if(this.left== null){
                e.appendChild(doc.createElement("emptyChild"));
            }else{
                e.appendChild(left.printTreap(doc));
            }if(this.right== null){
                e.appendChild(doc.createElement("emptyChild"));
            }else {
                e.appendChild(right.printTreap(doc));
            }
            return e;
        }

    }
}
