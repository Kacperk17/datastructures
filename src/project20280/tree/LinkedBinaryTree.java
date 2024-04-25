package project20280.tree;

import project20280.interfaces.Position;

import java.util.ArrayList;

/**
 * Concrete implementation of a binary tree using a node-based, linked
 * structure.
 */
public class LinkedBinaryTree<E> extends AbstractBinaryTree<E> {


    static java.util.Random rnd = new java.util.Random();
    /**
     * The root of the binary tree
     */
    protected Node<E> root = null; // root of the tree

    // LinkedBinaryTree instance variables
    /**
     * The number of nodes in the binary tree
     */
    private int size = 0; // number of nodes in the tree

    /**
     * Constructs an empty binary tree.
     */
    public LinkedBinaryTree() {
    } // constructs an empty binary tree

    // constructor

    public static LinkedBinaryTree<Integer> makeRandom(int n) {
        LinkedBinaryTree<Integer> bt = new LinkedBinaryTree<>();
        bt.root = randomTree(null, 1, n);
        return bt;
    }

    // nonpublic utility

    public static <T extends Integer> Node<T> randomTree(Node<T> parent, Integer first, Integer last) {
        if (first > last) return null;
        else {
            Integer treeSize = last - first + 1;
            Integer leftCount = rnd.nextInt(treeSize);
            Integer rightCount = treeSize - leftCount - 1;
            Node<T> root = new Node<T>((T) ((Integer) (first + leftCount)), parent, null, null);
            root.setLeft(randomTree(root, first, first + leftCount - 1));
            root.setRight(randomTree(root, first + leftCount + 1, last));
            return root;
        }
    }

    // accessor methods (not already implemented in AbstractBinaryTree)

    public static void main(String [] args) {
        LinkedBinaryTree<Integer> bt1 = new LinkedBinaryTree<>();
        LinkedBinaryTree<Integer> bt2 = new LinkedBinaryTree<>();
        LinkedBinaryTree<Integer> bt3 = new LinkedBinaryTree<>();

        bt1.addRoot(2);
        bt2.addRoot(3);
        bt2.addLeft(bt1.root, 4);
        bt2.addRight(bt1.root, 5);

        bt3.addRoot(6);
        bt3.addLeft(bt3.root, 7);
        bt3.addRight(bt3.root, 8);

        bt1.attach(bt1.root, bt2, bt3);

        System.out.println(bt1);
    }

    /**
     * Factory function to create a new node storing element e.
     */
    protected Node<E> createNode(E e, Node<E> parent, Node<E> left, Node<E> right) {
        return new Node<E>(e, parent, left, right);
    }

    /**
     * Verifies that a Position belongs to the appropriate class, and is not one
     * that has been previously removed. Note that our current implementation does
     * not actually verify that the position belongs to this particular list
     * instance.
     *
     * @param p a Position (that should belong to this tree)
     * @return the underlying Node instance for the position
     * @throws IllegalArgumentException if an invalid position is detected
     */
    protected Node<E> validate(Position<E> p) throws IllegalArgumentException {
        if (!(p instanceof Node)) throw new IllegalArgumentException("Not valid position type");
        Node<E> node = (Node<E>) p; // safe cast
        if (node.getParent() == node) // our convention for defunct node
            throw new IllegalArgumentException("p is no longer in the tree");
        return node;
    }

    /**
     * Returns the number of nodes in the tree.
     *
     * @return number of nodes in the tree
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Returns the root Position of the tree (or null if tree is empty).
     *
     * @return root Position of the tree (or null if tree is empty)
     */
    @Override
    public Position<E> root() {
        return root;
    }

    // update methods supported by this class

    /**
     * Returns the Position of p's parent (or null if p is root).
     *
     * @param p A valid Position within the tree
     * @return Position of p's parent (or null if p is root)
     * @throws IllegalArgumentException if p is not a valid Position for this tree.
     */
    @Override
    public Position<E> parent(Position<E> p) throws IllegalArgumentException {
        return validate(p).getParent();
    }

