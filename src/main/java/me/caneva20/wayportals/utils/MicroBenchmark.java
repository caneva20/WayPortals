package me.caneva20.wayportals.utils;

import com.google.common.base.Stopwatch;
import me.caneva20.messagedispatcher.dispachers.IConsoleMessageDispatcher;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.TimeUnit;

@Singleton
public class MicroBenchmark {

  private final IConsoleMessageDispatcher dispatcher;

  @Inject
  MicroBenchmark(IConsoleMessageDispatcher dispatcher) {
    this.dispatcher = dispatcher;
  }

  public interface Action {

    void run();
  }

  public interface Function<T> {

    T run();
  }

  public void run(String description, Action action) {
    var stopwatch = Stopwatch.createStarted();

    action.run();

    stopwatch.stop();

    dispatcher.debug(String.format("[Benchmark] %s took %sms (%sμs) to run", description,
        stopwatch.elapsed(TimeUnit.MILLISECONDS), stopwatch.elapsed(TimeUnit.MICROSECONDS)));
  }

  public <T> T run(String description, Function<T> action) {
    var stopwatch = Stopwatch.createStarted();

    var result = action.run();

    stopwatch.stop();

    dispatcher.debug(String.format("[Benchmark] %s took %sms (%sμs) to run", description,
        stopwatch.elapsed(TimeUnit.MILLISECONDS), stopwatch.elapsed(TimeUnit.MICROSECONDS)));

    return result;
  }
}
