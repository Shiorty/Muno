package at.htlkaindorf.ahif18.data;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Spliterator;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

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

    public void push(T element)
    {
        lock.lock();
        queue.add(element);
        notEmpty.signalAll();
        lock.unlock();
    }

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