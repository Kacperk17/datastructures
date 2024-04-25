package project20280.stacksqueues;

import project20280.interfaces.Queue;
import project20280.list.CircularlyLinkedList;

/**
 * Realization of a circular FIFO queue as an adaptation of a
 * CircularlyLinkedList. This provides one additional method not part of the
 * general Queue interface. A call to rotate() is a more efficient simulation of
 * the combination enqueue(dequeue()). All operations are performed in constant
 * time.
 */

public class LinkedCircularQueue<E> implements Queue<E> {

    private CircularlyLinkedList<E> list = new CircularlyLinkedList<>();
    private int size = 0;


    public static void main(String[] args) {


    }

    @Override
    public int size() {

        return size;
    }

    @Override
    public boolean isEmpty() {

        return size == 0;
    }

    @Override
    public void enqueue(E e) {


        list.addFirst(e);
        size++;

    }

    @Override
    public E first() {

        return list.get(0);
    }

    @Override
    public E dequeue() {


        E elementToRemove = list.get(size - 1);

        list.removeLast();
        size--;

        return elementToRemove;
    }

}
