import java.util.Iterator;
import java.util.NoSuchElementException;

public class Queue<Item> implements Iterable<Item>{
    private Node first;
    private Node last;
    private int size;

    private class Node {
        Item item;
        Node next;
    }
    public Queue() {
        first = null;
        last = null;
        size = 0;
    }
    public boolean isEmpty() {
        return first == null;
    }
    public int size() {
        return size;
    }
    public void enqueue(Item item) {
        Node oldLast = last;
        last = new Node();
        last.item = item;
        last.next = null;
        if (isEmpty()) {
            first = last;
        }
        else {
            oldLast.next = last;
        }
        size++;
    }
    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue underflow");
        }
        Item item = first.item;
        first = first.next;
        size--;
        if (isEmpty()) {
            last = null;
        }
        return item;
    }
    public Iterator<Item> iterator() {
        return new QueueIterator();
    }
    private class QueueIterator implements Iterator<Item> {
        private Node current = first;
        public boolean hasNext() {
            return current != null;
        }
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("Queue underflow");
            }
            Item item = current.item;
            current = current.next;
            return item;
        }
    }
}
