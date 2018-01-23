package proj.me.notetaking;

import android.os.Handler;
import android.os.Looper;

import proj.me.usecase.executor.ForegroundExecutor;

public class UIThread implements ForegroundExecutor {

    private static final class UIThreadInstanceHolder {
        private static UIThread INSTANCE = new UIThread();
    }

    public static UIThread getInstance() {
        return UIThreadInstanceHolder.INSTANCE;
    }

    private Handler handler;

    private UIThread() {
        handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void post(Runnable runnable) {
        handler.post(runnable);
    }
}