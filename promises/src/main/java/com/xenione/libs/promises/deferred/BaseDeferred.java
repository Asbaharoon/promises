/**
 * Copyright 2016 Eugeni Josep Senent i Gabriel
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
import java.util.concurrent.Executor;

public class BaseDeferred<P, R> extends AbsDeferred<P, R> {

	@Override
	public Deferred<P, R> start(P params) {
		doTask(params);
		return this;
	}

	@Override
	public Deferred<P, R> startOnExecutor(Executor executor, final P params) {
		executor.execute(new Runnable() {
			@Override
			public void run() {
				doTask(params);
			}
		});
		return this;
	}

	protected void doTask(P params) {

	}

}
