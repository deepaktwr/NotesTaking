package proj.me.usecase.executor;

/**
 * Created by root on 23/1/18.
 */

public interface BackgroundExecutor {
    void execute(final Runnable runnable, final int taskId);
    void finish(final int taskId);
}
