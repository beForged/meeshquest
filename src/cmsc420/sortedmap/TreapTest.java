package cmsc420.sortedmap;

import java.util.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TreapTest {
    @Test
    public static void main(String[] args) {
        Random random = new Random();
        Comparator<String> comparator = Comparator.comparing(String::toString);
        Treap<String, Integer> t = new Treap<>(comparator);
        TreeMap<String, Integer> r = new TreeMap<>(comparator);

        for(int i = 0;i<40;i++)
        {
            int randomInt = random.nextInt();
            t.put(String.valueOf(randomInt), randomInt);
            r.put(String.valueOf(randomInt), randomInt);
        }
        assertTrue(t.firstKey().equals(r.firstKey()));
        assertTrue (t.lastKey().equals(r.lastKey()));

        assertTrue (t.containsKey(t.firstKey()));
        assertTrue (t.containsKey(t.lastKey()));

        assertTrue (t.hashCode() == r.hashCode());
        assertTrue (t.toString().equals(r.toString()));

        assertTrue (t.equals(r));
        System.out.println(r.equals(t));
        assertTrue (r.equals(t));
        /**
         * =================================================
         */
/*
        Map a = t.subMap("g", "g");
        Map comp = new HashMap();
        assertTrue (a == comp);
        assertTrue(a.isEmpty());
*/
        /**
         * =================================================
         */
        System.out.println("\nentryset testing");
        Set treeb = r.entrySet();
        Set b = t.entrySet();

        Iterator treei = treeb.iterator();
        Iterator i = b.iterator();

        assertTrue(treeb.equals(b));
        assertTrue(b.equals(treeb));

        while(i.hasNext()){
            assertTrue(i.next().equals(treei.next()));
        }
        t.put("l", 12);
        r.put("l", 12);
        Iterator treei2 = treeb.iterator();
        Iterator i2 = b.iterator();
        while(i2.hasNext()){
            //System.out.println(i2.next());
            //System.out.println(treei2.next());
            assertTrue (i2.next().equals(treei2.next()));
        }
        /**
         * =================================================
         */
        System.out.println("\nsubmap test");

    }
}

