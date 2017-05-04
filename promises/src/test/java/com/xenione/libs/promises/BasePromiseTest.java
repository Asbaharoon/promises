package com.xenione.libs.promises;
import com.xenione.libs.promises.deferred.AbsDeferred;
import com.xenione.libs.promises.deferred.BaseDeferred;
import com.xenione.libs.promises.promise.listeners.AlwaysResult;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class BasePromiseTest {

	@Test
	public void whenPromiseIsDone_ChangeStateIntoResolved() {
		BaseDeferred<Integer, Integer> deferred = new BaseDeferred<Integer, Integer>() {
			@Override
			protected void doTask(Integer params) {
				this.resolve(params);
			}
		};
		deferred.start(1);
		assertThat(deferred.promise().isResolved(), is(true));
	}

	@Test
	public void whenPromiseIsDoneByAlways_ChangeStateIntoResolved() {
		AbsDeferred<Integer, Integer> deferred = new BaseDeferred<Integer, Integer>() {
			@Override
			protected void doTask(Integer params) {
				this.always(AlwaysResult.ok(params));
			}
		};
		deferred.start(1);
		assertThat(deferred.promise().isResolved(), is(true));
	}

	@Test
	public void whenPromiseIsFail_ChangeStateIntoRejected() {
		AbsDeferred<Integer, Integer> deferred = new BaseDeferred<Integer, Integer>() {
			@Override
			protected void doTask(Integer params) {
				this.reject(new Exception());
			}
		};
		deferred.start(1);
		assertThat(deferred.promise().isRejected(), is(true));
	}

	@Test
	public void whenPromiseIsCancel_ChangeStateIntoCanceled() {
		AbsDeferred<Integer, Integer> deferred = new BaseDeferred<Integer, Integer>() {
			@Override
			protected void doTask(Integer params) {
				this.cancel();
			}
		};
		deferred.start(1);
		assertThat(deferred.promise().isCanceled(), is(true));
	}
}
