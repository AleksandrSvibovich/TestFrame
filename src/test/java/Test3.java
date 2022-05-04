import org.testng.annotations.Test;

import java.util.*;

public class Test3 {

    public Test3(String... a) {

    }

    {
//        list.add("1");
    }

    public static void main(String[] args) {
        Test3 a = new Test3("1", "2", "3");
        a.addWord("a");
        a.addWord("a");
        a.addWord("no");
        a.addWord("yr");
        a.addWord("bv");
        a.addWord("cr");
        a.addWord("dfgg");
        a.addWord("iiiir");
        a.addWord("aaa");
        System.out.println(a.getList());
    }

    Object monitor = new Object();
    ArrayList<String> list = new ArrayList<String>();


    public Collection<String> getList() {
        return list;
    }

    public synchronized void addWord(String text) {

        synchronized (monitor) {
            if (!list.contains(text)) {
                list.add(text);
            }
        }
    }
}
