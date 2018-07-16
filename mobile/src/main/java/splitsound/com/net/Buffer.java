package splitsound.com.net;

import android.util.Pair;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Thread safe buffer list
 *
 * @version 0.0.1
 * @author Neel
 */
public class Buffer<T>
{
    // Storage structure
    private ArrayList<Pair<T, Integer>> bufferList = new ArrayList<Pair<T, Integer>>();

    // Thread lock instances
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();

    /**
     * Adds the value at the end of the thread safe buffer
     * with default ssrc to 0
     * @param val Value to be stored
     */
    public void add(T val)
    {
        writeLock.lock();
        try {
            bufferList.add(new Pair<T, Integer>(val, 0));
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * Adds the value at the end of the thread safe buffer
     * mapping the provided ssrc value
     *
     * @param val
     * @param ssrc
     */
    public void add(T val, int ssrc)
    {
        writeLock.lock();
        try {
            bufferList.add(new Pair<T, Integer>(val, ssrc));
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * Gets the value from the thread safe buffer
     * at the specified index
     *
     * @param index Position where the value exists
     * @return Value at the provided position
     */
    public Pair<T, Integer> get(int index)
    {
        readLock.lock();
        Pair<T, Integer> temp;
        try {
            temp = bufferList.get(index);
        } finally {
            readLock.unlock();
        }
        return temp;
    }

    /**
     * Gets the next value (first value) in the
     * thread safe buffer/queue and deletes the pair
     *
     * @return next value in the queue (first-value)
     */
    public Pair<T, Integer> getNext()
    {
        readLock.lock();
        Pair<T, Integer> temp;
        try {
            temp = bufferList.get(0);
            bufferList.remove(0);
        } finally {
            readLock.unlock();
        }
        return temp;
    }

    /**
     * Checks if the provided value exists in the thread safe buffer
     *
     * @param val Value that needs to be checked in the bufferlist
     * @return boolean based on whether value exists
     */
    public boolean exists(T val)
    {
        return bufferList.contains(val);
    }

    /**
     * Converts the buffer into human readable
     * list for debugging purposes
     *
     * @return human-readable string with info of the buffer
     */
    public String toString()
    {
        return bufferList.size() + " " + bufferList.toString();
    }

    /**
     * Checks if bufferlist is empty
     *
     * @return boolean based on empty or not empty
     */
    public boolean isEmpty()
    {
        return bufferList.isEmpty();
    }
}
