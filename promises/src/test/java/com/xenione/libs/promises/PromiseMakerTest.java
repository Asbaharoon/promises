package com.xenione.libs.promises;
import com.xenione.libs.promises.deferred.BaseDeferred;
import com.xenione.libs.promises.deferred.Deferred;
import com.xenione.libs.promises.promise.PromiseMaker;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;


@RunWith(MockitoJUnitRunner.class)
public class PromiseMakerTest {

	@Test
	public void startPromise_StartDeferred_PropagateStart() {
		Deferred<Integer, Void> task = mock(Deferred.class);

		PromiseMaker<Integer, Void> promiseMaker = PromiseMaker.Impl.make(task);
		promiseMaker.start(1);

		verify(task).start(1);
		verifyNoMoreInteractions(task);
	}

	@Test
	public void startPromise_OnTwoConsecutiveDeferred() {
		Deferred<Integer, Integer> task1 = new BaseDeferred<Integer, Integer>() {
			@Override
			protected void doTask(Integer params) {
				this.resolve(params);
			}
		};

		Deferred<Integer, Integer> task2 = mock(Deferred.class);

		PromiseMaker<Integer, Integer> promiseMaker = PromiseMaker.Impl.make(task1);
		promiseMaker.then(task2);
		promiseMaker.start(1);

		verify(task2).start(1);
		verifyNoMoreInteractions(task2);
	}

	@Test
	public void cancelPromise_OnTwoConsecutiveDeferred_PropagateCancel() {
		Deferred<Integer, Integer> task1 = new BaseDeferred<Integer, Integer>() {
			@Override
			protected void doTask(Integer params) {
				this.resolve(params);
			}
		};

		Deferred<Integer, Integer> task2 = mock(Deferred.class);

		PromiseMaker<Integer, Integer> promiseMaker = PromiseMaker.Impl.make(task1);
		promiseMaker.then(task2);
		promiseMaker.cancel();

		verify(task2).cancel();
		verifyNoMoreInteractions(task2);
	}
}
