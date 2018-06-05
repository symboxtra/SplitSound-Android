package splitsound.com.net;

import android.util.Pair;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by Neel on 5/14/2018.
 */

public class Buffer<T>
{
    private ArrayList<Pair<T, Integer>> bufferList = new ArrayList<Pair<T, Integer>>();
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();

    public void add(T val)
    {
        writeLock.lock();
        try
        {
            bufferList.add(new Pair<T, Integer>(val, 0));
        }
        finally
        {
            writeLock.unlock();
        }
    }

    public void add(T val, int ssrc)
    {
        writeLock.lock();
        try
        {
            bufferList.add(new Pair<T, Integer>(val, ssrc));
        }
        finally
        {
            writeLock.unlock();
        }
    }

    public Pair<T, Integer> get(int index)
    {
        readLock.lock();
        Pair<T, Integer> temp;
        try
        {
            temp = bufferList.get(index);
        }
        finally
        {
            readLock.unlock();
        }
        return temp;
    }

    public Pair<T, Integer> getNext()
    {
        readLock.lock();
        Pair<T, Integer> temp;
        try
        {
            temp = bufferList.get(0);
            bufferList.remove(0);
        }
        finally
        {
            readLock.unlock();
        }
        return temp;
    }

    public boolean exists(T val)
    {
        return bufferList.contains(val);
    }

    public String toString()
    {
        return bufferList.size() + " " + bufferList.toString();
    }

    public boolean isEmpty()
    {
        return bufferList.isEmpty();
    }
}
