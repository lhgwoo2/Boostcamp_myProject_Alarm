package com.boostcamp.sentialarm.Util.BaseAsyncTask;

import android.os.AsyncTask;
import android.util.Log;

import java.util.concurrent.Callable;

/**
 * Created by 현기 on 2017-08-05.
 *
 * AsyncTask를 상속받은 클래스로서, 비동기로 작업을 실행하고 결과를 전달해주는 기능이다.
 *
 *  주요기능
 *      doInBackground() 메서드: callable 객체에 작업 실행을 위임한다.
 *      onPostExecute() 및 관련 메서드: callback 객체에 결과를 전달한다.
 */

public class AsyncExecutor<T> extends AsyncTask<Void, Void, T> {
    private static final String TAG = "AsyncExecutor";

    private AsyncCallback<T> callback;
    private Callable<T> callable;
    private Exception occuredException;

    public AsyncExecutor<T> setCallable(Callable<T> callable) {
        this.callable = callable;
        return this;
    }

    public AsyncExecutor<T> setCallback(AsyncCallback<T> callback) {
        this.callback = callback;
        processAsyncExecutorAware(callback);
        return this;
    }

    @SuppressWarnings("unchecked")
    private void processAsyncExecutorAware(AsyncCallback<T> callback) {
        if (callback instanceof AsyncExecutorAware) {
            ((AsyncExecutorAware<T>) callback).setAsyncExecutor(this);
        }
    }

    @Override
    protected T doInBackground(Void... params) {
        try {
            return callable.call();
        } catch (Exception ex) {
            Log.e(TAG,
                    "exception occured while doing in background: "
                            + ex.getMessage(), ex);
            this.occuredException = ex;
            return null;
        }
    }

    @Override
    protected void onPostExecute(T result) {
        if (isCancelled()) {
            notifyCanceled();
        }
        if (isExceptionOccured()) {
            notifyException();
            return;
        }

        // 결과를 콜백으로 넘긴다.
        notifyResult(result);
    }

    // 취소 콜백
    private void notifyCanceled() {
        if (callback != null)
            callback.cancelled();
    }

    private boolean isExceptionOccured() {
        return occuredException != null;
    }

    // 예외 발생 콜백
    private void notifyException() {
        if (callback != null)
            callback.exceptionOccured(occuredException);
    }


    // 결과값을 콜백으로 전달
    private void notifyResult(T result) {
        if (callback != null)
            callback.onResult(result);
    }

}