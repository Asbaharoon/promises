/*
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

package com.xenione.libs.promises.promise.listeners;

public class AlwaysResult<R> {

	private R result;

	private Throwable throwable;

	public static <R> AlwaysResult<R> error(Throwable throwable) {
		return new AlwaysResult<>(null, throwable);
	}

	public static <R> AlwaysResult<R> ok(R result) {
		return new AlwaysResult<>(result, null);
	}

	public AlwaysResult(R result, Throwable throwable) {
		this.result = result;
		this.throwable = throwable;
	}

	public boolean isError() {
		return throwable != null;
	}

	public String getErrorMessage() {
		return throwable.getMessage();
	}

	public R getResult() {
		return result;
	}

	public Throwable getThrowable() {
		return throwable;
	}
}
