package at.htlkaindorf.ahif18.data;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Implements a FiFo Queue that is thread save
 * @param <T> type of the Queue contents
 */
public class ConcurrentQueue<T>{

    private Lock lock;
    private Condition notEmpty;

    private Queue<T> queue;

    public ConcurrentQueue()
    {
        lock = new ReentrantLock();
        notEmpty = lock.newCondition();
        queue = new LinkedList<>();
    }

    /**
     * Add a item to the queue<br>
     * Notifies everyone waiting to pop()
     * @param element the new element
     */
    public void push(T element)
    {
        lock.lock();
        queue.add(element);
        notEmpty.signalAll();
        lock.unlock();
    }

    /**
     * Gets the first element of the list<br>
     * Waits for another thread to add an item, if no element is present
     * @return the next element in the list
     * @throws InterruptedException when the thread is interrupted
     */
    public T pop() throws InterruptedException
    {
        lock.lock();

        while(queue.isEmpty()){
            notEmpty.await();
        }

        T element = queue.poll();

        lock.unlock();

        return element;
    }
}