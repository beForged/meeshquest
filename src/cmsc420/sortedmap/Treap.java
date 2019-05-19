package cmsc420.sortedmap;
import java.util.Comparator;

public class Treap<K,V> extends Treap2<K,V>{
    public Treap(){
    }
    public Treap(Comparator c){super(c);}
}
/*
import cmsc420.meeshquest.part3.GenericException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.Map;
public class Treap<K,V> extends AbstractMap<K,V> implements SortedMap<K,V> {
    private Comparator comparator;
    private Entry<K, V> root;
    private int modCount;
    private int size;
    static private Random rand = new Random();
    private transient EntrySet entrySet;
    public Treap() {
        comparator = null;
        modCount = 0;
    }
    public Treap(Comparator a) {
        this.comparator = a;
        modCount = 0;
    }
    @Override
    public Comparator comparator() {
        return comparator;
    }
    public Entry getRoot(){
        return this.root;
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
    public boolean equals(Object o){
        if(o instanceof Treap){
//TODO equals
        }
        return false;
    }
    public String toString(){
//TODO tostring
        return null;
    }
    @Override
    public int hashCode() {
//TODO hashcode
        return super.hashCode();
    }
    public Element printTreap(Document doc) throws GenericException {
        Entry e = root;
        if(e == null){
            throw new GenericException("emptyTree");
        }else{
            return e.printTreap(doc);
        }
    }
    public boolean checkHeap(Entry e){
//Entry e = root;
        if(e.left == null && e.right == null)
            return true;
        if(e.right == null){
            if(e.left.priority < e.priority)
                return false;
            else
                return checkHeap(e.left);
        }
        if(e.left == null){
            if(e.right.priority < e.priority)
                return false;
            else
                return checkHeap(e.right);
        }
        return checkHeap(e.left) && checkHeap(e.right);
    }
    public boolean checkBST(Entry e){
        if(e == null){
            return true;
        }
        int cmp;
        if(e.left != null) {
            cmp = comparator.compare(e.key, e.left.key);
            if (cmp < 0) {
                return false;
            }
        }
        if(e.right != null) {
            cmp = comparator.compare(e.key, e.right.key);
            if (cmp > 0)
                return false;
        }
        if(!checkBST(e.left) || !checkBST(e.right)){
            return false;
        }
        return true;
    }
    @Override
    public K firstKey() {
        return key(getFirstEntry());
    }
    @Override
    public K lastKey() {
        return key(getLastEntry());
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
            compare(key, key);
            root = new Entry<>(key, value, null);
            size = 1;
            modCount++;
//todo change size, modCount?
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
        size++;
        modCount++;
        return value; //todo check return type needed/ wanted
    }
    //we make a rotate class to call and then go from there
//left is true, right is false
//TODO check correct rotate
    private void rotate(Entry n) {
        while (n != null ) {
            if (n.left != null && n.left.priority > n.priority) {
                rightRotate(n);
            } else if (n.right != null && n.right.priority > n.priority) {
                leftRotate(n);
            }
            n = n.parent;
        }
    }
    */
/*
    T1, T2 and T3 are subtrees of the tree rooted with y
    (on left side) or x (on right side)
    y                               x
    / \     Right Rotation          /  \
    x   T3   – – – – – – – >        T1   y
    / \       < - - - - - - -            / \
    T1  T2     Left Rotation            T2  T3
    *//*

    private void rightRotate(Entry p){
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
    private void leftRotate(Entry p){
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
//TODO
    public void putAll(Map<? extends K, ? extends V> m) {
        //throw new UnsupportedOperationException();
        int mapsize = m.size();
        if (size==0 && mapsize!=0 && m instanceof SortedMap) {
            Comparator<?> c = ((SortedMap<?,?>)m).comparator();
        }
        super.putAll(m);
    }
    public void clear(){
        modCount++;
        size = 0;
        root = null;
    }
    final int compare(Object k1, Object k2){
        return comparator == null ? ((Comparable<? super K>)k1).compareTo((K)k2) : comparator.compare((K) k1, (K) k2);
    }
    public V get(Object key){
        if(key == null){
            throw new NullPointerException();
        }
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
    */
