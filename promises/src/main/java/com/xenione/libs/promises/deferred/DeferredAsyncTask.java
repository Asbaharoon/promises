/*
 * Copyright 2017 Eugeni Josep Senent i Gabriel
 * This is a derivative work of an open-source project jdeferred(https://github.com/jdeferred/jdeferred)
 * by Ray Tsang("saturnism") as major contributor.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xenione.libs.promises.deferred;

import android.os.AsyncTask;

import java.util.concurrent.CancellationException;
import java.util.concurrent.Executor;

public abstract class DeferredAsyncTask<P, R> extends AbsDeferred<P, R> {

	private Throwable throwable;

	protected AsyncTask<P, Void, R> asyncTask = new AsyncTask<P, Void, R>() {

		@Override
		protected R doInBackground(P... params) {
			return DeferredAsyncTask.this.doInBackground(params[0]);
		}

		@Override
		protected void onPostExecute(R result) {
			DeferredAsyncTask.this.onPostExecute(result);
		}

		@Override
		protected void onCancelled() {
			DeferredAsyncTask.this.onCancelled();
		}

		@Override
		protected void onCancelled(R result) {
			DeferredAsyncTask.this.onCancelled(result);
		}
	};

	
	protected final void onCancelled() {
		this.reject(new CancellationException());
	}
	
	protected final void onCancelled(R result) {
		this.reject(new CancellationException());
	}

	protected final void onPostExecute(R result) {
		if (throwable != null) {
			this.reject(throwable);
		} else {
			this.resolve(result);
		}
	}

	protected final R doInBackground(P params) {
		try {
			return doInBackgroundSafe(params);
		} catch (Throwable e) {
			throwable = e;
			return null;
		}
	}

	@Override
	public Deferred<P, R> startOnExecutor(Executor executor, P params) {
		doTask(executor, params);
		return this;
	}

	@Override
	public Deferred<P, R> start(P params) {
		doTask(AsyncTask.SERIAL_EXECUTOR, params);
		return this;
	}

	@Override
	protected void doTask(Executor executor, P params) {
		asyncTask.executeOnExecutor(executor, params);
	}

	@Override
	public Deferred<P, R> cancel() {
		asyncTask.cancel(true);
		return this;
	}

	protected abstract R doInBackgroundSafe(P params) throws Exception;
}
