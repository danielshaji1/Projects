package adts;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayStack<E> implements IStack<E> {

    private E[] data;
    private int top;  // Points to the next available slot

    @SuppressWarnings("unchecked")
    public ArrayStack() {
        data = (E[]) new Object[10];
        top = 0;
    }

    @Override
    public void push(E element) {
        if (top == data.length) {
            resize(data.length * 2); // Grow if full
        }
        data[top++] = element;
    }

    @Override
    public E pop() {
        if (isEmpty()) {
            throw new NoSuchElementException("Stack is empty.");
        }
        E element = data[--top];
        data[top] = null; // Avoid memory leak

        // Shrink array if too empty (but don’t shrink below 10)
        if (top > 0 && top == data.length / 4 && data.length > 10) {
            resize(data.length / 2);
        }

        return element;
    }

    @Override
    public E peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("Stack is empty.");
        }
        return data[top - 1];
    }

    @Override
    public boolean isEmpty() {
        return top == 0;
    }

    @Override
    public int size() {
        return top;
    }

    @SuppressWarnings("unchecked")
    private void resize(int capacity) {
        E[] newData = (E[]) new Object[capacity];
        for (int i = 0; i < top; i++) {
            newData[i] = data[i];
        }
        data = newData;
    }

    @Override
    public void clear() {
        for (int i = 0; i < top; i++) {
            data[i] = null;
        }
        top = 0;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < top;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return data[index++];
            }
        };
    }
}