/**
     * This makes an entry set that is backed by the map
     * so that changes in the treap change the entry set as well
     * (should be able to) remove and can get, but no add functionality
     * @return Set<Map.Entry<K,V>>
     *//*

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        EntrySet es = entrySet;
        return (es != null) ? es : (entrySet = new EntrySet());
    }
    @Override
//TODO part 3
    public boolean remove(Object key, Object value) {
        throw new UnsupportedOperationException();
    }
    final Entry<K,V> getCeilingEntry(K key){
        Entry<K,V> p = root;
        while(p != null){
            int cmp = compare(key, p.key);
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
            int cmp = compare(key, p.key);
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
    */
/**
     * Gets the entry for the least key greater than the specified
     * key; if no such entry exists, returns the entry for the least
     * key greater than the specified key; if no such entry exists
     * returns {@code null}.
     *//*

    final Entry<K,V> getHigherEntry(K key) {
        Entry<K,V> p = root;
        while (p != null) {
            int cmp = compare(key, p.key);
            if (cmp < 0) {
                if (p.left != null)
                    p = p.left;
                else
                    return p;
            } else {
                if (p.right != null) {
                    p = p.right;
                } else {
                    Entry<K,V> parent = p.parent;
                    Entry<K,V> ch = p;
                    while (parent != null && ch == parent.right) {
                        ch = parent;
                        parent = parent.parent;
                    }
                    return parent;
                }
            }
        }
        return null;
    }
    static <K,V> Map.Entry<K,V> exportEntry(Treap.Entry<K,V> e) {
        return (e == null) ? null :
                new AbstractMap.SimpleImmutableEntry<>(e);
    }
    */
