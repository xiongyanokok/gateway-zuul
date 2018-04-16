package com.xy.gateway.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.SettableFuture;

public class Test {
	
	private static final ExecutorService executor = Executors.newFixedThreadPool(10); 
	private static final ListeningExecutorService service = MoreExecutors.listeningDecorator(executor);

	public static void main(String[] args) throws Exception {
		future();
		
		//completableFuture();
		
		//settableFuture();
		
		executor.shutdown();
		executor.awaitTermination(1, TimeUnit.DAYS);
	}
	
	private static void future() {
		List<String> ds = list();
		long t1 = System.currentTimeMillis();
		List<Future<String>> list = ds.stream().map(d -> executor.submit(() -> call(d))).collect(Collectors.toList());
		List<String> rs = list.stream().map(c -> {
			try {
				return c.get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
				return null;
			}
		}).collect(Collectors.toList());
		for (String r : rs) {
			System.out.println(r);
		}
		System.out.println(System.currentTimeMillis() - t1);
	}
	
	private static void completableFuture() {
		List<String> ds = list();
		long t1 = System.currentTimeMillis();
		List<String> rs = new ArrayList<>();
		List<Throwable> es = new ArrayList<>();
		
		@SuppressWarnings("unchecked")
		CompletableFuture<String>[] list = ds.stream().map(d -> CompletableFuture.supplyAsync(() -> call(d), executor).whenCompleteAsync((v, e) -> {
			if (null != v) {
				rs.add(v);
			}
			if (null != e) {
				es.add(e);
			}
		})).toArray(CompletableFuture[]::new);
		
		// 等待所有结果返回
		//CompletableFuture.allOf(list).join();
		// 有一个返回就返回
		CompletableFuture.anyOf(list).join();
		
		
		for (String r : rs) {
			System.out.println(r);
		}
		for (Throwable e : es) {
			System.out.println(e.getMessage());
		}
		System.out.println(System.currentTimeMillis() - t1);
	}
	
	private static void settableFuture() {
		List<String> ds = list();
		long t1 = System.currentTimeMillis();
		/*SettableFuture<String> future = SettableFuture.create();
		future.addListener(() -> call("dd"), executor);
		future.set("sss");*/
		
		/*ListenableFuture<String> future = service.submit(() -> call("ddd"));
		Futures.addCallback(future, new FutureCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("成功->"+result);
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        });
		System.out.println(future.get());*/
		
		List<ListenableFuture<String>> list = ds.stream().map(d -> service.submit(() -> call(d))).collect(Collectors.toList());
		List<String> rs = list.stream().map(c -> {
			try {
				return c.get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
				return null;
			}
		}).collect(Collectors.toList());
		for (String r : rs) {
			System.out.println(r);
		}
		
		System.out.println(System.currentTimeMillis() - t1);
	}
	
	private static List<String> list() {
		List<String> ds = new ArrayList<>();
		for (int i=0; i<1000; i++) {
			ds.add("并发测试==="+i);
		}
		return ds;
	}
	
	private static String call(String s) {
		return "熊焱-"+s;
	}
	
}
