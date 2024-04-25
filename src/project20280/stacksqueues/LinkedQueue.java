package project20280.stacksqueues;

import project20280.interfaces.Queue;
import project20280.list.DoublyLinkedList;

public class LinkedQueue<E> implements Queue<E> {

    private DoublyLinkedList<E> ll;

    public static void main(String[] args) {
    }

    public LinkedQueue() {

        ll = new DoublyLinkedList<>();
    }

    @Override
    public int size() {
        return ll.size();
    }

    @Override
    public boolean isEmpty() {
        return ll.isEmpty();
    }

    @Override
    public void enqueue(E e) {


        ll.addLast(e);
    }

    @Override
    public E first() {


        return ll.get(0);
    }

    @Override
    public E dequeue() {

        if(!isEmpty()) {
            E dataToDequeue = ll.get(0);
            ll.removeFirst();
            return dataToDequeue;
        }
        else {
            return null;
        }

    }

    public String toString() {
        return ll.toString();
    }
}