/**
     * Returns the entry for the greatest key less than the specified key; if
     * no such entry exists (i.e., the least key in the Tree is greater than
     * the specified key), returns {@code null}.
     *//*

    final Entry<K,V> getLowerEntry(K key) {
        Entry<K,V> p = root;
        while (p != null) {
            int cmp = compare(key, p.key);
            if (cmp > 0) {
                if (p.right != null)
                    p = p.right;
                else
                    return p;
            } else {
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
            }
        }
        return null;
    }
    public Map.Entry<K, V> higherEntry(K key) {
        return exportEntry(getHigherEntry(key));
    }
    public K higherKey(K key) {
        return keyOrNull(getHigherEntry(key));
    }
    public Map.Entry<K, V> firstEntry() {
        return exportEntry(getFirstEntry());
    }
    public Map.Entry<K, V> lastEntry() {
        return exportEntry(getLastEntry());
    }
    public Map.Entry<K, V> pollFirstEntry() {
        Entry<K,V> p = getFirstEntry();
        Map.Entry<K,V> result = exportEntry(p);
        if (p != null)
            deleteEntry(p);
        return result;
    }
    public Map.Entry<K, V> pollLastEntry() {
        Entry<K,V> p = getLastEntry();
        Map.Entry<K,V> result = exportEntry(p);
        if (p != null)
            deleteEntry(p);
        return result;
    }
    public NavigableSet<K> descendingKeySet() {
//return descendingMap().navigableKeySet();
        throw new UnsupportedOperationException();
    }
    public SortedMap<K,V> subMap(K fromKey, K toKey) {
        return new SubMap(fromKey, toKey);
    }
    protected class SubMap extends AbstractMap<K,V> implements SortedMap<K,V> {
        protected K fromKey;
        protected K toKey;
        protected boolean inRange(Object key) {
            if (key == null) {
                return false;
            }
            return (Treap.this.comparator.compare(key, fromKey) >= 0) && (Treap.this.comparator.compare(key, toKey) < 0);
        }
        public SubMap(K fromKey, K toKey) {
            if (fromKey == null || toKey == null)
                throw new NullPointerException(
                        "Treap.SubMap.SubMap(fromKey, toKey): this map does not support null keys");
            if (compare(fromKey, toKey) > 0)
                throw new IllegalArgumentException(
                        "Treap.SubMap.SubMap(fromKey, toKey): fromKey comes after toKey");
            this.fromKey = fromKey;
            this.toKey = toKey;
        }
        public void clear() {
//todo this is wrong, bc it clears whole thing not just the submap
            Treap.this.clear();
        }
        public boolean containsKey(Object key) {
            return (inRange(key) && Treap.this.containsKey(key));
        }
        @Override
        public boolean containsValue(Object value) {
//todo contains value
            return false;
        }
        private transient Set <Map.Entry <K,V>> entrySet = new EntrySet();
        public Set entrySet() {
//todo skipliest line 1500
            return new AbstractSet() {
                public boolean add(Object o) {
                    Object key = ((Map.Entry) o).getKey();
                    Object value = ((Map.Entry) o).getValue();
                    boolean add = Treap.this.containsKey(key);
                    Treap.this.put((K) key, (V) value);
                    return add;
                }
                public boolean addAll(Collection c) {
                    Iterator i = c.iterator();
                    boolean add = false;
                    while (i.hasNext())
                        try {
                            add = add || this.add(i.next());
                        } catch (IllegalArgumentException iae) {
                            add = false;
                        }
                    return add;
                }
                public void clear() {
                    SubMap.this.clear();
                }
                @Override
                public boolean contains(Object o) {
                    return SubMap.this.containsKey(o);
                }
                public boolean containsAll(Collection c) {
                    Iterator i = c.iterator();
                    while (i.hasNext())
                        if (contains(i.next()) == false)
                            return false;
                    return true;
                }
                public boolean equals(Object o) {
                    if (!(o instanceof Collection))
                        return false;
                    Collection s = (Collection) o;
                    if (s.size() == SubMap.this.size() && this.containsAll(s))
                        return true;
                    return false;
                }
                // Returns the hash code value for this set.
                public int hashCode() {
                    return System.identityHashCode(this);
                }
                // Returns true if this set contains no elements.
                public boolean isEmpty() {
                    return SubMap.this.size() == 0;
                }
                @Override
                public Iterator iterator() {
                    return SubMap.this.entryIterator();
                }
                @Override
                public int size() {
                    return SubMap.this.size();
                }
                public boolean remove(Object o) {
//todo remove
                    throw new UnsupportedOperationException();
                }
                public boolean removeAll(Collection c) {
//todo
                    throw new UnsupportedOperationException();
                }
//retain, toArray, skiplist 1600
            };
        }
        public boolean equals(Object o) {
            if (!(o instanceof Map))
                return false;
            else {
                Iterator i = ((Map) o).entrySet().iterator();
                while (i.hasNext()) {
                    Map.Entry me = (Map.Entry) i.next();
                    if (inRange(me.getKey())) {
                        Treap.Entry n = Treap.this.getEntry(me.getKey());
                        if (n == null || !n.equals(me))
                            return false;
                    } else
                        return false;
                }
                return true;
            }
        }
        public V get(Object key) {
            if (!inRange(key))
                return null;
            Treap.Entry n = Treap.this.getEntry(key);
            if (n != null)
                return (V)n.value;
            else
                return null;
        }
        public int hashCode() {
            return System.identityHashCode(this);
        }
        public boolean isEmpty() {
            return size() == 0;
        }
        public Set keySet() {
            throw new UnsupportedOperationException();
        }
        public Object put(Object key, Object value) {
            if (key == null || value == null) {
                throw new NullPointerException();
            }
            if (!inRange(key))
                throw new IllegalArgumentException(
                        "Treap.SubMap.put(key, value): key is outside of the range allowed by this submap");
            else
                return Treap.this.put((K) key, (V) value);
        }
        public void putAll(Map t) {
            Iterator i = t.keySet().iterator();
            while (i.hasNext())
                if (!inRange(i.next()))
                    throw new IllegalArgumentException(
                            "Treap.SubMap.put(Map t): one or more key(s) in this map are outside of the range allowed by this submap");
            i = t.entrySet().iterator();
            while (i.hasNext()) {
                Map.Entry me = (Map.Entry) i.next();
                this.put(me.getKey(), me.getValue());
            }
        }
        public V remove(Object key) {
            if (!inRange(key))
                return null;
            else
                return Treap.this.remove(key);
        }
        public Collection values() {
            throw new UnsupportedOperationException();
        }
        public int size() {
//todo
            return -1;
        }
        public Comparator comparator() {
            return Treap.this.comparator();
        }
        public K firstKey() {
            Treap.Entry <K,V> n =  getCeilingEntry(fromKey);
            K first = key(n);
            if (compare(first, toKey) >= 0)
                throw new NoSuchElementException();
            return first;
        }
        public SortedMap headMap(Object toKey) {
            throw new UnsupportedOperationException();
        }
        public K lastKey() {
//todo lastkey maybe
            throw new UnsupportedOperationException();
        }
        public SortedMap subMap(K fromKey, K toKey) {
            if ((Treap.this.comparator.compare(this.fromKey, toKey) > 0)
                    || (Treap.this.comparator.compare(this.toKey, toKey) < 0))
                throw new IllegalArgumentException(
                        "SkipList.SubMap.subMap(fromKey, toKey): one or both key(s) is outside of the range allowed by this submap");
            else
                return new SubMap(fromKey, toKey);
        }
        public SortedMap tailMap(Object fromKey) {
            throw new UnsupportedOperationException();
        }
        protected Treap.Entry<K,V> findFirst(){
            if(size <= 0){
                return null;
            }
//todo fix this
            Treap.Entry p = root;
            while(p != null && Treap.this.comparator.compare(fromKey, p.left) >= 0){
                p = root.left;
            }
            return p;
        }
        protected Iterator entryIterator(){
            return new EntryIterator(findFirst());
        }
    }
    static <K> K key(Entry<K,?> e){
        if(e == null)
            throw new NoSuchElementException();
        return e.key;
    }
    static <K,V>K keyOrNull(Treap.Entry<K,V> e) {
        return (e == null) ? null : e.key;
    }
    private void deleteEntry(Entry<K,V> p){
        throw new UnsupportedOperationException();
    }
    */
