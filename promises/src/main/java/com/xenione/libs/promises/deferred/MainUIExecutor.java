package com.xenione.libs.promises.deferred;
import android.os.Handler;
import android.os.Looper;
import java.util.concurrent.Executor;

/**
 * Created by esenent on 04/05/2017.
 */

public class MainUIExecutor implements Executor {

	private Handler handler;

	public MainUIExecutor() {
		handler = new Handler(Looper.getMainLooper());
	}
	
	@Override
	public void execute(Runnable command) {
		handler.post(command);
	}
}
