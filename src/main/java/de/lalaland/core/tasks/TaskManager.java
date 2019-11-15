package de.lalaland.core.tasks;

import de.lalaland.core.CorePlugin;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public class TaskManager {

  private static final int THREAD_POOL_SIZE = 2;

  public TaskManager(final CorePlugin plugin){
    bukkitScheduler = Bukkit.getScheduler();
    scheduler = Executors.newScheduledThreadPool(THREAD_POOL_SIZE);
    this.plugin = plugin;
  }

  @Getter
  private final BukkitScheduler bukkitScheduler;
  @Getter
  private final ScheduledExecutorService scheduler;
  private final CorePlugin plugin;

  /**
   * Repeatedly executes a Runnable at a fixed rate.
   * - Thread Async
   *
   * @param runnable the Runnable that is going to be executed.
   * @param delay the delay before the first execution.
   * @param repeat the delay between each repeated execution.
   * @param timeUnit the time units.
   * @return a ScheduledFuture
   */
  public ScheduledFuture<?> executeScheduledTask(final Runnable runnable, final long delay, final long repeat, final TimeUnit timeUnit) {
    return scheduler.scheduleAtFixedRate(runnable, delay, repeat, timeUnit);
  }

  /**
   * Executes a Runnable Task Async once.
   * - Thread Async
   *
   * @param runnable the runnable to execute.
   */
  public void executeTask(final Runnable runnable){
    scheduler.execute(runnable);
  }

  /**
   * Executes a Runnable.
   * - Bukkit Sync
   *
   * @param runnable the runnable to execute.
   * @return
   */
  public BukkitTask runBukkitSync(final Runnable runnable){
    return bukkitScheduler.runTask(plugin, runnable);
  }

  /**
   * Executes a Runnable.
   * - Bukkit Async
   *
   * @param runnable the runnable to execute.
   * @return
   */
  public BukkitTask runBukkitAsync(final Runnable runnable){
    return bukkitScheduler.runTaskAsynchronously(plugin, runnable);
  }

  /**
   * Repeatedly executes a Runnable.
   * - Bukkit Sync
   * - Delays in Ticks
   *
   * @param runnable the runnable to execute.
   * @param delayTicks the delay before the first execution.
   * @param repeatTicks the dealy between each repeated execution.
   * @return a BukkitTask
   */
  public BukkitTask runRepeatedBukkit(final Runnable runnable, final long delayTicks, final long repeatTicks){
    return bukkitScheduler.runTaskTimer(plugin, runnable, delayTicks, repeatTicks);
  }

  /**
   * Repeatedly executes a Runnable.
   * - Bukkit Async
   * - Delays in Ticks
   *
   * @param runnable the runnable to execute.
   * @param delayTicks the delay before the first execution.
   * @param repeatTicks the dealy between each repeated execution.
   * @return a BukkitTask
   */
  public BukkitTask runRepeatedBukkitAsync(final Runnable runnable, final long delayTicks, final long repeatTicks){
    return bukkitScheduler
        .runTaskTimerAsynchronously(plugin, runnable, delayTicks, repeatTicks);
  }

  /**
   * Repeats a Runnable a fixed amount of times.
   * - BukkitSync
   * - Delays in Ticks
   *
   * @param runnable the runnable to execute.
   * @param delayTicks the delay before the first execution.
   * @param repeatDelay the delay between each further execution.
   * @param repeatAmount the amount of repeats.
   */
  public void runFixedTimesBukkitSync(final Runnable runnable, final long delayTicks, final long repeatDelay, final int repeatAmount){
    TickedRunnable.start(runnable, delayTicks, repeatDelay, repeatAmount, plugin, true);
  }

  /**
   * Repeats a Runnable a fixed amount of times.
   * - BukkitSync
   * - Delays in Ticks
   *
   * @param runnable the runnable to execute.
   * @param delayTicks the delay before the first execution.
   * @param repeatDelay the delay between each further execution.
   * @param repeatAmount the amount of repeats.
   */
  public void runFixedTimesBukkitAsync(final Runnable runnable, final long delayTicks, final long repeatDelay, final int repeatAmount){
    TickedRunnable.start(runnable, delayTicks, repeatDelay, repeatAmount, plugin, false);
  }

}
