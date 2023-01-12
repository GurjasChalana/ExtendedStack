package comp2402a4;

import java.util.Iterator;
import java.util.Random;

public class Tester {

    static <T> void showContents(Iterable<Integer> ds) {
        System.out.print("[");
        Iterator<Integer> it = ds.iterator();
        while (it.hasNext()) {
            System.out.print(it.next());
            if (it.hasNext()) {
                System.out.print(",");
            }
        }
        System.out.println("]");
    }

    static void sparrowTest(int n) {
        SlowSparrow slow = new SlowSparrow();
        FastSparrow fast = new FastSparrow();

        Random rand = new Random();
        for (int j = 0; j < n; j++) {
            int x = rand.nextInt(3*n/2);
            System.out.println("Slow push(" + x + ")");
            System.out.println("Fast push(" + x + ")");
            slow.push(x);
            fast.push(x);
            System.out.println("Slow: ");
            showContents(slow);
            System.out.println("Fast: ");
            showContents(fast);
            int i = rand.nextInt(slow.size());
            System.out.println("Slow get("+ i +") = " + slow.get(i));
            System.out.println("Fast get("+ i +") = " + fast.get(i));
            System.out.println("Slow max() = " + slow.max());
            System.out.println("Fast max() = " + fast.max());
            int k = rand.nextInt(slow.size()+1);
            System.out.println("Slow ksum("+ k +") = " + slow.ksum(k));
            System.out.println("Fast ksum("+ k +") = " + fast.ksum(k));
            if (slow.ksum(k) != fast.ksum(k)) {
                System.out.println("Slow ksum("+ k +") = " + slow.ksum(k));
                System.out.println("Fast ksum("+ k +") = " + fast.ksum(k));
                System.exit(-1);
            }
        }

        for (int j = 0; j < n/2; j++) {
            int i = rand.nextInt(slow.size());
            int x = rand.nextInt(3*n/2);
            System.out.println("Slow set(" + i + ", " + x + ") = " + slow.set(i, x));
            System.out.println("Fast set(" + i + ", " + x + ") = " + fast.set(i, x));
            System.out.println("Slow: ");
            showContents(slow);
            System.out.println("Fast: ");
            showContents(fast);
            i = rand.nextInt(slow.size());
            System.out.println("Slow get("+ i +") = " + slow.get(i));
            System.out.println("Fast get("+ i +") = " + fast.get(i));
            System.out.println("Slow max() = " + slow.max());
            System.out.println("Fast max() = " + fast.max());
            int k = rand.nextInt(slow.size()+1);
            System.out.println("Slow ksum("+ k +") = " + slow.ksum(k));
            System.out.println("Fast ksum("+ k +") = " + fast.ksum(k));
        }

        while (slow.size() > 0) {
            System.out.println("Slow pop() = " + slow.pop());
            System.out.println("Fast pop() = " + fast.pop());
            System.out.println("Slow: ");
            showContents(slow);
            System.out.println("Fast: ");
            showContents(fast);
            if(slow.size() > 0){
                int i = rand.nextInt(slow.size());
                System.out.println("Slow get("+ i +") = " + slow.get(i));
                System.out.println("Fast get("+ i +") = " + fast.get(i));
                System.out.println("Slow max() = " + slow.max());
                System.out.println("Fast max() = " + fast.max());
                int k = rand.nextInt(slow.size()+1);
                System.out.println("Slow ksum("+ k +") = " + slow.ksum(k));
                System.out.println("Fast ksum("+ k +") = " + fast.ksum(k));
            }
        }
    }

    public static void main(String[] args) {
        sparrowTest(10);
    }
}