    /**
     * Returns the Position of p's left child (or null if no child exists).
     *
     * @param p A valid Position within the tree
     * @return the Position of the left child (or null if no child exists)
     * @throws IllegalArgumentException if p is not a valid Position for this tree
     */
    @Override
    public Position<E> left(Position<E> p) throws IllegalArgumentException {
        return validate(p).getLeft();
    }



    /**
     * Returns the Position of p's right child (or null if no child exists).
     *
     * @param p A valid Position within the tree
     * @return the Position of the right child (or null if no child exists)
     * @throws IllegalArgumentException if p is not a valid Position for this tree
     */
    @Override
    public Position<E> right(Position<E> p) throws IllegalArgumentException {
        return validate(p).getRight();
    }

    /**
     * Places element e at the root of an empty tree and returns its new Position.
     *
     * @param e the new element
     * @return the Position of the new element
     * @throws IllegalStateException if the tree is not empty
     */
    public Position<E> addRoot(E e) throws IllegalStateException {

        root = new Node(e, null, null, null);
        size++;
        return root;
    }

    public void insert(E e) {

        Node<E> currNode = root;

       while(currNode.hasRightChild()) {
           currNode = currNode.getLeft();
       }

       createNode(e, currNode, null, null);


    }

    // recursively add Nodes to binary tree in proper position
    private Node<E> addRecursive(Node<E> p, E e) {

        if(!p.hasLeftChild()) {
            createNode(e, p, null, null);
        }

        if(p.hasRightChild()) {
            addRecursive(p.getLeft(), e);
        }

        return null;
    }

    /**
     * Creates a new left child of Position p storing element e and returns its
     * Position.
     *
     * @param p the Position to the left of which the new element is inserted
     * @param e the new element
     * @return the Position of the new element
     * @throws IllegalArgumentException if p is not a valid Position for this tree
     * @throws IllegalArgumentException if p already has a left child
     */
    public Position<E> addLeft(Position<E> p, E e) throws IllegalArgumentException {

        Node<E> parentNode = validate(p);
        Node<E> newNode = new Node(e, parentNode, null, null);
        parentNode.setLeft(newNode);
        size++;

        return parentNode;
    }

    /**
     * Creates a new right child of Position p storing element e and returns its
     * Position.
     *
     * @param p the Position to the right of which the new element is inserted
     * @param e the new element
     * @return the Position of the new element
     * @throws IllegalArgumentException if p is not a valid Position for this tree.
     * @throws IllegalArgumentException if p already has a right child
     */
    public Position<E> addRight(Position<E> p, E e) throws IllegalArgumentException {

        Node<E> parentNode = validate(p);
        Node<E> newNode = new Node(e, parentNode, null, null);
        parentNode.setRight(newNode);
        size++;

        return parentNode;
    }

    /**
     * Replaces the element at Position p with element e and returns the replaced
     * element.
     *
     * @param p the relevant Position
     * @param e the new element
     * @return the replaced element
     * @throws IllegalArgumentException if p is not a valid Position for this tree.
     */
    public E set(Position<E> p, E e) throws IllegalArgumentException {

        E replacedElement = validate(p).getElement();
        validate(p).setElement(e);

        return replacedElement;
    }

    /**
     * Attaches trees t1 and t2, respectively, as the left and right subtree of the
     * leaf Position p. As a side effect, t1 and t2 are set to empty trees.
     *
     * @param p  a leaf of the tree
     * @param t1 an independent tree whose structure becomes the left child of p
     * @param t2 an independent tree whose structure becomes the right child of p
     * @throws IllegalArgumentException if p is not a valid Position for this tree
     * @throws IllegalArgumentException if p is not a leaf
     */
    public void attach(Position<E> p, LinkedBinaryTree<E> t1, LinkedBinaryTree<E> t2) throws IllegalArgumentException {

        Node<E> leaf = validate(p);
        Node<E> leftNode = t1.root;
        Node<E> rightNode = t2.root;

        leaf.setLeft(leftNode);
        leaf.setRight(rightNode);



    }

