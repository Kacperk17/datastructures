package project20280.list;

import project20280.interfaces.List;

import java.util.Iterator;

public class CircularlyLinkedList<E> implements List<E> {

    private class Node<T> {
        private T data;
        private Node<T> next;

        public Node(T e, Node<T> n) {
            data = e;
            next = n;
        }

        public T getData() {
            return data;
        }

        public void setNext(Node<T> n) {
            next = n;
        }

        public Node<T> getNext() {
            return next;
        }

        public boolean isLastNode() {
            return this.getNext() == tail;
        }
    }

    private Node<E> tail = null;
    private int size = 0;

    public CircularlyLinkedList() {}

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public E get(int i) {

        // if list is empty
        if(isEmpty()) {
            return null;
        }
        else {
            Node<E> currNode = tail.getNext();

            while(i > 0) {
                currNode = currNode.getNext();
                i--;
            }

            E data = currNode.getData();
            return data;
        }


    }

    /**
     * Inserts the given element at the specified index of the list, shifting all
     * subsequent elements in the list one position further to make room.
     *
     * @param i the index at which the new element should be stored
     * @param e the new element to be stored
     */
    @Override
    public void add(int i, E e) {

        // if list is empty
        if(isEmpty()) {
            addFirst(e);
        }
        else { // if list is not empty

            Node<E> currNode = tail.getNext(); // first node in linked list

            // iterate through list (we want to get to node right before the desired position)
            while(i > 1) {
                currNode = currNode.getNext();
                i--;
            }

            Node<E> newNode = new Node(e, currNode.getNext());
            currNode.next = newNode;

            size++;

        }

    }


    @Override
    public E remove(int i) {


        if(isEmpty()) {
            return null;
        }
        else {

            Node<E> currNode = tail;

            // iterate through list
            while(i > 0) { // want to land at node before the one we want to remove

                currNode = currNode.getNext();
                i--;
            }

            Node<E> nodeToRemove = currNode.getNext();

            // bypass this node
            currNode.next = nodeToRemove.getNext();
            size--;

            return nodeToRemove.getData();

        }

    }

    public void rotate() {


        if(size > 1) { // if the list has more than 1 element

            Node<E> oldTail = tail;
            Node<E> newTail = tail.getNext();

            Node<E> secondLastElement = tail;
            while(secondLastElement.getNext() != tail) {
                secondLastElement = secondLastElement.getNext();
            }

            tail = newTail;
            secondLastElement.next = oldTail;
            oldTail.next = tail;

        }

    }

    private class CircularlyLinkedListIterator<E> implements Iterator<E> {
        Node<E> curr = (Node<E>) tail;

        @Override
        public boolean hasNext() {
            return curr != tail;
        }

        @Override
        public E next() {
            E res = curr.data;
            curr = curr.next;
            return res;
        }

    }

    @Override
    public Iterator<E> iterator() {
        return new CircularlyLinkedListIterator<E>();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public E removeFirst() {

        if(isEmpty()) {
            return null;
        }
        else if(size == 1) { // if there is only one element in the list

            E data = tail.getData();
            size--;
            tail = null;

            return data;
        }
        else { // if there is more than 1 element in the list

            Node<E> firstNode = tail.getNext();

            tail.next = firstNode.getNext();
            size--;
            return firstNode.getData();

        }


    }

    @Override
    public E removeLast() {

        // If list is empty
        if(isEmpty()) {
            return null;
        }
        else if (size == 1) { // if there is only one item in the list

            E data = tail.getData();
            tail = null;
            size--;
            return data;
        }
        else {
            E data = tail.getData();

            Node<E> currNode = tail;
            while(currNode.getNext() != tail) { // while we are not at 2nd last node
                currNode = currNode.getNext();
            }

            currNode.next = tail.getNext(); // bypass old tail
            tail = currNode; // reassign tail to new node
            size--;
            return data;
        }

    }

    @Override
    public void addFirst(E e) {

        if(isEmpty()) {
            tail = new Node(e, null);
            tail.next = tail;
        }
        else if(size == 1) { // if there is only one element in the list

            Node<E> newNode = new Node(e, tail);
            tail.next = newNode;

        }
        else { // if there is more than one element in the list

            Node<E> newNode = new Node(e, tail.getNext());
            tail.next = newNode;
        }
        size++;


    }

    @Override
    public void addLast(E e) {


        //if the list is empty
        if(isEmpty()) {
            tail = new Node(e, null);
            tail.next = tail;
        }
        else if (size == 1){ // if there is only one node in the list
            Node<E> oldTail = tail;
            tail = new Node(e, oldTail);

            oldTail.next = tail;
        }
        else { // if there is more than 1 node in the list
            Node<E> oldLastNode = tail;
            Node<E> newNode = new Node(e, tail.getNext());
            tail = newNode;

            oldLastNode.next = tail;

        }
        size++;

    }



    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> curr = tail;

        do {
            curr = curr.next;
            sb.append(curr.data);
            if (curr != tail) {
                sb.append(", ");
            }
        } while (curr != tail);
        sb.append("]");
        return sb.toString();
    }


    public static void main(String[] args) {
    }
}
