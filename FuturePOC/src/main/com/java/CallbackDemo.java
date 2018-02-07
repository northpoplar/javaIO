package com.java;


import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.nio.client.HttpAsyncClient;
import org.apache.http.nio.client.methods.HttpAsyncMethods;
import org.apache.http.nio.protocol.BasicAsyncResponseConsumer;
import org.apache.http.nio.protocol.HttpAsyncRequestProducer;

import java.util.concurrent.CompletableFuture;

/**
 * Created by zhoucl on 2018/2/6.
 */
public class CallbackDemo {
    public static HttpAsyncClient httpAsyncClient = HttpAsyncClients.createDefault();
    public static CompletableFuture<String> getHttpData(String url){
        System.out.println(Thread.currentThread().getName());
        CompletableFuture asyncFuture = new CompletableFuture();
        HttpAsyncRequestProducer httpAsyncRequestProducer = HttpAsyncMethods.create(new HttpPost(url));

        BasicAsyncResponseConsumer consumer = new BasicAsyncResponseConsumer();

        FutureCallback callback = new FutureCallback<HttpResponse>() {
            @Override
            public void completed(HttpResponse response) {
                asyncFuture.complete(response);
            }

            @Override
            public void failed(Exception ex) {
                asyncFuture.completeExceptionally(ex);
            }

            @Override
            public void cancelled() {
                asyncFuture.cancel(true);
            }
        };

        httpAsyncClient.execute(httpAsyncRequestProducer,consumer,callback);

        return asyncFuture;
    }

    public static void main(String[] args) throws Exception{
        CompletableFuture<String> future1 = CallbackDemo.getHttpData("http://www.jd.com");
//        CompletableFuture<String> future2 = CallbackDemo.getHttpData("http://www.jd.com");
//        CompletableFuture<String> future3 = CallbackDemo.getHttpData("http://www.jd.com");
//        CompletableFuture<String> future4 = CallbackDemo.getHttpData("http://www.jd.com");

        long beginTime = System.currentTimeMillis();
        String result1 = future1.get();
//        String result2 = future1.get();
//        String result3 = future1.get();
//        String result4 = future1.get();
        long endTime = System.currentTimeMillis();
        System.out.println(endTime - beginTime);
    }

}
