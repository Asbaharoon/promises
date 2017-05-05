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
import com.xenione.libs.promises.deferred.Deferred;
import com.xenione.libs.promises.deferred.MultiDeferredAdapter;
import com.xenione.libs.promises.deferred.MultiPromiseMakerAdapter;
import com.xenione.libs.promises.promise.listeners.AlwaysListener;
import com.xenione.libs.promises.promise.listeners.AlwaysResult;
import com.xenione.libs.promises.promise.listeners.CancelListener;
import com.xenione.libs.promises.promise.listeners.DoneListener;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public interface PromiseMaker<P, R_IN> extends TreePromise<R_IN> {

	PromiseMaker<P, R_IN> onExecutor(Executor executor);

	void start(P params);

	void cancel();

	Promise<R_IN> promise();

	class Impl<P, R_IN> implements PromiseMaker<P, R_IN> {

		private Deferred<P, R_IN> previous;

		private Executor executor = Executors.newSingleThreadExecutor();

		private Impl(Deferred<P, R_IN> previous) {
			this.previous = previous;
		}

		public static <P, R_IN> PromiseMaker<P, R_IN> make(Deferred<P, R_IN> deferred) {
			return new Impl<>(deferred);
		}

		@Override
		public PromiseMaker<P, R_IN> onExecutor(Executor executor) {
			this.executor = executor;
			return this;
		}

		@Override
		public <R_OUT> TreePromise<R_OUT> then(final Deferred<R_IN, R_OUT> deferred) {
			pipe(deferred);
			return new Impl<>(deferred).onExecutor(executor);
		}

		@SafeVarargs
		@Override
		public final TreePromise<MultiResult> when(final Deferred<R_IN, OneResult>... deferreds) {
			MultiDeferredAdapter<R_IN> multiDeferredAdapter = new MultiDeferredAdapter<>(deferreds);
			pipe(multiDeferredAdapter);
			return new Impl<>(multiDeferredAdapter).onExecutor(executor);
		}

		private void pipe(final Deferred<R_IN, ?> deferred) {
			previous.promise()
					.register(new DoneListener<R_IN>() {
						@Override
						public void onDone(R_IN result) {
							deferred.startOnExecutor(executor, result);
						}
					})
					.register(new CancelListener() {
						@Override
						public void onCancel() {
							deferred.cancel();
						}
					})
					.register(new AlwaysListener<R_IN>() {
						@Override
						public void onAlways(AlwaysResult<R_IN> result) {
							deferred.startOnExecutor(executor, result.getResult());
						}
					});
		}

		@Override
		public void start(P params) {
			previous.startOnExecutor(executor, params);
		}

		public void cancel() {
			previous.cancel();
		}

		@Override
		public Promise<R_IN> promise() {
			return previous.promise();
		}

		@Override
		public <R_OUT> TreePromise<R_OUT> then(final PromiseMaker<R_IN, R_OUT> promiseMaker) {
			pipe(promiseMaker);
			return promiseMaker;
		}

		@Override
		public TreePromise<MultiResult> when(PromiseMaker<R_IN, OneResult>... promiseMakers) {
			MultiPromiseMakerAdapter<R_IN> multiPromiseMakerAdapter = new MultiPromiseMakerAdapter<>(promiseMakers);
			pipe(multiPromiseMakerAdapter);
			return new Impl<>(multiPromiseMakerAdapter);
		}

		private void pipe(final PromiseMaker<R_IN, ?> promiseMaker) {
			previous.promise()
					.register(new DoneListener<R_IN>() {
						@Override
						public void onDone(R_IN result) {
							promiseMaker.start(result);
						}
					})
					.register(new CancelListener() {
						@Override
						public void onCancel() {
							promiseMaker.cancel();
						}
					})
					.register(new AlwaysListener<R_IN>() {
						@Override
						public void onAlways(AlwaysResult<R_IN> result) {
							promiseMaker.start(result.getResult());
						}
					});
		}
	}
}
