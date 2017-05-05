package com.xenione.promise;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.xenione.libs.promises.deferred.BaseDeferred;
import com.xenione.libs.promises.deferred.Deferred;
import com.xenione.libs.promises.deferred.DeferredAsyncTask;
import com.xenione.libs.promises.deferred.MainUIExecutor;
import com.xenione.libs.promises.promise.MultiResult;
import com.xenione.libs.promises.promise.OneResult;
import com.xenione.libs.promises.promise.PromiseMaker;
import com.xenione.libs.promises.promise.TreePromise;
import com.xenione.libs.promises.promise.listeners.AlwaysResult;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Deferred<Void, Integer> deferred1 = new BaseDeferred<Void, Integer>() {

			@Override
			protected void doTask(Executor executor, Void params) {
				Log.i("MainActivity", "deferred1#doTask(...)");
				this.resolve(1);
			}

		};

		Deferred<Integer, Integer> deferred2 = new BaseDeferred<Integer, Integer>() {

			@Override
			protected void doTask(Executor executor,Integer params) {
				Log.i("MainActivity", "deferred2#doTask(...)");
				this.resolve(params + 1);
			}
		};

		Deferred<Integer, Integer> deferred3 = new BaseDeferred<Integer, Integer>() {

			@Override
			protected void doTask(Executor executor,Integer params) {
				Log.i("MainActivity", "deferred3#doTask(...)");
				this.resolve(params + 1);
			}
		};

		Deferred<Integer, OneResult> deferred4a = new BaseDeferred<Integer, OneResult>() {

			@Override
			protected void doTask(Executor executor,Integer params) {
				Log.i("MainActivity", "deferred4a#doTask(...)");
				this.always(AlwaysResult.ok(new OneResult(1, this.promise(), null)));
			}
		};

		Deferred<Integer, OneResult> deferred4b = new BaseDeferred<Integer, OneResult>() {

			@Override
			protected void doTask(Executor executor,Integer params) {
				Log.i("MainActivity", "deferred4b#doTask(...)");
				this.always(AlwaysResult.<OneResult>error(new Exception()));
			}
		};

		Deferred<MultiResult, Void> deferred5 = new BaseDeferred<MultiResult, Void>() {

			@Override
			protected void doTask(Executor executor,MultiResult params) {
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

		Deferred<Void, Void> deferred10 = new BaseDeferred<Void, Void>() {

			@Override
			protected void doTask(Executor executor, Void params) {
				Log.i("MainActivity", "deferred10#doTask(...)");
				this.resolve(null);
			}
		};

		Deferred<Void, Void> deferred11 = new BaseDeferred<Void, Void>() {

			@Override
			protected void doTask(Executor executor, Void params) {
				Log.i("MainActivity", "deferred11#doTask(...)");
				this.resolve(null);
			}
		};

		Deferred<Void, OneResult> deferred12 = new BaseDeferred<Void, OneResult>() {

			@Override
			protected void doTask(Executor executor, Void params) {
				Log.i("MainActivity", "deferred12#doTask(...)");
				this.resolve(new OneResult(1, this.promise(), null));
			}
		};

		Deferred<OneResult, OneResult> deferred13 = new BaseDeferred<OneResult, OneResult>() {

			@Override
			protected void doTask(Executor executor, OneResult params) {
				Log.i("MainActivity", "deferred13#doTask(...)");
				this.resolve(new OneResult(1, this.promise(), null));
			}
		};

		Deferred<MultiResult, Void> deferred14 = new BaseDeferred<MultiResult, Void>() {

			@Override
			protected void doTask(Executor executor, MultiResult params) {
				Log.i("MainActivity", "deferred14#doTask(...)");
				this.always(null);
			}
		};

		PromiseMaker<Void, Void> promiseMaker3 = PromiseMaker.Impl.make(deferred10).onExecutor(new MainUIExecutor());

		PromiseMaker<Void, OneResult> promiseMaker3a = PromiseMaker.Impl.make(deferred12).onExecutor(new MainUIExecutor());

		promiseMaker3a.then(deferred13);

		PromiseMaker<Void, OneResult> promiseMaker3b = PromiseMaker.Impl.make(deferred12).onExecutor(new MainUIExecutor());

		promiseMaker3.when(promiseMaker3a, promiseMaker3b).then(deferred14);

		promiseMaker3.start(null);



/*		PromiseMaker<Void, Void> promiseMaker2 = PromiseMaker.Impl.make(deferred10).onExecutor(new MainUIExecutor());

		PromiseMaker<Void, Integer> promiseMaker1 = PromiseMaker.Impl.make(deferred1).onExecutor(Executors.newFixedThreadPool(4));

		promiseMaker2.then(deferred11);

		promiseMaker1.then(async0).then(deferred3).when(deferred4a, deferred4b).then(deferred5).then(async1).then(async2).then(async3).then(promiseMaker2);

		promiseMaker1.start(null);*/
	}
}
