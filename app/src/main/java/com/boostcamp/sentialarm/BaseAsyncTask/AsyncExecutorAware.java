package com.boostcamp.sentialarm.BaseAsyncTask;

/**
 * Created by 현기 on 2017-08-05.
 *
 *  콜백 구현 객체에서 AsyncExecutor에 대한 접근이 필요할 때가 있는데,
 *  이런 경우 콜백 구현 클래스는 AsyncExecutorAware 인터페이스를 구현하면 된다.
 *
 *  콜백 구현체가 AsyncExecutor 객체를 참조해야 할 경우에 사용되는 인터페이스
 */

public interface AsyncExecutorAware<T> {

    public void setAsyncExecutor(AsyncExecutor<T> asyncExecutor);

}
