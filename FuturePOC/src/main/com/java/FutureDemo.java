package com.java;

import java.time.Clock;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhoucl on 2018/2/6.
 */
public class FutureDemo {

    final static ExecutorService executor = Executors.newFixedThreadPool(3);

    public static void main(String[] args) {
RpcService rpcService = new RpcService();
HttpService httpService = new HttpService();
        Future<Map<String,String>> future1 = null;
        Future<Integer> future2 = null;
        Future<Integer> future3 = null;
        try{

            future1 = executor.submit(()->rpcService.getRpcResult("a","b"));
            future2 = executor.submit(()->httpService.getHttpResult());
            future3 = executor.submit(()->httpService.getHttpResult());

            long beginTime = System.nanoTime();

            Map<String,String> result1 = future1.get(300, TimeUnit.MILLISECONDS);
            Integer result2 = future2.get(300,TimeUnit.MILLISECONDS);
            Integer result3 = future3.get(300,TimeUnit.MILLISECONDS);

            long endTime = System.nanoTime();
            long costTime = (endTime - beginTime)/1000000;
            System.out.println(costTime);

        }
        catch (Exception e){
            if(future1 != null){
                future1.cancel(true);
            }
            if(future2 != null){
                future2.cancel(true);
            }
            e.printStackTrace();
        }
    }

    static class RpcService {

        Map<String, String> getRpcResult(String a,String b) throws Exception {
            System.out.println("getRpcResult:"+a+b+";"+Thread.currentThread().getName());
            Thread.sleep(100);
            return new HashMap<>(1);
        }
    }

    static class HttpService{
        Integer getHttpResult() throws InterruptedException {
            System.out.println("getHttpResult:"+Thread.currentThread().getName());
            Thread.sleep(200);
            return 1;
        }
    }
}
