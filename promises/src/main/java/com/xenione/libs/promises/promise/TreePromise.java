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

public interface TreePromise<R_IN> {

	<R_OUT> TreePromise<R_OUT> then(final Deferred<R_IN, R_OUT> deferred);

	TreePromise<MultiResult> when(Deferred<R_IN, OneResult>... deferreds);


	<R_OUT> TreePromise<R_OUT> then(PromiseMaker<R_IN, R_OUT> promiseMaker);

	TreePromise<MultiResult> when(PromiseMaker<R_IN, OneResult>... promiseMakers);
}
