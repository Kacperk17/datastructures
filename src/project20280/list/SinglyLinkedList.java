package project20280.list;

import project20280.interfaces.List;

import java.util.Iterator;

public class SinglyLinkedList<E> implements List<E> {

    private static class Node<E> {

        private E element;            // reference to the element stored at this node

        /**
         * A reference to the subsequent node in the list
         */
        private Node<E> next;         // reference to the subsequent node in the list

        /**
         * Creates a node with the given element and next node.
         *
         * @param e the element to be stored
         * @param n reference to a node that should follow the new node
         */
        public Node(E e, Node<E> n) {
            element = e;
            next = n;
        }

        // Accessor methods

        /**
         * Returns the element stored at the node.
         *
         * @return the element stored at the node
         */
        public E getElement() {
            return element;
        }

        /**
         * Returns the node that follows this one (or null if no such node).
         *
         * @return the following node
         */
        public Node<E> getNext() {
            return next;
        }

        // Modifier methods

        /**
         * Sets the node's next reference to point to Node n.
         *
         * @param n the node that should follow this one
         */
        public void setNext(Node<E> n) {
            next = n;
        }

    } //----------- end of nested Node class -----------

    /**
     * The head node of the list
     */
    private Node<E> head = null;               // head node of the list (or null if empty)


    /**
     * Number of nodes in the list
     */
    private int size = 0;                      // number of nodes in the list

    public SinglyLinkedList() {
    }              // constructs an initially empty list

    //@Override
    public int size() {


        return size;
    }

    //@Override
    public boolean isEmpty() {


        return head == null;
    }

    @Override
    public E get(int position) {

        if(isEmpty()) {
            return null;
        }
        else {
            // start at beginning of list
            Node<E> currentNode = head;

            // while next node is not null
            for(int i = 0; i < position; i++) {
                currentNode = currentNode.getNext(); // iterate
            }
            return currentNode.element;
        }
    }

    @Override
    public void add(int position, E e) {

        // if list is empty
        if(isEmpty()) {
            addFirst(e);
        }
        else { // if list is not empty

            if(position == 0) { // if we are asked to add first element
                addFirst(e);
            }
            else { // if we are asked to add at any other position
                Node<E> currentNode = head; // start at head
                Node<E> newNode = new Node(e, null);

                for (int i = 0; i < (position - 1); i++) { // get to node right before node we have to add
                    currentNode = currentNode.getNext();
                }

                // set next node of new node to next node of node we landed at
                newNode.setNext(currentNode.getNext());

                // set node we land at to new node
                currentNode.setNext(newNode);
                size++;

            }


        }
    }


    @Override
    public void addFirst(E e) {


        if(isEmpty()) { // if list is empty
            Node<E> newNode = new Node(e, null);
            head = newNode;
        }
        else { // if the list isnt empty

            Node<E> newNode = new Node(e, head);
            head = newNode;

        }

        size++;

    }

    @Override
    public void addLast(E e) {


        Node<E> newNode = new Node(e, null);

        if(isEmpty()) { // if the list is empty
            head = newNode;
        }
        else { // if list is not empty

            Node<E> currentNode = head;

            // while the next node is not null
            while (currentNode.getNext() != null) {
                // go to next node
                currentNode = currentNode.getNext();
            }

            // now that we are on last node, set next node to our new node
            currentNode.setNext(newNode);
        }

        size++;

    }

    @Override
    public E remove(int position) {


        // if list is empty
        if(isEmpty()) {
            return null;
        }
        else { // if list is not empty

            if(position == 0) { // if we are asked to remove first element
                removeFirst();
            }
            else { // if we are asked to remove any other element
                Node<E> currentNode = head; // start at head

                for (int i = 0; i < (position - 1); i++) { // get to node right before node we have to remove
                    currentNode = currentNode.getNext();
                }
                E elementToRemove = currentNode.getNext().element;

                // set next node of current node to 2 nodes down, bypassing the node we want to remove
                currentNode.setNext(currentNode.getNext().getNext());
                size--;
                return elementToRemove;

            }


        }

        return null;
    }

    @Override
    public E removeFirst() {


        if(isEmpty()) { // if the list is empty
            return null;
        }
        else { // if the list is not empty
            size--;
            E elementToRemove = head.element;
            head = head.getNext();
            return elementToRemove;
        }
    }

    @Override
    public E removeLast() {


        if(size == 1) { // if the list only has 1 element
            head = null;
        }

        // if the list is empty
        if(isEmpty()) {
            return null;
        }
        else { // if the list isnt empty
            size--;
            Node<E> currentNode = head;

            // while next node is NOT null
            while(currentNode.getNext() != null) {

                // If node after next node is null, i.e. if the next node is the last node
                if (currentNode.getNext().getNext() == null) {
                    E elementToRemove = currentNode.getNext().element;
                    currentNode.setNext(null);
                    return elementToRemove;

                }
                // go through list
                currentNode = currentNode.getNext();
            }

        }
        return null;

    }

    //@Override
    public Iterator<E> iterator() {
        return new SinglyLinkedListIterator<E>();
    }

    private class SinglyLinkedListIterator<E> implements Iterator<E> {
        Node<E> curr = (Node<E>) head;

        @Override
        public boolean hasNext() {
            return curr != null;
        }

        @Override
        public E next() {
            E res = curr.getElement();
            curr = curr.next;
            return res;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> curr = head;
        while (curr != null) {
            sb.append(curr.getElement());
            if (curr.getNext() != null)
                sb.append(", ");
            curr = curr.getNext();
        }
        sb.append("]");
        return sb.toString();
    }

    public static void main(String[] args) {
        SinglyLinkedList<Integer> ll = new SinglyLinkedList<Integer>();
        System.out.println("ll " + ll + " isEmpty: " + ll.isEmpty());
        //LinkedList<Integer> ll = new LinkedList<Integer>();

        ll.addFirst(0);
        ll.addFirst(1);
        ll.addFirst(2);
        ll.addFirst(3);
        ll.addFirst(4);
        ll.addLast(-1);
        //ll.removeLast();
        //ll.removeFirst();
        //System.out.println("I accept your apology");
        //ll.add(3, 2);
        System.out.println(ll);
        ll.remove(5);
        System.out.println(ll);

//        for(Integer i : ll) {
//            System.out.println(i);
//        }
        /*
        ll.addFirst(-100);
        ll.addLast(+100);
        System.out.println(ll);

        ll.removeFirst();
        ll.removeLast();
        System.out.println(ll);

        // Removes the item in the specified index
        ll.remove(2);
        System.out.println(ll);
        
         */
    }
}
