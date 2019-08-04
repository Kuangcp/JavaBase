package com.github.kuangcp.concurrent;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class CompletableFutureTest {

  @Test
  public void testAsync() throws InterruptedException, ExecutionException, TimeoutException {
    CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
      System.out.println("temp");
    });

    future.get(5, TimeUnit.SECONDS);
    if (future.isDone()) {
      System.out.println("complete");
    }
  }
}
