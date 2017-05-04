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

package com.xenione.libs.promises.promise;

import com.xenione.libs.promises.promise.listeners.AlwaysListener;
import com.xenione.libs.promises.promise.listeners.CancelListener;
import com.xenione.libs.promises.promise.listeners.DoneListener;
import com.xenione.libs.promises.promise.listeners.FailListener;

public interface Promise<R> {

	enum State {

		PENDING,

		REJECTED,

		RESOLVED,

		CANCELED,
	}

	boolean isPending();

	boolean isResolved();

	boolean isRejected();

	boolean isCanceled();

	Promise<R> register(DoneListener<R> listener);

	Promise<R> register(AlwaysListener<R> listener);

	Promise<R> register(FailListener listener);

	Promise<R> register(CancelListener listener);

}
