package com.boostcamp.sentialarm.BaseAsyncTask;

/**
 * Created by 현기 on 2017-08-05.
 *
 * AsyncCallback<T> - 비동기 처리 결과를 받을 때 사용되는 콜백 인터페이스이다.
 */

public interface AsyncCallback<T> {

    // 결과를 받는다.
    public void onResult(T result);

    // 처리도중 발생한 예외를 받는다.
    public void exceptionOccured(Exception e);

    // 작업을 취소했음을 받는다.
    public void cancelled();

}


