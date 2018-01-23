package proj.me.usecase.executor;

/**
 * Created by root on 23/1/18.
 */

public interface ForegroundExecutor {
    void post(final Runnable runnable);
}
