package cmsc420.sortedmap;

import cmsc420.meeshquest.part2.GenericException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.awt.geom.Point2D;
import java.util.*;
import java.util.Map;

public class Treap<K,V> extends AbstractMap<K,V> implements SortedMap<K,V>, NavigableMap<K, V> {

    private Comparator comparator;

    private Entry<K, V> root;

    private int modCount;

    private int size;
    static private Random rand = new Random();


    private transient EntrySet entrySet;
    private transient KeySet<K> navigatebleKeyset;

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

    public Element printTreap(Document doc) throws GenericException {
        Entry e = root;
        if(e == null){
            throw new GenericException("emptyTree");
        }else{
            return e.printTreap(doc);
        }
    }

    public boolean checkBST(){
        Entry e = root;
        return true;
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
    private void rotate(Entry n){
        if(n.left != null && n.priority < n.left.priority){
            leftRotate(n);
            rotate(n.parent);
        }else if(n.right!= null && n.priority < n.right.priority){
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
    public void putAll(Map<? extends K, ? extends V> m) {
        throw new UnsupportedOperationException();
        /*
        int mapsize = m.size();
        if (size==0 && mapsize!=0 && m instanceof SortedMap) {
            Comparator<?> c = ((SortedMap<?,?>)m).comparator();
        }
        super.putAll(m);
        */
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

    /**
     * This makes an entry set that is backed by the map
     * so that changes in the treap change the entry set as well
     * (should be able to) remove and can get, but no add functionality
     * @return Set<Map.Entry<K,V>>
     */
    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        EntrySet es = entrySet;
        return (es != null) ? es : (entrySet = new EntrySet());
    }

    public Set<K> keySet(){
        return navigatebleKeyset();
    }

    public NavigableSet<K> navigatebleKeyset(){
        KeySet<K> nks = navigatebleKeyset;
        return (nks != null) ? nks : (navigatebleKeyset = new KeySet<K>(this));
    }

    @Override
    //TODO part 3
    public boolean remove(Object key, Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<K, V> lowerEntry(K key) {
        return exportEntry(getLowerEntry(key));
    }

    @Override
    public K lowerKey(K key) {
        return keyOrNull(getLowerEntry(key));
    }

    @Override
    public Map.Entry<K, V> floorEntry(K key) {
        return exportEntry(getFloorEntry(key));
    }

    @Override
    public K floorKey(K key) {
        return keyOrNull(getFloorEntry(key));
    }

    @Override
    public Map.Entry<K, V> ceilingEntry(K key) {
        return exportEntry(getCeilingEntry(key));
    }

    @Override
    public K ceilingKey(K key) {
        return keyOrNull(getCeilingEntry(key));
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

    /**
     * Gets the entry for the least key greater than the specified
     * key; if no such entry exists, returns the entry for the least
     * key greater than the specified key; if no such entry exists
     * returns {@code null}.
     */
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

    /**
     * Returns the entry for the greatest key less than the specified key; if
     * no such entry exists (i.e., the least key in the Tree is greater than
     * the specified key), returns {@code null}.
     */
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

    @Override
    public Map.Entry<K, V> higherEntry(K key) {
        return exportEntry(getHigherEntry(key));
    }

    @Override
    public K higherKey(K key) {
        return keyOrNull(getHigherEntry(key));
    }

    @Override
    public Map.Entry<K, V> firstEntry() {
        return exportEntry(getFirstEntry());
    }

    @Override
    public Map.Entry<K, V> lastEntry() {
        return exportEntry(getLastEntry());
    }

    @Override
    public Map.Entry<K, V> pollFirstEntry() {
        Entry<K,V> p = getFirstEntry();
        Map.Entry<K,V> result = exportEntry(p);
        if (p != null)
            deleteEntry(p);
        return result;
    }

    @Override
    public Map.Entry<K, V> pollLastEntry() {
        Entry<K,V> p = getLastEntry();
        Map.Entry<K,V> result = exportEntry(p);
        if (p != null)
            deleteEntry(p);
        return result;
    }

    @Override
    public NavigableMap<K, V> descendingMap() {
        //todo
        return null;
        /*
        NavigableMap<K, V> km = descendingMap;
        return (km != null) ? km : (descendingMap = new DescendingSubMap<>(this, true, null, true, true, null, true));
        */
    }

    @Override
    public NavigableSet<K> navigableKeySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableSet<K> descendingKeySet() {
        //return descendingMap().navigableKeySet();
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
        return new AscendingSubMap<>(this, false, fromKey, fromInclusive, false, toKey,   toInclusive);
    }

    public SortedMap<K,V> subMap(K fromKey, K toKey) {
        return subMap(fromKey, true, toKey, false);
    }

    @Override
    public NavigableMap<K, V> headMap(K toKey, boolean inclusive) {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive) {
        throw new UnsupportedOperationException();
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

    /**
     * dummy value that serves as un unmachable fence key
     */
    private static final Object UNBOUNDED = new Object();

    /**
     * submap class
     */
    abstract static class NavigableSubMap<K,V> extends AbstractMap<K,V>
        implements NavigableMap<K,V>{
        final Treap<K,V> m;
        final K low, high;
        final boolean fromStart, toEnd;
        final boolean lowInclusive, highInclusive;

        /**
         * constructor argument
         * @param m the backing treap
         * @param lo low val
         * @param hi high val
         */
        NavigableSubMap(Treap<K,V> m,
                        boolean fromStart, K lo, boolean loInclusive,
                        boolean toEnd,     K hi, boolean hiInclusive) {
            if (!fromStart && !toEnd) {
                if (m.compare(lo, hi) > 0)
                    throw new IllegalArgumentException("fromKey > toKey");
            } else {
                if (!fromStart) // type check
                    m.compare(lo, lo);
                if (!toEnd)
                    m.compare(hi, hi);
            }

            this.m = m;
            this.fromStart = fromStart;
            this.low = lo;
            this.lowInclusive = loInclusive;
            this.toEnd = toEnd;
            this.high = hi;
            this.highInclusive = hiInclusive;
        }
        // internal utilities

        final boolean tooLow(Object key) {
            if (!fromStart) {
                int c = m.compare(key, low);
                if (c < 0 || (c == 0 && !lowInclusive))
                    return true;
            }
            return false;
        }

        final boolean tooHigh(Object key) {
            if (!toEnd) {
                int c = m.compare(key, high);
                if (c > 0 || (c == 0 && !highInclusive))
                    return true;
            }
            return false;
        }

        final boolean inRange(Object key) {
            return !tooLow(key) && !tooHigh(key);
        }

        final boolean inClosedRange(Object key) {
            return (fromStart || m.compare(key, low) >= 0)
                    && (toEnd || m.compare(high, key) >= 0);
        }

        final boolean inRange(Object key, boolean inclusive) {
            return inclusive ? inRange(key) : inClosedRange(key);
        }

        /*
         * Absolute versions of relation operations.
         * Subclasses map to these using like-named "sub"
         * versions that invert senses for descending maps
         */

        final Treap.Entry<K,V> absLowest() {
            Treap.Entry<K,V> e =
                    (fromStart ?  m.getFirstEntry() :
                            (lowInclusive ? m.getCeilingEntry(low) :
                                    m.getHigherEntry(low)));
            return (e == null || tooHigh(e.key)) ? null : e;
        }

        final Treap.Entry<K,V> absHighest() {
            Treap.Entry<K,V> e =
                    (toEnd ?  m.getLastEntry() :
                            (highInclusive ?  m.getFloorEntry(high) :
                                    m.getLowerEntry(high)));
            return (e == null || tooLow(e.key)) ? null : e;
        }

        final Treap.Entry<K,V> absCeiling(K key) {
            if (tooLow(key))
                return absLowest();
            Treap.Entry<K,V> e = m.getCeilingEntry(key);
            return (e == null || tooHigh(e.key)) ? null : e;
        }

        final Treap.Entry<K,V> absHigher(K key) {
            if (tooLow(key))
                return absLowest();
            Treap.Entry<K,V> e = m.getHigherEntry(key);
            return (e == null || tooHigh(e.key)) ? null : e;
        }

        final Treap.Entry<K,V> absFloor(K key) {
            if (tooHigh(key))
                return absHighest();
            Treap.Entry<K,V> e = m.getFloorEntry(key);
            return (e == null || tooLow(e.key)) ? null : e;
        }

        final Treap.Entry<K,V> absLower(K key) {
            if (tooHigh(key))
                return absHighest();
            Treap.Entry<K,V> e = m.getLowerEntry(key);
            return (e == null || tooLow(e.key)) ? null : e;
        }

        /** Returns the absolute high fence for ascending traversal */
        final Treap.Entry<K,V> absHighFence() {
            return (toEnd ? null : (highInclusive ?
                    m.getHigherEntry(high) :
                    m.getCeilingEntry(high)));
        }

        /** Return the absolute low fence for descending traversal  */
        final Treap.Entry<K,V> absLowFence() {
            return (fromStart ? null : (lowInclusive ?
                    m.getLowerEntry(low) :
                    m.getFloorEntry(low)));
        }

        // Abstract methods defined in ascending vs descending classes
        // These relay to the appropriate absolute versions

        abstract Treap.Entry<K,V> subLowest();
        abstract Treap.Entry<K,V> subHighest();
        abstract Treap.Entry<K,V> subCeiling(K key);
        abstract Treap.Entry<K,V> subHigher(K key);
        abstract Treap.Entry<K,V> subFloor(K key);
        abstract Treap.Entry<K,V> subLower(K key);

        /** Returns ascending iterator from the perspective of this submap */
        abstract Iterator<K> keyIterator();

        abstract Spliterator<K> keySpliterator();

        /** Returns descending iterator from the perspective of this submap */
        abstract Iterator<K> descendingKeyIterator();

        // public methods

        public boolean isEmpty() {
            return (fromStart && toEnd) ? m.isEmpty() : entrySet().isEmpty();
        }

        public int size() {
            return (fromStart && toEnd) ? m.size() : entrySet().size();
        }


        public final boolean containsKey(Object key) {
            if(key == null){
                throw new NullPointerException();
            }
            return inRange(key) && m.containsKey(key);
        }

        public final V put(K key, V value) {
            if (!inRange(key))
                throw new IllegalArgumentException("key out of range");
            return m.put(key, value);
        }

        public final V get(Object key) {
            return !inRange(key) ? null :  m.get(key);
        }

        public final V remove(Object key) {
            return !inRange(key) ? null : m.remove(key);
        }

        public final Map.Entry<K,V> ceilingEntry(K key) {
            return exportEntry(subCeiling(key));
        }

        public final K ceilingKey(K key) {
            return keyOrNull(subCeiling(key));
        }

        public final Map.Entry<K,V> higherEntry(K key) {
            return exportEntry(subHigher(key));
        }

        public final K higherKey(K key) {
            return keyOrNull(subHigher(key));
        }

        public final Map.Entry<K,V> floorEntry(K key) {
            return exportEntry(subFloor(key));
        }

        public final K floorKey(K key) {
            return keyOrNull(subFloor(key));
        }

        public final Map.Entry<K,V> lowerEntry(K key) {
            return exportEntry(subLower(key));
        }

        public final K lowerKey(K key) {
            return keyOrNull(subLower(key));
        }

        public final K firstKey() {
            return key(subLowest());
        }

        public final K lastKey() {
            return key(subHighest());
        }

        public final Map.Entry<K,V> firstEntry() {
            return exportEntry(subLowest());
        }

        public final Map.Entry<K,V> lastEntry() {
            return exportEntry(subHighest());
        }

        public final Map.Entry<K,V> pollFirstEntry() {
            Treap.Entry<K,V> e = subLowest();
            Map.Entry<K,V> result = exportEntry(e);
            if (e != null)
                m.deleteEntry(e);
            return result;
        }

        public final Map.Entry<K,V> pollLastEntry() {
            Treap.Entry<K,V> e = subHighest();
            Map.Entry<K,V> result = exportEntry(e);
            if (e != null)
                m.deleteEntry(e);
            return result;
        }

        // Views
        transient NavigableMap<K,V> descendingMapView;
        transient EntrySetView entrySetView;
        transient KeySet<K> navigableKeySetView;

        public final NavigableSet<K> navigableKeySet() {
            KeySet<K> nksv = navigableKeySetView;
            return (nksv != null) ? nksv :
                    (navigableKeySetView = new Treap.KeySet<>(this));
        }

        public final Set<K> keySet() {
            return navigableKeySet();
        }

        public NavigableSet<K> descendingKeySet() {
            return descendingMap().navigableKeySet();
        }

        public final SortedMap<K,V> subMap(K fromKey, K toKey) {
            return subMap(fromKey, true, toKey, false);
        }

        public final SortedMap<K,V> headMap(K toKey) {
            return headMap(toKey, false);
        }

        public final SortedMap<K,V> tailMap(K fromKey) {
            return tailMap(fromKey, true);
        }

        // View classes

        abstract class EntrySetView extends AbstractSet<Map.Entry<K,V>> {
            private transient int size = -1, sizemodCount;

            public int size() {
                if (fromStart && toEnd)
                    return m.size();
                if (size == -1 || sizemodCount != m.modCount) {
                    sizemodCount = m.modCount;
                    size = 0;
                    Iterator<?> i = iterator();
                    while (i.hasNext()) {
                        size++;
                        i.next();
                    }
                }
                return size;
            }

            public boolean isEmpty() {
                Treap.Entry<K,V> n = absLowest();
                return n == null || tooHigh(n.key);
            }

            public boolean contains(Object o) {
                if (!(o instanceof Map.Entry))
                    return false;
                Map.Entry<?,?> entry = (Map.Entry<?,?>) o;
                Object key = entry.getKey();
                if (!inRange(key))
                    return false;
                Treap.Entry<?,?> node = m.getEntry(key);
                return node != null &&
                        valEquals(node.getValue(), entry.getValue());
            }

            public boolean remove(Object o) {
                if (!(o instanceof Map.Entry))
                    return false;
                Map.Entry<?,?> entry = (Map.Entry<?,?>) o;
                Object key = entry.getKey();
                if (!inRange(key))
                    return false;
                Treap.Entry<K,V> node = m.getEntry(key);
                if (node!=null && valEquals(node.getValue(),
                        entry.getValue())) {
                    //todo remove
                    //m.deleteEntry(node);
                    return true;
                }
                return false;
            }
        }

        /**
         * Iterators for SubMaps
         */
        abstract class SubMapIterator<T> implements Iterator<T> {
            Treap.Entry<K,V> lastReturned;
            Treap.Entry<K,V> next;
            final Object fenceKey;
            int expectedmodCount;

            SubMapIterator(Treap.Entry<K,V> first,
                           Treap.Entry<K,V> fence) {
                expectedmodCount = m.modCount;
                lastReturned = null;
                next = first;
                fenceKey = fence == null ? UNBOUNDED : fence.key;
            }

            public final boolean hasNext() {
                return next != null && next.key != fenceKey;
            }

            final Treap.Entry<K,V> nextEntry() {
                Treap.Entry<K,V> e = next;
                if (e == null || e.key == fenceKey)
                    throw new NoSuchElementException();
                if (m.modCount != expectedmodCount)
                    throw new ConcurrentModificationException();
                next = successor(e);
                lastReturned = e;
                return e;
            }

            final Treap.Entry<K,V> prevEntry() {
                Treap.Entry<K,V> e = next;
                if (e == null || e.key == fenceKey)
                    throw new NoSuchElementException();
                if (m.modCount != expectedmodCount)
                    throw new ConcurrentModificationException();
                next = predecessor(e);
                lastReturned = e;
                return e;
            }

            final void removeAscending() {
                if (lastReturned == null)
                    throw new IllegalStateException();
                if (m.modCount != expectedmodCount)
                    throw new ConcurrentModificationException();
                // deleted entries are replaced by their successors
                if (lastReturned.left != null && lastReturned.right != null)
                    next = lastReturned;
                m.deleteEntry(lastReturned);
                lastReturned = null;
                expectedmodCount = m.modCount;
            }

            final void removeDescending() {
                if (lastReturned == null)
                    throw new IllegalStateException();
                if (m.modCount != expectedmodCount)
                    throw new ConcurrentModificationException();
                m.deleteEntry(lastReturned);
                lastReturned = null;
                expectedmodCount = m.modCount;
            }

        }

        final class SubMapEntryIterator extends SubMapIterator<Map.Entry<K,V>> {
            SubMapEntryIterator(Treap.Entry<K,V> first,
                                Treap.Entry<K,V> fence) {
                super(first, fence);
            }
            public Map.Entry<K,V> next() {
                return nextEntry();
            }
            public void remove() {
                removeAscending();
            }
        }

        final class DescendingSubMapEntryIterator extends SubMapIterator<Map.Entry<K,V>> {
            DescendingSubMapEntryIterator(Treap.Entry<K,V> last,
                                          Treap.Entry<K,V> fence) {
                super(last, fence);
            }

            public Map.Entry<K,V> next() {
                return prevEntry();
            }
            public void remove() {
                removeDescending();
            }
        }

    }

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
        //TODO this does a circular reference to abstractMap
        public void clear(){ Treap.this.clear();}

        public Spliterator<Map.Entry<K,V>> spliterator(){
            //I dont think we need to implement this
            return null;
        }
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

        public final boolean hasNext(){return next != null;}

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

        public void Remove(){
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

    static final class AscendingSubMap<K,V> extends NavigableSubMap<K,V> {
        AscendingSubMap(Treap<K,V> m, boolean fromStart, K lo, boolean loInclusive, boolean toEnd,
                        K hi, boolean hiInclusive) {
            super(m, fromStart, lo, loInclusive, toEnd, hi, hiInclusive);
        }

        public Comparator<? super K> comparator() {
            return m.comparator();
        }


        public NavigableMap<K,V> subMap(K fromKey, boolean fromInclusive,
                                        K toKey,   boolean toInclusive) {
            if (!inRange(fromKey, fromInclusive))
                throw new IllegalArgumentException("fromKey out of range");
            if (!inRange(toKey, toInclusive))
                throw new IllegalArgumentException("toKey out of range");
            return new AscendingSubMap<>(m, false, fromKey, fromInclusive, false, toKey,   toInclusive);
        }

        public NavigableMap<K,V> headMap(K toKey, boolean inclusive) {
            if (!inRange(toKey, inclusive))
                throw new IllegalArgumentException("toKey out of range");
            return new AscendingSubMap<>(m,
                    fromStart, low,    lowInclusive,
                    false,     toKey, inclusive);
        }


        public NavigableMap<K,V> tailMap(K fromKey, boolean inclusive) {

            if (!inRange(fromKey, inclusive))
                throw new IllegalArgumentException("fromKey out of range");
            return new AscendingSubMap<>(m, false, fromKey, inclusive, toEnd, high,      highInclusive);
        }


        public NavigableMap<K,V> descendingMap() {
            return null;
            /*
            NavigableMap<K,V> mv = descendingMapView;
            return (mv != null) ? mv :
                    (descendingMapView = new DescendingSubMap<>(m, fromStart, low, lowInclusive, toEnd,
                            high, highInclusive));

            */
        }


        Iterator<K> keyIterator() {
            throw new UnsupportedOperationException();
            //return new SubMapKeyIterator(absLowest(), absHighFence());
        }

        @Override
        Spliterator<K> keySpliterator() {
            throw new UnsupportedOperationException();
        }


        Iterator<K> descendingKeyIterator() {
            throw new UnsupportedOperationException();
            //return new DescendingSubMapKeyIterator(absHighest(), absLowFence());
        }


        final class AscendingEntrySetView extends EntrySetView {
            public Iterator<Map.Entry<K,V>> iterator() {
                return new SubMapEntryIterator(absLowest(), absHighFence());
            }
        }


        public Set<Map.Entry<K,V>> entrySet() {

            EntrySetView es = entrySetView;

            return (es != null) ? es : (entrySetView = new AscendingEntrySetView());

        }


        Treap.Entry<K,V> subLowest()       { return absLowest(); }

        Treap.Entry<K,V> subHighest()      { return absHighest(); }

        Treap.Entry<K,V> subCeiling(K key) { return absCeiling(key); }

        Treap.Entry<K,V> subHigher(K key)  { return absHigher(key); }

        Treap.Entry<K,V> subFloor(K key)   { return absFloor(key); }

        Treap.Entry<K,V> subLower(K key)   { return absLower(key); }
    }

    Iterator<K> keyIterator() {
        return new KeyIterator(getFirstEntry());
    }

    //todo might need to add submap functionality or keymap is unstupported
    static final class KeySet<E> extends AbstractSet<E> implements NavigableSet<E>{
        private final NavigableMap<E,?> m;
        KeySet(NavigableMap<E,?> map){m = map;}

        public Iterator<E> iterator(){
            return ((Treap.NavigableSubMap<E,?>)m).keyIterator();
        }

        @Override
        public NavigableSet<E> descendingSet() {
            return null;
        }

        public Iterator<E> descendingIterator(){
            return null;
        }
        public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) { throw new UnsupportedOperationException(); }
        public NavigableSet<E> headSet(E toElement, boolean inclusive) { throw new UnsupportedOperationException(); }
        public NavigableSet<E> tailSet(E fromElement, boolean inclusive) { throw new UnsupportedOperationException(); }
        public SortedSet<E> subSet(E fromElement, E toElement) { throw new UnsupportedOperationException(); }
        public SortedSet<E> headSet(E toElement) { throw new UnsupportedOperationException(); }
        public SortedSet<E> tailSet(E fromElement) { throw new UnsupportedOperationException(); }

        public int size(){return m.size();}

        public boolean isEmpty() { return m.isEmpty(); }

        public boolean contains(Object o){return m.containsKey(o);}

        public void clear() {m.clear();}

        public E lower(E e) {return m.lowerKey(e);}

        public E floor(E e) { return m.floorKey(e); }

        public E ceiling(E e) { return m.ceilingKey(e);}

        public E higher(E e) {return m.higherKey(e);}

        //skipping poll last and pollfirst
        @Override
        public E pollFirst() { throw new UnsupportedOperationException(); }

        @Override
        public E pollLast() { throw new UnsupportedOperationException(); }

        public E first() { return  m.firstKey();}
        public E last() {return m.lastKey();}

        public Comparator<? super E> comparator () {return m.comparator();}

        public boolean remove(Object o){
            int oldsize = size();
            m.remove(o);
            return size() != oldsize;
        }
        //skipping submaps and such

    }

    static final boolean valEquals(Object o1, Object o2){
        return (o1 == null ? o2 == null : o1.equals(o2));
    }
    /**
     * @return the first entry in the map according to keysort functionality
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

    /**
     *
     * @return the last entry in the map
     */
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
            return key + " = " + value;
        }

        public Element printTreap(Document doc){
            System.err.println(this);
            Element e = doc.createElement("node");
            //uncaught exception
            Point2D a = (Point2D.Float) value;
            e.setAttribute("key", (String) key);
            e.setAttribute("priority", Integer.toString(priority));
            String val = "(" + a.getX() + ", " + a.getY() + ")";
            e.setAttribute("value", val);
            if(this.right == null){
                e.appendChild(doc.createElement("emptyChild"));
            }else{
                e.appendChild(right.printTreap(doc));
            }if(this.left == null){
                e.appendChild(doc.createElement("emptyChild"));
            }else {
                e.appendChild(left.printTreap(doc));
            }
            return e;
        }

    }

}
