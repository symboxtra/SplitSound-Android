package splitsound.com.net;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by Neel on 5/14/2018.
 */

public class Buffer<T>
{
    private ArrayList<T> bufferList = new ArrayList<T>();
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();

    public void add(T val)
    {
        writeLock.lock();
        try
        {
            bufferList.add(val);
        }
        finally
        {
            writeLock.unlock();
        }
    }

    public T get(int index)
    {
        readLock.lock();
        T temp;
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

    public T remove()
    {
        readLock.lock();
        T temp;
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
}
