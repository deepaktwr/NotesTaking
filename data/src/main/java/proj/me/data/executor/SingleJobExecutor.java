package proj.me.data.executor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import proj.me.usecase.executor.BackgroundExecutor;

/**
 * Created by root on 14/8/17.
 */

public class SingleJobExecutor implements BackgroundExecutor {
    private static final int INITIAL_POOL_SIZE = 1;
    private static final int MAX_POOL_SIZE = 1;
    private static final int KEEP_ALIVE_TIME = 10;
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;

    private static final class ExecutorInstanceHolder{
        private static final SingleJobExecutor INSTANCE = new SingleJobExecutor();
    }

    public static SingleJobExecutor getInstance(){
        return ExecutorInstanceHolder.INSTANCE;
    }

    //private Map<Integer, Future<?>> runningFutures;
    private ThreadPoolExecutor threadPoolExecutor;

    private SingleJobExecutor(){
        BlockingQueue<Runnable> wordQueue = new LinkedBlockingQueue<>();
        threadPoolExecutor = new ThreadPoolExecutor(INITIAL_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT, wordQueue);
        //runningFutures = new HashMap<>();
    }

    @Override
    public void execute(Runnable runnable, int taskId) {
        /*if(runningFutures.containsKey(taskId)){
            Future<?> taskFuture = runningFutures.get(taskId);
            if(!taskFuture.isCancelled() && !taskFuture.isDone()) taskFuture.cancel(true);
        }
        runningFutures.put(taskId, threadPoolExecutor.submit(runnable));*/
        threadPoolExecutor.execute(runnable);
    }

    @Override
    public void finish(int taskId) {
        //runningFutures.remove(taskId);
    }
}
