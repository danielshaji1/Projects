package adts;

import java.util.Iterator;

/**
 * A wrapper class for the IStack interface.
 * This class delegates all operations to an underlying stack implementation.
 * <p>
 * By default, this wrapper uses the array-based implementation (ArrayStack).
 * To use a linked-list–based implementation instead, simply comment out the current assignment
 * and uncomment the alternative.
 *
 * @param <E> the type of elements stored in this stack
 */

public class CustomStack<E> implements IStack<E>  {
    private IStack<E> stackImpl;

    public CustomStack(IStack<E> stackImpl) {
        this.stackImpl = stackImpl;
    }

    public void setDelegate(IStack<E> newImpl) {
        this.stackImpl = newImpl;
    }

    @Override
    public void push(E element) {
        stackImpl.push(element);
    }

    @Override
    public E pop() {
        return stackImpl.pop();
    }

    @Override
    public E peek() {
        return stackImpl.peek();
    }

    @Override
    public boolean isEmpty() {
        return stackImpl.isEmpty();
    }

    @Override
    public int size() {
        return stackImpl.size();
    }

    @Override
    public void clear() {
        stackImpl.clear();
    }

    @Override
    public Iterator<E> iterator() {
        // Delegate iterator if the underlying implementation is Iterable
        if (stackImpl != null) {
            return stackImpl.iterator();
        }
        throw new UnsupportedOperationException("Underlying stack is not iterable");
    }
}