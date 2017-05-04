package com.xenione.promise;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.xenione.libs.promises.deferred.BaseDeferred;
import com.xenione.libs.promises.deferred.Deferred;
import com.xenione.libs.promises.deferred.DeferredAsyncTask;
import com.xenione.libs.promises.promise.MultiResult;
import com.xenione.libs.promises.promise.OneResult;
import com.xenione.libs.promises.promise.PromiseMaker;
import com.xenione.libs.promises.promise.listeners.AlwaysResult;

import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

	private PromiseMaker<Void, Integer> promiseMaker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Deferred<Void, Integer> deferred1 = new BaseDeferred<Void, Integer>() {

			@Override
			protected void doTask(Void params) {
				Log.i("MainActivity", "deferred1#doTask(...)");
				this.resolve(1);
			}

		};

		Deferred<Integer, Integer> deferred2 = new BaseDeferred<Integer, Integer>() {

			@Override
			protected void doTask(Integer params) {
				Log.i("MainActivity", "deferred2#doTask(...)");
				this.resolve(params + 1);
			}
		};

		Deferred<Integer, Integer> deferred3 = new BaseDeferred<Integer, Integer>() {

			@Override
			protected void doTask(Integer params) {
				Log.i("MainActivity", "deferred3#doTask(...)");
				this.resolve(params + 1);
			}
		};

		Deferred<Integer, OneResult> deferred4a = new BaseDeferred<Integer, OneResult>() {

			@Override
			protected void doTask(Integer params) {
				Log.i("MainActivity", "deferred4a#doTask(...)");
				this.always(AlwaysResult.ok(new OneResult(1, this.promise(), null)));
			}
		};

		Deferred<Integer, OneResult> deferred4b = new BaseDeferred<Integer, OneResult>() {

			@Override
			protected void doTask(Integer params) {
				Log.i("MainActivity", "deferred4b#doTask(...)");
				this.always(AlwaysResult.<OneResult>error(new Exception()));
			}
		};

		Deferred<MultiResult, Void> deferred5 = new BaseDeferred<MultiResult, Void>() {

			@Override
			protected void doTask(MultiResult params) {
				Log.i("MainActivity", "deferred5#doTask(...)");
				this.resolve(null);
			}
		};

		DeferredAsyncTask<Void, Void> async1 = new DeferredAsyncTask<Void, Void>() {
			@Override
			protected Void doInBackgroundSafe(Void params) throws Exception {
				Log.i("MainActivity", "deferred async1#doTask(...)");
				return null;
			}
		};

		DeferredAsyncTask<Void, Void> async2 = new DeferredAsyncTask<Void, Void>() {
			@Override
			protected Void doInBackgroundSafe(Void params) throws Exception {
				Thread.sleep(5 * 1000);
				Log.i("MainActivity", "deferred async2#doTask(...)");
				return null;
			}
		};

		DeferredAsyncTask<Void, Void> async3 = new DeferredAsyncTask<Void, Void>() {
			@Override
			protected Void doInBackgroundSafe(Void params) throws Exception {
				Log.i("MainActivity", "deferred async3#doTask(...)");
				return null;
			}
		};

		DeferredAsyncTask<Integer, Integer> async0 = new DeferredAsyncTask<Integer, Integer>() {
			@Override
			protected Integer doInBackgroundSafe(Integer params) throws Exception {
				Thread.sleep(5 * 1000);
				Log.i("MainActivity", "deferred async0#doTask(...)");
				return 1;
			}
		};

		promiseMaker = PromiseMaker.Impl.make(deferred1).onExecutor(Executors.newFixedThreadPool(4));

		promiseMaker.then(async0).then(deferred3).when(deferred4a, deferred4b).then(deferred5).then(async1).then(async2).then(async3);

		promiseMaker.start(null);
	}
}
