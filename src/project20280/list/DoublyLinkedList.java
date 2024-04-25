package project20280.list;

import project20280.interfaces.List;

import java.util.Iterator;

public class DoublyLinkedList<E> implements List<E> {

    private static class Node<E> {
        private E data;
        private Node<E> next;
        private Node<E> prev;

        public Node(E e, Node<E> p, Node<E> n) {
            data = e;
            prev = p;
            next = n;
        }

        public E getData() {
            return data;
        }

        public Node<E> getNext() {
            return next;
        }

        public Node<E> getPrev() {
            return prev;
        }

    }

    private final Node<E> head;
    private final Node<E> tail;
    private int size = 0;

    public DoublyLinkedList() {
        head = new Node<E>(null, null, null);
        tail = new Node<E>(null, head, null);
        head.next = tail;
    }

    private void addBetween(E e, Node<E> pred, Node<E> succ) {

        // declare new node with element
        Node<E> newNode = new Node(e, pred, succ);

        // change pred and succ to incorporate new node
        pred.next = newNode;
        succ.prev = newNode;

        size++;
    }

    @Override
    public int size() {

        return size;
    }

    @Override
    public boolean isEmpty() {

        return head.next == tail;
    }

    @Override
    public E get(int i) {


        // if the list is empty
        if(isEmpty()) {
            return null;
        }
        else { // if list is not empty
            Node<E> currNode = head.getNext();

            while (i > 0) { // iterate through list
                currNode = currNode.getNext();
                i--;
            }

            return currNode.getData();

        }

    }

    @Override
    public void add(int i, E e) {


        // if the list is not empty
        if(!isEmpty()) {
            Node<E> currNode = head;


            while (i > 0) { // iterate through list
                currNode = currNode.getNext();
                i--;
            }
            // if current node is the last node
            if(currNode.getNext() == null) {
                // make new node and next node is null
                Node<E> newNode = new Node(e, currNode, null);
                currNode.next = newNode;
            }
            else { // if current node is not the last one
                addBetween(e, currNode, currNode.getNext());
            }

            size++;

        }
    }

    @Override
    public E remove(int i) {


        // if list is empty
        if(isEmpty()) {
            return null;
        }
        else { // if list is not empty
            Node<E> currNode = head.getNext();

            // iterate through list
            while(i > 0) {
                currNode = currNode.getNext();
                i--;
            }

            // Remove currNode
            Node<E> prevNode = currNode.getPrev();

            // disconnect currNode and prevNode from each other, prevNode now points to node after currNode
            currNode.prev = null;
            prevNode.next = currNode.getNext();
            size--;
            return currNode.getData();

        }
    }

    private class DoublyLinkedListIterator<E> implements Iterator<E> {
        Node<E> curr = (Node<E>) head.next;

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
        return new DoublyLinkedListIterator<E>();
    }

    private E remove(Node<E> n) {

        // if list is empty
        if(isEmpty()) {
            return null;
        }
        else {

            Node<E> currNode = head;
            // iterate through list until node is found
            while(currNode != n) {
                currNode = currNode.getNext();
            }

            Node<E> prevNode = currNode.getPrev();

            // unlink current node from list
            currNode.prev = null;

            // set next node of prevNode to be next node of currNode
            prevNode.next = currNode.next;
            currNode.next = null;

            return currNode.getData();

        }

    }

    public E first() {
        if (isEmpty()) {
            return null;
        }
        return head.next.getData();
    }

    public E last() {

        if (isEmpty()) {
            return null;
        }
        return tail.prev.getData();
    }

    @Override
    public E removeFirst() {

        E data = head.next.getData();
        Node<E> firstNode = head.getNext();
        Node<E> secondNode = firstNode.getNext();

        // set next node from head to be second node and prev node of second node to be the head
        head.next = secondNode;
        secondNode.prev = head;

        // have first node point nowhere
        firstNode.next = null;
        firstNode.prev = null;
        size--;


        return data;
    }

    @Override
    public E removeLast() {

        E data = tail.prev.getData();
        Node<E> lastNode = tail.getPrev();
        Node<E> secondLastNode = lastNode.getPrev();

        // set next node of 2ndlast node to be tail and prev node of tail to be 2ndlast node
        tail.prev = secondLastNode;
        secondLastNode.next = tail;

        // disconnect lastNode from list
        lastNode.next = null;
        lastNode.prev = null;
        size--;

        return data;
    }

    @Override
    public void addLast(E e) {

        Node<E> lastNode = tail.prev;
        Node<E> newNode = new Node(e, lastNode, tail);

        // set last node and tail pointers accordingly
        lastNode.next = newNode;
        tail.prev = newNode;
        size++;

    }

    @Override
    public void addFirst(E e) {

        Node<E> firstNode = head.next;
        Node<E> newNode = new Node(e, head, firstNode);

        // set first node and head pointers accordingly
        firstNode.prev = newNode;
        head.next = newNode;
        size++;

    }

    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> curr = head.next;
        while (curr != tail) {
            sb.append(curr.data);
            curr = curr.next;
            if (curr != tail) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public static void main(String[] args) {
        DoublyLinkedList<Integer> ll = new DoublyLinkedList<Integer>();
        ll.addFirst(0);
        ll.addFirst(1);
        ll.addFirst(2);
        ll.addLast(-1);
        System.out.println(ll.get(1));
    }
}