    /**
     * Removes the node at Position p and replaces it with its child, if any.
     *
     * @param p the relevant Position
     * @return element that was removed
     * @throws IllegalArgumentException if p is not a valid Position for this tree.
     * @throws IllegalArgumentException if p has two children.
     */
    public E remove(Position<E> p) throws IllegalArgumentException {

        Node<E> nodeToRemove = validate(p);
        if(nodeToRemove.getLeft() != null && nodeToRemove.getRight() != null) { // if the node to remove has 2 children
            throw new IllegalArgumentException("Node to be removed cannot have 2 children");
        }
        if(!(p instanceof LinkedBinaryTree.Node<E>) ) {
            throw new IllegalArgumentException("p must be a node");
        }

        Node<E> parentNode = nodeToRemove.getParent();
        E elementToRemove = nodeToRemove.getElement();

        // 3 scenarios
        // - Node to be removed is root
        // - Node to be removed has no children
        // - Node to be removed has 1 child

        // if node is root
        if(nodeToRemove.getParent() == null) {
            nodeToRemove = null;
        }
        // if node has no children
        else if(nodeToRemove.getLeft() == null && nodeToRemove.getRight() == null) {
            // set parent node to having no children
            // if node to remove is on left
            if(nodeToRemove.isLeft()) {
                parentNode.setLeft(null);
            }
            else {
                parentNode.setRight(null);
            }

            // set node to remove to have no parent
            nodeToRemove.setParent(null);
        }
        else { // if node has one child

            // check if node to be removed has a left child
            if(nodeToRemove.hasLeftChild()) {
                Node<E> newNode = nodeToRemove.getLeft();

                // make the new node child of parent node
                // if node to remove is left
                if(nodeToRemove.isLeft()) {
                    parentNode.setLeft(newNode);
                }
                else {
                    parentNode.setRight(newNode);
                }

            }
            else { // new node must be the right child
                Node<E> newNode = nodeToRemove.getRight();

                // make the new node child of parent node
                // if node to remove is left
                if(nodeToRemove.isLeft()) {
                    parentNode.setLeft(newNode);
                }
                else {
                    parentNode.setRight(newNode);
                }

                nodeToRemove.setParent(null);
            }

        }

        size--;
        return elementToRemove;
    }

    public String toString() {
        return positions().toString();
    }

    public void createLevelOrder(ArrayList<E> l) {


        root = createLevelOrderHelper(l, root, 0);
    }

    // method that inserts to the left or right child of a given node depending on element
    private void insert_bst(Node<E> n, E e) {

        Integer element = (Integer) e;

        // if the new element is less than node element
        if(element <= (Integer) n.getElement()) {
            n.setLeft(new Node(e, n, null, null));
        }

        // if new element is greater than node element
        else {
            n.setRight(new Node(e, n, null, null));
        }
    }

    public void ordered_insert(E e) {
        // Base Case: If tree is empty
        if(root == null) {
            addRoot(e);
        }

        else { // if tree is not empty

            Node<E> currNode = root; // start at root
            Integer element = (Integer) e;


            // while current node is not null
            while(currNode != null) {

                // if element is less than current node (we want to insert it somewhere on left)
                if(element <= (Integer) currNode.getElement()) {

                    // if current node has a left child
                    if(currNode.hasLeftChild()) {
                        currNode = currNode.getLeft();
                    }
                    else {
                        currNode.setLeft(new Node(e, currNode, null, null));
                        break;
                    }

                }

                // if element is greater than current node (we want to insert it somewhere on the right)
                else {

                    // if current node has a right child
                    if(currNode.hasRightChild()) {
                        currNode = currNode.getRight();
                    }
                    else {
                        currNode.setRight(new Node(e, currNode, null, null));
                        break;
                    }
                }

            }


        }


    }

    private Node<E> createLevelOrderHelper(java.util.ArrayList<E> l, Node<E> p, int i) {

        if(i < l.size()) {
            Node<E> n = createNode(l.get(i), p, null, null);

            n.left = createLevelOrderHelper(l, n.left, 2* i + 1);
            n.right = createLevelOrderHelper(l, n.right, 2 * i + 2);
            ++size;
            return n;
        }

        return p;
    }

    public void createLevelOrder(E[] arr) {
        root = createLevelOrderHelper(arr, root, 0);
    }

