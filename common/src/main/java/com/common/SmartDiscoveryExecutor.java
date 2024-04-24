package com.common;


import com.common.exception.APIManagementExecutorException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

/**
 * Smart Discovery Executor
 * @param <R> Type of the result
 */
public class SmartDiscoveryExecutor<R> {
  private static final int THREAD_POOL_LIMIT = 10;
  private final ExecutorService executor;

  public SmartDiscoveryExecutor(String threadName, int poolSize) {
    int poolMaxSize = Math.min(poolSize, THREAD_POOL_LIMIT);
    BasicThreadFactory threadFactory = new BasicThreadFactory.Builder()
        .namingPattern(threadName + "-%d")
        .build();
    executor = Executors.newFixedThreadPool(poolMaxSize, threadFactory);
  }

  public List<R> executeTasks(List<Supplier<R>> tasks) throws APIManagementExecutorException {
    List<CompletableFuture<R>> futures = tasks
        .parallelStream().map(t -> CompletableFuture.supplyAsync(
            t, executor)).collect(Collectors.toList());
    try {
      return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
          .thenApply(x -> futures.stream().map(CompletableFuture::join)
              .collect(Collectors.toList())).get();
    } catch (CompletionException | ExecutionException e) {
      throw new APIManagementExecutorException(e);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      return Collections.emptyList();
    } finally {
      if (executor != null) {
        executor.shutdown();
      }
    }
  }
}