/**
     * dummy value that serves as un unmachable fence key
     * /
     private static final Object UNBOUNDED = new Object();
     /**
     * submap class
     *//*

    class EntrySet extends AbstractSet<Map.Entry<K,V>>{
        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
//todo
            return new EntryIterator(getFirstEntry());
        }
        @Override
        public boolean contains(Object o){
            if (!(o instanceof Map.Entry)){
                return false;
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
            return Treap.this.size;
        }
        @Override
        public void clear(){ Treap.this.clear();}
    }
    abstract class PrivateEntryIterator<T> implements Iterator<T> {
        Entry<K,V> next;
        Entry<K,V> lastReturned;
        int expectedmodCount;
        PrivateEntryIterator(Entry<K,V> first){
            expectedmodCount = modCount;
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
    final class KeyIterator extends PrivateEntryIterator<K> {
        KeyIterator(Entry<K,V> first) {
            super(first);
        }
        public K next() {
            return nextEntry().key;
        }
    }
    private class SubMapEntryIterator extends PrivateEntryIterator<Map.Entry<K,V>>{
        private final K firstExcludedKey;
        SubMapEntryIterator(Entry<K,V> first, Entry<K,V> firstExcluded) {
            super(first);
            firstExcludedKey = (firstExcluded == null ? null : firstExcluded.key);
        }
        public boolean hasNext(){
            return next != null && next.key != firstExcludedKey;
        }
        public Map.Entry <K,V> next() {
            if (next == null || next.key == firstExcludedKey)
                new NoSuchElementException();
            return nextEntry();
        }
    }
    Iterator<K> keyIterator() {
        return new KeyIterator(getFirstEntry());
    }
    static final boolean valEquals(Object o1, Object o2){
        return (o1 == null ? o2 == null : o1.equals(o2));
    }
    */
/**
     * @return the first entry in the map according to keysort functionality
     *//*

    final Entry<K,V> getFirstEntry(){
        Entry<K,V> p = root;
        if (p != null){
            while(p.left != null){
                p = p.left;
            }
        }
        return p;
    }
    */
/**
     *
     * @return the last entry in the map
     *//*

    final Entry<K,V> getLastEntry(){
        Entry<K,V> p = root;
        if (p != null){
            while(p.right != null){
                p = p.right;
            }
        }
        return p;
    }
    final class EntryIterator extends PrivateEntryIterator<Map.Entry<K,V>>{
        EntryIterator(Entry<K, V> first) {
            super(first);
        }
        @Override
        public Map.Entry<K, V> next() {
            return nextEntry();
        }
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
    //we make an inner class to hold each node in the map, and extend map.entry like in Treap
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
            return key + "=" + value;
        }
        public boolean equals(Object o) {
            if (!(o instanceof Map.Entry))
                return false;
            Map.Entry<?,?> e = (Map.Entry<?,?>)o;
            return valEquals(key,e.getKey()) && valEquals(value,e.getValue());
        }
        public int hashCode() {
            int keyHash = (key==null ? 0 : key.hashCode());
            int valueHash = (value==null ? 0 : value.hashCode());
            return keyHash ^ valueHash;
        }
        public Element printTreap(Document doc){
//System.err.println(this);
            Element e = doc.createElement("node");
//uncaught exception
            Point2D a = (Point2D.Float) value;
            e.setAttribute("key", (String) key);
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







*/

