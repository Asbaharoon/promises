package com.xenione.libs.promises;

import com.xenione.libs.promises.deferred.AbsDeferred;
import com.xenione.libs.promises.deferred.BaseDeferred;
import com.xenione.libs.promises.promise.listeners.CancelListener;
import com.xenione.libs.promises.promise.listeners.DoneListener;
import com.xenione.libs.promises.promise.listeners.FailListener;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.concurrent.Executor;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class DeferredTest {

	@Test
	public void whenCancelDeferred_PromiseDispatchCancel() {
		CancelListener cancelListener = mock(CancelListener.class);
		AbsDeferred deferred = new BaseDeferred<Void, Void>() {
			@Override
			protected void doTask(Executor executor, Void params) {

			}
		};

		deferred.promise().register(cancelListener);
		deferred.cancel();
		verify(cancelListener).onCancel();
		verifyNoMoreInteractions(cancelListener);
	}

	@Test
	public void whenStartDeferred_CancelDeferred_PromiseDispatchCancel() {
		CancelListener cancelListener = mock(CancelListener.class);
		AbsDeferred<Void, Void> deferred = new BaseDeferred<Void, Void>() {
			@Override
			protected void doTask(Executor executor, Void params) {
				this.cancel();
			}
		};

		deferred.promise().register(cancelListener);
		deferred.start(null);
		verify(cancelListener).onCancel();
		verifyNoMoreInteractions(cancelListener);
	}

	@Test(expected = IllegalStateException.class)
	public void whenCancelDeferred_OverAlreadyCanceledDeferred_throwException() {

		AbsDeferred deferred = new BaseDeferred<Void, Void>() {
			@Override
			protected void doTask(Executor executor,Void params) {

			}
		};
		deferred.cancel();
		deferred.cancel();
	}

	@Test
	public void whenStartDeferred_PromiseDispatchDone() {
		DoneListener<Void> doneListener = mock(DoneListener.class);
		AbsDeferred<Void, Void> deferred = new BaseDeferred<Void, Void>() {
			@Override
			protected void doTask(Executor executor, Void params) {
				this.resolve(null);
			}
		};

		deferred.promise().register(doneListener);
		deferred.start(null);
		verify(doneListener).onDone(null);
		verifyNoMoreInteractions(doneListener);
	}

	@Test(expected = IllegalStateException.class)
	public void whenDoneDeferred_OverAlreadyDoneDeferred_throwException() {
		AbsDeferred<Void, Void> deferred = new BaseDeferred<Void, Void>() {
			@Override
			protected void doTask(Executor executor, Void params) {
				this.resolve(null);
			}
		};
		deferred.start(null);
		deferred.start(null);
	}

	@Test
	public void whenFailDeferred_PromiseDispatchFail() {
		FailListener failListener = mock(FailListener.class);
		AbsDeferred<Void, Void> deferred = new BaseDeferred<Void, Void>() {
			@Override
			protected void doTask(Executor executor, Void params) {
				this.reject(new Throwable());
			}
		};

		deferred.promise().register(failListener);
		deferred.start(null);
		verify(failListener).onFail(any(Exception.class));
		verifyNoMoreInteractions(failListener);
	}

	@Test(expected = IllegalStateException.class)
	public void whenFailDeferred_OverAlreadyFailDeferred_throwException() {
		AbsDeferred<Void, Void> deferred = new BaseDeferred<Void, Void>() {
			@Override
			protected void doTask(Executor executor, Void params) {
				this.reject(new Exception());
			}
		};
		deferred.start(null);
		deferred.start(null);
	}
}
