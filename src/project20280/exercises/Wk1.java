package project20280.exercises;

import project20280.interfaces.List;
import project20280.list.SinglyLinkedList;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertNull;

public class Wk1 {

    public static void q1() {
        int[] my_array = {25, 14, 56, 15, 36, 56, 77, 18, 29, 49};

        System.out.println(my_array);

        //double average = ...;
    }
    public static void main(String [] args) {

        List<Integer> ll = new SinglyLinkedList<Integer>();
        assertNull(ll.removeFirst());

        ll.addLast(1);
        ll.addLast(2);
        ll.addLast(3);
        ll.removeLast();

        System.out.println(ll);


    }
}
