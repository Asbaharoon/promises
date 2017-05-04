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
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MultiResult implements Iterable<OneResult> {

	private final List<OneResult> results;

	public MultiResult(int size) {
		this.results = new CopyOnWriteArrayList<>(new OneResult[size]);
	}

	protected void set(int index, OneResult result) {
		results.set(index, result);
	}

	public boolean add(OneResult oneResult) {
		return results.add(oneResult);
	}

	public OneResult get(int index) {
		return results.get(index);
	}

	public Iterator<OneResult> iterator() {
		return results.iterator();
	}

	public int size() {
		return results.size();
	}

	@Override
	public String toString() {
		return "MultipleResults [results=" + results + "]";
	}
}
