package adts;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Array-based implementation of IQueue using a circular buffer.
 *
 * @param <E> the type of elements stored in this queue
 */
public class ArrayQueue<E> implements IQueue<E> {

    private E[] data;
    private int front;  // index of the first element
    private int size;   // number of elements

    @SuppressWarnings("unchecked")
    public ArrayQueue() {
        data = (E[]) new Object[10];
        front = 0;
        size = 0;
    }

    @Override
    public void enqueue(E element) {
        if (size == data.length) {
            resize(data.length * 2);
        }
        int avail = (front + size) % data.length;
        data[avail] = element;
        size++;
    }

    @Override
    public E dequeue() {
        if (isEmpty()) {
            throw new RuntimeException("Queue is empty.");
        }
        E element = data[front];
        data[front] = null; // avoid memory leak
        front = (front + 1) % data.length;
        size--;

        // Optional: shrink the array
        if (size > 0 && size == data.length / 4 && data.length > 10) {
            resize(data.length / 2);
        }

        return element;
    }

    @Override
    public E peek() {
        if (isEmpty()) {
            throw new RuntimeException("Queue is empty.");
        }
        return data[front];
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            data[(front + i) % data.length] = null;
        }
        front = 0;
        size = 0;
    }

    @SuppressWarnings("unchecked")
    private void resize(int capacity) {
        E[] newData = (E[]) new Object[capacity];
        for (int i = 0; i < size; i++) {
            newData[i] = data[(front + i) % data.length];
        }
        data = newData;
        front = 0;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int index = 0;
            @Override
            public boolean hasNext() {
                return index < size;
            }
            @Override
            public E next() {
                if (!hasNext()) throw new NoSuchElementException();
                E element = data[(front + index) % data.length];
                index++;
                return element;
            }
        };
    }
}
