import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.Condition;

/**
 * @class SimpleSemaphore
 *
 * @brief This class provides a simple counting semaphore
 *        implementation using Java a ReentrantLock and a
 *        ConditionObject.  It must implement both "Fair" and
 *        "NonFair" semaphore semantics, just liked Java Semaphores. 
 */
public class SimpleSemaphore {
    /**
     * Constructor initialize the data members.  
     */
    public SimpleSemaphore (int permits,
                            boolean fair)
    { 
        // TODO - you fill in here
    	counter = permits;
    	f = fair;
    	if(f){
    		waitList = new LinkedBlockingDeque<Thread>();
    	}
    }

    /**
     * Acquire one permit from the semaphore in a manner that can
     * be interrupted.
     */
    public void acquire() throws InterruptedException {
        // TODO - you fill in here
    	l.lockInterruptibly();
    	try{
    		if(!f){
	    		while(counter == 0){
	    			notAvailable.await();
	    		}
	    		counter--;
    		}else{
    			if(counter == 0){
    				waitList.add(Thread.currentThread());
    			}
    			while(counter == 0 || (!waitList.isEmpty() && waitList.getFirst() != Thread.currentThread())){
    				notAvailable.await();
    			}
    			counter--;
    			if(!waitList.isEmpty())
    				waitList.removeFirst();
    		}
    	}
    	finally{
    		l.unlock();
    	}
    }

    /**
     * Acquire one permit from the semaphore in a manner that
     * cannot be interrupted.
     */
    public void acquireUninterruptibly() {
        // TODO - you fill in here
    	l.lock();
    	try{
    		if(!f){
	    		while(counter == 0){
	    			notAvailable.awaitUninterruptibly();
	    		}
	    		counter--;
    		}else{
    			if(counter == 0){
    				waitList.add(Thread.currentThread());
    			}
    			while(counter == 0 || (!waitList.isEmpty() && waitList.getFirst() != Thread.currentThread())){
    				notAvailable.awaitUninterruptibly();
    			}
    			counter--;
    			if(!waitList.isEmpty())
    				waitList.removeFirst();
    		}
   	}
    	finally{
    		l.unlock();
    	}
    }

    /**
     * Return one permit to the semaphore.
     */
    void release() {
        // TODO - you fill in here
    	l.lock();
    	try{
    		counter++;
    		if (counter > 0) {
                notAvailable.signal();
            }
    	}
    	finally{
    		l.unlock();
    	}
    }

    /**
     * Define a ReentrantLock to protect the critical section.
     */
    // TODO - you fill in here
    private Lock l = new ReentrantLock();
    
    /**
     * Define a ConditionObject to wait while the number of
     * permits is 0.
     */
    // TODO - you fill in here
    private Condition notAvailable= l.newCondition();

    /**
     * Define a count of the number of available permits.
     */
    // TODO - you fill in here
    private int counter = 0;
    
    private boolean f;
    
    private LinkedBlockingDeque<Thread> waitList;
}

