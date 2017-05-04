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

import com.xenione.libs.promises.promise.BasePromise;
import com.xenione.libs.promises.promise.Promise;
import com.xenione.libs.promises.promise.listeners.AlwaysResult;

import java.util.concurrent.Executor;

public abstract class AbsDeferred<P, R> implements Deferred<P, R> {

	protected BasePromise<R> promise = new BasePromise<>();

	@Override
	public Deferred<P, R> resolve(final R resolve) {
		synchronized (this) {
			if (!promise.isPending()) {
				throw new IllegalStateException("Deferred object already finished, cannot resolve again");
			}
			promise.dispatchDone(resolve);
		}
		return this;
	}

	@Override
	public Deferred<P, R> reject(final Throwable reject) {
		synchronized (this) {
			if (!promise.isPending()) {
				throw new IllegalStateException("Deferred object already finished, cannot reject again");
			}
			promise.dispatchFail(reject);
		}
		return this;
	}

	@Override
	public Deferred<P, R> always(AlwaysResult<R> result) {
		synchronized (this) {
			if (!promise.isPending()) {
				throw new IllegalStateException("Deferred object already finished, cannot resolve again");
			}
			promise.dispatchAlways(result);
		}
		return this;
	}

	@Override
	public Deferred<P, R> cancel() {
		synchronized (this) {
			if (promise.isCanceled()) {
				throw new IllegalStateException("Deferred object already canceled, cannot reject again");
			}
			promise.dispatchCancel();
		}
		return this;
	}

	protected abstract void doTask(Executor executor, P params);

	public Promise<R> promise() {
		return promise;
	}
}
