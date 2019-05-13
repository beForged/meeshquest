package cmsc420.sortedmap;

import java.util.*;

public class TreapTest {
    public static void main(String[] args) {
        Treap2<String, Integer> t = new Treap2<>(((o1, o2) -> (-1)* ((String)o1).compareTo((String)o2)));
        TreeMap<String, Integer> r = new TreeMap<>(((o1, o2) -> (-1)* ((String)o1).compareTo((String)o2)));
        t.put("a", 1);
        t.put("b", 2);
        t.put("c", 3);
        t.put("d", 4);
        t.put("e", 5);
        t.put("f", 6);
        t.put("g", 7);
        t.put("h", 8);
        t.put("i", 8);
        t.put("j", 10);
        t.put("k", 11);
        r.put("a", 1);
        r.put("b", 2);
        r.put("c", 3);
        r.put("d", 4);
        r.put("e", 5);
        r.put("f", 6);
        r.put("g", 7);
        r.put("h", 8);
        r.put("i", 8);
        r.put("j", 10);
        r.put("k", 11);

        /**
         * ===============================================
         */

        System.out.println(t.toString());
        assert (t.containsKey("d"));
        assert (t.containsValue(2));
        assert (!t.containsKey("132"));
        assert (!t.containsValue(238));
        //System.out.println(t.comparator());
        assert(t.firstKey().equals("k"));
        assert (t.containsKey("d") );
        assert (t.containsValue(2) );
        assert (!t.containsKey("132"));
        assert (!t.containsValue(238));

        //System.out.println(t.comparator());
        assert(t.firstKey().equals("k"));
        assert(t.lastKey().equals("a"));

        assert (t.equals(r));
        /**
         * =================================================
         */

        Map a = t.subMap("g", "g");
        Map comp = new HashMap();
        assert (a == comp);
        /**
         * =================================================
         */

        Set b = t.entrySet();
        Iterator i = b.iterator();
        String comp1 = "";
        String comp2 = "";
        while(i.hasNext()){
            comp1 += i.next() + " ";
        }
        t.put("l", 12);
        Iterator i2 = b.iterator();
        while(i2.hasNext()){
            comp2 += i2.next() + " ";
        }
        System.out.println(comp1);
        System.out.println(comp2);
        assert(!comp1.equals(comp2));

    }
}

