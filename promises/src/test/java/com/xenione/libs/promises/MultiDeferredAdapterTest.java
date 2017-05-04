package com.xenione.libs.promises;
import com.xenione.libs.promises.deferred.BaseDeferred;
import com.xenione.libs.promises.deferred.Deferred;
import com.xenione.libs.promises.deferred.MultiDeferredAdapter;
import com.xenione.libs.promises.promise.MultiResult;
import com.xenione.libs.promises.promise.OneResult;
import com.xenione.libs.promises.promise.listeners.AlwaysListener;
import com.xenione.libs.promises.promise.listeners.AlwaysResult;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;


@RunWith(MockitoJUnitRunner.class)
public class MultiDeferredAdapterTest {

	@Test
	public void whenDoneParallelDeferred_CallDone() {
		Deferred<Void, OneResult> deferred1 = new BaseDeferred<Void, OneResult>() {
			@Override
			protected void doTask(Void params) {
				OneResult result = new OneResult(1, this.promise(), null);
				this.always(AlwaysResult.ok(result));
			}
		};

		Deferred<Void, OneResult> deferred2 = new BaseDeferred<Void, OneResult>() {
			@Override
			protected void doTask(Void params) {
				OneResult result = new OneResult(2, this.promise(), null);
				this.always(AlwaysResult.ok(result));
			}
		};

		MultiDeferredAdapter<Void> multiDeferredAdapter = new MultiDeferredAdapter<>(deferred1, deferred2);
		AlwaysListener<MultiResult> listener = mock(AlwaysListener.class);
		multiDeferredAdapter.promise().register(listener);

		multiDeferredAdapter.start(null);

		verify(listener).onAlways(any(AlwaysResult.class));
		verifyZeroInteractions(listener);

	}

	@Test
	public void whenCancelParallelDeferred_CallDone() {
		Deferred<Void, OneResult> deferred1 = new BaseDeferred<Void, OneResult>() {
			@Override
			protected void doTask(Void params) {
				OneResult result = new OneResult(1, this.promise(), null);
				this.always(AlwaysResult.ok(result));
			}
		};

		Deferred<Void, OneResult> deferred2 = new BaseDeferred<Void, OneResult>() {
			@Override
			protected void doTask(Void params) {
				this.always(AlwaysResult.<OneResult>error(new Exception()));
			}
		};

		MultiDeferredAdapter<Void> multiDeferredAdapter = new MultiDeferredAdapter<>(deferred1, deferred2);
		AlwaysListener<MultiResult> listener = mock(AlwaysListener.class);
		multiDeferredAdapter.promise().register(listener);

		multiDeferredAdapter.start(null);

		verify(listener).onAlways(any(AlwaysResult.class));
		verifyZeroInteractions(listener);
	}
}