    private Node<E> createLevelOrderHelper(E[] arr, Node<E> p, int i) {

        if(i < arr.length) {
            Node<E> n = createNode(arr[i], p, null, null);
            n.left = createLevelOrderHelper(arr, n.left, 2 * i + 1);
            n.right = createLevelOrderHelper(arr, n.right, 2 * i + 2);
            ++size;
            return n;
        }

        return p;
    }


    public String toBinaryTreeString() {
        BinaryTreePrinter<E> btp = new BinaryTreePrinter<>(this);
        return btp.print();
    }

    // method which creates a binary tree from a given inorder and preorder traversal
    public void construct(E[] inorder, E[] preorder) {

        root = constructHelper(inorder, preorder, 0, preorder.length, 0, null);
    }

    // construct helper
    public Node<E> constructHelper(E[] inorder, E[] preorder, int startIndex, int endIndex, int preIndex, Node<E> parent) {

        if(startIndex > endIndex || preIndex >= preorder.length) {
            return null;
        }
        int rootIndex = findIndex(inorder, preorder[preIndex]);
        Node<E> currNode = new Node(inorder[rootIndex], parent, null,null);

        currNode.left = constructHelper(inorder, preorder, startIndex, rootIndex-1, preIndex + 1, currNode);

        currNode.right = constructHelper(inorder, preorder, rootIndex+1, endIndex, preIndex + rootIndex - startIndex + 1, currNode);

        return currNode;
    }

    // find index of element in inorder
    private int findIndex(E[] inorder, E element) {
        int index = 0;

        for(E elementInorder : inorder) {

            if(elementInorder.equals(element)) {
                return index;
            }

            index++;
        }

        return index;
    }

    // method that finds path to a given node and returns directions in a list
    public String pathToNode(E e) {
        ArrayList<String> path = new ArrayList<>();

        pathToNodeHelper(root, e, path);

        StringBuilder sb = new StringBuilder();

        for(String dir : path) {
            if(dir.equals("0")) {
                sb.append('0');
            }
            else {
                sb.append('1');
            }
        }

        return sb.reverse().toString();


    }

    // path helper method that recursively traverses the tree and keeps track of the path
    private boolean pathToNodeHelper(Node<E> currNode, E e, ArrayList<String> path) {

        if (currNode == null) {
            return false;
        }

        try {
            if(currNode.getElement().equals(e)) {
                return true;
            }
        } catch (Exception ex) {}

        if(pathToNodeHelper(currNode.left, e, path)) {
            path.add("0");
            return true;
        }

        if(pathToNodeHelper(currNode.right, e, path)) {
            path.add("1");
            return true;
        }
        return false;

    }

    /**
     * Nested static class for a binary tree node.
     */
    protected static class Node<E> implements Position<E> {
        private E element;
        private Node<E> left, right, parent;

        public Node(E e, Node<E> p, Node<E> l, Node<E> r) {
            element = e;
            left = l;
            right = r;
            parent = p;
        }

        // accessor
        public E getElement() {
            return element;
        }

        // modifiers
        public void setElement(E e) {
            element = e;
        }

        public Node<E> getLeft() {
            return left;
        }

        public void setLeft(Node<E> n) {
            left = n;
        }

        public Node<E> getRight() {
            return right;
        }

        public void setRight(Node<E> n) {
            right = n;
        }

        public Node<E> getParent() {
            return parent;
        }

        public void setParent(Node<E> n) {
            parent = n;
        }

        // method that returns true if a node is the left of its parent node
        public boolean isLeft() {
            // check if there is a parent
            if(this.getParent() == null) {
                return false;
            }
            else {
                return parent.getLeft() == this;
            }

        }

        // method that returns true if a node is the right of its parent node
        public boolean isRight() {
            return !isLeft();
        }

        // method that returns true if a node has a left child node
        public boolean hasLeftChild() {
            return this.getLeft() != null;
        }
        // method that returns true if a node has a left child node
        public boolean hasRightChild() {
            return this.getRight() != null;
        }

        // method that returns true if a node is a leaf
        public boolean isLeaf() {
            return (!(this.hasLeftChild() && this.hasRightChild()));
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            if (element == null) {
                sb.append("\u29B0");
            } else {
                sb.append(element);
            }

            return sb.toString();
        }
    }
}
