package com.common;

import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;

/**
 * Retry Mechanism to retry a function for a given number of times
 * @param <T> Type of the return value
 */
@Slf4j
public class RetryMechanism<T> {

  private int retryCounter;
  private final int maxRetries;



  public RetryMechanism(int maxRetries) {
    this.maxRetries = maxRetries;
  }

  // Takes a function and executes it, if fails, passes the function to the retry command
  public T run(Supplier<T> function) {
    try {
      return function.get();
    } catch (Exception e) {
      return retry(function);
    }
  }

  public int getRetryCounter() {
    return retryCounter;
  }

  private T retry(Supplier<T> function) throws RuntimeException {
    retryCounter = 0;
    while (retryCounter < maxRetries) {
      try {
        return function.get();
      } catch (Exception ex) {
        retryCounter++;
        if (retryCounter >= maxRetries) {
          log.error("FAILED - Command failed on retry {}  of {} error: ", retryCounter, maxRetries);
          break;
        }
      }
    }
    throw new RuntimeException("Command failed on all of " + maxRetries + " retries");
  }
}
