package com.alibaba.dubbo.performance.demo.agent.agent.model;/**
 * Created by msi- on 2018/5/13.
 */

import java.util.concurrent.*;

/**
 * @program: TcpProject
 * @description:
 * @author: XSL
 * @create: 2018-05-13 16:56
 **/

public class MyFuture<T> implements ListenableFuture<T> {
//    private CountDownLatch latch = new CountDownLatch(1);
//    private T result;
    private CompletableFuture<T> future = new CompletableFuture<T>();

    @Override
    public ListenableFuture<T> addListener(Runnable listener, Executor executor) {
        if (executor == null) {
            executor = Runnable::run;
        }
        future.whenCompleteAsync((r,v) -> listener.run(),executor);
        return this;
    }

    @Override
    public CompletableFuture<T> toCompletableFuture() {
        return future;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return false;
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
//        latch.await();
        return future.get();
    }

    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
//        latch.await();
        return future.get();
    }

    public void done(T result){
        future.complete(result);
    }
}
