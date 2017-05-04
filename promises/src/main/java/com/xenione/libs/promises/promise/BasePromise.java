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

package com.xenione.libs.promises.promise;
import android.util.Log;

import com.xenione.libs.promises.promise.listeners.AlwaysListener;
import com.xenione.libs.promises.promise.listeners.AlwaysResult;
import com.xenione.libs.promises.promise.listeners.CancelListener;
import com.xenione.libs.promises.promise.listeners.DoneListener;
import com.xenione.libs.promises.promise.listeners.FailListener;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class BasePromise<R> implements Promise<R> {
	
	protected volatile State state = State.PENDING;

	protected final List<DoneListener<R>> doneCallbacks = new CopyOnWriteArrayList<>();
	protected final List<FailListener> failCallbacks = new CopyOnWriteArrayList<>();
	protected final List<CancelListener> cancelCallbacks = new CopyOnWriteArrayList<>();
	protected final List<AlwaysListener<R>> alwaysCallbacks = new CopyOnWriteArrayList<>();

	protected R resolveResult;
	protected Throwable rejectResult;

	@Override
	public Promise<R> register(DoneListener<R> listener) {
		synchronized (this) {
			if (isResolved()) {
				dispatchDone(listener, resolveResult);
			} else {
				doneCallbacks.add(listener);
			}
		}
		return this;
	}

	@Override
	public Promise<R> register(AlwaysListener<R> listener) {
		synchronized (this) {
			if (isResolved()) {
				dispatchAlways(listener, new AlwaysResult<>(resolveResult, rejectResult));
			} else {
				alwaysCallbacks.add(listener);
			}
		}
		return this;
	}

	@Override
	public Promise<R> register(FailListener listener) {
		synchronized (this) {
			if (isRejected()) {
				dispatchFail(listener, rejectResult);
			} else {
				failCallbacks.add(listener);
			}
		}
		return this;
	}

	@Override
	public Promise<R> register(CancelListener listener) {
		synchronized (this) {
			if (isCanceled()) {
				dispatchCancel(listener);
			} else {
				cancelCallbacks.add(listener);
			}
		}
		return this;
	}

	public void dispatchDone(R resolved) {
		for (DoneListener<R> callback : doneCallbacks) {
			try {
				dispatchDone(callback, resolved);
			} catch (Exception e) {
				Log.e("BasePromise", "an uncaught exception occured in a DoneCallback " + e.getMessage());
			}
		}
		state = State.RESOLVED;
		doneCallbacks.clear();
	}
	
	protected void dispatchDone(DoneListener<R> callback, R resolved) {
		callback.onDone(resolved);
	}
	
	public void dispatchFail(Throwable rejected) {
		for (FailListener callback : failCallbacks) {
			try {
				dispatchFail(callback, rejected);
			} catch (Exception e) {
				Log.e("BasePromise", "an uncaught exception occured in a FailCallback " + e.getMessage());
			}
		}
		state = State.REJECTED;
		failCallbacks.clear();
	}
	
	protected void dispatchFail(FailListener listener, Throwable rejected) {
		listener.onFail(rejected);
	}

	public void dispatchCancel() {
		for (CancelListener callback : cancelCallbacks) {
			try {
				dispatchCancel(callback);
			} catch (Exception e) {
				Log.e("BasePromise", "an uncaught exception occured in a CancelCallback " + e.getMessage());
			}
		}
		state = State.CANCELED;
		cancelCallbacks.clear();
	}

	protected void dispatchCancel(CancelListener listener) {
		listener.onCancel();
	}

	public void dispatchAlways(AlwaysResult<R> result) {
		for (AlwaysListener<R> listener : alwaysCallbacks) {
			try {
				dispatchAlways(listener, result);
			} catch (Exception e) {
				Log.e("BasePromise", "an uncaught exception occured in a AlwaysCallback " + e.getMessage());
			}
		}
		state = State.RESOLVED;
		alwaysCallbacks.clear();
	}

	protected void dispatchAlways(AlwaysListener<R> listener, AlwaysResult<R> result) {
		listener.onAlways(result);
	}

	@Override
	public boolean isPending() {
		return state == State.PENDING;
	}

	@Override
	public boolean isResolved() {
		return state == State.RESOLVED;
	}

	@Override
	public boolean isRejected() {
		return state == State.REJECTED;
	}

	@Override
	public boolean isCanceled() {
		return state == State.CANCELED;
	}
}
