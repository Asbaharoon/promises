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
import com.xenione.libs.promises.promise.MultiResult;
import com.xenione.libs.promises.promise.OneResult;
import com.xenione.libs.promises.promise.listeners.AlwaysListener;
import com.xenione.libs.promises.promise.listeners.AlwaysResult;

import java.util.concurrent.atomic.AtomicInteger;

public class MultiDeferredAdapter<R_IN> extends BaseDeferred<R_IN, MultiResult>
		implements AlwaysListener<OneResult> {

	private Deferred<R_IN, OneResult>[] deferreds;
	private MultiResult results;
	private AtomicInteger countDown;

	public MultiDeferredAdapter(Deferred<R_IN, OneResult>... deferreds) {
		this.deferreds = deferreds;
		results = new MultiResult(deferreds.length);
		countDown = new AtomicInteger(deferreds.length);
	}

	@Override
	protected void doTask(R_IN params) {
		for (Deferred<R_IN, OneResult> deferred : deferreds) {
			deferred.start(params).promise().register(this);
		}
	}

	@Override
	public void onAlways(AlwaysResult<OneResult> result) {
		if (results.add(new OneResult(results.size(), this.promise(), null))
				&& countDown.decrementAndGet() == 0) {
			always(AlwaysResult.ok(results));
		}
	}
}

